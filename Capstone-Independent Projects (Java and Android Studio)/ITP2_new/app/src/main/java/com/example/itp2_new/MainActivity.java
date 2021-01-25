package com.example.itp2_new;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.time.LocalTime;
import java.util.Date;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {
    @TargetApi(Build.VERSION_CODES.O)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView=findViewById(R.id.message);
        LocalTime est = LocalTime.now();
        textView.setText(est.getHour() + "");
        if ((est.getHour() == 5 && est.getMinute() > 45) || (est.getHour() > 5 && est.getHour() < 12)){
            textView.setText("Good Morning");
        }else if (est.getHour() < 18 && est.getHour() >= 12){
            textView.setText("Good Afternoon");
        }else{
            textView.setText("Good Evening");
        }

        TextView newtextView=findViewById(R.id.time);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
        String currentDateandTime = sdf.format(new Date());
        newtextView.setText(currentDateandTime);





    }
}