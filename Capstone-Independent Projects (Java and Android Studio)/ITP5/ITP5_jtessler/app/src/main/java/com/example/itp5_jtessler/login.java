package com.example.itp5_jtessler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NoCache;
import com.android.volley.toolbox.HurlStack;
import org.json.JSONException;
import org.json.JSONObject;



public class login extends AppCompatActivity {


    // Represents RequestQueue.
    RequestQueue requestQueue;

    // Represents cache
    Cache cache = new NoCache();

    // Set up the network to use HttpURLConnection as the HTTP client.
    Network network = new BasicNetwork(new HurlStack());

    //link to the magic number generator
    String url ="https://infocpst.luddy.indiana.edu/magic-number/generate";

    //team number variable
    int team_number = 54;

    //number being typed in variable
    int number;

    //represents the id of the xml editText view
    EditText numberInput;

    //the button used to translate
    Button translate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //links to the editText to the xml page
        numberInput = (EditText) findViewById(R.id.numberInput);

        //links to the translate button on the xml page
        translate = (Button) findViewById(R.id.translate);

        //allows the button to be clicked and run code within the brackets
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //grabs the number that is inputted
                number = Integer.valueOf(numberInput.getText().toString());

                //this just shows the number on the bottom when the translate button is clicked
                // sort of a symbol to show that the button has been clicked and working
                showToast(String.valueOf(number));

                //if statement saying that if there is nothing in the request queue
                //then will request a new request and get the cache and network
                if (requestQueue == null) {
                    requestQueue = new RequestQueue(cache, network);

                    requestQueue.start();
                }

                //this set of code uses the team number variable and the number
                // variable and pushes it to the link in order to obtain a respons
                JSONObject json = new JSONObject();
                try {
                    json.accumulate("team", team_number);
                    json.accumulate("number", number);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // This set of code requests a string response from the URL above
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, json,
                        new Response.Listener<JSONObject>() {
                            //this set of code takes the JSON response and parses it
                            //it returns the status either true or false and then the message below
                            //it
                            @Override
                            public void onResponse(JSONObject response) {
                                TextView textView2 = findViewById(R.id.status);
                                TextView textView=findViewById(R.id.message);

                                try {
                                    textView.setText(response.getString("status"));
                                    textView2.setText(response.getString("message"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, null);

// This just adds the request to the RequestQueue
                requestQueue.add(req);

            }

        });

    }
    // this allows the number to appear below once the translate button is clicked
    private void showToast(String text) {
        Toast.makeText(login.this, text, Toast.LENGTH_SHORT).show();


    }


}