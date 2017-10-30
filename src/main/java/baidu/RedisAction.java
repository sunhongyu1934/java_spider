package baidu;

import redis.clients.jedis.Jedis;

/**
 * Created by Administrator on 2017/7/18.
 */
public class RedisAction {
    private Jedis jedis;
    public RedisAction(String host, int port){
        jedis=new Jedis(host,port);
    }
    public String get(String key){
        String cname=jedis.spop(key);
        return cname;
    }
    public void set(String key,String valye){
        jedis.sadd(key,valye);
    }

    public long getslength(String key){
        return jedis.scard(key);
    }

    public void setstr(String key,String value){
        jedis.set(key,value);
    }

    public void selectda(int a){
        jedis.select(a);
    }

}
