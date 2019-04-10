package up.common.entity;

import java.io.Serializable;
import java.util.List;

public class UserGroup implements Serializable {
    private Integer id;

    private String groupname;

    private String groupdescription;

    private Byte ifinner;

    private List<UserEntity> userEntities;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname == null ? null : groupname.trim();
    }

    public String getGroupdescription() {
        return groupdescription;
    }

    public void setGroupdescription(String groupdescription) {
        this.groupdescription = groupdescription == null ? null : groupdescription.trim();
    }

    public Byte getIfinner() {
        return ifinner;
    }

    public void setIfinner(Byte ifinner) {
        this.ifinner = ifinner;
    }

    public void setUserEntities(List<UserEntity> userEntities) {
        this.userEntities = userEntities;
    }

    public List<UserEntity> getUserEntities() {
        return userEntities;
    }
}