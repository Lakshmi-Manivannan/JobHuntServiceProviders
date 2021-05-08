package com.lakshmimanivannan.jobhuntappserviceproviders;

public class ServiceClass {

    String image;
    String u_name;
    String category,timestamp;
    String location;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ServiceClass(String image, String u_name, String category, String timestamp, String location) {
        this.image = image;
        this.u_name = u_name;
        this.category = category;
        this.timestamp = timestamp;
        this.location = location;
    }
}
