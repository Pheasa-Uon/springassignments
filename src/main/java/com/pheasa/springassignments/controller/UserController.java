package com.pheasa.springassignments.controller;

import com.pheasa.springassignments.configuration.annotations.AuditFilter;
import com.pheasa.springassignments.configuration.annotations.MyRetryable;
import com.pheasa.springassignments.entity.User;
import com.pheasa.springassignments.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "${app.cors.allowed-origins}")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @AuditFilter
    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @AuditFilter
    @PostMapping
    public User registerUser(@RequestBody User user){
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setBstatus(true);

        return userService.createUser(user);
    }

    @AuditFilter
    @PutMapping("/edit/{id}")
    public ResponseEntity<User> updatedUser(@PathVariable Long id, @RequestBody User user){
        User updated = userService.updatedUser(id, user);

        return ResponseEntity.ok(updated);
    }

    @AuditFilter
    @MyRetryable(maxRetries = 3, retryDelay = 2000, retryFor = {RuntimeException.class})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delectedUser(@PathVariable Long id){
        userService.deletedUser(id);
        return ResponseEntity.ok("User has been deleted : " + id);
    }

    @AuditFilter
    @MyRetryable(maxRetries = 3, retryDelay = 2000, retryFor = {RuntimeException.class})
    @GetMapping("/search")
    public List<User> searchUsers(@RequestParam(required = false) String keyword) {
        return userService.searchUser(keyword);
    }
}
