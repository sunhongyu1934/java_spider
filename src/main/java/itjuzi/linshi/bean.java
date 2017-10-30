package itjuzi.linshi;

import java.util.List;

/**
 * Created by Administrator on 2017/6/16.
 */
public class bean {
    public String status;
    public String message;
    public Re result;
    public static class Re{
        public List<RR> Result;
        public static class RR{
            public String Name;
            public String WebSite;
        }
    }

}
