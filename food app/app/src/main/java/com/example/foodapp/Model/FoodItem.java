package com.example.foodapp.Model;

public class FoodItem {
    String base_price;
    String name;

    public FoodItem(){

    }

    public FoodItem(String price, String name) {
        this.base_price = price;
        this.name = name;
    }

    public String getBase_price() {
        return base_price;
    }

    public void setBase_price(String base_price) {
        this.base_price = base_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
