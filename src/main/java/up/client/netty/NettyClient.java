package up.client.netty;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
 

/**
 * @ClassName: NettyClient
 * @Description: Netty客户端用于与服务端通讯
 * @Author: chenjd
 * @Date: 2019-02-15 16:18
 **/

@Component
@Data
@Slf4j
public class NettyClient extends Thread{

	@Autowired
	ApplicationContext ctx;

	public static ChannelFuture channelFuture;
	
	private String ip;
	private int port;
	
	public void run() { 
 
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap(); // (1)
			b.group(workerGroup); // (2)
			b.channel(NioSocketChannel.class); // (3)
			b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {  
					ch.pipeline().addLast(new ObjectEncoder());
					ch.pipeline().addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
					ch.pipeline().addLast(ctx.getBean(ClientMsgHandler.class));
				}
			});
 
			// Start the client.
			channelFuture = b.connect(ip, port).sync(); // (5)
		 
			// Wait until the connection is closed.
			channelFuture.channel().closeFuture().sync(); 
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e){
			System.out.println(e.getMessage()); 
		}
		finally {
			workerGroup.shutdownGracefully();
		}
		 
	}
	public void start(String ip, int port) {
		this.ip = ip;
		this.port = port; 
		start();
	}

	public ChannelFuture getChannelFuture() {
		return channelFuture;
	}

}
