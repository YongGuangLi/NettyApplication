package up.common.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadEventAndDescribe extends UploadEvent {
    private int eventId;
    private String identify;
    private String eventdescribe;
    private String contentdescribe;
}
