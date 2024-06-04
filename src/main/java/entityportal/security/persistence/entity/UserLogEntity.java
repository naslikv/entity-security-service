package entityportal.security.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name="UserLog")
@Table(name="user_log_entity")
public class UserLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    Long id;

    @Column(name="Action")
    String action;

    @Column(name="Date")
    Long date;

    @Column(name="UserID")
    Long userID;
}
