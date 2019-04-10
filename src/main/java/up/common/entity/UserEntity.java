package up.common.entity;

import java.io.Serializable;

public class UserEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;

	private String username;

	private String realname;

	private int roleid;

	private int groupid;

	private String password;

	private String telnumber;

	private String phonenumber;

	private String email;

	private String description;

	private String ukey;

	private long locktime;

	private long passwordmodifytime;

	private boolean ifinner;

	private boolean notfirstlogin;

	private boolean delflag;

	private RoleEntity roleEntity;

	private UserGroup userGroup;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public int getRoleid() {
		return roleid;
	}

	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}

	public int getGroupid() {
		return groupid;
	}

	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTelnumber() {
		return telnumber;
	}

	public void setTelnumber(String telnumber) {
		this.telnumber = telnumber;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUkey() {
		return ukey;
	}

	public void setUkey(String ukey) {
		this.ukey = ukey;
	}

	public long getLocktime() {
		return locktime;
	}

	public void setLocktime(long locktime) {
		this.locktime = locktime;
	}

	public long getPasswordmodifytime() {
		return passwordmodifytime;
	}

	public void setPasswordmodifytime(long passwordmodifytime) {
		this.passwordmodifytime = passwordmodifytime;
	}

	public boolean isIfinner() {
		return ifinner;
	}

	public void setIfinner(boolean ifinner) {
		this.ifinner = ifinner;
	}

	public boolean isNotfirstlogin() {
		return notfirstlogin;
	}

	public void setNotfirstlogin(boolean notfirstlogin) {
		this.notfirstlogin = notfirstlogin;
	}

	public boolean isDelflag() {
		return delflag;
	}

	public void setDelflag(boolean delflag) {
		this.delflag = delflag;
	}

	public RoleEntity getRoleEntity() {
		return roleEntity;
	}

	public UserGroup getUserGroup() {
		return userGroup;
	}

	public void setRoleEntity(RoleEntity roleEntity) {
		this.roleEntity = roleEntity;
	}

	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}
}