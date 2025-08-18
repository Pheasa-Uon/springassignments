package com.pheasa.springassignments.services;

import com.pheasa.springassignments.entity.User;

import java.util.List;

public interface UserService {

    User createUser(User user);
    User updatedUser(Long id, User user);
    User deletedUser(Long id);
    List<User> getAllUsers();
    List<User> searchUser(String keyword);

}
