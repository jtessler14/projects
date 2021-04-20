package com.example.prowess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Pulling extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    Spinner Stretches, s2;
    ArrayList<String> StretchTarget = new ArrayList<>();
    ArrayList<String> StretchTitle = new ArrayList<>();
    ArrayAdapter<String> targetAdapter;
    ArrayAdapter<String> titleAdapter;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);
        Stretches = findViewById(R.id.Stretches);


        //link that selects every stretch from the stretching table
        String url = "http://cgi.sice.indiana.edu/~hhauf/Stretching.php";


        ///This chunk of code goes through the URL and takes the stretches that were pulled from the database into JSON format
        //the data (The stretching title) was then put into a drop down menu for users to choose from
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("Stretch");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String stretchTarget = jsonObject.optString("StretchTitle");
                        StretchTarget.add(stretchTarget);
                        targetAdapter = new ArrayAdapter<>(Pulling.this,
                                android.R.layout.simple_spinner_item, StretchTarget);
                        targetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

        //displays stretches into the spinner
        requestQueue.add(jsonObjectRequest);
        Stretches.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, Stretch.class);
        startActivity(intent);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
