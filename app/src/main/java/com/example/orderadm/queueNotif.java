package com.example.orderadm;

public class queueNotif {

    public String  notifTitle, notifDesc, notifFrom, notifTo, notifTopic;

    public queueNotif(String notifTitle, String notifDesc, String notifFrom, String notifTo, String notifTopic) {
        this.notifTitle = notifTitle;
        this.notifDesc = notifDesc;
        this.notifFrom = notifFrom;
        this.notifTo = notifTo;
        this.notifTopic = notifTopic;
    }

    public queueNotif() {
    }

    public String getNotifTopic() {
        return notifTopic;
    }

    public void setNotifTopic(String notifTopic) {
        this.notifTopic = notifTopic;
    }

    public String getNotifTitle() {
        return notifTitle;
    }

    public void setNotifTitle(String notifTitle) {
        this.notifTitle = notifTitle;
    }

    public String getNotifDesc() {
        return notifDesc;
    }

    public void setNotifDesc(String notifDesc) {
        this.notifDesc = notifDesc;
    }

    public String getNotifFrom() {
        return notifFrom;
    }

    public void setNotifFrom(String notifFrom) {
        this.notifFrom = notifFrom;
    }

    public String getNotifTo() {
        return notifTo;
    }

    public void setNotifTo(String notifTo) {
        this.notifTo = notifTo;
    }
}
