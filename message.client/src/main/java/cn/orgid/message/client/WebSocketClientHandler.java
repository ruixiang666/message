package cn.orgid.message.client;

import org.apache.log4j.Logger;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

@Sharable
public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {
	
	private static Logger logger = Logger.getLogger(WebSocketClientHandler.class);
	
	private final WebSocketClientHandshaker handshaker;
	private ChannelPromise handshakeFuture;

	

	WebSocketClient client;

	public WebSocketClientHandler(WebSocketClientHandshaker handshaker, WebSocketClient client) {

		this.handshaker = handshaker;
		this.client = client;
	}

	public ChannelFuture handshakeFuture() {
		return handshakeFuture;
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) {
		handshakeFuture = ctx.newPromise();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		handshaker.handshake(ctx.channel());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		
		client.close();
		client.init();
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		Channel ch = ctx.channel();

		if (!handshaker.isHandshakeComplete()) {
			handshaker.finishHandshake(ch, (FullHttpResponse) msg);
			logger.info("WebSocket Client connected!");
			handshakeFuture.setSuccess();

			return;
		}
		if (msg instanceof FullHttpResponse) {
			
			return;
			
		}

		WebSocketFrame frame = (WebSocketFrame) msg;
		if (frame instanceof PongWebSocketFrame) {
			client.onPong((PongWebSocketFrame) msg);
		}
		if (frame instanceof TextWebSocketFrame) {
			TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
			
			System.out.println("receive msg:"+textFrame.text());
			client.onTextMessage(textFrame.text());
			
		} else if (frame instanceof CloseWebSocketFrame) {

			ch.close();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		if (!handshakeFuture.isDone()) {
			handshakeFuture.setFailure(cause);
		}
		ctx.close();
	}

	/**
	 * @Description 连接确认类
	 * @date 2016年12月7日 下午3:51:31
	 * @version 1.0
	 */
	public static class ConnectionCheck {

		private long checkTime;

		private long returnTime;

		public long getCheckTime() {
			return checkTime;
		}

		public void refreshForm(ConnectionCheck c) {
			this.checkTime = c.checkTime;
			this.returnTime = c.returnTime;
		}

		public void setCheckTime(long checkTime) {
			this.checkTime = checkTime;
		}

		public long getReturnTime() {
			return returnTime;
		}

		public void setReturnTime(long returnTime) {
			this.returnTime = returnTime;
		}

	}

}
