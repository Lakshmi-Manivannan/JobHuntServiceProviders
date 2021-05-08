package com.lakshmimanivannan.jobhuntappserviceproviders;

public class AcceptedRequestHelperClass {
    String username,servicename,location,category,user_img,service_img,timestamp,servicenumber,usernumber,servicemail,useremail,description;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUser_img() {
        return user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getService_img() {
        return service_img;
    }

    public void setService_img(String service_img) {
        this.service_img = service_img;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getServicenumber() {
        return servicenumber;
    }

    public void setServicenumber(String servicenumber) {
        this.servicenumber = servicenumber;
    }

    public String getUsernumber() {
        return usernumber;
    }

    public void setUsernumber(String usernumber) {
        this.usernumber = usernumber;
    }

    public String getServicemail() {
        return servicemail;
    }

    public void setServicemail(String servicemail) {
        this.servicemail = servicemail;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AcceptedRequestHelperClass(String username, String servicename, String location, String category, String user_img, String service_img, String timestamp, String servicenumber, String usernumber, String servicemail, String useremail, String description) {
        this.username = username;
        this.servicename = servicename;
        this.location = location;
        this.category = category;
        this.user_img = user_img;
        this.service_img = service_img;
        this.timestamp = timestamp;
        this.servicenumber = servicenumber;
        this.usernumber = usernumber;
        this.servicemail = servicemail;
        this.useremail = useremail;
        this.description = description;
    }
}
