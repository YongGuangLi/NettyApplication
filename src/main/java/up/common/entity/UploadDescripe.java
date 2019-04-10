package up.common.entity;

import java.io.Serializable;

public class UploadDescripe implements Serializable {
    private Integer id;

    private String identify;

    private String eventdescribe;

    private String contentdescribe;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify == null ? null : identify.trim();
    }

    public String getEventdescribe() {
        return eventdescribe;
    }

    public void setEventdescribe(String eventdescribe) {
        this.eventdescribe = eventdescribe == null ? null : eventdescribe.trim();
    }

    public String getContentdescribe() {
        return contentdescribe;
    }

    public void setContentdescribe(String contentdescribe) {
        this.contentdescribe = contentdescribe == null ? null : contentdescribe.trim();
    }
}