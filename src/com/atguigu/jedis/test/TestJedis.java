package com.atguigu.jedis.test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;

public class TestJedis {

	//  redis-cli -p xxxx -h xxx 
	@SuppressWarnings("resource")
	@Test
	public void test() {
		
		Jedis jedis = new Jedis("192.168.11.127", 6379);
		
		String pong = jedis.ping();
		
		System.out.println(pong);
	}
	
	@Test
	public void testPool(){
		//默认的连接池配置
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		
		System.out.println(poolConfig);
		// 创建连接池对象
		JedisPool jedisPool=new JedisPool(poolConfig, "192.168.11.127", 6379,60000);
		
		Jedis jedis = jedisPool.getResource();
		
		String ping = jedis.ping();
		
		System.out.println(ping);
		//如果是从连接池中获取的，那么执行close方法只是将连接放回到池中
		jedis.close();
		
		jedisPool.close();}


	@Test
	public void testSentinel() throws Exception {
		
		Set<String> set = new HashSet<>();
		// set中放的是哨兵的Ip和端口
		set.add("192.168.11.127:26379");
		
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
//		哨兵的连接池
		JedisSentinelPool jedisSentinelPool = new JedisSentinelPool("mymaster", set, poolConfig, 60000);
		//使用哨兵的连接池对象获取jedis
		Jedis jedis = jedisSentinelPool.getResource();
		
		String value = jedis.get("k7");
		
//		jedis.set("Jedis", "jedis");
		
		System.out.println(value);
		
	}
	
	@Test
	public void testCluster(){
		
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		
		//Jedis Cluster will attempt to discover cluster nodes automatically
		
		jedisClusterNodes.add(new HostAndPort("192.168.11.127", 6379));
		
		JedisCluster jc = new JedisCluster(jedisClusterNodes);
		
		jc.set("foo", "bar");
		
		String value = jc.get("foo");
		
		System.out.println(value);
	}

}
