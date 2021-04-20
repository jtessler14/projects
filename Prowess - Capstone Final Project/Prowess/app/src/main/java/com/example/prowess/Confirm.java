package com.example.prowess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.HttpResponse;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import org.w3c.dom.Text;

public class Confirm extends AppCompatActivity {

    Button confirm;
    EditText age_editText, height_editText, weight_editText;

    ImageView imageView;
    TextView name, email, fName, lName;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        //variables that represent the user's DOB, height, and weight
        age_editText = (EditText) findViewById(R.id.age);
        height_editText = (EditText) findViewById(R.id.Height);
        weight_editText = (EditText) findViewById(R.id.weight);

        //variable that represents the confirms button
        confirm = (Button) findViewById(R.id.confirm);
        //when the confirmed button is clicked it triggers a reaction
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Confirm.this.OnConfirm();
            }
        });


//variables that represent all the information pulled from google
        imageView = findViewById(R.id.profilePicture);
        name = (TextView) findViewById(R.id.name);
        fName = (TextView) findViewById(R.id.fname);
        lName = (TextView) findViewById(R.id.lname);
        email = (TextView) findViewById(R.id.email);

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
    public void OnConfirm() {

        Log.d("UpdateActivity", "hello");

        Intent intent = new Intent(this, Goal.class);
        startActivity(intent);

        String personGivenName = fName.getText().toString();
        String personFamilyName = lName.getText().toString();
        String personEmail = email.getText().toString();
        String age = age_editText.getText().toString();
        String height = height_editText.getText().toString();
        String weight = weight_editText.getText().toString();
        String type = "Confirm";


        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, personGivenName, personFamilyName, personEmail, age,height, weight);


    }

}

