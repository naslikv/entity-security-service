package entityportal.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserLogActions {
    LOGIN("Login"),LOGOUT("Logout");
    String value;

}
