package cn.orgid.message.domain.server;

import cn.orgid.message.domain.service.ClientService;
import cn.orgid.message.domain.service.MessageService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ChatServerInitializer extends ChannelInitializer<Channel> {

	
	SslContext sslCtx;
	
	

	public ChatServerInitializer(ClientService clientService, MessageService messageService) {
	
		super();
	
	}
	
	public ChatServerInitializer(SslContext sslCtx) {
		
		super();
		this.sslCtx=sslCtx;
	
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {

		ChannelPipeline pipeline = ch.pipeline();
		if (sslCtx != null) {
            pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        }
		pipeline.addLast(new HttpServerCodec());
		pipeline.addLast(new ChunkedWriteHandler());
		pipeline.addLast(new HttpObjectAggregator(64 * 1024));
		pipeline.addLast(new WebSocketServerProtocolHandler("/"));
		pipeline.addLast(new WebSocketFrameHandler());

	}

}
