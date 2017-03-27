package com.cosc.nathaniel.plantcaresystem;

import android.util.Log;

public class ConnectionMethods {
    static String SERV_IP = "192.168.0.30";
    static String SERV_PORT = "8080";
    static String Q_PLANT = "/plant/";
    static String Q_CURRENT = "/current";

    static public String queryServer(String query){
        ConnectTask connection = new ConnectTask();
        String response = "";
        try{
            //make connection with server and pull data
            response = connection.execute(SERV_IP + ":" + SERV_PORT + query).get();
        } catch(Exception e){
            Log.e("DEBUG", "Error in try connection in MainActivity");
            e.printStackTrace();
        }
        return response;
    }

    static public String parseData(String data, String key){
        //data format: {'health': '100', 'humid': '45', 'id': '1', 'light': '4', 'ptype': '1', 'temp': '78', 'water': '3'}

        //remove ' : { } from string
        data = data.replaceAll("'|:|\\{|\\}", "");
        Log.e("DEBUG", data);
        //split at ", "
        for (String item : data.split(",\\s")){
            Log.e("DEBUG", item);
            //if keys match, return value
            if ( key.equals(item.split("\\s")[0]) ){
                return item.split("\\s")[1];
            }
        }
        return "";
    }
}
