package com.api_rate_limiter.api_rate_limiter.service;

import com.api_rate_limiter.api_rate_limiter.dto.UserDto;
import com.api_rate_limiter.api_rate_limiter.entity.MembershipLookup;
import com.api_rate_limiter.api_rate_limiter.entity.User;
import com.api_rate_limiter.api_rate_limiter.repository.MembershipLookupRepository;
import com.api_rate_limiter.api_rate_limiter.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private MembershipLookupRepository membershipLookupRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, MembershipLookupRepository membershipLookupRepository, PasswordEncoder passwordEncoder) {
      this.userRepository = userRepository;
      this.membershipLookupRepository = membershipLookupRepository;
      this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        MembershipLookup membership = membershipLookupRepository.findByMembershipName("free");
        if (membership == null) {
          throw new RuntimeException("Default membership not found!");
        }
        user.setMembershipLookup(membership);

        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setModifiedAt(now);
        userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User findUserByUsername(String username) {
      return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public boolean updateUserMembership(String username, String membershipName) {
      User user = userRepository.findByUsername(username).orElse(null);
      if (user == null) {
        return false;
      }

      MembershipLookup membership = membershipLookupRepository.findByMembershipName(membershipName);
      if (membership == null) {
        return false;
      }

      user.setMembershipLookup(membership);
      userRepository.save(user);
      return true;
    }

    private UserDto convertEntityToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        return userDto;
    }
}
