package com.fpt.recruitmentsystem.repository;

import com.fpt.recruitmentsystem.model.Account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findById(Integer id);
    Account getEmailById(Integer id);
    boolean existsAccountByEmail(String email);
	Optional<Account> findByEmail(String email);
	@Query("SELECT a FROM Account a JOIN a.role r WHERE r.name = :rolename")
	List<Account> findAllAccountsforrole(@Param("rolename") String rolename);
	
	Page<Account> findAll(Pageable pageable);
}
