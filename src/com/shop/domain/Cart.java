package com.shop.domain;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    public Map<String, CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Map<String, CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    private Map<String,CartItem> cartItems = new HashMap<String,CartItem>();
    private double total;

}
