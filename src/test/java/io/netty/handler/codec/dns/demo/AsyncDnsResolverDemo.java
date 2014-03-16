/**
 * 
 */
package io.netty.handler.codec.dns.demo;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ChannelFactory;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.dns.resolver.AsynchronousDnsResolver;

/**
 * @author isdom
 *
 */
public class AsyncDnsResolverDemo {

	private static final Logger LOG =
			LoggerFactory.getLogger(AsyncDnsResolverDemo.class);
	
	/**
	 * @param args
	 * @throws Exception 
	 * @throws  
	 */
	public static void main(String[] args) {
		final AsynchronousDnsResolver resolver = new AsynchronousDnsResolver(
				new ChannelFactory<DatagramChannel> () {

					@Override
					public DatagramChannel newChannel() {
						return new NioDatagramChannel();
					}},
				new NioEventLoopGroup(1)
//				,new InetSocketAddress("8.8.8.8", 53) // google
//				,new InetSocketAddress("8.8.4.4", 53)  // google
//				,new InetSocketAddress("114.114.114.114", 53)  // 114 only ipv4
//				,new InetSocketAddress("114.114.115.115", 53)  // 114 only ipv4
//				,new InetSocketAddress("223.6.6.6", 53)  //	ali  only ipv4
//				,new InetSocketAddress("223.5.5.5", 53)  //	ali  only ipv4
				,new InetSocketAddress("202.101.172.35", 53)  // dianxin only ipv4
//				,new InetSocketAddress("202.101.172.47", 53)  // dianxin only ipv4
//					,new InetSocketAddress("208.67.222.222", 53)  // OpenDNS  no resp
//					,new InetSocketAddress("208.67.220.220", 53) // OpenDNS  no resp
//					,new InetSocketAddress("199.91.73.222", 53)  // V2EX DNS
//					,new InetSocketAddress("178.79.131.110", 53) // V2EX DNS
				);
		final String domain = //"www.a.shifen.com";
				//"www.baidu.com";
				"www.yinxiang.com";
				//"isdom.myqnapcloud.com";
		
		Object addr = null;
		
		try {
			addr = resolver.lookup(domain, InternetProtocolFamily.IPv4).sync().get();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("exception when resolve", e);
		}
		
		if ( null == addr ) {
			System.out.println( "domain(" + domain + ") unresolved.");
			System.exit(-1);
		}
		
		if ( addr instanceof InetAddress ) {
			System.out.println( "domain(" + domain + ") resolved to " + ((InetAddress)addr).getHostAddress());
		}
		else {
			System.out.println( "domain(" + domain + ") resolve result's type: " + 
					addr.getClass() + ", and content is " + addr);
		}
				
	}

}
