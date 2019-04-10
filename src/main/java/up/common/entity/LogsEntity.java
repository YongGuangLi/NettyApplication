package up.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogsEntity implements Serializable {
    private Integer id;

    private String type;

    private Integer userId;

    private Integer level;

    private String targetObj;

    private String actionDesc;

    private String actionTime;

    private String lastmodifytime;

    private Boolean delflag;

    private String ip;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getTargetObj() {
        return targetObj;
    }

    public void setTargetObj(String targetObj) {
        this.targetObj = targetObj == null ? null : targetObj.trim();
    }

    public String getActionDesc() {
        return actionDesc;
    }

    public void setActionDesc(String actionDesc) {
        this.actionDesc = actionDesc == null ? null : actionDesc.trim();
    }

    public String getActionTime() {
        return actionTime;
    }

    public void setActionTime(String actionTime) {
        this.actionTime = actionTime == null ? null : actionTime.trim();
    }

    public String getLastmodifytime() {
        return lastmodifytime;
    }

    public void setLastmodifytime(String lastmodifytime) {
        this.lastmodifytime = lastmodifytime == null ? null : lastmodifytime.trim();
    }

    public Boolean getDelflag() {
        return delflag;
    }

    public void setDelflag(Boolean delflag) {
        this.delflag = delflag;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    @Override
    public String toString() {
        return "LogsEntity{" +
                "id=" + id +
                '}';
    }
}