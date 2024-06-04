package entityportal.security.endpoint;

import entityportal.security.model.TokenRequest;
import entityportal.security.model.TokenResponse;
import entityportal.security.model.User;
import entityportal.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/tokens")
public class TokenEndpoint {
    private final TokenService tokenService;

    @Autowired
    public TokenEndpoint(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping()
    public ResponseEntity<TokenResponse> createToken(@RequestBody TokenRequest request) {
        TokenResponse response=tokenService.generateToken(request);
        if(response!=null){
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    @PostMapping("/verify")
    public ResponseEntity<User> verifyToken(@RequestHeader("Authorization") String authorization){
        User userDetail=tokenService.verifyToken(authorization);
        if(userDetail!=null){
            return ResponseEntity.ok(userDetail);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
