package com.example.arsalansiddiq.beem.models;

public class UserSelectedItems {

    private int skuID, quantityLoose, quantityCarton, price, totalAmount;

    public UserSelectedItems(int skuID, int quantityLoose, int quantityCarton, int price, int totalAmount) {
        this.skuID = skuID;
        this.quantityLoose = quantityLoose;
        this.quantityCarton = quantityCarton;
        this.price = price;
        this.totalAmount = totalAmount;
    }

    public int getSkuID() {
        return skuID;
    }

    public int getQuantityLoose() {
        return quantityLoose;
    }

    public int getQuantityCarton() {
        return quantityCarton;
    }

    public int getPrice() {
        return price;
    }

    public int getTotalAmount() {
        return totalAmount;
    }
}
