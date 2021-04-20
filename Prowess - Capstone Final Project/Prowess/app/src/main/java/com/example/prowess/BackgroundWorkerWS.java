package com.example.prowess;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.List;

public class BackgroundWorkerWS extends AsyncTask<String, Void, String> {


    Context context;

    BackgroundWorkerWS(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {


        String type8 = params[0];

        //php file which runs an insert statement
        String Insert_url = "http://cgi.sice.indiana.edu/~jtessler/InsertExerciseSchedule.php";

        if (type8.equals("Workout")) {
            try {

                String UserID = params[1];
                String Date = params[2];


                String exerciseTitle = params[3];
                String weight = params[4];
                String reps = params[5];
                String sets = params[6];
                String max = params[7];


                //this set of code connects everything to the luddy servers
                URL url = new URL(Insert_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                //This set of code inserts the userID, date, exerciseTitle, weight, reps, sets, and
                //max weight into an insert statement in the php file which inserts it into the database
                String post_data = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(UserID, "UTF-8") + "&"
                        + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(Date, "UTF-8") + "&"
                        + URLEncoder.encode("exerciseTitle", "UTF-8") + "=" + URLEncoder.encode(exerciseTitle, "UTF-8") + "&"
                        + URLEncoder.encode("weight", "UTF-8") + "=" + URLEncoder.encode(weight, "UTF-8") + "&"
                        + URLEncoder.encode("reps", "UTF-8") + "=" + URLEncoder.encode(reps, "UTF-8") + "&"
                        + URLEncoder.encode("sets", "UTF-8") + "=" + URLEncoder.encode(sets, "UTF-8") + "&"
                        + URLEncoder.encode("max", "UTF-8") + "=" + URLEncoder.encode(max, "UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                } Log.d("backgroundworker", result);
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


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
