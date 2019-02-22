package java_spider;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

public class redis {
    public static void main(String args[]){
        int a=0;
        JedisCluster cluster;
            Set<HostAndPort> nodes = new HashSet<>();
            nodes.add(new HostAndPort("10.64.5.13", 7001));
            nodes.add(new HostAndPort("10.64.5.14", 7001));
            nodes.add(new HostAndPort("10.64.9.3", 7001));
            nodes.add(new HostAndPort("10.64.9.4", 7001));
            nodes.add(new HostAndPort("10.64.9.13", 7001));
            cluster=new JedisCluster(nodes);

        Set<HostAndPort> nodes2 = new HashSet<>();
        nodes2.add(new HostAndPort("172.29.237.209", 7000));
        nodes2.add(new HostAndPort("172.29.237.209", 7001));
        nodes2.add(new HostAndPort("172.29.237.209", 7002));
        nodes2.add(new HostAndPort("172.29.237.214", 7003));
        nodes2.add(new HostAndPort("172.29.237.214", 7004));
        nodes2.add(new HostAndPort("172.29.237.214", 7005));
        nodes2.add(new HostAndPort("172.29.237.215", 7006));
        nodes2.add(new HostAndPort("172.29.237.215", 7007));
        nodes2.add(new HostAndPort("172.29.237.215", 7008));
        JedisCluster cluster2=new JedisCluster(nodes2);
        Set<String> set=cluster2.smembers("comp_zl");
        System.out.println(set);
        for(String s:set){
            cluster.sadd("comp_zl",s);
            a++;
            System.out.println(a+"*********************************************");
        }
    }
}
