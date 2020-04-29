package com.example.orderadm;

public class userList {
    public String userName, email, pass, depart, level, topic, tempTopic;

    public userList(String userName, String email, String pass, String Category, String level, String topic, String tempTopic) {
        this.userName = userName;
        this.email = email;
        this.pass = pass;
        this.depart = Category;
        this.level = level;
        this.topic = topic;
        this.tempTopic = tempTopic;
    }

    public userList(){
    }

    public String getTempTopic() {
        return tempTopic;
    }

    public void setTempTopic(String tempTopic) {
        this.tempTopic = tempTopic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
