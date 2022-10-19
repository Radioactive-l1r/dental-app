package com.example.dental;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class login_signup extends AppCompatActivity {



    TextView sign_up;
    Button login;
    EditText phno,password;
    String ip;
    String name_s,phno_s,mail_s,password_s;
    ArrayList<String> phone_list=new ArrayList<>();
    Map<String, String> ph_pas_map = new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        SharedPreferences sharedPreferences= getSharedPreferences("myPref", MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

        phno=findViewById(R.id.phno);
        password=findViewById(R.id.password);

        sign_up=findViewById(R.id.sign_up);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            open_sign_dialog();
            }
        });

        login=findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                phno_s=phno.getText().toString();
                password_s=password.getText().toString();
                if(TextUtils.isEmpty(phno_s) || TextUtils.isEmpty(password_s))
                {
                    Toast.makeText(login_signup.this, "some fields are empty !", Toast.LENGTH_SHORT).show();
                }
                else if(!phone_list.contains(phno_s))
                {
                    Toast.makeText(login_signup.this, "user doesn't exist", Toast.LENGTH_SHORT).show();
                }
                else
                {
                        /* check if pasword matches*/
                        String map_pas=ph_pas_map.get(phno_s);
                        if(password_s.equals(map_pas))
                        {
                            Toast.makeText(login_signup.this, "logged in", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(login_signup.this, patient_main.class);
//                            String strName = null;
                            i.putExtra("number", phno_s);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);


                            sharedPreferences.edit().putString("phno", phno_s).commit();
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(login_signup.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
                        }
                }
            }
        });
        sharedPreferences.edit().putString("phno", "").commit();
        phno.setText("");
        password.setText("");
        ip=sharedPreferences.getString("ip", "");
        common.create_pd(login_signup.this);

        new bg("fetch").execute();

    }

    void open_sign_dialog()
    {
        Dialog d=new Dialog(this);
        d.setContentView(R.layout.sign_up_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView sign_up ,name,phno,mail,password;
        sign_up=d.findViewById(R.id.sign_up);
        name=d.findViewById(R.id.name);
        phno=d.findViewById(R.id.phno);
        mail=d.findViewById(R.id.mail);
        password=d.findViewById(R.id.password);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                name_s=name.getText().toString();
                password_s=password.getText().toString();
                phno_s=phno.getText().toString();
                mail_s=mail.getText().toString();
                if(TextUtils.isEmpty(name_s)||TextUtils.isEmpty(password_s)||TextUtils.isEmpty(phno_s)||TextUtils.isEmpty(mail_s))
                {
                    Toast.makeText(login_signup.this, "Some fields are empty!", Toast.LENGTH_SHORT).show();
                }
                else  if(phone_list.contains(phno_s))
                {
                    Toast.makeText(login_signup.this, "User already exists! ", Toast.LENGTH_SHORT).show();
                }
                else {
                    new bg("insert").execute();
                    d.dismiss();
                }
            }
        });
        d.show();
    }


    class bg extends AsyncTask<Object,Void,Void>
    {  private String action;

        bg(String action) {
            this.action = action;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            common.show();
        }

        @Override
        protected Void doInBackground(Object... objects) {
            if(action.contains("fetch"))
            {
                JSONArray jsonArr;
                jsonArr = common.send_req(ip,"c_qry=SELECT * FROM patient");
                phone_list.clear();
                ph_pas_map.clear();
                for (int i = 0; i < jsonArr.length(); i++)
                {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = jsonArr.getJSONObject(i);
                        Log.d("patients", "jarray: : "+jsonObj);

                        //String name_=jsonObj.getString("traffic_controller_name");
                        String num_=jsonObj.getString("phno");
                        String password_=jsonObj.getString("password");
                        ph_pas_map.put(num_,password_);
//                        if(num_.equals("sa") && password_.equals("sa"))
//                        {
//                            Log.d("compared", "doInBackground: ");
//                        }
                        phone_list.add(num_);

                        } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
            else if(action.contains("insert"))
            {
                common.send_req(ip,"c_qry=insert into patient(name,phno,mail,password) values ('"+name_s+"','"+phno_s+"','"+mail_s+"','"+password_s+"')");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused)
        {
            super.onPostExecute(unused);
        common.dism();
            if(!action.contains("fetch"))
            {
                new bg("fetch").execute();
            }
        }
    }

}