package cn.orgid.message.domain.server;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.Security;
import javax.net.ssl.KeyManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import cn.orgid.message.domain.service.ClientService;
import cn.orgid.message.domain.service.MessageService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class MessageServer {

	private final EventLoopGroup workerGroup = new NioEventLoopGroup();
	

	private Channel channel;

	private int port;

	private boolean ssl;

	private String sslKeyPath;
	
	private String sslKeyPasswd;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isSsl() {
		return ssl;
	}

	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}

	public String getSslKeyPath() {
		return sslKeyPath;
	}
	
	

	public String getSslKeyPasswd() {
		return sslKeyPasswd;
	}

	public void setSslKeyPasswd(String sslKeyPasswd) {
		this.sslKeyPasswd = sslKeyPasswd;
	}

	public void setSslKeyPath(String sslKeyPath) {
		this.sslKeyPath = sslKeyPath;
	}

	@Autowired
	ClientService clientService;

	@Autowired
	MessageService messageService;

	public void startup() {

		Thread t = new Thread(new Runnable() {
			public void run() {
				start();
			}
		});
		t.start();
	}

	private void start() {

		InetSocketAddress address = new InetSocketAddress(port);
		ServerBootstrap boot = new ServerBootstrap();
		if(ssl){
			SslContext ctx = getSslContext(sslKeyPath,sslKeyPasswd);
			boot.group(workerGroup).channel(NioServerSocketChannel.class).childHandler(createInitializer(ctx));
		}else{
			boot.group(workerGroup).channel(NioServerSocketChannel.class).childHandler(createInitializer());
		}
		ChannelFuture f = boot.bind(address).syncUninterruptibly();
		channel = f.channel();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				if (channel != null)
					channel.close();
				workerGroup.shutdownGracefully();
			}
		});
		f.channel().closeFuture().syncUninterruptibly();
		

	}

	private static SslContext getSslContext(String sslKeyPath, String passwd) {
		
		SslContext sslCtx = null;
		
		try {
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			sslCtx = SslContextBuilder.forServer(ssc.certificate(),
					ssc.privateKey()).build();
			String algorithm = Security
					.getProperty("ssl.KeyManagerFactory.algorithm");
			if (algorithm == null) {
				algorithm = "SunX509";
			}
			KeyStore ks = KeyStore.getInstance("JKS");
			FileInputStream fin = new FileInputStream(sslKeyPath);
			ks.load(fin, passwd.toCharArray());
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
			kmf.init(ks, passwd.toCharArray());
			sslCtx = SslContextBuilder.forServer(kmf).build();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return sslCtx;

	}
	
	
	protected ChannelHandler createInitializer(SslContext ctx) {
		
		return new ChatServerInitializer(ctx);
	}
	

	protected ChannelHandler createInitializer() {
		
		return new ChatServerInitializer(clientService, messageService);

	}

	public static void main(String[] args) {

		MessageServer s = new MessageServer();
		s.setPort(2048);
		s.startup();

	}

}
