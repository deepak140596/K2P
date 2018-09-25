package com.sapicons.deepak.k2psapicons.Others;

public class Local {
    private String title, image, price, ad;

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public Local(String title, String image, String price, String ad, Long timestamp) {
        this.title = title;
        this.image = image;
        this.price = price;
        this.ad = ad;
        this.timestamp = timestamp;
    }

    private Long timestamp;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Local(){}


}
