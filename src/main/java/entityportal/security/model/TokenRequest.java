package entityportal.security.model;

import lombok.Data;

@Data
public class TokenRequest {
    String userName;
    String password;
}
