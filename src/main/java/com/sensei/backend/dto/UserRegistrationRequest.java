package com.sensei.backend.dto;

public class UserRegistrationRequest {
    
    private String name;
    private String email;
    private String password;
    private String referralCode;  // Optional: code user enters during signup
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getReferralCode() { return referralCode; }
    public void setReferralCode(String referralCode) { this.referralCode = referralCode; }
}
