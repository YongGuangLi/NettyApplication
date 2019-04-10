package up.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;

	private String name;

	private String roletype;

	private String description;

	private boolean ifinner;

	private int runstatus;

	private int assetsmanage;

	private int colinfo;

	private int event;

	private int privilege;

	private int syslog;

	private int configuration;

	private int system;

	private int paradigm;

	private int comm;

	private int topo;

	private int netflow;

	private int test;

	private int backup;

	private int baselinkcheck;

	private int systemupdate;

	private List<UserEntity> userEntities;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRoletype() {
		return roletype;
	}

	public void setRoletype(String roletype) {
		this.roletype = roletype;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isIfinner() {
		return ifinner;
	}

	public void setIfinner(boolean ifinner) {
		this.ifinner = ifinner;
	}

	public int getRunstatus() {
		return runstatus;
	}

	public void setRunstatus(int runstatus) {
		this.runstatus = runstatus;
	}

	public int getAssetsmanage() {
		return assetsmanage;
	}

	public void setAssetsmanage(int assetsmanage) {
		this.assetsmanage = assetsmanage;
	}

	public int getColinfo() {
		return colinfo;
	}

	public void setColinfo(int colinfo) {
		this.colinfo = colinfo;
	}

	public int getEvent() {
		return event;
	}

	public void setEvent(int event) {
		this.event = event;
	}

	public int getPrivilege() {
		return privilege;
	}

	public void setPrivilege(int privilege) {
		this.privilege = privilege;
	}

	public int getSyslog() {
		return syslog;
	}

	public void setSyslog(int syslog) {
		this.syslog = syslog;
	}

	public int getConfiguration() {
		return configuration;
	}

	public void setConfiguration(int configuration) {
		this.configuration = configuration;
	}

	public int getSystem() {
		return system;
	}

	public void setSystem(int system) {
		this.system = system;
	}

	public int getParadigm() {
		return paradigm;
	}

	public void setParadigm(int paradigm) {
		this.paradigm = paradigm;
	}

	public int getComm() {
		return comm;
	}

	public void setComm(int comm) {
		this.comm = comm;
	}

	public int getTopo() {
		return topo;
	}

	public void setTopo(int topo) {
		this.topo = topo;
	}

	public int getNetflow() {
		return netflow;
	}

	public void setNetflow(int netflow) {
		this.netflow = netflow;
	}

	public int getTest() {
		return test;
	}

	public void setTest(int test) {
		this.test = test;
	}

	public int getBackup() {
		return backup;
	}

	public void setBackup(int backup) {
		this.backup = backup;
	}

	public int getBaseLinkCheck() {
		return baselinkcheck;
	}

	public void setBaseLinkCheck(int baseLinkCheck) {
		this.baselinkcheck = baselinkcheck;
	}

	public int getSystemupdate() {
		return systemupdate;
	}

	public void setSystemupdate(int systemupdate) {
		this.systemupdate = systemupdate;
	}

	public int getBaselinkcheck() {
		return baselinkcheck;
	}

	public List<UserEntity> getUserEntities() {
		return userEntities;
	}

	public void setBaselinkcheck(int baselinkcheck) {
		this.baselinkcheck = baselinkcheck;
	}

	public void setUserEntities(List<UserEntity> userEntities) {
		this.userEntities = userEntities;
	}

	@Override
	public String toString() {
		return "RoleEntity [id=" + id + ", name=" + name + ", roletype=" + roletype + ", description=" + description
				+ ", ifinner=" + ifinner + ", runstatus=" + runstatus + ", assetsmanage=" + assetsmanage + ", colinfo="
				+ colinfo + ", event=" + event + ", privilege=" + privilege + ", syslog=" + syslog + ", configuration="
				+ configuration + ", system=" + system + ", paradigm=" + paradigm + ", comm=" + comm + ", topo=" + topo
				+ ", netflow=" + netflow + ", test=" + test + ", backup=" + backup + ", BaseLinkCheck=" + baselinkcheck
				+ ", systemupdate=" + systemupdate + "]";
	}

}
