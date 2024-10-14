package com.api_rate_limiter.api_rate_limiter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "membership_lookup", schema = "rate_limiter")
public class MembershipLookup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "membership_name", nullable = false, unique = true)
    private String membershipName;

    @Column(name = "requests_per_second", nullable = false)
    private Integer requestsPerSecond;

    @Column(name = "rate_limit_window_seconds", nullable = false)
    private Integer rateLimitWindowSeconds;

}

