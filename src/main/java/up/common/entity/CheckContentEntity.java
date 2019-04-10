package up.common.entity;

import java.io.Serializable;

public class CheckContentEntity implements Serializable {
  private Integer id;

  private Integer type;

  private String uuid;

  private String ip;

  private String content;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid == null ? null : uuid.trim();
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
