package com.pheasa.springassignments.repository;

import com.pheasa.springassignments.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByBstatusTrue();
    @Query("SELECT u FROM User u WHERE u.bstatus = true AND " +
            "(" +
            "LOWER(u.usercode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.fullname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
            ")")
    List<User> searchActiveUsersByKeyword(@Param("keyword") String keyword);


    @Query("SELECT MAX(r.usercode) FROM User r")
    String findMaxUserCode();
}
