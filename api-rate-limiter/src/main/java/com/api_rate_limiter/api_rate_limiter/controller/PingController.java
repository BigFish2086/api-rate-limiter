package com.api_rate_limiter.api_rate_limiter.controller;

import com.api_rate_limiter.api_rate_limiter.entity.User;
import com.api_rate_limiter.api_rate_limiter.entity.MembershipLookup;
import com.api_rate_limiter.api_rate_limiter.repository.UserRepository;
import com.api_rate_limiter.api_rate_limiter.repository.MembershipLookupRepository;
import com.api_rate_limiter.api_rate_limiter.service.RateLimitingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1")
public class PingController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MembershipLookupRepository membershipLookupRepository;

    @Autowired
    private RateLimitingService rateLimitingService;

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String userName = userDetails.getUsername();
            User user = userRepository.findByUsername(userName).orElse(null);
            MembershipLookup membership = user.getMembershipLookup();

            if (rateLimitingService.isRateLimited(userName, membership.getRequestsPerSecond(), membership.getRateLimitWindowSeconds())) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded");
            }

            return ResponseEntity.ok("Pong");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }
}
