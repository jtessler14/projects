package com.example.prowess;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

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
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class Goal extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    ListView userID;
    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        //json link which brings back the UserID associated to the account so the user can insert
        //the userID as well with anything else
        getJSON("http://cgi.sice.indiana.edu/~jtessler/UserID.php");

        //userID variable so the program represents what user is on the application
        userID = (ListView) findViewById(R.id.userID_goal);


        //this set of code takes the email associated with the account from google
        //we can do a lot with the user's email information
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        email = findViewById(R.id.email_goal);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personEmail = acct.getEmail();
            email.setText(personEmail);

        }

    }

    //when the Flexibility goal is clicked it takes the user to the home screen
    public void OnFlexibility(View view) {

//takes user to the home screen
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);

        //takes the userID to the background worker
        String userid = (userID.getItemAtPosition(0).toString());
        String type = "flex";

        BackgroundWorkerGoal backgroundWorker = new BackgroundWorkerGoal(Goal.this);
        backgroundWorker.execute(type, userid);

    }

    //when the Build Muscle goal is clicked it takes the user to the home screen
    public void OnMuscle(View view) {

        //takes user to the home screen
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);

        //takes the userID to the background worker
        String userid = (userID.getItemAtPosition(0).toString());
        String type = "muscle";

        BackgroundWorkerGoal backgroundWorker = new BackgroundWorkerGoal(Goal.this);
        backgroundWorker.execute(type, userid);

    }

    //when the Lose Fat goal is clicked it takes the user to the home screen
    public void OnFat(View view) {

        //takes user to the home screen
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);

        Log.d("Before", "this is the very start");

        //takes the userID to the background worker
        String userid = (userID.getItemAtPosition(0).toString());
        Log.d("Before", userid);

        String type = "fat";




        BackgroundWorkerGoal backgroundWorker = new BackgroundWorkerGoal(Goal.this);
        backgroundWorker.execute(type, userid);

    }

    //this brings back the UserID so we can use it to insert it into the database
    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {

//uses the email to search for the UserID within the User Table in the database
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    TextView email = findViewById(R.id.email_goal);
                    String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email.getText().toString(), "UTF-8");
                    System.out.println(post_data);
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();

                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                        System.out.println(json);
                    }
                    bufferedWriter.close();
                    outputStream.close();
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    //this loads the userID into the application for the developers to use
    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] user = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            user[i] = obj.getString("UserID");


            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.custom_listview_userinfo, user);
            userID.setAdapter(arrayAdapter);
        }


    }

}