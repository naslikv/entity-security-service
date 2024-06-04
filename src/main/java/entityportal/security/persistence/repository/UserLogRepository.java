package entityportal.security.persistence.repository;

import entityportal.security.persistence.entity.UserLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserLogRepository extends JpaRepository<UserLogEntity,Long> {
    @Query(value = "select IFNULL(count(*),0) from user_log_entity where Action='Login' and UserID=:userID",nativeQuery = true)
    Long getLoginCountByUserID(@Param("userID") Long userID);
}
