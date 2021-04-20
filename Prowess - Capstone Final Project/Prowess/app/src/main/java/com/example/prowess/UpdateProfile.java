package com.example.prowess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.HttpResponse;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class UpdateProfile extends AppCompatActivity {

    Button confirm;
    EditText height_editText, weight_editText;
    ListView dob;
    ImageView imageView;
    TextView name, email, fName, lName;
    ImageButton Cancel;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

//cancel button that allows the user to cancel their attempt to update the profile
        Cancel = (ImageButton) findViewById(R.id.cancel);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserProfile();
            }
        });

//variables that represent the user's DOB, height, and weight
        dob = (ListView) findViewById(R.id.dob);
        height_editText = (EditText) findViewById(R.id.Height);
        weight_editText = (EditText) findViewById(R.id.weight);

        //confirm button that confirms the user's updated information
        confirm = (Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateProfile.this.OnUpdate();
            }
        });

        //select statement that gets userID

        getJSON("http://cgi.sice.indiana.edu/~jtessler/UserInfo.php");



//variables that represent the user's profile pic, name, and email
        imageView = findViewById(R.id.profilePicture);
        name = (TextView) findViewById(R.id.name);
        fName = (TextView) findViewById(R.id.fname);
        lName = (TextView) findViewById(R.id.lname);
        email = (TextView) findViewById(R.id.email);

        //allow the following information to be displayed from google
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            Uri personPhoto = acct.getPhotoUrl();


            name.setText(personName);
            email.setText(personEmail);
            fName.setText(personGivenName);
            lName.setText(personFamilyName);
            Glide.with(this).load(String.valueOf(personPhoto)).into(imageView);
        }




    }

    //takes everything on the update page and sends it to the background worker to send to Database
    public void OnUpdate() {

        Log.d("UpdateActivity", "hello");

        //takes the user back to the user profile page
        Intent intent = new Intent(this, userprofile.class);
        startActivity(intent);

        //runs the following data through the background worker
        String personEmail = email.getText().toString();
        String height = height_editText.getText().toString();
        String weight = weight_editText.getText().toString();
        String type = "Update";


        BackgroundWorkerUpdateProfile backgroundWorker = new BackgroundWorkerUpdateProfile(this);
        backgroundWorker.execute(type, personEmail, height, weight);


    }

//brings back the user's date of birth that is in the database
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


                    //brings back the user's DOB based on the email that is displayed
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    OutputStream outputStream = con.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    TextView emailView = findViewById(R.id.email);
                    String post_data = URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(emailView.getText().toString(),"UTF-8");
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

    //displays the users DOB
    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] user = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            user[i] = obj.getString("DOB" );



            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.custom_listview_userinfo, user);
            dob.setAdapter(arrayAdapter);
        }

    }
//opens the user profile page
    public void openUserProfile() {
        Intent intent = new Intent(this, userprofile.class);
        startActivity(intent);
    }

}