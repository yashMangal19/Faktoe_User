package com.example.faktoe_user.Products;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;

public class ProductModel {
    private String Name, Description, Category, Brand, Rating, ImageUrl,Id,ShopId;
    private long MRP, SellingPrice;
    private DocumentReference DocRef;
    public ProductModel(){

    }

    public ProductModel(String name, String description, String category, long MRP, long sellingPrice, String brand, String rating, String ImageUrl, String Id,String ShopId) {
        this.Name = name;
        this.Description = description;
        this.Category = category;
        this.MRP = MRP;
        this.SellingPrice = sellingPrice;
        this.Brand = brand;
        this.Rating = rating;
        this.ImageUrl = ImageUrl;
        this.Id = Id;
        this.ShopId = ShopId;
    }
    @Exclude
    public DocumentReference getDocRef() {
        return DocRef;
    }

    public void setDocRef(DocumentReference docRef) {
        DocRef = docRef;
    }

    public long getMRP() {
        return MRP;
    }

    public void setMRP(long MRP) {
        this.MRP = MRP;
    }

    public long getSellingPrice() {
        return SellingPrice;
    }

    public void setSellingPrice(long sellingPrice) {
        SellingPrice = sellingPrice;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getShopId() {
        return ShopId;
    }

    public void setShopId(String shopId) {
        ShopId = shopId;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }
}
