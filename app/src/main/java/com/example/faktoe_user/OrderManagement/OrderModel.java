package com.example.faktoe_user.OrderManagement;

public class OrderModel {
    private String shopName, shopId, isPacked;

    public OrderModel(){
    }

    public OrderModel(String shopName, String shopId, String isPacked) {
        this.shopName = shopName;
        this.shopId = shopId;
    }

    public String getIsPacked() {
        return isPacked;
    }

    public void setIsPacked(String isPacked) {
        this.isPacked = isPacked;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
