package cn.orgid.message.domain.component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Component;

import cn.orgid.message.domain.config.Config;
import cn.orgid.message.domain.service.MessageApplicationException;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

@Component
public class ChannelComponent {

	Logger logger = Logger.getLogger(ChannelComponent.class);

	@Autowired
	Config config;

	private Map<String, Channel> channelMapping = new HashMap<String, Channel>();

	private Map<String, Channel> platformChannelMapping = new HashMap<String, Channel>();

	private Map<String, Set<String>> appChannelMapping = new HashMap<String, Set<String>>();

	public void putChannel(String id, Channel channel) {

		channelMapping.put(id, channel);

	}

	/**
	 * 
	 * @param appId
	 * @param channel
	 */
	public void putPlatformChannel(String appId, Channel channel) {

		// 多个平台客户端可能都链接到消息服务器
		Set<String> s = appChannelMapping.get(appId);
		if (s == null) {
			s = new HashSet<String>();
			appChannelMapping.put(appId, s);
		}
		String cid = UUID.randomUUID().toString();
		s.add(cid);
		platformChannelMapping.put(cid, channel);

	}

	public Channel getPlatformChannel(String id) {

		return platformChannelMapping.get(id);

	}

	public Channel get(String id) {

		return channelMapping.get(id);

	}

	private void remove(String id) {

		channelMapping.remove(id);
	}

	public boolean sendMsg(String id, String msg) {

		if (msg == null) {
			throw new MessageApplicationException("消息对象不能为空");
		}
		TextWebSocketFrame frame = new TextWebSocketFrame(msg);
		Channel c = get(id);
		if (c != null && c.isActive()) {
			send(frame, c);
			return true;
		} else {
			if (c != null && !c.isOpen())
				remove(id);
			return false;
		}

	}

	public boolean report(String appId, String msg) {
		try {
			if (msg == null)
				throw new ApplicationContextException("消息不能为空");
			Set<String> s = appChannelMapping.get(appId);
			if (s == null)
				return false;
			int count = 0;
			for (String string : s) {
				Channel c = getPlatformChannel(string);
				if (c != null && c.isActive()) {
					TextWebSocketFrame frame = new TextWebSocketFrame(msg);
					send(frame, c);
					count++;
				}
			}
			return count > 0;
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}

	}

	private void send(TextWebSocketFrame frame, Channel c) {
		try {
			c.writeAndFlush(frame);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
