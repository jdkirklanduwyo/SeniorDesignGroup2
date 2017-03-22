//Adapted from COSC 4730 project 7
//class to send info between main and async task
package com.cosc.nathaniel.plantcaresystem;

public class AsyncCoordinator {
    private String message = "";

    public void setMsg(String msg) {
        message = msg;
    }

    public String getMsg() {return message;}
}
