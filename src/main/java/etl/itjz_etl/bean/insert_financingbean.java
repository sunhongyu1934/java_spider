package etl.itjz_etl.bean;

/**
 * Created by Administrator on 2017/4/26.
 */
public class insert_financingbean {
    private String cid;
    private String sDate;
    private String sName;
    private String sAmount;
    private String sStage;
    private String sInstitution;
    private String sFromUrl;
    private String pid;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getsDate() {
        return sDate;
    }

    public void setsDate(String sDate) {
        this.sDate = sDate;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsAmount() {
        return sAmount;
    }

    public void setsAmount(String sAmount) {
        this.sAmount = sAmount;
    }

    public String getsStage() {
        return sStage;
    }

    public void setsStage(String sStage) {
        this.sStage = sStage;
    }

    public String getsInstitution() {
        return sInstitution;
    }

    public void setsInstitution(String sInstitution) {
        this.sInstitution = sInstitution;
    }

    public String getsFromUrl() {
        return sFromUrl;
    }

    public void setsFromUrl(String sFromUrl) {
        this.sFromUrl = sFromUrl;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }


    @Override
    public String toString() {
        return "insert_financingbean{" +
                "cid='" + cid + '\'' +
                ", sDate='" + sDate + '\'' +
                ", sName='" + sName + '\'' +
                ", sAmount='" + sAmount + '\'' +
                ", sStage='" + sStage + '\'' +
                ", sInstitution='" + sInstitution + '\'' +
                ", sFromUrl='" + sFromUrl + '\'' +
                ", pid='" + pid + '\'' +
                '}';
    }
}
