package entityportal.security.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import entityportal.security.common.Constants;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class User {
    Long id;
    String userName;
    String password;
    String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TIME_FORMAT_ISO_8601)
    ZonedDateTime modifiedOn;
    Role role;
    List<String> authorities;
    boolean firstLogin;
}
