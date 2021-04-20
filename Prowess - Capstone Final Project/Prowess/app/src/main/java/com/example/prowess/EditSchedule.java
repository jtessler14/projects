package com.example.prowess;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Locale;


public class EditSchedule extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    GoogleSignInClient mGoogleSignInClient;



    private static final String TAG = "EditSchedule";
    ImageButton Cancel;
    ListView userID;
    TextView email;

    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    Spinner spinner1, spinner2, spinner3, spinner4, spinner5, spinner6, spinner7;
    ArrayList<String> Exercise = new ArrayList<>();
    ArrayAdapter<String> targetAdapter;
    RequestQueue requestQueue;
    Button Workout;
    EditText weight1new, weight2new, weight3new, weight4new, weight5new, weight6new, weight7new,
            sets1new, sets2new, sets3new, sets4new, sets5new, sets6new, sets7new,
            reps1new, reps2new, reps3new, reps4new, reps5new, reps6new, reps7new,
            max1new, max2new, max3new, max4new, max5new, max6new, max7new;




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_schedule);


//json link which brings back the UserID assocaited to the account so the user can insert
        //the userID as well with anything else
        getJSON("http://cgi.sice.indiana.edu/~jtessler/UserID.php");

        //variable that represents the userID
        userID = (ListView) findViewById(R.id.userID);


        //this set of code takes the email associated with the account from google
        //we can do a lot with the user's email information
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        email = findViewById(R.id.email_ews);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personEmail = acct.getEmail();
            email.setText(personEmail);

        }

//These sets of variables represent the weight, reps, sets, and max weight for
        //each of the workouts the user is scheduling for that day
        weight1new = (EditText) findViewById(R.id.editWeight1);
        weight2new = (EditText) findViewById(R.id.editWeight2);
        weight3new = (EditText) findViewById(R.id.editWeight3);
        weight4new = (EditText) findViewById(R.id.editWeight4);
        weight5new = (EditText) findViewById(R.id.editWeight5);
        weight6new = (EditText) findViewById(R.id.editWeight6);
        weight7new = (EditText) findViewById(R.id.editWeight7);

        reps1new = (EditText) findViewById(R.id.editTextReps1);
        reps2new = (EditText) findViewById(R.id.editTextReps2);
        reps3new = (EditText) findViewById(R.id.editTextReps3);
        reps4new = (EditText) findViewById(R.id.editTextReps4);
        reps5new = (EditText) findViewById(R.id.editTextReps5);
        reps6new = (EditText) findViewById(R.id.editTextReps6);
        reps7new = (EditText) findViewById(R.id.editTextReps7);

        sets1new = (EditText) findViewById(R.id.editTextSets1);
        sets2new = (EditText) findViewById(R.id.editTextSets2);
        sets3new = (EditText) findViewById(R.id.editTextSets3);
        sets4new = (EditText) findViewById(R.id.editTextSets4);
        sets5new = (EditText) findViewById(R.id.editTextSets5);
        sets6new = (EditText) findViewById(R.id.editTextSets6);
        sets7new = (EditText) findViewById(R.id.editTextSets7);

        max1new = (EditText) findViewById(R.id.editTextMaxWeight1);
        max2new = (EditText) findViewById(R.id.editTextMaxWeight2);
        max3new = (EditText) findViewById(R.id.editTextMaxWeight3);
        max7new = (EditText) findViewById(R.id.editTextMaxWeight4);
        max4new = (EditText) findViewById(R.id.editTextMaxWeight5);
        max5new = (EditText) findViewById(R.id.editTextMaxWeight6);
        max6new = (EditText) findViewById(R.id.editTextMaxWeight7);

//when button is clicked it triggers an event
        Workout = (Button) findViewById(R.id.workout);
        Workout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScheduleActivity();
            }
        });

        //when button is clicked it brings back to WorkoutSchedule
        Cancel = (ImageButton) findViewById(R.id.cancel);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWorkoutScheduleActivity();
            }
        });

        //Each of these spinners is a dropdown which the user can select a workout from
        spinner1 = (Spinner) findViewById(R.id.spinnerWorkout1);
        spinner2 = (Spinner) findViewById(R.id.spinnerWorkout2);
        spinner3 = (Spinner) findViewById(R.id.spinnerWorkout3);
        spinner4 = (Spinner) findViewById(R.id.spinnerWorkout4);
        spinner5 = (Spinner) findViewById(R.id.spinnerWorkout5);
        spinner6 = (Spinner) findViewById(R.id.spinnerWorkout6);
        spinner7 = (Spinner) findViewById(R.id.spinnerWorkout7);
        requestQueue = Volley.newRequestQueue(this);

        //this set of code runs a select statement within the php from the link below. It then takes
        //the Exercises that have been selected and puts them into the spinner for the user to
        //choose from
        String url = "http://cgi.sice.indiana.edu/~jtessler/Exercises.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("Exercise");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String exercise = jsonObject.optString("ExerciseTitle");
                        Exercise.add(exercise);
                        targetAdapter = new ArrayAdapter<>(EditSchedule.this,
                                R.layout.spinner_format, Exercise);
                        targetAdapter.setDropDownViewResource(R.layout.spinnerdropdown);
                        spinner1.setAdapter(targetAdapter);
                        spinner2.setAdapter(targetAdapter);
                        spinner3.setAdapter(targetAdapter);
                        spinner4.setAdapter(targetAdapter);
                        spinner5.setAdapter(targetAdapter);
                        spinner6.setAdapter(targetAdapter);
                        spinner7.setAdapter(targetAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
        spinner1.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);
        spinner3.setOnItemSelectedListener(this);
        spinner4.setOnItemSelectedListener(this);
        spinner5.setOnItemSelectedListener(this);
        spinner6.setOnItemSelectedListener(this);
        spinner7.setOnItemSelectedListener(this);




//this set of code brings back todays date
        String date_l = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
        TextView datenew = (TextView) findViewById(R.id.todaystdate);

        datenew.setText(date_l);

        //this set of code sets the text view as today's date by default, but the spinner can be changed to
        //whatever date the user desires
        String date_n = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
        TextView date = (TextView) findViewById(R.id.defaultdate);

        date.setText(date_n);

        mDisplayDate = (TextView)findViewById(R.id.defaultdate);
        final TextView weight1 = (TextView) findViewById(R.id.editWeight1);
        final TextView weight2 = (TextView) findViewById(R.id.editWeight2);
        final TextView weight3 = (TextView) findViewById(R.id.editWeight3);
        final TextView weight4 = (TextView) findViewById(R.id.editWeight4);
        final TextView weight5 = (TextView) findViewById(R.id.editWeight5);
        final TextView weight6 = (TextView) findViewById(R.id.editWeight6);
        final TextView weight7 = (TextView) findViewById(R.id.editWeight7);
        final TextView reps1 = (TextView) findViewById(R.id.editTextReps1);
        final TextView reps2 = (TextView) findViewById(R.id.editTextReps2);
        final TextView reps3 = (TextView) findViewById(R.id.editTextReps3);
        final TextView reps4 = (TextView) findViewById(R.id.editTextReps4);
        final TextView reps5 = (TextView) findViewById(R.id.editTextReps5);
        final TextView reps6 = (TextView) findViewById(R.id.editTextReps6);
        final TextView reps7 = (TextView) findViewById(R.id.editTextReps7);
        final TextView sets1 = (TextView) findViewById(R.id.editTextSets1);
        final TextView sets2 = (TextView) findViewById(R.id.editTextSets2);
        final TextView sets3 = (TextView) findViewById(R.id.editTextSets3);
        final TextView sets4 = (TextView) findViewById(R.id.editTextSets4);
        final TextView sets5 = (TextView) findViewById(R.id.editTextSets5);
        final TextView sets6 = (TextView) findViewById(R.id.editTextSets6);
        final TextView sets7 = (TextView) findViewById(R.id.editTextSets7);
        final TextView max1 = (TextView) findViewById(R.id.editTextMaxWeight1);
        final TextView max2 = (TextView) findViewById(R.id.editTextMaxWeight2);
        final TextView max3 = (TextView) findViewById(R.id.editTextMaxWeight3);
        final TextView max4 = (TextView) findViewById(R.id.editTextMaxWeight4);
        final TextView max5 = (TextView) findViewById(R.id.editTextMaxWeight5);
        final TextView max6 = (TextView) findViewById(R.id.editTextMaxWeight6);
        final TextView max7 = (TextView) findViewById(R.id.editTextMaxWeight7);

//when the textview is clicked a spinner pops up for the user to choose their date
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        EditSchedule.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                //and if statement stateing that if the date chosen doesn't match up with today's
                //date, then everything in the textboxes will revert back to nothing in their.
                if (R.id.defaultdate != R.id.todaystdate) {
                    weight1.setText(null);
                    weight2.setText(null);
                    weight3.setText(null);
                    weight4.setText(null);
                    weight5.setText(null);
                    weight6.setText(null);
                    weight7.setText(null);
                    reps1.setText(null);
                    reps2.setText(null);
                    reps3.setText(null);
                    reps4.setText(null);
                    reps5.setText(null);
                    reps6.setText(null);
                    reps7.setText(null);
                    sets1.setText(null);
                    sets2.setText(null);
                    sets3.setText(null);
                    sets4.setText(null);
                    sets5.setText(null);
                    sets6.setText(null);
                    sets7.setText(null);
                    max1.setText(null);
                    max2.setText(null);
                    max3.setText(null);
                    max4.setText(null);
                    max5.setText(null);
                    max6.setText(null);
                    max7.setText(null);


                }


            }
        });

//this ondatesetlistener displays the date that the user picked from the datepicker
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
                String date = year + "/" + month + "/" + day;
                mDisplayDate.setText(date);


            }


        };



    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //when the "Workout!" button is clicked, it opens the Workout Schedule Screen
    //it also sends information to the background worker to insert into the database
    public void openScheduleActivity() {
        //opens workout schedule screen
        Intent intent = new Intent(this, WorkoutSchedule.class);
        startActivity(intent);

        //WORKOUTPLAN

//takes all the information entered for the user's workout that they scheduled and inserts it into the database.
// The user is then able to see their schedueled workout for that day by going into the datepicker and
        //selecting the workout they entered for the specific day

        String userid = (userID.getItemAtPosition(0).toString());
        String date = mDisplayDate.getText().toString();
        String exercise1 = spinner1.getSelectedItem().toString();
        String weight1 = weight1new.getText().toString();
        String reps1 = reps1new.getText().toString();
        String sets1 = sets1new.getText().toString();
        String max1 = max1new.getText().toString();

        String type8 = "Workout";

        BackgroundWorkerWS backgroundWorker = new BackgroundWorkerWS(this);
        backgroundWorker.execute(type8, userid, date, exercise1, weight1, reps1, sets1, max1);

        /////////////////////////////////////////////////////////////////////////////////

//takes all the information entered for the user's workout that they scheduled and inserts it into the database.
// The user is then able to see their schedueled workout for that day by going into the datepicker and
// selecting the workout they entered for the specific day
        exercise1 = spinner2.getSelectedItem().toString();
        weight1 = weight2new.getText().toString();
        sets1 = sets2new.getText().toString();
        reps1 = reps2new.getText().toString();
        max1 = max2new.getText().toString();



            BackgroundWorkerWS backgroundWorker1 = new BackgroundWorkerWS(this);
            backgroundWorker1.execute(type8, userid, date, exercise1, weight1, reps1, sets1, max1);



//        //////////////////////////////////////////////////////////////////////////

// takes all the information entered for the user's workout that they scheduled and inserts it into the database.
// The user is then able to see their schedueled workout for that day by going into the datepicker and
// selecting the workout they entered for the specific day
        exercise1 = spinner3.getSelectedItem().toString();
        weight1 = weight3new.getText().toString();
        sets1 = sets3new.getText().toString();
        reps1 = reps3new.getText().toString();
        max1 = max3new.getText().toString();


        BackgroundWorkerWS backgroundWorker2 = new BackgroundWorkerWS(this);
        backgroundWorker2.execute(type8, userid, date, exercise1, weight1, reps1, sets1, max1);

////        /////////////////////////////////////////////////////////////////////////

// takes all the information entered for the user's workout that they scheduled and inserts it into the database.
// The user is then able to see their schedueled workout for that day by going into the datepicker and
// selecting the workout they entered for the specific day
        exercise1 = spinner4.getSelectedItem().toString();
        weight1 = weight4new.getText().toString();
        sets1 = sets4new.getText().toString();
        reps1 = reps4new.getText().toString();
        max1 = max4new.getText().toString();



        BackgroundWorkerWS backgroundWorker3 = new BackgroundWorkerWS(this);
        backgroundWorker3.execute(type8, userid, date, exercise1, weight1, reps1, sets1, max1);

////        /////////////////////////////////////////////////////////////////////

// takes all the information entered for the user's workout that they scheduled and inserts it into the database.
// The user is then able to see their schedueled workout for that day by going into the datepicker and
// selecting the workout they entered for the specific day
        exercise1 = spinner5.getSelectedItem().toString();
        weight1 = weight5new.getText().toString();
        sets1 = sets5new.getText().toString();
        reps1 = reps5new.getText().toString();
        max1 = max5new.getText().toString();



        BackgroundWorkerWS backgroundWorker4 = new BackgroundWorkerWS(this);
        backgroundWorker4.execute(type8, userid, date, exercise1, weight1, reps1, sets1, max1);

////        //////////////////////////////////////////////////////////////////////

// takes all the information entered for the user's workout that they scheduled and inserts it into the database.
// The user is then able to see their schedueled workout for that day by going into the datepicker and
// selecting the workout they entered for the specific day
        exercise1 = spinner6.getSelectedItem().toString();
        weight1 = weight6new.getText().toString();
        sets1 = sets6new.getText().toString();
        reps1 = reps6new.getText().toString();
        max1 = max6new.getText().toString();


        BackgroundWorkerWS backgroundWorker5 = new BackgroundWorkerWS(this);
        backgroundWorker5.execute(type8, userid, date, exercise1, weight1, reps1, sets1, max1);

////        ///////////////////////////////////////////////////////////////////////////

// takes all the information entered for the user's workout that they scheduled and inserts it into the database.
// The user is then able to see their schedueled workout for that day by going into the datepicker and
// selecting the workout they entered for the specific day
        exercise1 = spinner7.getSelectedItem().toString();
        weight1 = weight7new.getText().toString();
        sets1 = sets7new.getText().toString();
        reps1 = reps7new.getText().toString();
        max1 = max7new.getText().toString();



        BackgroundWorkerWS backgroundWorker6 = new BackgroundWorkerWS(this);
        backgroundWorker6.execute(type8, userid, date, exercise1, weight1, sets1, reps1, max1);


        //////////////////////////////////////////////////////////////////////////



    }

    public void openWorkoutScheduleActivity() {
        Intent intent = new Intent(this, WorkoutSchedule.class);
        startActivity(intent);
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
                    TextView email = findViewById(R.id.email_ews);
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