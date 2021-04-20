package com.example.prowess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
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
import java.util.List;

public class WaterTracker extends AppCompatActivity {

    private Button maleBttn;
    private Button femaleBttn;
    private ListView userID;
    GoogleSignInClient mGoogleSignInClient;
    TextView email;

    private TextView goalText;
    private EditText intake;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_tracker);

        //select statement that gets userID
        getJSON("http://cgi.sice.indiana.edu/~jtessler/UserID.php");

        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomePage();
            }
        });



        //this set of code takes the email associated with the account from google
        //we can do a lot with the user's email information
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        email = findViewById(R.id.email_wt);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personEmail = acct.getEmail();
            email.setText(personEmail);

        }

        userID = (ListView) findViewById(R.id.userID_wt);

        maleBttn = (Button)findViewById(R.id.maleBttn);
        femaleBttn = (Button)findViewById(R.id.femaleBttn);

        intake = (EditText)findViewById(R.id.waterIntake);
        goalText = (TextView)findViewById(R.id.goal);

        goalText.setText("3.7 L");
        maleBttn.setBackgroundDrawable(getResources().getDrawable(R.drawable.red));
        femaleBttn.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
    }

    public void OnMale(View view) {
        maleBttn.setBackgroundDrawable(getResources().getDrawable(R.drawable.red));
        femaleBttn.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));

        goalText.setText("3.7 L");
    }

    public void OnFemale(View view) {
        femaleBttn.setBackgroundDrawable(getResources().getDrawable(R.drawable.red));
        maleBttn.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));

        goalText.setText("2.7 L");
    }

    public void OnSubmit(View view) {
        if(!intake.getText().toString().equals("0")){
            String type = "update";
            String userid = (userID.getItemAtPosition(0).toString());


            BackgroundWorkerWT backgroundWorker = new BackgroundWorkerWT(this);
            backgroundWorker.execute(type, intake.getText().toString(), userid);
        }
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
                    TextView email = findViewById(R.id.email_wt);
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

    public void openHomePage() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
        finish();
    }
}