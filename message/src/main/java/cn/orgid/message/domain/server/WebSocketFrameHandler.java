package cn.orgid.message.domain.server;

import org.apache.log4j.Logger;

import cn.orgid.message.domain.service.Action;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class WebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	
	private static Logger logger = Logger.getLogger(WebSocketFrameHandler.class);
	private static int count = 0;

	private static long t1;

	public WebSocketFrameHandler() {

		super();
		

	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		
		super.userEventTriggered(ctx, evt);

	}

	/**
	 * 连接断开
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		
		super.channelInactive(ctx);
		
	}

	
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		
		String in =null;
		try{
			ByteBuf buffer = msg.content();
			byte[] bs = new byte[buffer.readableBytes()];
			buffer.readBytes(bs);
			 in = new String(bs);
			System.out.println("in:" + in);
			 logger.info("in:"+in);
			Action event = Action.create(in,ctx.channel());
			String rep = event.execute();
			System.out.println("out:"+rep);
			TextWebSocketFrame frame = new TextWebSocketFrame(rep);
			ctx.channel().writeAndFlush(frame);
		}catch(Throwable e){
			e.printStackTrace();
			Action event = Action.unknowAction(in);
			String rep = event.execute();
			//System.out.println("out:" + rep);
			TextWebSocketFrame frame = new TextWebSocketFrame(rep);
			ctx.channel().writeAndFlush(frame);
		}

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
		cause.printStackTrace();
	}
}
