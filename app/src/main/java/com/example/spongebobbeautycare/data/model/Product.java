package com.example.spongebobbeautycare.data.model;

public class Product {
    String name,
    description,
            howtouse,
    suitablefor,
            inggredients,
    image,
            category,id;

    public Product(String name, String description, String howtouse, String suitablefor, String inggredients, String image, String category) {
        this.name = name;
        this.description = description;
        this.howtouse = howtouse;
        this.suitablefor = suitablefor;
        this.inggredients = inggredients;
        this.image = image;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Product() {
         }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHowtouse() {
        return howtouse;
    }

    public void setHowtouse(String howtouse) {
        this.howtouse = howtouse;
    }

    public String getSuitablefor() {
        return suitablefor;
    }

    public void setSuitablefor(String suitablefor) {
        this.suitablefor = suitablefor;
    }

    public String getInggredients() {
        return inggredients;
    }

    public void setInggredients(String inggredients) {
        this.inggredients = inggredients;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
