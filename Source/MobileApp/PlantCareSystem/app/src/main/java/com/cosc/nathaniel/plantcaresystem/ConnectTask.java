//Adapted from COSC 4730 project 7
package com.cosc.nathaniel.plantcaresystem;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ConnectTask extends AsyncTask<String, MySocket, MySocket>{

    private MySocket sock = new MySocket();
    private AsyncCoordinator coord = new AsyncCoordinator();
    private boolean done = false;

    @Override
    protected MySocket doInBackground(String... params) {

        // from http://stackoverflow.com/questions/35886537/how-to-build-rest-client-in-android-using-httpurlconnection
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            //open connection to url
            url = new URL("http://" + params[0] + ":8080");
            //url = new URL("http://www.google.com");
            urlConnection = (HttpURLConnection) url
                    .openConnection();

            //initialize streams
            OutputStream out = urlConnection.getOutputStream();
            InputStream in = urlConnection.getInputStream();

            //write request to server
            String req = "/plant/1";
            out.write(req.getBytes());

            //get server response
            InputStreamReader isw = new InputStreamReader(in);

            //parse response
            int data = isw.read();
            String respStr = "";
            while (data != -1) {
                char current = (char) data;
                data = isw.read();
                respStr = respStr + current;
            }

            //send response to log
            Log.e("RESPONSE", respStr);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return sock;
    }

    //method to pass coordinator object to main activity
    public AsyncCoordinator getCoord(){return coord;}
}