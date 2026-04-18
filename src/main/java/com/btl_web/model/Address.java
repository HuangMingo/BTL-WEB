package com.btl_web.model;

public class Address {
    private int id;
    private String name;
    private String username;
    private String recipientName;
    private String recipientPhone;
    private String shippingAddress;

    public Address() {
    }

    public Address(int id, String name, String username, String recipientName, String recipientPhone, String shippingAddress) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.recipientName = recipientName;
        this.recipientPhone = recipientPhone;
        this.shippingAddress = shippingAddress;
    }

    public Address(int id, String shippingAddress) {
        this(id, null, null, null, null, shippingAddress);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }


  
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    } 
}
