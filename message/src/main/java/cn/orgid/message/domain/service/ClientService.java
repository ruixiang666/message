package cn.orgid.message.domain.service;

import io.netty.channel.Channel;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.orgid.message.domain.component.ChannelComponent;
import cn.orgid.message.domain.dao.client.ClientConnectionDAO;
import cn.orgid.message.domain.dao.client.ClientDAO;
import cn.orgid.message.domain.dao.message.ReliableMessageDAO;
import cn.orgid.message.domain.dao.platform.ApplicationDAO;
import cn.orgid.message.domain.model.client.Client;
import cn.orgid.message.domain.model.client.ClientConnection;
import cn.orgid.message.domain.model.client.ClientConnection.State;
import cn.orgid.message.domain.model.message.ReliableMessage;

@Service
@Transactional
public class ClientService {
	
	
	
    Logger logger = Logger.getLogger(ClientService.class);
    @Autowired
    ClientDAO clientDAO;

    @Autowired
    ApplicationDAO applicationDAO;

    @Autowired
    ClientConnectionDAO clientConnectionDAO;

    @Autowired
    ChannelComponent channelComponent;
    
    
    @Autowired
    ReliableMessageDAO reliableMessageDAO;

    /**
     * 通过appId 和appSecret ，创建一个客户端
     *
     * @param client 客户端信息
     * @return
     */
    public Client createClient(Client client) {
        client.refreshToken();
        clientDAO.save(client);
        return client;
    }


    /**
     * 获取最新的客户端信息，用于提供给客户端程序与消息平台建立链接
     *
     * @param id
     * @return
     */
    public Client getClientForToken(Long id) {
        Client c = clientDAO.findOne(id);
        c.refreshToken();
        clientDAO.save(c);
        return c;
    }

    /**
     * 获取当前在线用户信息
     *
     * @param clientTag 用户类型
     * @return
     */
    public List<ClientConnection> getOnlineAPP(String clientTag) {
        if (StringUtils.isBlank(clientTag)) {
            return clientConnectionDAO.findByConnectionState(State.Open.name());
        }
        return clientConnectionDAO.findByClientTagAndConnectionState(clientTag, State.Open.name());
    }

    /**
     * 客户端注册链接时，判断是否已经存在链接，如果不存在则创建一个
     *
     * @param clientId
     * @param connectionToken
     * @param channel
     * @return
     */
    public void registeClientConnection(Long clientId, String connectionToken, Channel channel) {
    	
    	if(channel==null||!channel.isActive()){
    		logger.info("忽略无效链接注册");
    		return ;
    	}
        Client c = clientDAO.findOne(clientId);
        if (c == null || !c.isTokenValid(connectionToken)) {
            throw new MessageApplicationException("链接token错误");
        }
        ClientConnection clientConnection = new ClientConnection(c);
        String cid = UUID.randomUUID().toString();
        channelComponent.putChannel(cid, channel);
        clientConnection.setAddr(channel.toString());
        clientConnection.setChannelId(cid);
        clientConnectionDAO.save(clientConnection);
        
        List<ReliableMessage> messages =reliableMessageDAO.findByToClientIdAndAcknowledge(clientId, false);
        for (ReliableMessage reliableMessage : messages) {
        	channelComponent.sendMsg(clientConnection.getChannelId(), reliableMessage.getContentWithMsgKey());
		}
        
    }

    /**
     * 刷新连接信息
     *
     * @param clientId
     */
    public void reflashClientConnection(Long clientId) {
    	
        List<ClientConnection> list = clientConnectionDAO.findByClientIdAndConnectionState(clientId, State.Open.name());
        if (!list.isEmpty()) {
            ClientConnection clientConnection = list.get(0);
            clientConnection.reflash();
            clientConnectionDAO.save(clientConnection);
        }
        
    }

    /**
     * 连接关闭
     *
     * @param id
     */
    public void closeClientConnection(Long id) {
    	
        ClientConnection clientConnection = clientConnectionDAO.findOne(id);
        clientConnection.close();
        clientConnectionDAO.save(clientConnection);
        Client c = clientDAO.findOne(clientConnection.getClientId());
        c.refreshToken();
        clientDAO.save(c);
        
    }

    /**
     * 根据连接id获取连接信息
     *
     * @param clientId
     * @return
     */
    public Client getClientById(Long clientId) {
        return clientDAO.findOne(clientId);
    }

    public boolean validate(Long clientId, String connectionToken) {
        ClientConnection clientConnection = clientConnectionDAO.findByClientIdAndConnectionTokenAndConnectionState(
                clientId, connectionToken, ClientConnection.State.Open.name());
        if (clientConnection != null) {
            return true;
        }
        return false;
    }

    public void registerPlatformClient(String appId, Channel channel) {
    	
    	if(channel==null||!channel.isActive()){
    		logger.info("忽略无效链接注册");
    		return ;
    	}
        channelComponent.putPlatformChannel(appId, channel);
        
    }

}
