package entityportal.security.model;

import lombok.Data;

@Data
public class TokenResponse {
    String authToken;
    Long userID;
}
