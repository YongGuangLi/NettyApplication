package up.common.entity;

import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PageEntity implements Serializable {
    int pageNum; // 当前页数
    int total; // 总记录数
    int pages; // 总页数
    int pageSize; // 每页记录数

    @Override
    public String toString() {
        return "PageEntity{" +
                "pageNum=" + pageNum +
                ", total=" + total +
                ", pages=" + pages +
                ", pageSize=" + pageSize +
                '}';
    }
}
