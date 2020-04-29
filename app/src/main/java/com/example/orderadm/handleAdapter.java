package com.example.orderadm;

public class handleAdapter {

    public String name, category, description, status, approval, request;

    public handleAdapter(String name, String category, String description, String status, String approval, String request) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.status = status;
        this.approval = approval;
        this.request = request;
    }

    public handleAdapter() {
    }

    public String getNname() {
        return name;
    }

    public void setNname(String name) {
        this.name = name;
    }

    public String getCCategory() {
        return category;
    }

    public void setCCategory(String category) {
        this.category = category;
    }

    public String getDDescription() {
        return description;
    }

    public void setDDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public String getRRequest() {
        return request;
    }

    public void setSubject(String request) {
        this.request = request;
    }
}
