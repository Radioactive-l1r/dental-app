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
import android.view.Gravity;
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
    EditText phno, password;
    String name_s, phno_s, mail_s, password_s;
    ArrayList<String> phone_list=new ArrayList<>();
    Map<String, String> ph_pas_map = new HashMap<>();
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

        phno = findViewById(R.id.phno);
        password = findViewById(R.id.password);

        sign_up = findViewById(R.id.sign_up);
        sign_up.setOnClickListener(view -> open_sign_dialog());

        login = findViewById(R.id.login);
        login.setOnClickListener(view -> {

            phno_s = phno.getText().toString();
            password_s = password.getText().toString();

            if(TextUtils.isEmpty(phno_s) || TextUtils.isEmpty(password_s))
            {
                toast = Toast.makeText(login_signup.this, "some fields are empty !", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            // doctor login
            else if(phno_s.contains("0123456789") && password_s.contains("1234"))
            {
                startActivity(new Intent(getApplicationContext(), doctor_main.class));
                overridePendingTransition(0,0);
            }
            else if(!phone_list.contains(phno_s))
            {
                toast = Toast.makeText(login_signup.this, "user doesn't exist", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            else
            {
                    /* check if pasword matches*/
                    String map_pas=ph_pas_map.get(phno_s);
                    if(password_s.equals(map_pas))
                    {
                        Toast.makeText(login_signup.this, "logged in", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(login_signup.this, patient_main.class);
                        i.putExtra("phno", phno_s);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                        SharedPreferences sharedPreferences = this.getSharedPreferences("dental", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("phno", phno_s);
                        editor.apply();
                        startActivity(i);
                    }
                    else
                    {
                        toast = Toast.makeText(login_signup.this, "Incorrect password!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
            }
        });

        phno.setText("");
        password.setText("");
        common.create_pd(login_signup.this);

        new bg("fetch").execute();

    }

    void open_sign_dialog()
    {
        Dialog d = new Dialog(this);
        d.setContentView(R.layout.sign_up_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView sign_up, name, phno, mail, password;
        sign_up = d.findViewById(R.id.sign_up);
        name = d.findViewById(R.id.name);
        phno = d.findViewById(R.id.phno);
        mail = d.findViewById(R.id.mail);
        password = d.findViewById(R.id.password);

        sign_up.setOnClickListener(view -> {

            name_s = name.getText().toString().replace("'", "''");
            password_s = password.getText().toString().replace("'", "''");
            phno_s = phno.getText().toString();
            mail_s = mail.getText().toString();

            if(TextUtils.isEmpty(name_s)||TextUtils.isEmpty(password_s)||TextUtils.isEmpty(phno_s)||TextUtils.isEmpty(mail_s))
            {
                toast = Toast.makeText(login_signup.this, "Some fields are empty!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            else  if(phone_list.contains(phno_s))
            {
                toast = Toast.makeText(login_signup.this, "User already exists! ", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            else {
                new bg("insert").execute();
                d.dismiss();
                toast = Toast.makeText(login_signup.this, "Sign Up Successful", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

        d.show();
    }


    class bg extends AsyncTask<Object,Void,Void>
    {
        private String action;

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
                jsonArr = common.send_req("c_qry=SELECT phno,password FROM patient");
                phone_list.clear();
                ph_pas_map.clear();

                for (int i = 0; i < jsonArr.length(); i++)
                {
                    JSONObject jsonObj;
                    try {
                        jsonObj = jsonArr.getJSONObject(i);

                        String num_ = jsonObj.getString("phno");
                        String password_ = jsonObj.getString("password");

                        ph_pas_map.put(num_,password_);
                        phone_list.add(num_);

                        } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
            else if(action.contains("insert"))
            {
                common.send_req("c_qry=insert into patient(name,phno,mail,password) values ('"+name_s+"','"+phno_s+"','"+mail_s+"','"+password_s+"')");
                phone_list.add(phno_s);
                ph_pas_map.put(phno_s, password_s);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused)
        {
            super.onPostExecute(unused);
            common.dism();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}