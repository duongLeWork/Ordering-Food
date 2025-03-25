package com.example.foodordering.models;


public class User {
    private String name;
    private String email;
    private String role;
    private String profileImage;

    // Constructor
    public User(String name, String email, String role, String profileImage) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.profileImage = profileImage;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }
}
