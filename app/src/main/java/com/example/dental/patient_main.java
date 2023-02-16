package com.example.dental;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class patient_main extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener {

    String number_s, names_S;
    String age_type_S,date_S,time_s,problem_S,opp_id_S;
    TextView book,history,bookd,date,time;
    Calendar calendar;
    int year,month,day;
    int CalendarHour, CalendarMinute;
    TimePickerDialog timePickerDialog;
    ImageView logOut;
    TextView orthodontics, pub_health_dent, oral_med_rad, pedodontics, oral_max_sur;
    TextView oral_path, prosthodontics, peridontics, cons_endo;
    Toast toast;

    ArrayList<String> id_list=new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);

        common.create_pd(patient_main.this);

        SharedPreferences sharedPreferences = this.getSharedPreferences("dental", MODE_PRIVATE);
        number_s= sharedPreferences.getString("phno", "");

        calendar= Calendar.getInstance();
        book=findViewById(R.id.book);
        history=findViewById(R.id.history);

        book.setOnClickListener(view -> booK_dialog());

        history.setOnClickListener(view -> {

            Intent i = new Intent(patient_main.this, patient_history.class);
            i.putExtra("phno", number_s);
            i.putExtra("name", names_S);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        });

        logOut = findViewById(R.id.log_out);
        logOut.setOnClickListener(view -> onBackPressed());

        orthodontics = findViewById(R.id.orthodontics);
        oral_med_rad = findViewById(R.id.oral_med_rad);
        pub_health_dent = findViewById(R.id.pub_health_dent);
        prosthodontics = findViewById(R.id.prosthodontics);
        oral_path = findViewById(R.id.oral_path);
        oral_max_sur = findViewById(R.id.oral_max_sur);
        pedodontics = findViewById(R.id.pedodontics);
        cons_endo = findViewById(R.id.cons_endo);
        peridontics = findViewById(R.id.peridontics);

        orthodontics.setOnClickListener(this::sendInfo);
        oral_med_rad.setOnClickListener(this::sendInfo);
        pub_health_dent.setOnClickListener(this::sendInfo);
        prosthodontics.setOnClickListener(this::sendInfo);
        oral_path.setOnClickListener(this::sendInfo);
        oral_max_sur.setOnClickListener(this::sendInfo);
        pedodontics.setOnClickListener(this::sendInfo);
        peridontics.setOnClickListener(this::sendInfo);
        cons_endo.setOnClickListener(this::sendInfo);

        new bg("name").execute();
        new bg("fetch").execute();
    }

    void sendInfo(View view){
        Intent intent = new Intent(patient_main.this, DepartmentInfo.class);
        TextView dept = (TextView) view;
        intent.putExtra("dept", dept.getText().toString());
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void booK_dialog()
    {
        //generate id of appointment
        genereate_id();

        Dialog d=new Dialog(this);
        d.setContentView(R.layout.book_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText problem;
        TextView kid ,adult, elderly;

        problem = d.findViewById(R.id.problem);
        kid = d.findViewById(R.id.kid);
        adult = d.findViewById(R.id.adult);
        elderly = d.findViewById(R.id.elderly);
        bookd=d.findViewById(R.id.book);
        date=d.findViewById(R.id.date);
        time=d.findViewById(R.id.time);

        kid.setBackgroundResource(R.drawable.text_bg);
        kid.setTextColor(Color.WHITE);
        age_type_S=kid.getText().toString();

        kid.setOnClickListener(view -> {

            kid.setBackgroundResource(R.drawable.text_bg);
            kid.setTextColor(Color.WHITE);
            age_type_S=kid.getText().toString();
            adult.setBackgroundResource(R.drawable.age_deselect);
            adult.setTextColor(getResources().getColor(R.color.teal_700));
            elderly.setBackgroundResource(R.drawable.age_deselect);
            elderly.setTextColor(getResources().getColor(R.color.teal_700));
        });

        adult.setOnClickListener(view -> {

            adult.setBackgroundResource(R.drawable.text_bg);
            adult.setTextColor(Color.WHITE);
            age_type_S=adult.getText().toString();
            kid.setBackgroundResource(R.drawable.age_deselect);
            kid.setTextColor(getResources().getColor(R.color.teal_700));
            elderly.setBackgroundResource(R.drawable.age_deselect);
            elderly.setTextColor(getResources().getColor(R.color.teal_700));
        });

        elderly.setOnClickListener(view -> {

            elderly.setBackgroundResource(R.drawable.text_bg);
            elderly.setTextColor(Color.WHITE);
            age_type_S=elderly.getText().toString();
            kid.setBackgroundResource(R.drawable.age_deselect);
            kid.setTextColor(getResources().getColor(R.color.teal_700));
            adult.setBackgroundResource(R.drawable.age_deselect);
            adult.setTextColor(getResources().getColor(R.color.teal_700));
        });

        date.setOnClickListener(view -> open_date_picekr());
        time.setOnClickListener(view -> open_time_picker());

        bookd.setOnClickListener(view -> {

            date_S = date.getText().toString();
            time_s  = time.getText().toString();
            problem_S = problem.getText().toString();
            problem_S = problem_S.replace("'", "''").replace("\n","_");
//            Toast.makeText(this, ""+problem_S, Toast.LENGTH_SHORT).show();

            if(TextUtils.isEmpty(date_S) || TextUtils.isEmpty(time_s) || TextUtils.isEmpty(problem_S))
            {
                toast = Toast.makeText(patient_main.this, "Some fields are empty!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            else
            {
                new bg("insert").execute();
                d.dismiss();
                toast = Toast.makeText(patient_main.this, "Appointment Booked", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
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
    }
    void open_time_picker()
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, h, m) -> {

            String timess= h+":"+m;
            time.setText(timess);
            String format;

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
            {
                common.send_req("c_qry=insert into appointment(opp_id,name,phno,age_type,date_,time_,problem) values ('"+opp_id_S+"','"+ names_S+"','"+number_s+"','"+age_type_S+"','"+date_S+"','"+time_s+"','"+problem_S+ "')");
            }
            if(action.contains("fetch"))
            {
                JSONArray jsonArr;
                jsonArr=  common.send_req("c_qry=SELECT opp_id FROM appointment");
                id_list.clear();

                for (int i = 0; i < jsonArr.length(); i++)
                {
                    JSONObject jsonObj;
                    try {
                        jsonObj = jsonArr.getJSONObject(i);

                        String id = jsonObj.getString("opp_id");
                        id_list.add(id);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
            if(action.contains("name"))
            {
                JSONArray jsonArr;
                jsonArr=  common.send_req("c_qry=SELECT name FROM patient where phno='"+number_s+"'");

                for (int i = 0; i < jsonArr.length(); i++)
                {
                    JSONObject jsonObj;
                    try {
                        jsonObj = jsonArr.getJSONObject(i);
                        names_S = jsonObj.getString("name");




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
            //save the name in sharedprefernces
            if(action.contains("name"))
            {
                SharedPreferences sharedPreferences = patient_main.this.getSharedPreferences("dental", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", names_S);
                editor.apply();
            }
        }
    }
     void genereate_id()
    {
        /* generate again if id already exists*/

        String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(4);

        for (int i = 0; i < 4; i++)
        {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
            if(i==3)
            {
                String g_id=sb.toString();
                check_id_availiblty(g_id);
            }
        }
    }

     void check_id_availiblty(String id)
    {
            if(id_list.contains(id))
            {
                genereate_id();
            }
            else
            {
                opp_id_S=id;
                id_list.add(id);
            }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), login_signup.class));
        overridePendingTransition(0,0);
    }

}