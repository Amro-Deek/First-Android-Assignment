package com.example.perfstore;

import java.io.Serializable;

public class Perfume implements Serializable {

    public String name;
    public String brand;
    public String gender;
    public boolean onOffer;
    public int quantity;
    public double price;
    public int imageResId;

    public Perfume(String name, String brand, String gender, boolean onOffer, int quantity, double price, int imageResId) {

        this.name = name;
        this.brand = brand;
        this.gender = gender;
        this.onOffer = onOffer;
        this.quantity = quantity;
        this.price = price;
        this.imageResId = imageResId;
    }

    public Perfume(Perfume other) {
        this.name = other.name;
        this.brand = other.brand;
        this.gender = other.gender;
        this.onOffer = other.onOffer;
        this.quantity = other.quantity;
        this.price = other.price;
        this.imageResId = other.imageResId;
    }



    @Override
    public String toString() {
        return "Perfume{" +
                "name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", gender='" + gender + '\'' +
                ", onOffer=" + onOffer +
                ", quantity=" + quantity +
                ", price=" + price +
                ", imageResId=" + imageResId +
                '}';
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isOnOffer() {
        return onOffer;
    }

    public void setOnOffer(boolean onOffer) {
        this.onOffer = onOffer;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

}
