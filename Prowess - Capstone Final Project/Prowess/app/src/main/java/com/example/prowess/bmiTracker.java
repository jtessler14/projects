package com.example.prowess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class bmiTracker extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    ListView userID;
    TextView email;

    Button calculate;
    TextView result;
    EditText weight, height;
    ImageButton back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi_tracker);

        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomePage();
            }
        });

//json link which brings back the UserID associated to the account so the user can insert
        //the userID as well with anything else
        getJSON("http://cgi.sice.indiana.edu/~jtessler/UserID.php");



        //variables that represent certain numbers
        weight = (EditText) findViewById(R.id.weight);
        height = (EditText) findViewById(R.id.height);
        result = (TextView) findViewById(R.id.result);
        calculate = (Button) findViewById(R.id.btn);

//when the calculate button is clicked it calculates what is in the edit text
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcBMI();


            }


        });
//userID variable so the program represents what user is on the application
        userID = (ListView) findViewById(R.id.userID_bmi);


        //this set of code takes the email associated with the account from google
        //we can do a lot with the user's email information
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        email = findViewById(R.id.email_bmi);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personEmail = acct.getEmail();
            email.setText(personEmail);

        }




    }


    private void calcBMI() {
        String str_height = height.getText().toString();
        String str_weight = weight.getText().toString();

        /*String type = "result";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, str_weight, str_height);*/

        //if statement stating that if the edit texts are filled in it takes the number
        //and runs it through the variables in order to get the BMI of the User
        if (str_height != null && !"".equals(str_height) && str_weight != null && !"".equals(str_weight)) {
            float heightVal = Float.parseFloat(str_height);
            float weightVal = Float.parseFloat(str_weight);

//calculates the BMI
            float bmi = (weightVal / (heightVal * heightVal))*703;

            //Gets today's date and time
            SimpleDateFormat dateTime = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss", Locale.getDefault());


//takes the BMI, userID, and date and time and runs it through the background worker
            String str_bmi = String.valueOf(bmi);
            String currentDateandTime = dateTime.format(new Date());
            String userid = (userID.getItemAtPosition(0).toString());
            String type = "result";


            BackgroundWorkerBMI backgroundWorker = new BackgroundWorkerBMI(this);
            backgroundWorker.execute(type, str_bmi, currentDateandTime, userid);

            displayBMI(bmi);
        }



        /*String str_Result = result.getText().toString();
        String type = "result";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, str_Result);*/

    }

    //when the bmi is calculated it will give the number that their bmi as well as tell them
    //how underweight or overweight the user is based on their height and weight
    private void displayBMI(Float bmi) {
        String bmiLabel = "";

        if (Float.compare(bmi, 15f) <= 0) {
            bmiLabel = "Very Severely Underweight";
        } else if (Float.compare(bmi, 15f) > 0 && Float.compare(bmi, 16f) <= 0) {
            bmiLabel = "Severely Underweight";
        } else if (Float.compare(bmi, 16f) > 0 && Float.compare(bmi, 18.5f) <= 0) {
            bmiLabel = "Underweight";
        } else if (Float.compare(bmi, 18.5f) > 0 && Float.compare(bmi, 25f) <= 0) {
            bmiLabel = "Normal";
        } else if (Float.compare(bmi, 25f) > 0 && Float.compare(bmi, 30f) <= 0) {
            bmiLabel = "Overweight";
        } else if (Float.compare(bmi, 30f) > 0 && Float.compare(bmi, 35f) <= 0) {
            bmiLabel = "Obese Class I";
        } else if (Float.compare(bmi, 35f) > 0 && Float.compare(bmi, 40f) <= 0) {
            bmiLabel = "Obese Class II";
        } else {
            bmiLabel = "Obese Class III";
        }

        bmiLabel = bmi + "\n" + bmiLabel;
        result.setText(bmiLabel);
    }

    /*public void onRes(View view){
        String str_Weight = weight.getText().toString();
        String str_Height = height.getText().toString();
        String type = "result";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, str_Weight, str_Height);
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
                    TextView email = findViewById(R.id.email_bmi);
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

    public void openHomePage() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
        finish();
    }


}