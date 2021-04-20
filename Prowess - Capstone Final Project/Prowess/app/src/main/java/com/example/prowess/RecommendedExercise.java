package com.example.prowess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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
import java.util.List;

public class RecommendedExercise extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    ListView userID, goalID, lvexercises;
    TextView email;

    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_exercise);

//select statement that gets userID
        getJSON("http://cgi.sice.indiana.edu/~jtessler/UserID.php");

        //Select statement that gets goalID
        getJSON2("http://cgi.sice.indiana.edu/~jtessler/GoalID.php");

        //select statement that gets recommended workouts
//        getJSON3("http://cgi.sice.indiana.edu/~jtessler/recommendedWorkouts.php");


//represents the back button for the user to click on to go back to the home screen
        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomePage();
            }
        });

        //represents the goal and userID for the developers to use in order to make the data show
        userID = (ListView) findViewById(R.id.userID_re);
        goalID = (ListView) findViewById(R.id.goalID);

        //represents the listview that the workouts will be showing up in
        lvexercises = (ListView) findViewById(R.id.lvExercises);


        //this set of code takes the email associated with the account from google
        //we can do a lot with the user's email information
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        email = findViewById(R.id.email_re);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personEmail = acct.getEmail();
            email.setText(personEmail);

        }



    }

    //opens up the home page after the back button is pressed
    public void openHomePage() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
        finish();
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
                    TextView email = findViewById(R.id.email_re);
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



            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.custom_listview, user);
            userID.setAdapter(arrayAdapter);
        }

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


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

                    //uses the email and runs it through a select statement
                    //the select statement finds the userID by using the email and returns the
                    //goalID
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    TextView email = findViewById(R.id.email_re);
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
        GetJSON2 getJSON2 = new GetJSON2();
        getJSON2.execute();
    }

//returns the goalID in order to obtain the exercises associated with the goal
    private void loadIntoListView2(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] user = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            user[i] = obj.getString("GoalID" );



            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(RecommendedExercise.this, R.layout.custom_listview, user);
            goalID.setAdapter(arrayAdapter);
        }

    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//    private void getJSON3(final String urlWebService) {
//
//        class GetJSON3 extends AsyncTask<Void, Void, String> {
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
//                try {
//                    loadIntoListView3(s);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            protected String doInBackground(Void... voids) {
//                try {
//
//                    //uses the email and runs it through a select statement
//                    //the select statement finds the userID by using the email and returns the
//                    //goalID
//                    URL url = new URL(urlWebService);
//                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                    con.setRequestMethod("POST");
//                    con.setDoOutput(true);
//                    con.setDoInput(true);
//                    OutputStream outputStream = con.getOutputStream();
//                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
////                    ListView GoalID = findViewById(R.id.goalID);
//                    String post_data = URLEncoder.encode("goalID", "UTF-8") + "=" + URLEncoder.encode(goalID.getItemAtPosition(0).toString(), "UTF-8");
//                    System.out.println(post_data);
//
//                    bufferedWriter.write(post_data);
//                    bufferedWriter.flush();
//
//                    StringBuilder sb = new StringBuilder();
//                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                    String json;
//                    while ((json = bufferedReader.readLine()) != null) {
//                        sb.append(json + "\n");
//                        System.out.println(json);
//                    }
//                    bufferedWriter.close();
//                    outputStream.close();
//                    return sb.toString().trim();
//                } catch (Exception e) {
//                    return null;
//                }
//            }
//        }
//        GetJSON3 getJSON3 = new GetJSON3();
//        getJSON3.execute();
//    }
//
//    //returns the goalID in order to obtain the exercises associated with the goal
//    private void loadIntoListView3(String json) throws JSONException {
//        JSONArray jsonArray = new JSONArray(json);
//        String[] user = new String[jsonArray.length()];
//        for (int i = 0; i < jsonArray.length(); i++) {
//            JSONObject obj = jsonArray.getJSONObject(i);
//            user[i] = obj.getString("ExerciseTitle" );
//
//
//
//            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(RecommendedExercise.this, R.layout.custom_listview, user);
//            lvexercises.setAdapter(arrayAdapter);
//        }
//
//    }






}