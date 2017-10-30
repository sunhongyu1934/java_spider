package etl.itjz_etl.bean;

/**
 * Created by Administrator on 2017/4/26.
 */
public class insert_projectbean {
    private String sName;
    private String cid;
    private String sAddress;
    private String sTag;
    private String iOpreateState;
    private String sLogoUrl;
    private String sFromUrl;

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getsAddress() {
        return sAddress;
    }

    public void setsAddress(String sAddress) {
        this.sAddress = sAddress;
    }

    public String getsTag() {
        return sTag;
    }

    public void setsTag(String sTag) {
        this.sTag = sTag;
    }

    public String getiOpreateState() {
        return iOpreateState;
    }

    public void setiOpreateState(String iOpreateState) {
        this.iOpreateState = iOpreateState;
    }

    public String getsLogoUrl() {
        return sLogoUrl;
    }

    public void setsLogoUrl(String sLogoUrl) {
        this.sLogoUrl = sLogoUrl;
    }

    public String getsFromUrl() {
        return sFromUrl;
    }

    public void setsFromUrl(String sFromUrl) {
        this.sFromUrl = sFromUrl;
    }


    @Override
    public String toString() {
        return "insert_projectbean{" +
                "sName='" + sName + '\'' +
                ", cid='" + cid + '\'' +
                ", sAddress='" + sAddress + '\'' +
                ", sTag='" + sTag + '\'' +
                ", iOpreateState='" + iOpreateState + '\'' +
                ", sLogoUrl='" + sLogoUrl + '\'' +
                ", sFromUrl='" + sFromUrl + '\'' +
                '}';
    }
}
