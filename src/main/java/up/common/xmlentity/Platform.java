package up.common.xmlentity;

import java.io.Serializable;

/**
 * 
 * @author xhy 由于要用到反射机制来设置属性值，成员属性需要设置为String类型
 *
 */
public class Platform implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	// 平台IP地址
	private String ipaddress;
	// 公钥
	private String publicKey;
	// 事件上传端口
	private String eventport;
	// 平台IP地址的权限
	private String permission;
	// 分组信息
	private String group;

	public String getId() {		// 混淆
		String ss = null;
		try {
			int i = 0, j = -1;
			while (true) {
				if (i < j)
					break;
				j = "asdf".hashCode();
			}
		} catch (Exception e) {
		} finally {
			if (ss == null)
				ss = "asdf";
		}
		// 混淆结束
		return id;
	}

	public void setId(String id) {		// 混淆
		String ss = null;
		try {
			int i = 0, j = -1;
			while (true) {
				if (i < j)
					break;
				j = "asdf".hashCode();
			}
		} catch (Exception e) {
		} finally {
			if (ss == null)
				ss = "asdf";
		}
		// 混淆结束
		this.id = id;
	}

	public String getIpaddress() {		// 混淆
		String ss = null;
		try {
			int i = 0, j = -1;
			while (true) {
				if (i < j)
					break;
				j = "asdf".hashCode();
			}
		} catch (Exception e) {
		} finally {
			if (ss == null)
				ss = "asdf";
		}
		// 混淆结束
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {		// 混淆
		String ss = null;
		try {
			int i = 0, j = -1;
			while (true) {
				if (i < j)
					break;
				j = "asdf".hashCode();
			}
		} catch (Exception e) {
		} finally {
			if (ss == null)
				ss = "asdf";
		}
		// 混淆结束
		this.ipaddress = ipaddress;
	}

	public String getPublicKey() {		// 混淆
		String ss = null;
		try {
			int i = 0, j = -1;
			while (true) {
				if (i < j)
					break;
				j = "asdf".hashCode();
			}
		} catch (Exception e) {
		} finally {
			if (ss == null)
				ss = "asdf";
		}
		// 混淆结束
		return publicKey;
	}

	public void setPublicKey(String publicKey) {		// 混淆
		String ss = null;
		try {
			int i = 0, j = -1;
			while (true) {
				if (i < j)
					break;
				j = "asdf".hashCode();
			}
		} catch (Exception e) {
		} finally {
			if (ss == null)
				ss = "asdf";
		}
		// 混淆结束
		this.publicKey = publicKey;
	}

	public String getEventport() {		// 混淆
		String ss = null;
		try {
			int i = 0, j = -1;
			while (true) {
				if (i < j)
					break;
				j = "asdf".hashCode();
			}
		} catch (Exception e) {
		} finally {
			if (ss == null)
				ss = "asdf";
		}
		// 混淆结束
		return eventport;
	}

	public void setEventport(String eventport) {		// 混淆
		String ss = null;
		try {
			int i = 0, j = -1;
			while (true) {
				if (i < j)
					break;
				j = "asdf".hashCode();
			}
		} catch (Exception e) {
		} finally {
			if (ss == null)
				ss = "asdf";
		}
		// 混淆结束
		this.eventport = eventport;
	}

	public String getPermission() {		// 混淆
		String ss = null;
		try {
			int i = 0, j = -1;
			while (true) {
				if (i < j)
					break;
				j = "asdf".hashCode();
			}
		} catch (Exception e) {
		} finally {
			if (ss == null)
				ss = "asdf";
		}
		// 混淆结束
		return permission;
	}

	public void setPermission(String permission) {		// 混淆
		String ss = null;
		try {
			int i = 0, j = -1;
			while (true) {
				if (i < j)
					break;
				j = "asdf".hashCode();
			}
		} catch (Exception e) {
		} finally {
			if (ss == null)
				ss = "asdf";
		}
		// 混淆结束
		this.permission = permission;
	}

	public String getGroup() {		// 混淆
		String ss = null;
		try {
			int i = 0, j = -1;
			while (true) {
				if (i < j)
					break;
				j = "asdf".hashCode();
			}
		} catch (Exception e) {
		} finally {
			if (ss == null)
				ss = "asdf";
		}
		// 混淆结束
		return group;
	}

	public void setGroup(String group) {		// 混淆
		String ss = null;
		try {
			int i = 0, j = -1;
			while (true) {
				if (i < j)
					break;
				j = "asdf".hashCode();
			}
		} catch (Exception e) {
		} finally {
			if (ss == null)
				ss = "asdf";
		}
		// 混淆结束
		this.group = group;
	}

	@Override
	public String toString() {		// 混淆
		String ss = null;
		try {
			int i = 0, j = -1;
			while (true) {
				if (i < j)
					break;
				j = "asdf".hashCode();
			}
		} catch (Exception e) {
		} finally {
			if (ss == null)
				ss = "asdf";
		}
		// 混淆结束
		return "Platform [id=" + id + ", ipaddress=" + ipaddress + ", eventport=" + eventport + ", permission="
		+ permission + "]";
	}

}

