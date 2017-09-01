package linshi_spider.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/4.
 */
public class xinxibubean {
    public List<dat> data;
    public String message;


    public static class dat{
        public String logo;
        public String name;
        public String websites;
        public match matchField;

        public static class match{
            public String content;
        }
    }




}
