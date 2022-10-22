package com.example.dental;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class doctor_main extends AppCompatActivity
{
    String name_s,date_s,time,_S,id_s,feedback_,feedbacK_s;
    String ip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);
        SharedPreferences sharedPreferences= getSharedPreferences("myPref", MODE_PRIVATE);
        ip=sharedPreferences.getString("ip", "");
        common.create_pd(doctor_main.this);



        new bg().execute();
    }


    class bg extends AsyncTask<Object,Void,Void>

    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            common.show();
        }

        @Override
        protected Void doInBackground(Object... objects) {
            JSONArray jsonArr;
            jsonArr=  common.send_req(ip,"c_qry=SELECT  * FROM appointment");

            for (int i = 0; i < jsonArr.length(); i++)
            {
                JSONObject jsonObj = null;
                try {
                    jsonObj = jsonArr.getJSONObject(i);
                    Log.d("appointments", "jarray: : "+jsonObj);

                    //String name_=jsonObj.getString("traffic_controller_name");
                    String id=jsonObj.getString("opp_id");
                    String password_=jsonObj.getString("password");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            common.dism();
        }
    }

}