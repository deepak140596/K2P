package com.sapicons.deepak.k2psapicons.Others;

public class Ad {
    public String title;
    public String price;
    public String category;
    public String description;
    public String image;
    public String time;
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Ad(){}

    public Ad(String title, String price, String category, String description, String time, String id) {
        this.title = title;
        this.price = price;
        this.category = category;
        this.description = description;
        this.image = image;
        this.time = time;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}
