package simutong.sismutong_touzishijian;

import java.util.List;

/**
 * Created by Administrator on 2017/6/5.
 */
public class DetBean {
    public List<Result> result;
    public class Result{
        public List<Des> descData;
        public class Des{
            public String descCn;
        }
        public Ene enterprise;
        public class Ene{
            public List<Ep> epContact;
            public class Ep{
                public String epContactAddressCn;
                public String epContactEmail;
                public String epContactPersonCn;
                public String epContactTel;
            }
            public List<epDe> epDescData;
            public class epDe{
                public String descCn;
            }
            public Plac epRegistrationPlace;
            public class Plac{
                public String dicNameCn;
            }

            public String epSetupDate;
            public String epShortnameCn;
            public String epWeb;
            public List<Ind> industry;
            public class Ind{
                public String dicNameCn;
                public String dicNameEn;
                public String dicPathNameCn;
                public String dicPathNameEn;
            }
        }

        public List<Inv> investInves;
        public class Inv{
            public Org org;
            public class Org{
                public List<Dec> orgDesc;
                public class Dec{
                    public String descCn;
                }
                public String orgNameCn;
                public String orgSetupDate;
                public String orgShortnameCn;
                public String orgShortnameEn;
                public Ot orgType;
                public class Ot{
                    public String dicNameCn;
                    public String dicNameEn;
                }
            }
        }
        public St investStage;
        public class St{
            public String dicNameCn;
        }
    }
}
