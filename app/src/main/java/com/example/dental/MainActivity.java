package com.example.dental;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref;
        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        sharedPref.edit().putString("ip", "http://dentaldb.42web.io/sql.php?").commit();


        SharedPreferences sharedPreferences= getSharedPreferences("myPref", MODE_PRIVATE);
        String num=sharedPreferences.getString("phno", "");
        if(TextUtils.isEmpty(num))
        {
            Toast.makeText(this, " login page open", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "check user and enter mainapges", Toast.LENGTH_SHORT).show();
        }

        startActivity(new Intent(getApplicationContext(), login_signup.class));
        overridePendingTransition(0,0);
    }
}