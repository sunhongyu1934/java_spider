package etl.itjz_etl.bean;

/**
 * Created by Administrator on 2017/4/24.
 */
public class finacbean {
    private String fid;
    private String name;
    private String time;
    private String round;
    private String money;
    private String vc;



    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getVc() {
        return vc;
    }

    public void setVc(String vc) {
        this.vc = vc;
    }

    @Override
    public String toString() {
        return "finacbean{" +
                "fid='" + fid + '\'' +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", round='" + round + '\'' +
                ", money='" + money + '\'' +
                ", vc='" + vc + '\'' +
                '}';
    }
}
