package up.common.entity;

import java.io.Serializable;

public class CollectRuleEntity implements Serializable {
    private Integer id;

    private String name;

    private Integer level;

    private Integer subtype;

    private Integer eventtype;

    private String devicetype;

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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getSubtype() {
        return subtype;
    }

    public void setSubtype(Integer subtype) {
        this.subtype = subtype;
    }

    public Integer getEventtype() {
        return eventtype;
    }

    public void setEventtype(Integer eventtype) {
        this.eventtype = eventtype;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype == null ? null : devicetype.trim();
    }
}