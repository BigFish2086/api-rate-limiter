package com.api_rate_limiter.api_rate_limiter.service;

import com.api_rate_limiter.api_rate_limiter.dto.UserDto;
import com.api_rate_limiter.api_rate_limiter.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    User findUserByUsername(String username);
}
