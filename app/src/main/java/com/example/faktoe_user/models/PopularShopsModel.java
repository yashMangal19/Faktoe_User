package com.example.faktoe_user.models;

import android.widget.ImageView;

public class PopularShopsModel {
   String  shopName;
   String address;
   String signatureImage;
   String id;

   public PopularShopsModel() {

   }

//    public PopularShopsModel(String signatureImage) {
//        this.signatureImage = signatureImage;
//    }

    public String getSignatureImage() {
        return signatureImage;
    }

    public void setSignatureImage(String signatureImage) {
        this.signatureImage = signatureImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PopularShopsModel(String signatureImage, String shopName, String address, String id) {
        this.shopName = shopName;
        this.address = address;
        this.signatureImage = signatureImage;
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
