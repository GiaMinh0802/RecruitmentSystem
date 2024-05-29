package com.fpt.recruitmentsystem.repository;

import com.fpt.recruitmentsystem.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    @Query("SELECT t FROM Token t WHERE t.account.id = :id")
    Token findTokenByAccountId(Integer id);

    @Query("SELECT t FROM Token t WHERE t.account.email = :email")
    Optional<Token> findTokenByAccountEmail(String email);

    Optional<Token> findByToken(String token);

    Optional<Token> findByAccountToken(String token);
}
