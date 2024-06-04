package entityportal.security.service;

import entityportal.security.config.JwtService;
import entityportal.security.config.user.TabulaUserDetailsService;
import entityportal.security.config.user.UserDetail;
import entityportal.security.model.TokenRequest;
import entityportal.security.model.TokenResponse;
import entityportal.security.model.User;
import entityportal.security.persistence.entity.JWTTokenEntity;
import entityportal.security.persistence.repository.JWTTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
public class TokenService {

    private final TabulaUserDetailsService tabulaUserDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;
    private final JWTTokenRepository jwtTokenRepository;

    @Autowired
    public TokenService(TabulaUserDetailsService tabulaUserDetailsService,
                        BCryptPasswordEncoder bCryptPasswordEncoder,
                        JwtService jwtService,
                        JWTTokenRepository jwtTokenRepository) {
        this.tabulaUserDetailsService = tabulaUserDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtService = jwtService;
        this.jwtTokenRepository = jwtTokenRepository;
    }

    public TokenResponse generateToken(TokenRequest request) {
        UserDetail userDetails = (UserDetail) tabulaUserDetailsService.loadUserByUsername(request.getUserName());
        if (userDetails != null) {
            if (bCryptPasswordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
                String token = jwtService.generateToken(request.getUserName());
                JWTTokenEntity tokenEntity = new JWTTokenEntity();
                tokenEntity.setToken(token);
                tokenEntity.setIsExpired(false);
                tokenEntity.setIsRevoked(false);
                jwtTokenRepository.save(tokenEntity);
                TokenResponse tokenResponse = new TokenResponse();
                tokenResponse.setAuthToken(token);
                tokenResponse.setUserID(userDetails.getId());
                return tokenResponse;
            }
        }
        return null;
    }

    public User verifyToken(String authorization) {
        if (StringUtils.isNotBlank(authorization)) {
            authorization = authorization.replace("Bearer ", "");
            String userName = jwtService.extractUsername(authorization);
            UserDetail userDetail = (UserDetail) tabulaUserDetailsService.loadUserByUsername(userName);
            User user = new User();
            user.setId(userDetail.getId());
            user.setUserName(userDetail.getUsername());
            user.setPassword(userDetail.getPassword());
            user.setAuthorities(Collections.singletonList(userDetail.getAuthorities().stream()
                    .toList().get(0).getAuthority()));
            return user;
        }
        return null;
    }
}
