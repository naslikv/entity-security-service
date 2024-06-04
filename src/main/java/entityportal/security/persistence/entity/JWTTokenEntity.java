package entityportal.security.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name="JWTToken")
@Table(name="jwt_token_entity")
public class JWTTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    Long id;

    @Column(name="Token")
    String token;

    @Column(name="IsExpired")
    Boolean isExpired;

    @Column(name="IsRevoked")
    Boolean isRevoked;
}
