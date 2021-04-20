package com.example.prowess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
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
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Stopwatch extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    ListView userID;


    Chronometer chronometer;
    ImageButton startbtn, stopbtn, back;
    Button timerbttn;

    private boolean isResume;
    Handler handler;
    long tMilliSec, tStart, tBuff, tUpdate = 0L;
    int sec, min, millisec;
    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

        //php link gets userID from select statement
        getJSON("http://cgi.sice.indiana.edu/~jtessler/UserID.php");


        //this set of code takes the email associated with the account from google
        //we can do a lot with the user's email information
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        email = findViewById(R.id.email_sw);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personEmail = acct.getEmail();
            email.setText(personEmail);

        }

        //variables that represent the following...
        chronometer = findViewById(R.id.chronometer);
        userID = (ListView) findViewById(R.id.userID_sw);
        startbtn = findViewById(R.id.startbtn);
        stopbtn = findViewById(R.id.stopbtn);
        timerbttn = findViewById(R.id.timerbttn);
        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomePage();
            }
        });

        handler = new Handler();

        //this button opens up the timer activity
        timerbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimerActivity();
            }
        });

        //this on click if statement says if the system clock is running have the pause button be
        //shown
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isResume) {
                    tStart = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                    chronometer.start();
                    isResume = true;
                    //stopbtn.setVisibility(View.GONE);
                    startbtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));

//else part... If the clock is not going the play button should be shown it also takes the following
                    //time and userID to insert into the background worker which inserts it into the
                    //database
                }else {
                    tBuff += tMilliSec;
                    handler.removeCallbacks(runnable);
                    chronometer.stop();
                    isResume = false;
                    stopbtn.setVisibility(View.VISIBLE);
                    startbtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));

                    String time = chronometer.getText().toString();
                    String userid = (userID.getItemAtPosition(0).toString());
                    String type = "SW";

                    BackgroundWorkerSW backgroundWorker = new BackgroundWorkerSW(Stopwatch.this);
                    backgroundWorker.execute(type, time, userid);


                }

            }
        });

//when the clock is stopped, it show the play button
        stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isResume) {
                    startbtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));

                    tMilliSec = 0L;
                    tStart = 0L;
                    tBuff = 0L;
                    sec = 0;
                    min = 0;
                    millisec = 0;
                    chronometer.setText("00:00:00");

                }
            }
        });

    }

    //math equations for the stopwatch
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            tMilliSec = SystemClock.uptimeMillis() - tStart;
            tUpdate = tBuff + tMilliSec;
            sec = (int) (tUpdate/1000);
            min = sec/60;
            sec = sec%60;
            millisec = (int) (tUpdate%100);
            chronometer.setText(String.format("%02d",min)+":"
                    +String.format("%02d",sec)+":"+String.format("%02d",millisec));
            handler.postDelayed(this, 60);


        }
    };

    //when the clock is stopped it send the time to the background worker and to the databse
    public void OnStop(View view) {

        String str_time = chronometer.getText().toString();
        String type = "stop";

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(str_time, type);

    }

    //opens up the timer activity when the timer button is clicked
    public void openTimerActivity() {
        Intent intent = new Intent(this, Timer.class);
        startActivity(intent);
        finish();
    }

    //goes to the homepage when the homepage button is clicked
    public void openHomePage() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }

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
                    TextView email = findViewById(R.id.email_sw);
                    String post_data = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email.getText().toString(),"UTF-8");
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
            user[i] = obj.getString("UserID" );



            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.custom_listview_userinfo, user);
            userID.setAdapter(arrayAdapter);
        }

    }




}