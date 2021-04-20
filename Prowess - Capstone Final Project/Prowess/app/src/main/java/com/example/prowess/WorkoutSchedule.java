package com.example.prowess;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
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
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//Try FragmentActivity
public class WorkoutSchedule extends AppCompatActivity{


    private static final String TAG = "WorkoutSchedule";
    ImageButton Update, back;

    private TextView mDisplayDate, wo;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    public static ListView exercise1, weight1, sets1, reps1, max1;
    GoogleSignInClient mGoogleSignInClient;
    TextView email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_schedule);

//variable that represents the back button to go to the home screen
        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomePage();
            }
        });

        //variables that represent the exercise, weight, sets, reps, and max weight
        //that will be displayed from the database
        exercise1 = (ListView) findViewById(R.id.spinnerWorkout1);
        weight1 = (ListView) findViewById(R.id.editWeight1);
        sets1 = (ListView) findViewById(R.id.editTextSets1);
        reps1 = (ListView) findViewById(R.id.editTextReps1);
        max1 = (ListView) findViewById(R.id.editTextMaxWeight1);




        //url link that has a select statement which brings back the title of the exercise
        getJSON("http://cgi.sice.indiana.edu/~jtessler/ExerciseScheduleData.php");

        //url link that has a select statement which brings back the title of the exercise
        getJSON2("http://cgi.sice.indiana.edu/~jtessler/ExerciseScheduleData.php");

        //url link that has a select statement which brings back the title of the exercise
        getJSON3("http://cgi.sice.indiana.edu/~jtessler/ExerciseScheduleData.php");

        //url link that has a select statement which brings back the title of the exercise
        getJSON4("http://cgi.sice.indiana.edu/~jtessler/ExerciseScheduleData.php");

        //url link that has a select statement which brings back the title of the exercise
        getJSON5("http://cgi.sice.indiana.edu/~jtessler/ExerciseScheduleData.php");



        //this set of code takes the email associated with the account from google
        //we can do a lot with the user's email information
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        email = findViewById(R.id.email_ws);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personEmail = acct.getEmail();
            email.setText(personEmail);

        }


//this variable represents the button that allows the user to edit theit workout
        Update = (ImageButton) findViewById(R.id.update);
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditActivity();
            }
        });


//represents the date but set so it is automatically started at today's date
        String date_n = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
        TextView date = (TextView) findViewById(R.id.defaultdate);

        date.setText(date_n);

//represents the date textview
        mDisplayDate = (TextView)findViewById(R.id.defaultdate);


        //when the textview is clicked a spinner pops up for the user to choose their date
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        WorkoutSchedule.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }


        });

//displays the date that was chosen from the datepicker
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                String date = year + "/" + month + "/" + day;
                mDisplayDate.setText(date);

                //The next series of JSON code 1-5 brings back the workout scheduled for that day
                //from the database
                //When the date is changed from the date picker and displayed, then it
                getJSON("http://cgi.sice.indiana.edu/~jtessler/ExerciseScheduleData.php");
                getJSON2("http://cgi.sice.indiana.edu/~jtessler/ExerciseScheduleData.php");
                getJSON3("http://cgi.sice.indiana.edu/~jtessler/ExerciseScheduleData.php");
                getJSON4("http://cgi.sice.indiana.edu/~jtessler/ExerciseScheduleData.php");
                getJSON5("http://cgi.sice.indiana.edu/~jtessler/ExerciseScheduleData.php");


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
                            TextView email = findViewById(R.id.email_ws);
                            String post_data = URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(Date.getText().toString(),"UTF-8") + "&"
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

//displays the exercise titles for the specific day
            private void loadIntoListView(String json) throws JSONException {
                JSONArray jsonArray = new JSONArray(json);
                String[] user = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    user[i] = obj.getString("ExerciseTitle" );


                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(WorkoutSchedule.this, R.layout.custom_listview, user);
                    exercise1.setAdapter(arrayAdapter);
                }

            }

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
                    TextView email = findViewById(R.id.email_ws);
                    String post_data = URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(Date.getText().toString(),"UTF-8") + "&"
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

//displays the weight for the specific exercises for the specific day
    private void loadIntoListView2(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] user = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            user[i] = obj.getString("Weight" );



            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(WorkoutSchedule.this, R.layout.custom_listview, user);
            weight1.setAdapter(arrayAdapter);
        }

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void getJSON3(final String urlWebService) {

        class GetJSON3 extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView3(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {

                    //uses the date that is from the date picker as well as the email to run it through a
                    //select statement which takes email and date, the userID associated
                    //with that email, and brings back the different reps for the workout they chose for
                    ///that day
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    TextView Date = findViewById(R.id.defaultdate);
                    TextView email = findViewById(R.id.email_ws);
                    String post_data = URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(Date.getText().toString(),"UTF-8") + "&"
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
        GetJSON3 getJSON3 = new GetJSON3();
        getJSON3.execute();
    }

    //displays the reps for the specific exercises for the specific day
    private void loadIntoListView3(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] user = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            user[i] = obj.getString("Reps" );



            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(WorkoutSchedule.this, R.layout.custom_listview, user);
            reps1.setAdapter(arrayAdapter);
        }

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void getJSON4(final String urlWebService) {

        class GetJSON4 extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView4(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {

                    //uses the date that is from the date picker as well as the email to run it through a
                    //select statement which takes email and date, the userID associated
                    //with that email, and brings back the different sets for the workout they chose for
                    ///that day
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    TextView Date = findViewById(R.id.defaultdate);
                    TextView email = findViewById(R.id.email_ws);
                    String post_data = URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(Date.getText().toString(),"UTF-8") + "&"
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
        GetJSON4 getJSON4 = new GetJSON4();
        getJSON4.execute();
    }

            //displays the sets for the specific exercises for the specific day
        private void loadIntoListView4(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] user = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            user[i] = obj.getString("Sets" );



            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(WorkoutSchedule.this, R.layout.custom_listview, user);
            sets1.setAdapter(arrayAdapter);
        }

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void getJSON5(final String urlWebService) {

        class GetJSON5 extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView5(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {

                    //uses the date that is from the date picker as well as the email to run it through a
                    //select statement which takes email and date, the userID associated
                    //with that email, and brings back the max weights for the workout they chose for
                    ///that day
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    TextView Date = findViewById(R.id.defaultdate);
                    TextView email = findViewById(R.id.email_ws);
                    String post_data = URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(Date.getText().toString(),"UTF-8") + "&"
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
        GetJSON5 getJSON5 = new GetJSON5();
        getJSON5.execute();
    }

            //displays the max. weight for the specific exercises for the specific day
        private void loadIntoListView5(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] user = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            user[i] = obj.getString("MaxWeight" );



            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(WorkoutSchedule.this, R.layout.custom_listview, user);
            max1.setAdapter(arrayAdapter);
        }

    }




        };


    }

    public void openEditActivity() {
        Intent intent = new Intent(this, EditSchedule.class);
        startActivity(intent);
    }

    //Everything that was commented above is the same exact thing as down here.
    //there are multiple of the same code because this is the first set of information you see
    //because it goes with today's set date.
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


                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    TextView Date = findViewById(R.id.defaultdate);
                    TextView email = findViewById(R.id.email_ws);
                    String post_data = URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(Date.getText().toString(),"UTF-8") + "&"
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

    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] user = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            user[i] = obj.getString("ExerciseTitle" );



            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.custom_listview, user);
            exercise1.setAdapter(arrayAdapter);
        }

    }





    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////






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


                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    TextView Date = findViewById(R.id.defaultdate);
                    TextView email = findViewById(R.id.email_ws);
                    String post_data = URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(Date.getText().toString(),"UTF-8") + "&"
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

    private void loadIntoListView2(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] user = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            user[i] = obj.getString("Weight" );



            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.custom_listview, user);
            weight1.setAdapter(arrayAdapter);
        }

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void getJSON3(final String urlWebService) {

        class GetJSON3 extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView3(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {


                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    TextView Date = findViewById(R.id.defaultdate);
                    TextView email = findViewById(R.id.email_ws);
                    String post_data = URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(Date.getText().toString(),"UTF-8") + "&"
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
        GetJSON3 getJSON3 = new GetJSON3();
        getJSON3.execute();
    }

    private void loadIntoListView3(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] user = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            user[i] = obj.getString("Reps" );



            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.custom_listview, user);
            reps1.setAdapter(arrayAdapter);
        }

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void getJSON4(final String urlWebService) {

        class GetJSON4 extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView4(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {


                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    TextView Date = findViewById(R.id.defaultdate);
                    TextView email = findViewById(R.id.email_ws);
                    String post_data = URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(Date.getText().toString(),"UTF-8") + "&"
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
        GetJSON4 getJSON4 = new GetJSON4();
        getJSON4.execute();
    }

    private void loadIntoListView4(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] user = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            user[i] = obj.getString("Sets" );



            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.custom_listview, user);
            sets1.setAdapter(arrayAdapter);
        }

    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void getJSON5(final String urlWebService) {

        class GetJSON5 extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView5(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {


                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    TextView Date = findViewById(R.id.defaultdate);
                    TextView email = findViewById(R.id.email_ws);
                    String post_data = URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(Date.getText().toString(),"UTF-8") + "&"
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
        GetJSON5 getJSON5 = new GetJSON5();
        getJSON5.execute();
    }

    private void loadIntoListView5(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] user = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            user[i] = obj.getString("MaxWeight" );



            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.custom_listview, user);
            max1.setAdapter(arrayAdapter);
        }

    }

    public void openHomePage() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
        finish();
    }



}