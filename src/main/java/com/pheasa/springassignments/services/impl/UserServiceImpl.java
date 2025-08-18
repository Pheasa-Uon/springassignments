package com.pheasa.springassignments.services.impl;

import com.pheasa.springassignments.entity.User;
import com.pheasa.springassignments.repository.UserRepository;
import com.pheasa.springassignments.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user){
        if(user.getUsercode() == null || user.getUsercode().isEmpty()){
            String maxUserCode = userRepository.findMaxUserCode();
            int nextcode = 1;
            if(maxUserCode != null){
                nextcode = Integer.parseInt(maxUserCode) + 1;
            }
            user.setUsercode(String.format("%05d", nextcode)); // fixed
        }
        return userRepository.save(user);
    }

    @Override
    public User updatedUser(Long id, User existingUser){
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()){
            User user = optionalUser.get();

            user.setUsername(existingUser.getUsername());
            user.setFullname(existingUser.getFullname());
            user.setEmail(existingUser.getEmail());
            user.setDescription(existingUser.getDescription());
            user.setUpdatedAt(LocalDateTime.now());
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found with id " + id);
        }
    }

    @Override
    public List<User> getAllUsers(){
        return userRepository.findByBstatusTrue(); // method name fixed to Java convention
    }

    @Override
    public User deletedUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setBstatus(false);  // Soft delete
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    @Override
    public List<User> searchUser(String keyword) { // fixed method name
        if (keyword == null || keyword.isBlank()) {
            return userRepository.findAll().stream()
                    .filter(User::isBstatus)
                    .toList();
        }
        return userRepository.searchActiveUsersByKeyword(keyword);
    }
}
