package com.dc.chandra.nitchatzz;

public class Faculty {
    public String name;
    public String email;
    public String phone;
    //public String avata;
    public Status status;
    public Message message;


    public Faculty(){
        status = new Status();
        message = new Message();
        status.isOnline = false;
        status.timestamp = 0;
        message.idReceiver = "0";
        message.idSender = "0";
        message.text = "";
        message.timestamp = 0;
    }
}