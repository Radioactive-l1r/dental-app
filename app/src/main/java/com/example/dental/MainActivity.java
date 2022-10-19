package com.example.dental;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView home;
    TextView refresh;
    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref;
        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        sharedPref.edit().putString("ip", "http://dentaldb.42web.io/sql.php?").commit();


//        SharedPreferences sharedPreferences= getSharedPreferences("myPref", MODE_PRIVATE);
//        String num=sharedPreferences.getString("phno", "");
//        if(TextUtils.isEmpty(num))
//        {
//            Toast.makeText(this, " login page open", Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            Toast.makeText(this, "check user and enter mainapges", Toast.LENGTH_SHORT).show();
//        }

        home = findViewById(androidx.constraintlayout.widget.R.id.home);
        if(isNetworkAvailable()){
            home.setText("HOME");
            startActivity(new Intent(getApplicationContext(), login_signup.class));
        }else{
            home.setText("You Are Offline\n Please Connect to Internet");
        }

        refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent());
            }
        });

        overridePendingTransition(0,0);
    }

    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = (connectivityManager != null) ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetInfo != null && activeNetInfo.isConnected();
    }

}