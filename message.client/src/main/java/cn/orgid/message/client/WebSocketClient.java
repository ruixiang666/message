package cn.orgid.message.client;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.orgid.message.client.SendingMessage.ActionCode;
import cn.orgid.message.client.SendingMessage.ClientKey;
import cn.orgid.message.client.SendingMessage.Key;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

public class WebSocketClient {
	
	

	private static Logger logger = Logger.getLogger(WebSocketClient.class);
	
	private Channel ch;

	private Bootstrap b;

	private EventLoopGroup group;

	private PongMsg pong;

	private WebSocketConf socketConf;

	private List<TextMessageListener> messageListeners;
	
	 SendingMessage registerMessage;

	public List<TextMessageListener> getMessageListeners() {
		return messageListeners;
	}

	public void setMessageListeners(List<TextMessageListener> messageListeners) {
		this.messageListeners = messageListeners;
	}

	public WebSocketConf getSocketConf() {
		return socketConf;
	}

	public void setSocketConf(WebSocketConf socketConf) {
		this.socketConf = socketConf;
	}

	public void init() {

		connect();
		startPingWebSocketFrameTask();
		pong = new PongMsg(socketConf.getPongTimeOut());
		pong.touch();
		startCheckPongWebSocketFrameTask();
		

	}

	public void startPingWebSocketFrameTask() {

		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					while (true) {
						Debug.println("send ping message  to server", socketConf.isDebug());
						ping();
						try {
							Thread.sleep(socketConf.getPingTimeInterval());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
		t.start();

	}

	public void startCheckPongWebSocketFrameTask() {

		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					while (true) {
						try {
							boolean isTimeOut = pong.isTimeOut();
							if (isTimeOut) {
								Debug.println(" pong from server time out ", socketConf.isDebug());
								Debug.println("reconneting ...", socketConf.isDebug());
								reconnet();
								Debug.println("reconnected ...", socketConf.isDebug());
							}
							try {
								Thread.sleep(socketConf.getCheckPongTimeInterval());
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

						} catch (Throwable e) {
							e.printStackTrace();
						}

					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
		t.start();

	}

	void reconnet() {
		
		try {
			if (group != null)
				group.shutdownGracefully();
			connect();
			
			send(registerMessage);
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	public void close() {

		try {
			if (group != null)
				group.shutdownGracefully();
				
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	private void connect() {

		try {
			URI uri = new URI(socketConf.getWsUrl());
			String scheme = uri.getScheme() == null ? "ws" : uri.getScheme();
			final String host = uri.getHost();
			final int port = uri.getPort();
			final boolean ssl = "wss".equalsIgnoreCase(scheme);
			final SslContext sslCtx;
			if (ssl) {
				sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
			} else {
				sslCtx = null;
			}
			final WebSocketClientHandler handler = new WebSocketClientHandler(WebSocketClientHandshakerFactory
					.newHandshaker(uri, WebSocketVersion.V13, null, false, new DefaultHttpHeaders()), this);
			group = new NioEventLoopGroup();
			b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) {
					ChannelPipeline p = ch.pipeline();
					if (sslCtx != null) {
						p.addLast(sslCtx.newHandler(ch.alloc(), host, port));
					}
					p.addLast(new HttpClientCodec(), new HttpObjectAggregator(8192), handler);
				}
			});
			ch = b.connect(uri.getHost(), port).sync().channel();
			handler.handshakeFuture().sync();
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	

	public void ping() {

		if (ch != null){
			send(new PingWebSocketFrame());
		}
		

	}

	public void send(SendingMessage msg) {
		
		String s = msg.toJson();
		TextWebSocketFrame frame = new TextWebSocketFrame(s);
		send(frame);

	}

	public void send(WebSocketFrame f) {

		if (ch != null)
			ch.writeAndFlush(f);

	}

	public void onPong(PongWebSocketFrame msg) {

		Debug.println("receiver pong message from server", socketConf.isDebug());
		if (pong != null)
			pong.touch();

	}

	public void sendRegisterClientMessage(Long clientId, String clientToken) {

	
		SendingMessage message = new SendingMessage();
		message.setAction(ActionCode.RegisterClient.name());
		message.add(ClientKey.id.name(), clientId);
		message.add(ClientKey.token.name(), clientToken);
		send(message);

	}

	public void sendReportMessage(Long clientId, String clientToken, Object content) {

		SendingMessage message = new SendingMessage();
		message.setAction(ActionCode.Report.name());
		message.add(ClientKey.id.name(), clientId);
		message.add(ClientKey.token.name(), clientToken);
		message.add(Key.content.name(), content);
		send(message);

	}

	public void sendChatMessage(Long fromClientId, Long toClientId, Object content) {

		SendingMessage message = new SendingMessage();
		message.setAction(ActionCode.SendMessage.name());
		message.add(Key.fromClientId.name(), fromClientId);
		message.add(Key.toClientId.name(), toClientId);
		message.add(Key.content.name(), content);
		send(message);

	}

	public static class PongMsg {

		private long time;

		private long pongTimeOut;

		public PongMsg(long pongTimeOut) {

			this.pongTimeOut = pongTimeOut;

		}

		public boolean isTimeOut() {
			return System.currentTimeMillis() - time > pongTimeOut;
		}

		public void touch() {
			time = System.currentTimeMillis();
		}

	}

	public void onTextMessage(String text) {

		
		if (StringUtils.isBlank(text)) {
			return;
		}
		
		try {
			String content = text.replaceAll("\u0027", "'");
			JSONObject jsonObject = JSON.parseObject(content);
			String k = jsonObject.getString("msgKey");
			if (StringUtils.isNotBlank(k)) {
				SendingMessage msg = SendingMessage.AcknowledgeMessage(k);
				send(msg);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		for (int i=0;messageListeners!=null&&i<messageListeners.size();i++) {
			TextMessageListener listener=messageListeners.get(i);
			listener.onTextMessage(text);
		}

	}

	

}
