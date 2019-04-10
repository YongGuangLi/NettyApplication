package up.common.entity;

import java.io.Serializable;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BackupdataDateEntity implements Serializable {
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private String createtime;

    private String date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCreatetime() {
        return createtime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date == null ? null : date.trim();
    }
}