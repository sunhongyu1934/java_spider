package sanshiliuke;

import java.util.ArrayList;
import java.util.List;

public class kelistbean {
    public static class one{
        public Da data;
        public static class Da{
            public List<Lab> label;
            public static class Lab{
                public String id;
            }
        }
    }

    public static class detail{
        public Da data;
        public static class Da{
            public Da.Page pageData;
            public static class Page{
                public String totalPages;
                public List<De> data=new ArrayList<De>();
                public static class De {
                    public String id;
                    public String name;
                    public List<String> tags=new ArrayList<String>();
                    public String logo;
                }
            }
        }
    }
}
