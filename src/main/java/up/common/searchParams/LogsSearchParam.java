package up.common.searchParams;

import lombok.Data;

import java.io.Serializable;

@Data
public class LogsSearchParam implements Serializable {
    private static final long serialVersionUID = 6849794470554667710L;

    private String type;
    private int level;
    private String keyWord;
    private int sort;
    private String startTime;
    private String endTime;
}
