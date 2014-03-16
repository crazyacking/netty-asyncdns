/**
 * 
 */
package io.netty.handler.codec.dns.demo;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ChannelFactory;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.dns.resolver.AsynchronousDnsResolver;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

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
				,new InetSocketAddress("202.101.172.47", 53)  // dianxin only ipv4
//					,new InetSocketAddress("208.67.222.222", 53)  // OpenDNS  no resp
//					,new InetSocketAddress("208.67.220.220", 53) // OpenDNS  no resp
//					,new InetSocketAddress("199.91.73.222", 53)  // V2EX DNS
//					,new InetSocketAddress("178.79.131.110", 53) // V2EX DNS
				);
		final String domain = //"taurus.sina.com.cn";
				//"jupiter.sina.com.cn";
				//"www.sina.com.cn";
				//"www.baidu.com";
				//"www.yinxiang.com";
				"isdom.myqnapcloud.com";
		
		final Future<List<Inet4Address>> future = resolver.resolve4(domain);
		
		future.syncUninterruptibly();
		
		future.addListener(new GenericFutureListener<Future<List<Inet4Address>>>() {

			@Override
			public void operationComplete(final Future<List<Inet4Address>> future)
					throws Exception {
				if ( future.isSuccess() ) {
					showResult(future.get(), domain);
				}
				else {
					System.out.println( "domain(" + domain + ") unresolved.");
					System.exit(-1);
				}
			}
		});
	}

	private static void showResult(final List<Inet4Address> addrs, final String domain) {
		
		for ( Object addr : addrs) {
			if ( addr instanceof String ) {
				LOG.info( "domain({}) resolved to {}", domain, addr);
			}
			else if ( addr instanceof Inet4Address ) {
				LOG.info( "domain({}) resolved to {}", domain, ((Inet4Address)addr).getHostAddress());
			}
		}
		
		System.exit(0);
	}
}
