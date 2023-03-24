package com.safar.snippets.model;

public class Owner {

    String name, email, userType;
    int rollNumber;
    double rating;

    public Owner() {
    }

    public Owner(String name, String email, String userType, int rollNumber, double rating) {
        this.name = name;
        this.email = email;
        this.userType = userType;
        this.rollNumber = rollNumber;
        this.rating = rating;
    }

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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(int rollNumber) {
        this.rollNumber = rollNumber;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
