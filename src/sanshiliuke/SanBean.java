package sanshiliuke;

import java.util.ArrayList;
import java.util.List;

public class SanBean {
    public Da data;
    public static class Da{
        public Page pageData;
        public static class Page{
            public List<De> data=new ArrayList<De>();
            public static class De {
                public String id;
            }
        }
    }

}
