package sanshiliuke;

import java.util.ArrayList;
import java.util.List;

public class SanDetailBean {

    public static class Detail {
        public Da data;

        public static class Da {
            public List<Itag> industryTag=new ArrayList<Itag>();

            public static class Itag {
                public String name;
            }

            public String startDateDesc;
            public String scale;
            public String address1Desc;
            public String weibo;
            public String intro;
            public String logo;
            public List<Optag> operationTag=new ArrayList<Optag>();

            public static class Optag {
                public String name;
            }

            public String address2Desc;
            public String brief;
            public String website;
            public Prodesc companyIntroduce;

            public static class Prodesc {
                public String productService;
            }

            public String fullName;
            public String name;
            public String weixin;
        }
    }

    public static class Finac{
        public List<Da> data=new ArrayList<Da>();
        public static class Da{
            public String financeAmount;
            public String financeAmountUnit;
            public String financeDate;
            public List<Tou> participantVos=new ArrayList<Tou>();
            public static class Tou{
                public String entityName;
            }
            public String phase;
        }
    }

    public static class Chuang{
        public Da data;
        public static class Da{
            public List<Mem> members=new ArrayList<Mem>();
            public static class Mem{
                public String name;
                public String position;
                public String intro;
            }
            public String story;
        }
    }
}
