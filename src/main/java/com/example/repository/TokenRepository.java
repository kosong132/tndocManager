package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    boolean existsByToken(String token);

    void deleteByToken(String token); // Optional: Clean up tokens when they are used
}
