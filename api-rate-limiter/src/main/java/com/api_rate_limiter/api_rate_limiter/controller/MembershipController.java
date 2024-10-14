package com.api_rate_limiter.api_rate_limiter.controller;

import com.api_rate_limiter.api_rate_limiter.entity.MembershipLookup;
import com.api_rate_limiter.api_rate_limiter.repository.MembershipLookupRepository;
import com.api_rate_limiter.api_rate_limiter.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/api/v1/membership")
public class MembershipController {

    @Autowired
    private MembershipLookupRepository membershipLookupRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public ResponseEntity<?> listAllMemberships() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            List<MembershipLookup> memberships = membershipLookupRepository.findAll();
            if (memberships.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No memberships found");
            }
            return ResponseEntity.ok(memberships);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }

    // TODO: Implement the following endpoint using post and relevant DTO
    @GetMapping("/subscribe/{membershipName}")
    public ResponseEntity<?> subscribeToMembership(@PathVariable String membershipName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null &&
            authentication.isAuthenticated() &&
            authentication.getPrincipal() instanceof UserDetails
          ) {
          UserDetails userDetails = (UserDetails) authentication.getPrincipal();
          boolean updated = userService.updateUserMembership(userDetails.getUsername(), membershipName);
          if(!updated) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Membership not found");
          }
          return ResponseEntity.ok("Successfully subscribed to membership: " + membershipName);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }

}
