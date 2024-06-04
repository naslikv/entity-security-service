package entityportal.security.endpoint;

import entityportal.security.model.PasswordResetRequest;
import entityportal.security.model.Role;
import entityportal.security.model.User;
import entityportal.security.model.UserLog;
import entityportal.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserEndpoint {
    private final UserService userService;

    @Autowired
    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        if (userList != null && !userList.isEmpty()) {
            return ResponseEntity.ok(userList);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserByID(@PathVariable("id") Long id) {
        User response = userService.getUserByID(id);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roleList = userService.getAllRoles();
        if (roleList != null && !roleList.isEmpty()) {
            return ResponseEntity.ok(roleList);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping()
    public ResponseEntity<User> addUser(@RequestBody User request) {
        User response = userService.addUser(request);
        if (response != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        return ResponseEntity.unprocessableEntity().build();
    }


    @PatchMapping("/{id}")
    public ResponseEntity<User> modifyUser(@PathVariable("id") Long id,
                                           @RequestBody User request) {
        request.setId(id);
        User modified = userService.modifyUser(request);
        if (modified != null) {
            return ResponseEntity.ok(modified);
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id,
                                           @RequestBody User request) {
        request.setId(id);
        User modified = userService.updateUser(request);
        if (modified != null) {
            return ResponseEntity.ok(modified);
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    @PostMapping("/logs")
    public ResponseEntity addUserLog(@RequestBody UserLog request, @RequestHeader("Authorization") String authorization) {
        UserLog response = userService.addUserLog(request,authorization);
        if (response != null) {
            return ResponseEntity.accepted().build();
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    @PatchMapping("/passwords/reset")
    public ResponseEntity resetPassword(@RequestBody PasswordResetRequest request) {
        boolean isModified = userService.resetPassword(request);
        if (isModified) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.unprocessableEntity().build();
    }
}
