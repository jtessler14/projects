package com.example.itp5_jtessler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

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

public class BackgroundWorker extends AsyncTask<String, Void, String> {
    Context context;
    AlertDialog alertDialog;
    BackgroundWorker (Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        //this links to the login php that I made that is also linked ot the data base
        String login_url = "http://cgi.sice.indiana.edu/~jtessler/login.php";
        if(type.equals("login")) {
            try {
                //represents the user_name that is in my php login document
                String user_name = params[1];

                //represents the password that is in my php login document
                String password = params[2];

                //this set of code takes the url that I put above, the one with the login query
                //as well as login information and it connects it to the php file that is in
                //my cgi-pub folder that is on the luddy servers
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                //this set of code takes the user_name and password from the php login file
                //it uses it to go into the database that the php file is connected to and
                //recognizes the username and password that is needed to matchup in order to login
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
//This set of code displays a message after you click the login button. It will say "Login Status"
    //Continuing on the onPost Execute part...
    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");
    }

    //This set of code displays an if statement and happens after the the 'login' button is clicked
    //it's saying that if the result says "login success" below the "Login Status" part which is stated in the login.php document,
    //then it will take you to the page with the number and translate button like on ITP 4... The way this works is that on the php
    //document, if the username and password matchup with the database, then it says "login success"

    //If the username and password to do not matchup with the database, the else message is displayed from the php document saying
    // "Username and/or password is not correct..."
    @Override
    protected void onPostExecute(String result) {

        if (result.equals("login success")) {
            Intent intent = new Intent(context, login.class);
            context.startActivity(intent);
            alertDialog.setMessage(result);
            alertDialog.show();
        } else {
            alertDialog.setMessage(result);
            alertDialog.show();

        }


    }
//This updates the progress of the app
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
