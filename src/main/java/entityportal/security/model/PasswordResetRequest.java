package entityportal.security.model;

import lombok.Data;

@Data
public class PasswordResetRequest {
    String userName;
    String oldPassword;
    String newPassword;
}
