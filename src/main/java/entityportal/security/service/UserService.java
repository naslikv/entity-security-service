package entityportal.security.service;
import entityportal.security.persistence.entity.JWTTokenEntity;
import entityportal.security.persistence.entity.RoleEntity;
import entityportal.security.persistence.entity.UserEntity;
import entityportal.security.persistence.entity.UserLogEntity;
import entityportal.security.persistence.repository.JWTTokenRepository;
import entityportal.security.persistence.repository.RoleRepository;
import entityportal.security.persistence.repository.UserLogRepository;
import entityportal.security.persistence.repository.UserRepository;
import entityportal.security.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class UserService {
    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserLogRepository userLogRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTTokenRepository jwtTokenRepository;

    @Autowired
    public UserService(JdbcTemplate jdbcTemplate,
                       UserRepository userRepository,
                       RoleRepository roleRepository,
                       UserLogRepository userLogRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       JWTTokenRepository jwtTokenRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userLogRepository = userLogRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtTokenRepository = jwtTokenRepository;
    }

    public List<Role> getAllRoles(){
        return jdbcTemplate.query("SELECT re.ID as id," +
                "re.Description as description " +
                 "FROM role_entity re ",(resultSet,rowIndex)->{
            Role role=new Role();
            role.setId(resultSet.getLong("id"));
            role.setDescription(resultSet.getString("description"));
            return role;
        });
    }

    public  List<User> getAllUsers(){
        return jdbcTemplate.query("SELECT ue.ID as id," +
                "ue.UserName as userName," +
                "ue.Password as password," +
                "ue.Status as status," +
                "ue.ModifiedDate as modifiedDate," +
                "ue.RoleID as roleID," +
                "re.Description as roleDescription " +
                "from user_entity ue " +
                "left outer join role_entity re on re.ID=ue.RoleID  order by ue.ModifiedDate desc ",(resultSet,rowIndex)->{
            User user=new User();
            user.setId(resultSet.getLong("id"));
            user.setUserName(resultSet.getString("userName"));
            user.setPassword(resultSet.getString("password"));
            user.setStatus(resultSet.getString("status"));
            try{
                ZoneId zoneId = ZoneId.of("Asia/Kolkata");
                Long modifiedDate = resultSet.getLong("modifiedDate");
                if (modifiedDate > 0L) {
                    user.setModifiedOn(ZonedDateTime.ofInstant(Instant.ofEpochSecond(modifiedDate), zoneId));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            Role role=new Role();
            role.setId(resultSet.getLong("roleID"));
            role.setDescription(resultSet.getString("roleDescription"));
            user.setRole(role);
            return user;
        });
    }

    public User addUser(User request){
        if(validateCreateUser(request)) {
            UserEntity entity = new UserEntity();
            UserEntity savedEntity = null;
            entity.setUserName(request.getUserName());
            entity.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
            entity.setStatus(request.getStatus());
            entity.setRoleID(request.getRole().getId());
            entity.setModifiedDate(Instant.now().getEpochSecond());
            try {
                savedEntity = userRepository.save(entity);

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (savedEntity != null) {
                request.setId(savedEntity.getId());
                request.setModifiedOn(ZonedDateTime.ofInstant(Instant.ofEpochSecond(savedEntity.getModifiedDate()), ZoneId.of("Asia/Kolkata")));
                return request;
            }
        }
        return null;
    }
//Todo add a better error handling mechanism
    private boolean validateCreateUser(User request){
        boolean isValid=true;
        if(StringUtils.isBlank(request.getUserName())){
            isValid=false;
        }
        else{
            if(userRepository.existsByUserName(request.getUserName())){
                isValid=false;
            }
        }
        if(StringUtils.isBlank(request.getPassword())){
            isValid=false;
        }
        if(StringUtils.isBlank(request.getStatus())){
            isValid=false;
        }
        if(request.getRole()==null||request.getRole().getId()==null){
            isValid=false;
        }
        return isValid;
    }

    public User modifyUser(User request){
        Optional<UserEntity> entity=userRepository.findById(request.getId());
        if(entity.isPresent()){
            UserEntity entityToModify=entity.get();
            if(StringUtils.isNotBlank(request.getStatus())){
                entityToModify.setStatus(request.getStatus());
            }
            if(StringUtils.isNotBlank(request.getPassword())){
                entityToModify.setPassword(request.getPassword());
            }
            if(request.getRole()!=null&&request.getRole().getId()!=null){
                entityToModify.setRoleID(request.getRole().getId());
            }
            entityToModify.setModifiedDate(Instant.now().getEpochSecond());
            entityToModify=userRepository.save(entityToModify);
            return getUserByID(entityToModify.getId());
        }
        return null;
    }

    public User updateUser(User request){
        Optional<UserEntity> entity=userRepository.findById(request.getId());
        if(entity.isPresent()){
            UserEntity entityToUpdate=entity.get();
            entityToUpdate.setPassword(request.getPassword());
            entityToUpdate.setStatus(request.getStatus());
            if(request.getRole()!=null) {
                entityToUpdate.setRoleID(request.getRole().getId());
            }
            entityToUpdate.setModifiedDate(Instant.now().getEpochSecond());
            entityToUpdate=userRepository.save(entityToUpdate);
            return getUserByID(entityToUpdate.getId());
        }
        return null;
    }

    public User getUserByID(Long id){
        Optional<UserEntity> userEntityOptional=userRepository.findById(id);
        if(userEntityOptional.isPresent()){
            User user=new User();
            UserEntity userEntity=userEntityOptional.get();
            user.setId(userEntity.getId());
            user.setUserName(userEntity.getUserName());
            user.setPassword(userEntity.getPassword());
            user.setStatus(userEntity.getStatus());
            user.setModifiedOn(ZonedDateTime.ofInstant(Instant.ofEpochSecond(userEntity.getModifiedDate()),ZoneId.of("Asia/Kolkata")));
            Optional<RoleEntity> roleEntity=roleRepository.findById(userEntity.getRoleID());
            if(roleEntity.isPresent()){
                Role role=new Role();
                role.setId(roleEntity.get().getId());
                role.setDescription(roleEntity.get().getDescription());
                user.setRole(role);
            }
            Long loginCount=userLogRepository.getLoginCountByUserID(id);
            user.setFirstLogin(loginCount==0L);
            return user;
        }
        return null;
    }

    public UserLog addUserLog(UserLog request, String authorizationHeader){
        if(userRepository.existsById(request.getUser().getId())){
            UserLogEntity logEntity=new UserLogEntity();
            logEntity.setUserID(request.getUser().getId());
            logEntity.setAction(request.getAction().getValue());
            logEntity.setDate(Instant.now().getEpochSecond());
            logEntity=userLogRepository.save(logEntity);
            if(request.getAction()== UserLogActions.LOGOUT){
                String jwt=authorizationHeader.replace("Bearer ","");
                Optional<JWTTokenEntity> tokenEntity=jwtTokenRepository.findByToken(jwt);
                if(tokenEntity.isPresent()){
                    JWTTokenEntity jwtTokenEntity=tokenEntity.get();
                    jwtTokenEntity.setIsExpired(true);
                    jwtTokenEntity.setIsRevoked(true);
                    jwtTokenRepository.save(jwtTokenEntity);
                }
            }
            request.setId(logEntity.getId());
            request.setDate(ZonedDateTime.ofInstant(Instant.ofEpochSecond(logEntity.getDate()),ZoneId.of("Asia/Kolkata")));
            return request;
        }
        return null;
    }
    public boolean resetPassword(PasswordResetRequest request){
        if(userRepository.existsByUserName(request.getUserName())){
            Optional<UserEntity> userEntity=userRepository.findByUserName(request.getUserName());
            if(userEntity.isPresent()&&bCryptPasswordEncoder.matches(request.getOldPassword(), userEntity.get().getPassword())){
                User user=new User();
                user.setId(userEntity.get().getId());
                user.setPassword(bCryptPasswordEncoder.encode(request.getNewPassword()));
                modifyUser(user);
                return true;
            }
        }
        return false;
    }

}
