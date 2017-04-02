package com.cosc.nathaniel.plantcaresystem;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ConnectionMethods {
    static String Q_ALL_TYPES = "/data/types";
    static String Q_TYPE = "/data/";
    static String Q_PLANT = "/plant/";
    static String Q_CURRENT = "/current";
    static String Q_ADD_PLANT = "/add/plant/";
    static String Q_ADD_TYPE = "/add/type/";
    static String Q_UPDATE_PLANT = "/update/plant/";
    static String Q_UPDATE_TYPE = "/update/type/";
    static String Q_UPDATE_CURRENT = "/update/current/";

    private static String servIP = "192.168.0.30";
    private static String servPort = "8080";
    private static int numOfPlants = 9;

    static public String queryServer(String query){
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

    static public String parsePlant(String data, String key){
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

    static public List<String> getTypeList(){
        //settings:[{'water': '3', 'humid': '45', 'name': 'BeanSprout', 'family': 'bean', 'light': '4', 'foliagecolor': '4', 'waterr': '4-6', 'soilr': '2-4', 'temp': '78', 'foliage': 'leafy', 'fertilizer': '6', 'lightrange': '1', 'heatzone': '2', 'id': '1', 'size': '8-12in'},{'water': '4', 'humid': '65', 'name': 'Houseplant', 'family': 'fern', 'light': '4', 'foliagecolor': '4', 'waterr': '6-7', 'soilr': '1-4', 'temp': '70', 'foliage': 'leafy', 'fertilizer': '7', 'lightrange': '1', 'heatzone': '2', 'id': '2', 'size': '24-46in'},{'water': '8', 'humid': '85', 'name': 'Mushroom', 'family': 'fungus', 'light': '2', 'foliagecolor': '8', 'waterr': '7-8', 'soilr': '3-3', 'temp': '85', 'foliage': 'fungus', 'fertilizer': '4', 'lightrange': '0', 'heatzone': '4', 'id': '3', 'size': '2-4in'},{'water': '3', 'humid': '55', 'name': 'PurpleLettuce', 'family': 'lettuce', 'light': '5', 'foliagecolor': '4', 'waterr': '4-6', 'soilr': '2-4', 'temp': '79', 'foliage': 'leafy', 'fertilizer': '6', 'lightrange': '1', 'heatzone': '12', 'id': '4', 'size': '6-8in'},]
        List<String> names = new ArrayList<String>();
        Log.e("DEBUG", "-----------------point 1");
        String data = queryServer(Q_ALL_TYPES);
        Log.e("DEBUG", "-----------------point 2");
        for (String item : data.split("\\},")) {
            Log.e("DEBUG", "-----------------item " + item);
            names.add(parsePlant(item, "name"));
            Log.e("DEBUG", "-----------------added " + parsePlant(item, "name"));
        }
        return names;
    }

    static public int getNumOfPlants(){ return numOfPlants; }

    static public void setNumOfPlants(int numAdded){ numOfPlants += numAdded; }

    static public void setIP(String newIP){
        servIP = newIP;
    }
}
