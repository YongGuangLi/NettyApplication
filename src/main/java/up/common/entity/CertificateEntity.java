package up.common.entity;

import lombok.Data;

import java.io.Serializable;

public class CertificateEntity implements Serializable {
	private int id;

	private String ip;

	private String type;

	private String name;

	private String format;

	private String validityPeriod;

	private String subject;

	private String issue;

	private String content;

	private String originalContent;

	public int getId() {
		return id;
	}

	public String getIp() {
		return ip;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getFormat() {
		return format;
	}

	public String getValidityPeriod() {
		return validityPeriod;
	}

	public String getSubject() {
		return subject;
	}

	public String getIssue() {
		return issue;
	}

	public String getContent() {
		return content;
	}

	public String getOriginalContent() {
		return originalContent;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void setValidityPeriod(String validityPeriod) {
		this.validityPeriod = validityPeriod;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setOriginalContent(String originalContent) {
		this.originalContent = originalContent;
	}

	@Override
	public String toString() {
		return "CertificateEntity{" +
				"id=" + id +
				", ip='" + ip + '\'' +
				", type='" + type + '\'' +
				", name='" + name + '\'' +
				", format='" + format + '\'' +
				", validityPeriod='" + validityPeriod + '\'' +
				", subject='" + subject + '\'' +
				", issue='" + issue + '\'' +
				", content='" + content + '\'' +
				", originalContent='" + originalContent + '\'' +
				'}';
	}
}