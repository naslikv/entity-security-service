package entityportal.security.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name="Role")
@Table(name="role_entity")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    Long id;

    @Column(name="Description")
    String description;
}
