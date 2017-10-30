package sanshiliuke;

import java.util.ArrayList;
import java.util.List;

public class AppBean {
    public static class serach{
        public String msg="";
        public Da data;
        public static class Da{
            public String totalPages="";
            public List<det> data=new ArrayList<det>();
            public static class det{
                public String id="";
                public String fullName="";
                public String intro="";
            }
        }
    }

    public static class detail{
        public Da data;
        public static class Da{
            public Ta tags;
            public Bas basic;
            public static class Bas{
                public String address1="";
                public String name="";
                public String logo="";
                public String startAt="";
            }
            public Re relatedLink;
            public static class Re{
                public String website="";
                public String weixin="";
            }
            public static class Ta {
                public List<dd> data=new ArrayList<dd>();
                public static class dd {
                    public String name="";
                }
            }
            public fin pastFinance;
            public static class fin{
                public List<dd> data=new ArrayList<dd>();
                public static class dd{
                    public String financeAmount;
                    public List<inv> investEntity=new ArrayList<inv>();
                    public static class inv{
                        public String name;
                    }
                    public String phase;
                    public String asset;
                    public String financeDate;
                }
            }
        }
    }



}
