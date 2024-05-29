package com.fpt.recruitmentsystem.repository;

import com.fpt.recruitmentsystem.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    public Admin findAdminByAccountId(Integer id);
}
