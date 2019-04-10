package up.common.entity;

import lombok.Builder;

import java.io.Serializable;

@Builder
public class HolescanEntity implements Serializable {
    private String taskid;

    private Byte returnValue;

    private String content;

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid == null ? null : taskid.trim();
    }

    public Byte getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Byte returnValue) {
        this.returnValue = returnValue;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}