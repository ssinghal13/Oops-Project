package com.example.foodapp.Model;

public class InfoItem {
        int CartItemsTotal;
        int CartTotal;
        int Discount;

    public InfoItem() {

    }
    public InfoItem(int cartItemsTotal, int cartTotal, int discount){
        CartItemsTotal=cartItemsTotal;
        CartTotal=cartTotal;
        Discount=discount;
    }

    public int getCartItemsTotal() {
        return CartItemsTotal;
    }

    public void setCartItemsTotal(int cartItemsTotal) {
        CartItemsTotal = cartItemsTotal;
    }

    public int getCartTotal() {
        return CartTotal;
    }

    public void setCartTotal(int cartTotal) {
        CartTotal = cartTotal;
    }

    public int getDiscount() {
        return Discount;
    }

    public void setDiscount(int discount) {
        Discount = discount;
    }
}
