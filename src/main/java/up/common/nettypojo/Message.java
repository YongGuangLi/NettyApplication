package up.common.nettypojo;

import java.io.Serializable;

import lombok.Data;

/**
 * @ClassName: Message
 * @Description: 用于Netty客户端与服务端传输的对象
 * @Author: chenjd
 * @Date: 2019-02-15 16:12
 **/

@Data
public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String head; // 请求头，用于判断用哪个类做处理
	private Object body; // 请求Body,做请求参数和返回信息

	public String getHead() {
		return head;
	}

	public Object getBody() {
		return body;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public void setBody(Object body) {
		this.body = body;
	}
}
