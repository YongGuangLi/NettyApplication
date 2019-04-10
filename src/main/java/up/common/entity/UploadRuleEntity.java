package up.common.entity;

import java.io.Serializable;

public class UploadRuleEntity implements Serializable {
    private Integer id;

    private String name;

    private String devicetype;

    private Integer eventtype;

    private Integer subtype;

    private String collectdes;

    private Integer level;

    private Integer combineevent;

    private Integer threshodevent;

    private Integer cpuevent;

    private Integer diskevent;

    private Integer memevent;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getCpuevent() {
        return cpuevent;
    }

    public void setCpuevent(Integer cpuevent) {
        this.cpuevent = cpuevent;
    }

    public Integer getDiskevent() {
        return diskevent;
    }

    public void setDiskevent(Integer diskevent) {
        this.diskevent = diskevent;
    }

    public Integer getMemevent() {
        return memevent;
    }

    public void setMemevent(Integer memevent) {
        this.memevent = memevent;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype == null ? null : devicetype.trim();
    }

    public Integer getEventtype() {
        return eventtype;
    }

    public void setEventtype(Integer eventtype) {
        this.eventtype = eventtype;
    }

    public Integer getSubtype() {
        return subtype;
    }

    public void setSubtype(Integer subtype) {
        this.subtype = subtype;
    }

    public String getCollectdes() {
        return collectdes;
    }

    public void setCollectdes(String collectdes) {
        this.collectdes = collectdes == null ? null : collectdes.trim();
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getCombineevent() {
        return combineevent;
    }

    public void setCombineevent(Integer combineevent) {
        this.combineevent = combineevent;
    }

    public Integer getThreshodevent() {
        return threshodevent;
    }

    public void setThreshodevent(Integer threshodevent) {
        this.threshodevent = threshodevent;
    }
}