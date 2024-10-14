package com.api_rate_limiter.api_rate_limiter.repository;

import com.api_rate_limiter.api_rate_limiter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    User findByUsername(String username);
}
