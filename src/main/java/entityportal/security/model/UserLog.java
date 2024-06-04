package entityportal.security.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import entityportal.security.common.Constants;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class UserLog {
    Long id;
    UserLogActions action;
    User user;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT_ISO_8601)
    ZonedDateTime date;
}
