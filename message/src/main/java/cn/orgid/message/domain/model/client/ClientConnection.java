package cn.orgid.message.domain.model.client;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.orgid.message.domain.model.EntityBase;

/**
 * @Description 连接信息类
 * @version 1.0
 * @date 2016年12月7日 下午4:28:23
 */
@Entity
@Table(name = "t_client_conn")
public class ClientConnection extends EntityBase {

    private static final long serialVersionUID = 1L;
    /** 连接客户端id */
    private Long clientId;
    /** 连接端名称 */
    private String name;
    /** 连接密钥id */
    private String appId;
    /** 连接创建时间 */
    private Date connectTime;
    /** 连接关闭时间 */
    private Date closeTime;
    /** 连接状态 */
    private String connectionState;
    /** 连接凭证 */
    private String connectionToken;
    /** 连接对象类型 */
    private String clientTag;
    /** 连接通道id */
    private String channelId;
    /** 连接ip */
    private String addr;

    public enum State {
        Open, Close
    }

    public ClientConnection() {
    }

    public ClientConnection(Client c) {
        this.clientId = c.getId();
        this.name = c.getName();
        this.appId = c.getAppId();
        this.connectTime = new Date();
        this.connectionState = State.Open.name();
        this.connectionToken = c.getToken();
        this.clientTag = c.getTag();
    }

    public Date getConnectTime() {
        return connectTime;
    }

    public void setConnectTime(Date connectTime) {
        this.connectTime = connectTime;
    }

    public String getConnectionState() {
        return connectionState;
    }

    public void setConnectionState(String connectionState) {
        this.connectionState = connectionState;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConnectionToken() {
        return connectionToken;
    }

    public void setConnectionToken(String connectionToken) {
        this.connectionToken = connectionToken;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public void reflash() {
        this.connectionState = State.Open.name();
        this.connectTime = new Date();
        this.closeTime = null;
    }

    public void close() {
        this.connectionState = State.Close.name();
        this.closeTime = new Date();
    }

    public boolean isDisConnection() {
        return new Date().getTime() - this.connectTime.getTime() > 60 * 10 * 1000;
    }

    public String getClientTag() {
        return clientTag;
    }

    public void setClientTag(String clientTag) {
        this.clientTag = clientTag;
    }

    public boolean isOpen() {
        if (this.connectionState != null && connectionState.equals(State.Open.name())) {
            return true;
        }
        return false;
    }

}
