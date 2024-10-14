package com.api_rate_limiter.api_rate_limiter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RateLimitingService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public boolean isRateLimited(String username, int requestsPerSecond, int rateLimitWindowSeconds) {
        String redisKey = "rate_limit:" + username;
        String rateLimitKey = redisKey + ":count";

        // Increment the counter
        Long requestCount = redisTemplate.opsForValue().increment(rateLimitKey, 1);

        if (requestCount == 1) {
            // Set expiration on the first request
            redisTemplate.expire(rateLimitKey, rateLimitWindowSeconds, TimeUnit.SECONDS);
        }

        return requestCount > requestsPerSecond; // Return true if the user is rate limited
    }
}
