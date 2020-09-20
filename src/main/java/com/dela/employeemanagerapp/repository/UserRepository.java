package com.dela.employeemanagerapp.repository;

import com.dela.employeemanagerapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
