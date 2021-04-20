package com.example.prowess;


import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

public class BackgroundWorkerGoal extends AsyncTask<String, Void, String> {

    Context context;
    AlertDialog alertDialog;

    BackgroundWorkerGoal(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        Log.d("Before", "this is the very start");

        String type = params[0];

        Log.d("ThisIsMyTag", type);
        System.out.println(type);

        //link goes to php file that inserts the the user's goal that they chose
        String login_url = "https://cgi.sice.indiana.edu/~drigali/phpgoals/usergoal.php";

        try {
            //takes the goal that is chosen
            String UserID = params[1];
            String GoalID = "";

            if (type.equals("flex")) {
                GoalID = "3";//params[2];
                Log.d("FlexIf",  type+" if statement passed " + GoalID);
            }
            else if (type.equals("muscle")){
                GoalID = "1";//params[2];
                Log.d("MuscleIf",  type+" if statement passed " + GoalID);
            }
            else if (type.equals("fat")){
                GoalID = "2";//params[2];
                Log.d("FatIf",  type+" if statement passed " + GoalID);
            }

            //this set of code takes the url that put above
            //and connects it to the php file that is in
            //my cgi-pub folder that is on the luddy servers
            URL url = new URL(login_url);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setDoInput(true);

            //Log.d("Connection", "Connection has passed "+UserID + GoalID);

            //depending on what goal is chosen, it will take the goalID from the if statments above
            //and the UserID and enter them into the database
            OutputStream outputStream = httpsURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("UserID", "UTF-8") + "=" + URLEncoder.encode(UserID, "UTF-8") + "&"
                    + URLEncoder.encode("GoalID", "UTF-8") + "=" + URLEncoder.encode(GoalID, "UTF-8");
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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
