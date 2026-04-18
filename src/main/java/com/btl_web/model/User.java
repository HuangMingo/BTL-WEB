package com.btl_web.model;

import java.util.ArrayList;


public class User {

    private String username;
    private String fullName;
    private String password;
    private int age;
    private String gender;
    private String email;
    private String phone;
    private String baseAddress;
    private Address defaultAddress; // dia chi mac dinh

    public User(String username, String fullName, String password) {
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.age = 0;
        this.gender = "";
        this.email = "";
        this.phone = "";
        this.baseAddress = "";
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBaseAddress() {
        return baseAddress;
    }

    public void setBaseAddress(String baseAddress) {
        this.baseAddress = baseAddress;
    }


    public Address getDefaultAddress() {
        return defaultAddress;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setDefaultAddress(Address defaultAddress) {
        this.defaultAddress = defaultAddress;
    }
    
}
