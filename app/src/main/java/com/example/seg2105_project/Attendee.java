package com.example.seg2105_project;

public class Attendee {
    private String name;
    private String email;
    private String registrationStatus; // "pending", "approved", or "rejected"

    public Attendee(String name, String email) {
        this.name = name;
        this.email = email;
        this.registrationStatus = "pending"; // Default status
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(String status) {
        this.registrationStatus = status;
    }
}

