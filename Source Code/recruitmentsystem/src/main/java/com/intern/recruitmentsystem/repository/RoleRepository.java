package com.fpt.recruitmentsystem.repository;

import com.fpt.recruitmentsystem.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
