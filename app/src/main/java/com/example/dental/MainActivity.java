package com.example.dental;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView home;
    TextView refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        home = findViewById(androidx.constraintlayout.widget.R.id.home);
        if(isNetworkAvailable()){
            home.setText("HOME");
            startActivity(new Intent(getApplicationContext(), login_signup.class));
        }else{
            home.setText("You Are Offline\n Please Connect to Internet");
        }

        refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(view -> {
            finish();
            startActivity(getIntent());
        });

        overridePendingTransition(0,0);
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = (connectivityManager != null) ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetInfo != null && activeNetInfo.isConnected();
    }

}