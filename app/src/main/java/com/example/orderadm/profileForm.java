package com.example.orderadm;

public class profileForm {

    private String name,description, Category, request, status, date, Uid, Approval, queryHandle, supervised, complete;

    public profileForm(String request , String name, String description, String category, String status, String date, String Uid, String approval, String queryHandle, String supervised, String complete) {
        this.request = request;
        this.name = name;
        this.description = description;
        this.Category = category;
        this.status = status;
        this.date = date;
        this.Uid = Uid;
        this.Approval = approval;
        this.queryHandle = queryHandle;
        this.supervised = supervised;
        this.complete = complete;

    }

    public profileForm() {
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

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        this.Category = category;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getApproval() {
        return Approval;
    }

    public void setApproval(String approval) {
        Approval = approval;
    }

    public String getQueryHandle() {
        return queryHandle;
    }

    public void setQueryHandle(String queryHandle) {
        this.queryHandle = queryHandle;
    }

    public String getSupervised() {
        return supervised;
    }

    public void setSupervised(String supervised) {
        this.supervised = supervised;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }
}
