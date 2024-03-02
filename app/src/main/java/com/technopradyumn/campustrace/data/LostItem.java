package com.technopradyumn.campustrace.data;

public class LostItem {
    private String itemId;
    private String image;
    private String name;
    private String category;
    private String locationLastSeen;
    private String contactNo;
    private String additionalDetail;
    private String time;
    private String status;

    public LostItem(String itemId, String image, String name, String category, String locationLastSeen, String contactNo, String additionalDetail, String time, String status) {
        this.itemId = itemId;
        this.image = image;
        this.name = name;
        this.category = category;
        this.locationLastSeen = locationLastSeen;
        this.contactNo = contactNo;
        this.additionalDetail = additionalDetail;
        this.time = time;
        this.status = status;
    }

    public LostItem() {

    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocationLastSeen() {
        return locationLastSeen;
    }

    public void setLocationLastSeen(String locationLastSeen) {
        this.locationLastSeen = locationLastSeen;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getAdditionalDetail() {
        return additionalDetail;
    }

    public void setAdditionalDetail(String additionalDetail) {
        this.additionalDetail = additionalDetail;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}