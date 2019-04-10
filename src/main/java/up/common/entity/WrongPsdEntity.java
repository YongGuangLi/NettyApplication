package up.common.entity;

import java.io.Serializable;

public class WrongPsdEntity implements Serializable {
    private Integer userid;

    private Long wrongtimestamp;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Long getWrongtimestamp() {
        return wrongtimestamp;
    }

    public void setWrongtimestamp(Long wrongtimestamp) {
        this.wrongtimestamp = wrongtimestamp;
    }
}