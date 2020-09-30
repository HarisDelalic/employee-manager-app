package com.dela.employeemanagerapp.repository;

import com.dela.employeemanagerapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByEmail(String email);

    @Query("from User u where u.username like :searchTerm% " +
            "or u.email like :searchTerm% " +
            "or u.firstName like :searchTerm% " +
            "or u.lastName like :searchTerm%")
    List<User> findUsers(@Param("searchTerm") String searchTerm);

    void deleteByUsername(String username);
}
