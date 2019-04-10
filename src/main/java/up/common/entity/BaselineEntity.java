package up.common.entity;

import lombok.Builder;

import java.io.Serializable;

@Builder
public class BaselineEntity implements Serializable {
    private String taskId;

    private Integer returnValue;

    private String ip;

    private String content;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId == null ? null : taskId.trim();
    }

    public Integer getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Integer returnValue) {
        this.returnValue = returnValue;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}