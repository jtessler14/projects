package com.example.prowess;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class BackgroundWorkerMaps extends AsyncTask<String, Void, String> {

    Context context;
    AlertDialog alertDialog;
    String StringHolder = "" ;
    JSONObject jsonObject = null ;

    BackgroundWorkerMaps(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
//        Log.d("Before", "this is the very start");

        String type = params[0];

        try {
            //if statement that when the start button is clicked it does the following
            if (type.equals("start")){
                String startLat = params[1];
                String startLng = params[2];
                String userID = params[3];

                Log.d("start latLng", type + "," + startLat + " " + startLng);

                //link tha inserts the starting longitude, latitude, and userID into the mysql table
                //with the php/msql statements within the link
                String login_url = "https://cgi.sice.indiana.edu/~drigali/PHPRunTracker/runTrackStart.php";

//                String startLatLng = startLat + "," + startLng;
//                Log.d("LatLng", startLatLng);

                URL url = new URL(login_url);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.setDoInput(true);

                //this set of code takes the starting long, lat, and userID and inserts them into
                //the php file as variables and inserts them into mysql accordingly
                OutputStream outputStream = httpsURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("startLat", "UTF-8") + "=" + URLEncoder.encode(startLat, "UTF-8") + "&"
                        + URLEncoder.encode("startLng", "UTF-8") + "=" + URLEncoder.encode(startLng, "UTF-8") + "&"
                        + URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(userID, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpsURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpsURLConnection.disconnect();
                return result;

                //else if statment stating when the stop button is pressed it does the following
            } else if(type.equals("end")){
                String endLat = params[1];
                String endLng = params[2];
                String userID = params[3];
                Log.d("end latLng", type + "," + endLat + " " + endLng);

                //php file that has an update statement in it
                String login_url = "https://cgi.sice.indiana.edu/~drigali/PHPRunTracker/runTrackStop.php";
//                String login_url = "https://cgi.sice.indiana.edu/~drigali/PHPRunTracker/test.php";


//                String startLatLng = startLat + "," + startLng;
//                Log.d("LatLng", startLatLng);

                //connects to the link and the luddy database
                URL url = new URL(login_url);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.setDoInput(true);

                //takes the ending latitude, longitude, as well as the userID that is being used
                //and updates the run according to the userID and the runID already in the database
                OutputStream outputStream = httpsURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("endLat", "UTF-8") + "=" + URLEncoder.encode(endLat, "UTF-8") + "&"
                        + URLEncoder.encode("endLng", "UTF-8") + "=" + URLEncoder.encode(endLng, "UTF-8") + "&"
                        + URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(userID, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpsURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpsURLConnection.disconnect();
                return result;

////               START OF DISTANCE DETERMINATION
//                String login_url2 = "https://cgi.sice.indiana.edu/~drigali/PHPRunTracker/runTrackDistance.php";
//
////                String endLatLng = endLat + "," + endLng;
////                Log.d("LatLng", endLatLng);
//
//                URL url2 = new URL(login_url2);
//                HttpsURLConnection httpsURLConnection2 = (HttpsURLConnection) url.openConnection();
//                httpsURLConnection.setRequestMethod("POST");
//                httpsURLConnection.setDoOutput(true);
//                httpsURLConnection.setDoInput(true);
//
//                //this set of code takes the user_name and password from the php login file
//                //it uses it to go into the database that the php file is connected to and
//                //recognizes the username and password that is needed to matchup in order to login
//                OutputStream outputStream2 = httpsURLConnection.getOutputStream();
//                BufferedWriter bufferedWriter2 = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
//                String post_data2 = URLEncoder.encode("endLat", "UTF-8") + "=" + URLEncoder.encode(endLat, "UTF-8") + "&"
//                        + URLEncoder.encode("endLng", "UTF-8") + "=" + URLEncoder.encode(endLng, "UTF-8");
//                bufferedWriter2.write(post_data2);
//                bufferedWriter2.flush();
//
//                bufferedWriter2.close();
//                outputStream2.close();
//                InputStream inputStream2 = httpsURLConnection.getInputStream();
//                BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputStream2, "iso-8859-1"));
//                String result2 = "";
//                String line2 = "";
//                while ((line2 = bufferedReader2.readLine()) != null) {
//                    result2 += line;
//                }
//                bufferedReader.close();
//                inputStream.close();
//                httpsURLConnection.disconnect();
//                return result;

            }

//            if(/*!"".equals(startLat) && */!"".equals(endLat)){
//                Log.d("coordinates", "sLat" + startLat + " " +  "sLng" +startLng + " " + "eLat" +endLat+ " " + "eLng" +endLng);
//
//                double sLat = Double.parseDouble(startLat);
//                double sLng = Double.parseDouble(startLng);
//
//                double eLat = Double.parseDouble(endLat);
//                double eLng = Double.parseDouble(endLng);
//
//                Log.d("distancevariables", sLat + " " + sLng + " " + eLat+ " " + eLng);
//
//                double dist = distance(sLat, eLat, sLng, eLng/*, "M"*/);
//                String distance = String.valueOf(dist);
//
//                String startLatLng = sLat + "," + sLng;
//                String endLatLng = eLat + "," + eLng;
//
//
//                //this set of code takes the url that I put above, the one with the login query
//                //as well as login information and it connects it to the php file that is in
//                //my cgi-pub folder that is on the luddy servers
//                URL url = new URL(login_url);
//                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
//                httpsURLConnection.setRequestMethod("POST");
//                httpsURLConnection.setDoOutput(true);
//                httpsURLConnection.setDoInput(true);
//
//                Log.d("Connection", "Connection has passed "+startLatLng + endLatLng);
//
//                //this set of code takes the user_name and password from the php login file
//                //it uses it to go into the database that the php file is connected to and
//                //recognizes the username and password that is needed to matchup in order to login
//                OutputStream outputStream = httpsURLConnection.getOutputStream();
//                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
//                String post_data = URLEncoder.encode("startLatLng", "UTF-8") + "=" + URLEncoder.encode(startLatLng, "UTF-8") + "&"
//                        + URLEncoder.encode("endLatLng", "UTF-8") + "=" + URLEncoder.encode(endLatLng, "UTF-8")
//                        + URLEncoder.encode("distance", "UTF-8") + "=" + URLEncoder.encode(distance, "UTF-8");
//                bufferedWriter.write(post_data);
//                bufferedWriter.flush();
//                bufferedWriter.close();
//                outputStream.close();
//                InputStream inputStream = httpsURLConnection.getInputStream();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
//                String result = "";
//                String line = "";
//                while ((line = bufferedReader.readLine()) != null) {
//                    result += line;
//                }
//                bufferedReader.close();
//                inputStream.close();
//                httpsURLConnection.disconnect();
//                return result;
//            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
    //the following set of code helps record the distance of the runs that the user performs
    private double distance(double lat1, double lon1, double lat2, double lon2/*, char unit*/) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
