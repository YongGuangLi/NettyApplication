package up.common.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AbnormalEventWarningEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 告警标识
	private int id;
	// 告警类别
	private String warningtype;
	// 创建时间
	private String createtime;
	// 更新时间
	private String updatetime;
	// 重要程度
	private int level;
	// 是否弹窗
	private int popup;
	// 是否声光告警
	private int alarm;
	// 是否上送主站
	private int sendup;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWarningtype() {
		return warningtype;
	}

	public void setWarningtype(String warningtype) {
		this.warningtype = warningtype;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getPopup() {
		return popup;
	}

	public void setPopup(int popup) {
		this.popup = popup;
	}

	public int getAlarm() {
		return alarm;
	}

	public void setAlarm(int alarm) {
		this.alarm = alarm;
	}

	public int getSendup() {
		return sendup;
	}

	public void setSendup(int sendup) {
		this.sendup = sendup;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

}
