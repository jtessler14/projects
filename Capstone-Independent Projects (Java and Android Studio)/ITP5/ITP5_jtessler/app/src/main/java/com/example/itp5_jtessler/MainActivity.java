package com.example.itp5_jtessler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {
//text boxes that will be edited
    EditText Username_editText, Password_editText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //edits the text for the username and password for when you are typing them
        Username_editText = (EditText) findViewById(R.id.username);
        Password_editText = (EditText) findViewById(R.id.password);

    }
public void OnLogin(View view) {
        //this set of code gets the username and password string that is typed in the
        //username and password section of the application
        String username = Username_editText.getText().toString();
        String password = Password_editText.getText().toString();
        String type = "login";

        //this set of code is linked to the BackgroundWorker.java page. Basically these
    //two pages will work together in order to complete the required task. This page just works
    //the xml and the backgroundworker runs code in the background that doesn't have much to do with the xml
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, username, password);
}

}



