package simutong.sismutong_touzishijian;

import java.util.List;

/**
 * Created by Administrator on 2017/6/5.
 */
public class SerBean {
    public List<Result> result;
    public class Result{
        public String eventId;
        public String epIndustryNameCn;
        public String eventDate;
        public String investRoundNameCn;
        public String investor;
        public String epSname;
        public String epLogo;
        public List<String> tags;
        public String packInfo;
    }

}
