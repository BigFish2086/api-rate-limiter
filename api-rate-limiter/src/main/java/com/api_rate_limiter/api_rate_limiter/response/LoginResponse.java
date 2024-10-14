package com.api_rate_limiter.api_rate_limiter.response;

public class LoginResponse {
    private String token;

    private long expiresIn;

    public String getToken() {
        return token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public LoginResponse(String token, long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }
}
