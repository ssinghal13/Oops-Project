package com.example.foodapp.Model;

public class CartItem {
    String Name;
    String Price;
    String Quantity;
    String Product_ID;

    public CartItem() {
    }

    public CartItem(String name, String price, String quantity, String product_ID) {
        Name = name;
        Price = price;
        Quantity = quantity;
        Product_ID=product_ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public void setProduct_ID(String product_ID){
        Product_ID=product_ID;
    }

    public String getProduct_ID(){
        return Product_ID;
    }
}
