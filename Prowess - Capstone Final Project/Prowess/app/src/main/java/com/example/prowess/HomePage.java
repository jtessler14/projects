package com.example.prowess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class HomePage extends AppCompatActivity {

    private static final String TAG = "WorkoutSchedule";
    ListView exercise1, weight1;
    TextView email;

    GoogleSignInClient mGoogleSignInClient;
    ImageView profilepicbttn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

///////////////////////////////////////////////////////////////////////////////////////////////////////////
        //displays today's date
        String date_n = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
        TextView date = (TextView) findViewById(R.id.defaultdate);

        date.setText(date_n);

        //the first link grabs the exercise title and the second link grabs the weight they are doing
        getJSON("http://cgi.sice.indiana.edu/~jtessler/ExerciseScheduleData.php");
        getJSON2("http://cgi.sice.indiana.edu/~jtessler/ExerciseScheduleData.php");

        //variable that describes the exercises that will be displayed
        exercise1 = (ListView) findViewById(R.id.spinnerWorkout1);

        //variable that describes the weight that will be displayed next to the exercises
        weight1 = (ListView) findViewById(R.id.editWeight1);


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //this set of code takes the email associated with the account from google
        //we can do a lot with the user's email information
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        email = findViewById(R.id.email_hp);

        //displays the profile picture on the top of the screen
        profilepicbttn = findViewById(R.id.profile);

//when the last user clicks sign in again when they open up the app, it automatically uses their
        //account info to sign in instead of making them choose it again
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            Uri personPhoto = acct.getPhotoUrl();
            String personEmail = acct.getEmail();
            email.setText(personEmail);

//This displays their profile picture in a circle
            Glide.with(this).load(String.valueOf(personPhoto)).into(profilepicbttn);
        }

    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //When the user profile button is clicked it brings them to the user profile activity
    public void OnProfile(View view) {
        Intent intent = new Intent(this, userprofile.class);
        startActivity(intent);
    }


//when the workout log button is clicked, it brings them to the workout log
    public void OnExercise(View item) {
        Intent intent = new Intent(this, WorkoutSchedule.class);
        startActivity(intent);

    }

    //when the recommedned workouts button is clicked, it brings the user to the recommended
    //workouts
    public void OnRecommended(View view) {
        Intent intent = new Intent(this, RecommendedExercise.class);
        startActivity(intent);

    }

//when tutorials button is clicked it, user will be brought to the tutorials page
    //feature is not finished
    public void OnTutorials(View view) {


    }

//when stretch button is clicked it brings the user to the new stretch activity
    public void OnStretch(View view) {
        Intent intent = new Intent(this, Stretch.class);
        startActivity(intent);


    }

    //when the Run Tracker button is clicked, it brings them to the run tracker activity
    public void OnRunning(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

//when the Timer button is selected it brings them to the timer and stopwatch activity
    public void OnTimer(View view) {
        Intent intent = new Intent(this, Timer.class);
        startActivity(intent);

    }

    //when the gym finder is clicked, it brings the user to the Gym Finder activity
    public void OnGymFinder(View view) {
        Intent intent = new Intent(this, GymMap.class);
        startActivity(intent);


    }

    //when the bmi calculator button is clicked, it brings the user to the bmi calculator page
    public void OnWeight(View view) {
        Intent intent = new Intent(this, bmiTracker.class);
        startActivity(intent);


    }

    //when the calorie tracker button is clicked, it brings the user to the calorie tracker activity
    //feature not finished
    public void OnCalorie(View view) {


    }

    //When the user clicks on the water tracker activity, it brings the user to the water tracker
    //activity
    public void OnWater(View view) {
        Intent intent = new Intent(this, WaterTracker.class);
        startActivity(intent);


    }

    ///////////////////////////////////////////////////////////////////////////////////////


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


                    //uses the date that is from the date picker as well as the email to run it through a
                    //select statement which takes email and date, the userID associated
                    //with that email, and brings back the different workout titles for
                    ///that day
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    TextView Date = findViewById(R.id.defaultdate);
                    TextView email = findViewById(R.id.email_hp);
                    String post_data = URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(Date.getText().toString(), "UTF-8") + "&"
                            + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email.getText().toString(), "UTF-8");
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

    //displays the exercise titles for today's date
    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] user = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            user[i] = obj.getString("ExerciseTitle");


            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.custom_listview, user);
            exercise1.setAdapter(arrayAdapter);
        }

    }


//    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void getJSON2(final String urlWebService) {

        class GetJSON2 extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView2(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {


                    //uses the date that is from the date picker as well as the email to run it through a
                    //select statement which takes email and date, the userID associated
                    //with that email, and brings back the different weight the user put for themselves for
                    ///that day
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    TextView Date = findViewById(R.id.defaultdate);
                    TextView email = findViewById(R.id.email_hp);
                    String post_data = URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(Date.getText().toString(), "UTF-8") + "&"
                            + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email.getText().toString(), "UTF-8");
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
        GetJSON2 getJSON2 = new GetJSON2();
        getJSON2.execute();
    }

    //displays the weight for the specific exercises for today's date
    private void loadIntoListView2(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] user = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            user[i] = obj.getString("Weight");


            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.custom_listview, user);
            weight1.setAdapter(arrayAdapter);
        }
//
//    }


    }

}