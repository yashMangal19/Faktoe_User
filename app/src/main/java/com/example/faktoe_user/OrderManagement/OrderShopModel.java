package com.example.faktoe_user.OrderManagement;

public class OrderShopModel {
    private String name , shopId;
    private String userId;
    private long sellingPrice, count;

    public OrderShopModel(){

    }

    public OrderShopModel(String name, long count, long sellingPrice, String shopId,String userId) {
        this.name = name;
        this.count = count;
        this.sellingPrice = sellingPrice;
        this.shopId = shopId;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(long sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}

