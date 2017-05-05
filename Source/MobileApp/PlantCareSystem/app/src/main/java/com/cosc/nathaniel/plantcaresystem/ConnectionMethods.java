package com.cosc.nathaniel.plantcaresystem;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;

//this class contains methods to interact with the server
public class ConnectionMethods {
    //format for queries to server, some require parameters appended
    static String Q_ALL_PLANTS = "/plants";
    static String Q_PLANT = "/plant/"; //append id
    static String Q_CURRENT = "/current";
    static String Q_ADD_PLANT = "/add/plant/"; //append name lightSens waterSens humidSens tempSens health waterTimer foliageColor
    static String Q_UPDATE_CURRENT = "/update/current/"; //append id lightS waterS humidS tempS health inTraining
    static String Q_REMOVE = "/remove/plant/"; //append id
    static String Q_SET_SCORE = "/current/userscore/"; //append num, 0=Fine, 1=Needs Water, 2=Needs Light, 3=Bad Temp, 4=Bad Humid
    static String Q_GET_SETTINGS = "/settings";
    static String Q_SET_SETTINGS = "/settings/set/"; //append activateWaterFlag activateLightFlag sendTempNotificationFlag sendHumidNotificationFlag waitFlag
    static String Q_GET_WAITING = "/settings/waiting";

    private static String servIP = "192.168.0.30";
    private static String servPort = "8080";
    private static int notifNum = 3;

    //checks whether connection to server is open
    static public boolean isConnected(){
        //start connection thread
        ConnectTask connection = new ConnectTask();
        String response = "";
        try{
            //make connection with server and pull data
            response = connection.execute(servIP + ":" + servPort + Q_ALL_PLANTS).get();
        } catch(Exception e){
            Log.e("DEBUG", "Error in try connection in ConnectionMethods");
            e.printStackTrace();
        }
        //if connection was unsuccessful, response is "error"
        return !(response.equals("error"));
    }

    //send a request to ReST server and returns the server's response
    //String query is the last segment of the url sent, options are listed in global vars
    static public String queryServer(String query){
        //start connection thread
        ConnectTask connection = new ConnectTask();
        String response = "";
        try{
            //make connection with server and pull data
            response = connection.execute(servIP + ":" + servPort + query).get();
        } catch(Exception e){
            Log.e("DEBUG", "Error in try connection in ConnectionMethods");
            e.printStackTrace();
        }
        return response;
    }

    //takes unformated data from server and gets item associated with key
    static public String parsePlant(String data, String key){
        //plant data format: {'health': '100', 'humid': '45', 'id': '1', 'light': '4', 'ptype': '1', 'temp': '78', 'water': '3'}
        //settings data format: {'tempFlag': '0', 'waitFlag': '0', 'waterFlag': '0', 'lightFlag': '0', 'humidFlag': '0'}

        //remove ' : { } from string
        data = data.replaceAll("'|:|\\{|\\}", "");
        //split at ", "
        for (String item : data.split(",\\s")){
            //if keys match, return value
            if ( key.equals(item.split("\\s")[0]) ){
                return item.split("\\s")[1];
            }
        }
        //if key not found, return empty string
        return "";
    }

    //get list of IDs of all plants in database
    static public List<String> getIDList(){
        //data format:[{'water': '3', 'humid': '45', 'name': 'BeanSprout', 'family': 'bean', 'light': '4', 'foliagecolor': '4', 'waterr': '4-6', 'soilr': '2-4', 'temp': '78', 'foliage': 'leafy', 'fertilizer': '6', 'lightrange': '1', 'heatzone': '2', 'id': '1', 'size': '8-12in'},{'water': '4', 'humid': '65', 'name': 'Houseplant', 'family': 'fern', 'light': '4', 'foliagecolor': '4', 'waterr': '6-7', 'soilr': '1-4', 'temp': '70', 'foliage': 'leafy', 'fertilizer': '7', 'lightrange': '1', 'heatzone': '2', 'id': '2', 'size': '24-46in'},{'water': '8', 'humid': '85', 'name': 'Mushroom', 'family': 'fungus', 'light': '2', 'foliagecolor': '8', 'waterr': '7-8', 'soilr': '3-3', 'temp': '85', 'foliage': 'fungus', 'fertilizer': '4', 'lightrange': '0', 'heatzone': '4', 'id': '3', 'size': '2-4in'},{'water': '3', 'humid': '55', 'name': 'PurpleLettuce', 'family': 'lettuce', 'light': '5', 'foliagecolor': '4', 'waterr': '4-6', 'soilr': '2-4', 'temp': '79', 'foliage': 'leafy', 'fertilizer': '6', 'lightrange': '1', 'heatzone': '12', 'id': '4', 'size': '6-8in'},]

        List<String> idList = new ArrayList<String>();
        //get data on all plants from server
        String data = queryServer(Q_ALL_PLANTS);
        //split data on },
        for (String item : data.split("\\},")) {
            //get ID from segment of data
            idList.add(parsePlant(item, "id"));
        }
        return idList;
    }

    //returns the name of the plant with the given id
    static public String getPlantName(String id){
        return parsePlant(queryServer(Q_PLANT + id), "name");
    }

    //returns the id of the current plant
    static public String getCurrentPlantID(){
        return parsePlant(queryServer(Q_CURRENT), "id");
    }

    //sets the IP address of the server
    static public void setIP(String newIP){
        servIP = newIP;
    }

    //get and set the number of times per day to notify user
    static public void setNotifNum(int num){notifNum = num;}
    static public int getNotifNum(){return notifNum;}
}
