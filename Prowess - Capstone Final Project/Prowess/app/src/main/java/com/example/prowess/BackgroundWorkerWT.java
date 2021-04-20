package com.example.prowess;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

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
import java.time.LocalDate;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class BackgroundWorkerWT extends AsyncTask<String, Void, String> {

    Context context;
    AlertDialog alertDialog;
    String StringHolder = "" ;

    BackgroundWorkerWT(Context ctx) {
        context = ctx;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected String doInBackground(String... params) {


        String type = params[0];
        String userID = params[2];
        String date = java.time.LocalDate.now().toString();

        char[] dt = date.toCharArray();
//        Log.d("characters", toString(dt[0] + dt[2]);

//        2 0 2 1 - 0 4 - 1 7
//        0 1 2 3 4 5 6 7 8 9

        String year = String.valueOf(dt[0]);
        year = year.concat(String.valueOf(dt[1]));
        year = year.concat(String.valueOf(dt[2]));
        year = year.concat(String.valueOf(dt[3]));

        String month = String.valueOf(dt[5]);
        month = month.concat(String.valueOf(dt[6]));

        String day = String.valueOf(dt[8]);
        day = day.concat(String.valueOf(dt[9]));

//        Log.d("Before", year + " " + month + " " + day);

        try {
            if (type.equals("update")){
                String intake = params[1];

                String login_url = "https://cgi.sice.indiana.edu/~drigali/watertrackerPHP/updateWater.php";

                URL url = new URL(login_url);
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setRequestMethod("POST");
                httpsURLConnection.setDoOutput(true);
                httpsURLConnection.setDoInput(true);

                //this set of code takes the user_name and password from the php login file
                //it uses it to go into the database that the php file is connected to and
                //recognizes the username and password that is needed to matchup in order to login
                OutputStream outputStream = httpsURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("intake", "UTF-8") + "=" + URLEncoder.encode(intake, "UTF-8") + "&"
                        + URLEncoder.encode("year", "UTF-8") + "=" + URLEncoder.encode(year, "UTF-8") + "&"
                        + URLEncoder.encode("month", "UTF-8") + "=" + URLEncoder.encode(month, "UTF-8") + "&"
                        + URLEncoder.encode("day", "UTF-8") + "=" + URLEncoder.encode(day, "UTF-8") + "&"
                        + URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(userID, "UTF-8");

                Log.d("after", date);

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
            }
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
