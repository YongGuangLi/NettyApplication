package up.common.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlarmRuleEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private String name;
	
	private String deviceType;
	
	private int eventType;
	
	private int subType;
	
	private Integer timeRange;
	
	private int number;
	
	private Integer threshold;
	
	private int level;
	
	private boolean firstUploadFlag;
	
	private int combineUploadTime;
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
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public int getEventType() {
		return eventType;
	}
	public void setEventType(int eventType) {
		this.eventType = eventType;
	}
	public int getSubType() {
		return subType;
	}
	public void setSubType(int subType) {
		this.subType = subType;
	}
	public Integer getTimeRange() {
		return timeRange;
	}
	public void setTimeRange(Integer timeRange) {
		this.timeRange = timeRange;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public Integer getThreshold() {
		return threshold;
	}
	public void setThreshold(Integer threshold) {
		this.threshold = threshold;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public boolean isFirstUploadFlag() {
		return firstUploadFlag;
	}
	public void setFirstUploadFlag(boolean firstUploadFlag) {
		this.firstUploadFlag = firstUploadFlag;
	}
	public int getCombineUploadTime() {
		return combineUploadTime;
	}
	public void setCombineUploadTime(int combineUploadTime) {
		this.combineUploadTime = combineUploadTime;
	}
	
	
	
}
