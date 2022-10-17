package com.example.dental;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class patient_main extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener {
    String ip;
    String number_s;
    TextView name,book,history;
    String age_type_S,date_S,time_s,problem_S,opp_id_S;
    Calendar calendar;
    int year,month,day;
    TextView bookd,date,time;
    TimePickerDialog timePickerDialog;
    int CalendarHour, CalendarMinute;

    ArrayList<String> id_list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);

        SharedPreferences sharedPreferences= getSharedPreferences("myPref", MODE_PRIVATE);
        ip=sharedPreferences.getString("ip", "");
        common.create_pd(patient_main.this);

        Bundle extras = getIntent().getExtras();
        number_s= sharedPreferences.getString("phno", "");
        Toast.makeText(this, ""+number_s, Toast.LENGTH_SHORT).show();

        calendar= Calendar.getInstance();
        name=findViewById(R.id.name);
        book=findViewById(R.id.book);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booK_dialog();
            }
        });
        history=findViewById(R.id.history);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(patient_main.this, patient_history.class);
                String strName = null;
                i.putExtra("number", number_s);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
            }
        });

        new bg("name").execute();
    }

    void booK_dialog()
    {

        //generate id of appointment
        genereate_id();
        Dialog d=new Dialog(this);
        d.setContentView(R.layout.book_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText problem;
        problem=d.findViewById(R.id.problem);
        TextView kid ,adult, elderly;
        kid=d.findViewById(R.id.kid);
        adult=d.findViewById(R.id.adult);
        elderly=d.findViewById(R.id.elderly);
        kid.setBackgroundResource(R.drawable.text_bg);
        kid.setTextColor(Color.WHITE);
        age_type_S=kid.getText().toString();


        bookd=d.findViewById(R.id.book);
        date=d.findViewById(R.id.date);
        time=d.findViewById(R.id.time);
        kid.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                kid.setBackgroundResource(R.drawable.text_bg);
                kid.setTextColor(Color.WHITE);
                age_type_S=kid.getText().toString();
                adult.setBackgroundResource(R.drawable.age_deselect);
                adult.setTextColor(R.color.teal_700);
                elderly.setBackgroundResource(R.drawable.age_deselect);
                elderly.setTextColor(R.color.teal_700);
            }
        });

        adult.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                adult.setBackgroundResource(R.drawable.text_bg);
                adult.setTextColor(Color.WHITE);
                age_type_S=adult.getText().toString();
                kid.setBackgroundResource(R.drawable.age_deselect);
                kid.setTextColor(R.color.teal_700);
                elderly.setBackgroundResource(R.drawable.age_deselect);
                elderly.setTextColor(R.color.teal_700);
            }
        });
        elderly.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                elderly.setBackgroundResource(R.drawable.text_bg);
                elderly.setTextColor(Color.WHITE);
                age_type_S=elderly.getText().toString();
                kid.setBackgroundResource(R.drawable.age_deselect);
                kid.setTextColor(R.color.teal_700);
                adult.setBackgroundResource(R.drawable.age_deselect);
                adult.setTextColor(R.color.teal_700);
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                open_date_picekr();
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_time_picker();
            }
        });
        bookd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                date_S=date.getText().toString();
                time_s=time.getText().toString();
                problem_S=problem.getText().toString();
                if(TextUtils.isEmpty(date_S) || TextUtils.isEmpty(time_s) || TextUtils.isEmpty(problem_S))
                {
                    Toast.makeText(patient_main.this, "Some fields are empty!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    problem_S.replace(" ","_");
                    new bg("insert").execute();
                    d.dismiss();
                }

                Toast.makeText(patient_main.this, ""+date_S, Toast.LENGTH_SHORT).show();
            }
        });

        d.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    void open_date_picekr()
    {
        DatePickerDialog datePickerDialog;
        datePickerDialog=new DatePickerDialog(this,  this, calendar.get(android.icu.util.Calendar.YEAR), calendar.get(android.icu.util.Calendar.MONTH), calendar.get(android.icu.util.Calendar.DATE));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        month=i1+1;
        day=i2;
        year=i;
        String d=day+"_"+month+"_"+year;
        date.setText(d);
      //  fil(date_tv.getText().toString());
    }
    void open_time_picker()
    {
        final int[] hr = {0};
        final int[] mn = {0};
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int h, int m) {
                hr[0] =h;
                mn[0] =m;
                String timess= h+":"+m;
                time.setText(timess);
                String am_pm = "";
                String format;
//                Calendar datetime = Calendar.getInstance();
//                datetime.set(Calendar.HOUR_OF_DAY, h);
//                datetime.set(Calendar.MINUTE, m);
//
//                if (datetime.get(Calendar.AM_PM) == Calendar.AM)
//                    am_pm = "AM";
//                else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
//                    am_pm = "PM";
//

                calendar = Calendar.getInstance();
                CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                CalendarMinute = calendar.get(Calendar.MINUTE);
                if (h == 0) {

                    h += 12;

                    format = "AM";
                }
                else if (h == 12) {

                    format = "PM";

                }
                else if (h > 12) {

                    h -= 12;

                    format = "PM";

                }
                else {

                    format = "AM";
                }

                time.setText(h+":"+m+":"+format);
                //  a_time_ET.setText(time);
            }
        };
        timePickerDialog =new TimePickerDialog(patient_main.this, AlertDialog.THEME_HOLO_LIGHT,onTimeSetListener, CalendarHour, CalendarMinute,false);
        timePickerDialog.show();
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
            if (action.contains("insert"))
            {   String id=number_s+date_S+time_s;
                common.send_req(ip,"c_qry=insert into appointment(opp_id,phno,age_type,date_,time_,problem) values ('"+opp_id_S+"','"+number_s+"','"+age_type_S+"','"+date_S+"','"+time_s+"','"+problem_S+ "')");

            }
            if(action.contains("fetch"))
            {
                JSONArray jsonArr;
                jsonArr=  common.send_req(ip,"c_qry=SELECT  * FROM appointment");
                id_list.clear();

                for (int i = 0; i < jsonArr.length(); i++)
                {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = jsonArr.getJSONObject(i);
                        Log.d("id", "jarray: : "+jsonObj);

                        //String name_=jsonObj.getString("traffic_controller_name");
                        String id=jsonObj.getString("opp_id");
                        String password_=jsonObj.getString("password");
                        id_list.add(id);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
            if(action.contains("name"))
            {
                JSONArray jsonArr;
                jsonArr=  common.send_req(ip,"c_qry=SELECT  * FROM patient where phno='"+number_s+"'");

                for (int i = 0; i < jsonArr.length(); i++)
                {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = jsonArr.getJSONObject(i);
                        Log.d("id", "jarray: : "+jsonObj);

                        //String name_=jsonObj.getString("traffic_controller_name");
                        String nameS=jsonObj.getString("name");
                        name.setText(nameS);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            common.dism();
            if(!action.contains("fetch"))
            {
                new bg("fetch").execute();
            }
        }
    }
     void genereate_id()
    {/**generate again if id already exists*/


        String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(4);

        for (int i = 0; i < 4; i++)
        {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));

            if(i==3)
            {    Log.d("random", "generated_id=: "+sb.toString());
                String g_id=sb.toString();
                check_id_availiblty(g_id);


            }
        }
    }

     void check_id_availiblty(String id)
    {
            if(id_list.contains(id))
            {
                Log.d("random", "generating new id> ");
                genereate_id();
            }
            else
            {
                /**,....set id**/
                Log.d("random", "not generatinf , but id_seted: "+id);
               // id_ET.setText(id);
                opp_id_S=id;
            }

    }


}