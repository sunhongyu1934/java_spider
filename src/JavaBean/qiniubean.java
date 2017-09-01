package JavaBean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/30.
 */
public class qiniubean {
    private String chose;
    private String database;
    private String fieldupdate;
    private List<String> fieldlogos;
    private String name;
    private String begin;
    private String end;


    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getFieldlogos() {
        return fieldlogos;
    }

    public void setFieldlogos(List<String> fieldlogos) {
        this.fieldlogos = fieldlogos;
    }

    public String getChose() {
        return chose;
    }

    public void setChose(String chose) {
        this.chose = chose;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getFieldupdate() {
        return fieldupdate;
    }

    public void setFieldupdate(String fieldupdate) {
        this.fieldupdate = fieldupdate;
    }



}
