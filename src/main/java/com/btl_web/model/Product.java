package com.btl_web.model;

import java.math.BigDecimal;

public class Product {
    private String id;
    private String name;
    private String group;
    private String segment;
    private String size;
    private String color;
    private BigDecimal price;

    public Product() {
    }

    public Product(String id, String name, String group, String segment, String size, String color,
            BigDecimal price) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.segment = segment;
        this.size = size;
        this.color = color;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}