package up.common.searchParams;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CollectSearchParam implements Serializable {
    private static final long serialVersionUID = 6849794470564667710L;

    private int level;
    private String deviceType;
    private String deviceName;
    private String beginTime;
    private String endTime;
}
