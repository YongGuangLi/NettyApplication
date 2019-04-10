package up.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadEvent implements Serializable {
    private Integer id;

    private Date uploadTime;

    private Integer level;

    private String ip;

    private String deviceName;

    private String deviceType;

    private Integer eventType;

    private Integer subType;

    private String content;

    private Integer eventNumber;

    private String desmap;

    private String manufacture;

    private Date collectTime;

    private Integer repeatNum;

    private Integer combineChange;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName == null ? null : deviceName.trim();
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType == null ? null : deviceType.trim();
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public Integer getSubType() {
        return subType;
    }

    public void setSubType(Integer subType) {
        this.subType = subType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getEventNumber() {
        return eventNumber;
    }

    public void setEventNumber(Integer eventNumber) {
        this.eventNumber = eventNumber;
    }

    public String getDesmap() {
        return desmap;
    }

    public void setDesmap(String desmap) {
        this.desmap = desmap == null ? null : desmap.trim();
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture == null ? null : manufacture.trim();
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

    public Integer getRepeatNum() {
        return repeatNum;
    }

    public void setRepeatNum(Integer repeatNum) {
        this.repeatNum = repeatNum;
    }

    public void setCombineChange(Integer combineChange) {this.combineChange = combineChange;}

    public Integer getCombineChange() {return this.combineChange;}

    @Override
    public String toString() {
        return "UploadEvent{" +
                "id=" + id +
                '}';
    }
}