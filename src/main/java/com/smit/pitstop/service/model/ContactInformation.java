package com.smit.pitstop.service.model;

public record ContactInformation(String firstName, String lastName, String email, String phoneNumber, String comment) {

    @Override
    public String toString() {
        return firstName + " " + lastName + ", " + email + " " + phoneNumber + " " + comment;
    }

}
