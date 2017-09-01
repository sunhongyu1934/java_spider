package redis;

import redis.clients.jedis.Jedis;

/**
 * Created by xuzq on 2017/7/3.
 */
public class RedisAction {

    private Jedis jedis;

    public RedisAction(String host, int port){
        jedis = new Jedis(host, port);
    }

    public  boolean getAllInfoCompanyId(String company_id){
        if(jedis.get("all_" + company_id) != null){
            return true;
        }
        return false;
    }

    public void setAllInfoCompanyId(String company_id, String company_name){
        jedis.set("all_" + company_id, company_name);
    }

    public String getAllInfoContent(String company_id){
        return jedis.get("all_" + company_id);
    }

    public boolean getQYGXCompanyId(String company_id){
        if(jedis.get("qygx_" + company_id) != null){
            return true;
        }
        return false;
    }

    public void setQYGXCompanyId(String company_id, String company_name){
        jedis.set("qygx_" + company_id, company_name);
    }

    public String getQYGXContent(String company_id){
        return jedis.get("qygx_" + company_id);
    }

    public static void main(String[] args) {
        //RedisAction redisAction = new RedisAction(args[0], Integer.valueOf(args[1]));
        RedisAction redisAction = new RedisAction("a026.hb2.innotree.org", 6379);

        for(int index = 0; index < 100; index++){
            redisAction.setAllInfoCompanyId(String.valueOf(index), "Content_All" + String.valueOf(index));
            redisAction.setQYGXCompanyId(String.valueOf(index), "Content_qygx" + String.valueOf(index));
        }

        for(int index = 0; index < 100; index++){

            System.out.println(redisAction.getAllInfoCompanyId(String.valueOf(index)));
            System.out.println(redisAction.getQYGXCompanyId(String.valueOf(index)));
        }
    }
}
