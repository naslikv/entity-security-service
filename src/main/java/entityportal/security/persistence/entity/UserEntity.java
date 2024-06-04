package entityportal.security.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "User")
@Table(name = "user_entity")
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    Long id;

    @Column(name="UserName")
    String userName;

    @Column(name="Password")
    String password;

    @Column(name="Status")
    String status;

    @Column(name="ModifiedDate")
    Long modifiedDate;

    @Column(name="RoleID")
    Long roleID;

}
