package entityportal.security.config.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("userDetailsService")
public class TabulaUserDetailsService implements UserDetailsService {
    private final JdbcTemplate jdbcTemplate;

    private static final String USER_BY_USERNAME = "select ue.ID as id,ue.UserName as userName,ue.Password as password ,re.Description as role from user_entity ue " +
            "left outer join role_entity re on re.ID=ue.RoleID where ue.UserName=?";

    @Autowired
    public TabulaUserDetailsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return jdbcTemplate.queryForObject(USER_BY_USERNAME, new Object[]{
                new SqlParameterValue(Types.VARCHAR, username)
        }, (rs, rowNum) -> {
            UserDetail userDetail = new UserDetail();
            userDetail.setId(rs.getLong("id"));
            userDetail.setUsername(rs.getString("userName"));
            userDetail.setPassword(rs.getString("password"));
            String role=rs.getString("role");
            List<GrantedAuthority> grantedAuthorityList=new ArrayList<>();
            GrantedAuthority grantedAuthority=null;
            if(role.equalsIgnoreCase("admin")){
               grantedAuthority=new SimpleGrantedAuthority("ROLE_ADMIN");
            }
            else{
                 grantedAuthority=new SimpleGrantedAuthority("ROLE_USER");
            }
            grantedAuthorityList.add(grantedAuthority);
            userDetail.setAUTHORITIES(grantedAuthorityList);
            return userDetail;
        });
    }
}
