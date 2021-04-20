package com.example.prowess;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.ArrayList;
import java.util.Locale;

public class Stretch extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ListView userID;
    TextView email;
    GoogleSignInClient mGoogleSignInClient;


    ImageButton back;
    private EditText mEditTextInput;
    TextView mTextViewCountDown;
    Button mButtonSet;
    public Button mButtonStartPause;
    private Button mButtonReset;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;
    Spinner Stretches, s2;
    ArrayList<String> StretchTarget = new ArrayList<>();
    ArrayList<String> StretchTitle = new ArrayList<>();
    ArrayAdapter<String> targetAdapter;
    ArrayAdapter<String> titleAdapter;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stretch);

        //Gets the Stretching.php file that is connected to our team database
        getJSON("http://cgi.sice.indiana.edu/~jtessler/UserID.php");


        userID = (ListView) findViewById(R.id.userID_stretch);

        //this set of code takes the email associated with the account from google
        //we can do a lot with the user's email information
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        email = findViewById(R.id.email_stretch);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personEmail = acct.getEmail();
            email.setText(personEmail);

        }


        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomePage();
            }
        });

        //Represents buttons and fields for users to interact with
        requestQueue = Volley.newRequestQueue(this);
        Stretches = (Spinner)findViewById(R.id.Stretches);
        mEditTextInput = findViewById(R.id.edit_text_input);
        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        mButtonSet = findViewById(R.id.button_set);
        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);
        //Making sure the user enters a positive number
        mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mEditTextInput.getText().toString();
                if (input.length() == 0) {
                    Toast.makeText(Stretch.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                long millisInput = Long.parseLong(input) * 60000;
                if (millisInput == 0) {
                    Toast.makeText(Stretch.this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
                    return;
                }
                setTime(millisInput);
                mEditTextInput.setText("");
            }
        });
        //When the start/pause button is clicked, they are sent to their respected methods
        //Once the user presses start, the information of the stretch will be sent to the Stretch_WorkoutPlan table in our team database
        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();

                    String str_Stretch = Stretches.getSelectedItem().toString();
                    String str_Time = mTextViewCountDown.getText().toString();
                    String userid = (userID.getItemAtPosition(0).toString());
                    String type = "stretch";

                    BackgroundWorkerStretch backgroundWorker = new BackgroundWorkerStretch(Stretch.this);
                    backgroundWorker.execute(type, str_Stretch, str_Time, userid);
                }
            }
        });
        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        //This link grabs the StretchTitle's from the Stretch table in our team database
        //These StretchTitle's are then populated in the stretch dropdown/spinner
        String url = "http://cgi.sice.indiana.edu/~hhauf/Stretching.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("Stretch");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String stretchTarget = jsonObject.optString("StretchTitle");
                        StretchTarget.add(stretchTarget);
                        targetAdapter = new ArrayAdapter<>(Stretch.this,
                                R.layout.simple_spinner_item, StretchTarget);
                        targetAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                        Stretches.setAdapter(targetAdapter);

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
        Stretches.setOnItemSelectedListener(this);
    }
    //This sets the time
    private void setTime(long milliseconds) {
        mStartTimeInMillis = milliseconds;
        resetTimer();
        closeKeyboard();
    }
    //This starts the timer
    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateWatchInterface();
            }
        }.start();
        mTimerRunning = true;
        updateWatchInterface();
    }
    //This pauses the timer
    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateWatchInterface();
    }
    //This resets the timer
    private void resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
        updateWatchInterface();
    }
    //This update the countdowntext to amount of time the user specifies
    //There are different formats depending on how much time is left
    private void updateCountDownText() {
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }
        mTextViewCountDown.setText(timeLeftFormatted);
    }
    //This changes the visibility of the buttons to keep a more sleek look and less bulky
    private void updateWatchInterface() {
        if (mTimerRunning) {
            mEditTextInput.setVisibility(View.INVISIBLE);
            mButtonSet.setVisibility(View.INVISIBLE);
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Pause");
        } else {
            mEditTextInput.setVisibility(View.VISIBLE);
            mButtonSet.setVisibility(View.VISIBLE);
            mButtonStartPause.setText("Start");
            if (mTimeLeftInMillis < 1000) {
                mButtonStartPause.setVisibility(View.INVISIBLE);
            } else {
                mButtonStartPause.setVisibility(View.VISIBLE);
            }
            if (mTimeLeftInMillis < mStartTimeInMillis) {
                mButtonReset.setVisibility(View.VISIBLE);
            } else {
                mButtonReset.setVisibility(View.INVISIBLE);
            }
        }
    }
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    //This stops the timer
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);
        editor.apply();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        String Stretch = String.valueOf(Stretches.getSelectedItem());

        mStartTimeInMillis = prefs.getLong("startTimeInMillis", 600000);
        mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        updateCountDownText();
        updateWatchInterface();
        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateWatchInterface();
            } else {
                startTimer();


            }
        }
    }
    //Depending on what stretch the user selects from the dropdown, the timer will set itself to the correct amount of time that is needed
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String text = Stretches.getSelectedItem().toString();
        if (text.equals("Quad")) {
            mTextViewCountDown.setText("0:30");
            mTimeLeftInMillis = 30000;
            mStartTimeInMillis = 30000;
        }
        if (text.equals("Hamstring")){
            mTextViewCountDown.setText("0:20");
            mTimeLeftInMillis = 20000;
            mStartTimeInMillis = 20000;
        }
        if (text.equals("Calf")){
            mTextViewCountDown.setText("0:30");
            mTimeLeftInMillis = 30000;
            mStartTimeInMillis = 30000;
        }
        if (text.equals("Inner Thigh")){
            mTextViewCountDown.setText("1:00");
            mTimeLeftInMillis = 60000;
            mStartTimeInMillis = 60000;
        }
        if (text.equals("Supine")){
            mTextViewCountDown.setText("0:45");
            mTimeLeftInMillis = 45000;
            mStartTimeInMillis = 45000;
        }
        if (text.equals("Child's Pose")){
            mTextViewCountDown.setText("2:00");
            mTimeLeftInMillis = 120000;
            mStartTimeInMillis = 120000;
        }
        if (text.equals("Knee-to-Chest")){
            mTextViewCountDown.setText("0:30");
            mTimeLeftInMillis = 30000;
            mStartTimeInMillis = 30000;
        }
        if (text.equals("Piriformis")){
            mTextViewCountDown.setText("0:30");
            mTimeLeftInMillis = 30000;
            mStartTimeInMillis = 30000;
        }
        if (text.equals("Spinal Twist")){
            mTextViewCountDown.setText("0:30");
            mTimeLeftInMillis = 30000;
            mStartTimeInMillis = 30000;
        }
        if (text.equals("Pelvic Twist")){
            mTextViewCountDown.setText("1:30");
            mTimeLeftInMillis = 90000;
            mStartTimeInMillis = 90000;
        }
        if (text.equals("Cat-Cow")){
            mTextViewCountDown.setText("1:00");
            mTimeLeftInMillis = 60000;
            mStartTimeInMillis = 60000;
        }
        if (text.equals("Sphinx")){
            mTextViewCountDown.setText("2:00");
            mTimeLeftInMillis = 120000;
            mStartTimeInMillis = 120000;
        }
        if (text.equals("Eagle Arms")){
            mTextViewCountDown.setText("1:00");
            mTimeLeftInMillis = 60000;
            mStartTimeInMillis = 60000;
        }
        if (text.equals("Reverse Prayer")){
            mTextViewCountDown.setText("1:00");
            mTimeLeftInMillis = 60000;
            mStartTimeInMillis = 60000;
        }
        if (text.equals("Cow Face Pose")){
            mTextViewCountDown.setText("1:00");
            mTimeLeftInMillis = 60000;
            mStartTimeInMillis = 60000;
        }
        if (text.equals("Assisted Side Bend")){
            mTextViewCountDown.setText("0:20");
            mTimeLeftInMillis = 20000;
            mStartTimeInMillis = 20000;
        }
        if (text.equals("Fingers Up & Down")){
            mTextViewCountDown.setText("0:45");
            mTimeLeftInMillis = 45000;
            mStartTimeInMillis = 45000;
        }
        if (text.equals("Butterfly")){
            mTextViewCountDown.setText("2:00");
            mTimeLeftInMillis = 120000;
            mStartTimeInMillis = 120000;
        }
        if (text.equals("Hip Flexor Lunge")){
            mTextViewCountDown.setText("1:00");
            mTimeLeftInMillis = 60000;
            mStartTimeInMillis = 60000;
        }
        if (text.equals("Side Lunge")){
            mTextViewCountDown.setText("0:30");
            mTimeLeftInMillis = 30000;
            mStartTimeInMillis = 30000;
        }
        if (text.equals("Cobra Pose")){
            mTextViewCountDown.setText("0:30");
            mTimeLeftInMillis = 30000;
            mStartTimeInMillis = 30000;
        }
        if (text.equals("Standing Quadricep")){
            mTextViewCountDown.setText("0:30");
            mTimeLeftInMillis = 30000;
            mStartTimeInMillis = 30000;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }

    public void openHomePage() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
        finish();
    }

    /*public void onBut(View view){
        String str_Stretch = Stretches.getSelectedItem().toString();
        String str_Time = mTextViewCountDown.getText().toString();
        String type = "register";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, str_Stretch, str_Time);
    }*/

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
                    TextView email = findViewById(R.id.email_stretch);
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


}