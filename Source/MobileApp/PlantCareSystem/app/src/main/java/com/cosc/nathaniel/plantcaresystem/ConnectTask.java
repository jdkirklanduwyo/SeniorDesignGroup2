//Adapted from COSC 4730 project 7
package com.cosc.nathaniel.plantcaresystem;

import android.os.AsyncTask;
import android.util.Log;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class ConnectTask extends AsyncTask<String, String, String>{

    @Override
    protected String doInBackground(String... params) {

        String respStr = "";
        // adapted from http://stackoverflow.com/questions/35886537/how-to-build-rest-client-in-android-using-httpurlconnection
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            //open connection to url
            url = new URL("http://" + params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            //initialize input stream and stream reader
            InputStream in = urlConnection.getInputStream();
            InputStreamReader isw = new InputStreamReader(in);

            //get server response
            int data = isw.read();

            //parse response
            while (data != -1) {
                char current = (char) data;
                data = isw.read();
                respStr = respStr + current;
            }

        } catch (Exception e) {
            Log.e("DEBUG", "Connection to server failed");
            e.printStackTrace();
            respStr = "error";
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return respStr;
    }
}