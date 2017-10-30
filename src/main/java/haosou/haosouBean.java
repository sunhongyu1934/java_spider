package haosou;

import java.util.List;

/**
 * Created by Administrator on 2017/7/7.
 */
public class haosouBean {
    public static class haosous{
        public List<Data> data;
        public static class Data{
            public Dataa data;
            public static class Dataa{
                public String week_year_ratio;
                public String month_year_ratio;
                public String week_chain_ratio;
                public String month_chain_ratio;
                public String week_index;
                public String month_index;
            }
        }
    }


    public static class quantu{
        public Data data;
        public static class Data{
            public List<Pro> province;
            public static class Pro{
                public String entity;
                public String percent;

                @Override
                public String toString() {
                    return "{" +
                            "entity='" + entity + '\'' +
                            ", percent='" + percent + '\'' +
                            '}';
                }
            }

            public List<Int> interest;
            public static class Int {
                public String entity;
                public String percent;

                @Override
                public String toString() {
                    return "{" +
                            "entity='" + entity + '\'' +
                            ", percent='" + percent + '\'' +
                            '}';
                }
            }

            public List<SSS> sex;
            public static class SSS{
                public String entity;
                public String percent;
            }

            public List<AAA> age;
            public static class AAA{
                public String entity;
                public String percent;
                public SS sex;

                @Override
                public String toString() {
                    return "{" +
                            "entity='" + entity + '\'' +
                            ", percent='" + percent + '\'' +
                            ", sex=" + sex +
                            '}';
                }

                public static class SS{
                    public String male;
                    public String female;

                    @Override
                    public String toString() {
                        return "{" +
                                "male='" + male + '\'' +
                                ", female='" + female + '\'' +
                                '}';
                    }
                }
            }

        }
    }



}
