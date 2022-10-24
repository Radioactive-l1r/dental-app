package com.example.dental;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class patient_history extends AppCompatActivity
{
  TextView name;
  String ip,number_s;
  String opp_id_s, feed_back_s;
  adapter Adapeter;
  ArrayList<model> modelArrayList;
  RecyclerView rv;
  Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_history);

        name =findViewById(R.id.name);

        SharedPreferences sharedPreferences= getSharedPreferences("myPref", MODE_PRIVATE);
        ip=sharedPreferences.getString("ip", "");
        common.create_pd(patient_history.this);

        Bundle extras = getIntent().getExtras();
        number_s= sharedPreferences.getString("phno", "");
//        Toast.makeText(this, ""+number_s, Toast.LENGTH_SHORT).show();
        new bg("name").execute();

        rv=findViewById(R.id.rv);
        modelArrayList=new ArrayList<>();
        Adapeter=new adapter(modelArrayList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(layoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(Adapeter);
    }

    class bg extends AsyncTask<Object,Void,Void>
    {
        private  String action;

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
          else   if(action.contains("fetch"))
            {
                JSONArray jsonArr;
                jsonArr=  common.send_req(ip,"c_qry=SELECT  * FROM appointment where phno='"+number_s+"'");
                modelArrayList.clear();
                for (int i = 0; i < jsonArr.length(); i++)
                {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = jsonArr.getJSONObject(i);
                        Log.d("appointments", "jarray: : "+jsonObj);

                        //String name_=jsonObj.getString("traffic_controller_name");
                        String id=jsonObj.getString("opp_id");
                        String date_=jsonObj.getString("date_").replace("_","/");
                        String time_=jsonObj.getString("time_");
                        String problem_=jsonObj.getString("problem");
                        String d_advice_= jsonObj.getString("d_advice");
                        String status=jsonObj.getString("status");
                        String feed_back=jsonObj.getString("feedback");
                        modelArrayList.add(new model(id,date_,time_,problem_,d_advice_,feed_back ,status));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
          else   if(action.contains("delete"))
            {//DELETE FROM `appointment` WHERE opp_id='qwwq';
                common.send_req(ip,"c_qry=DELETE FROM appointment where opp_id='"+opp_id_s+"'");
            }
          else if(action.contains("feedback")){
              common.send_req(ip, "c_qry=UPDATE appointment SET feedback='"+feed_back_s+"' where opp_id='"+opp_id_s+"'");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            common.dism();
            Adapeter.notifyDataSetChanged();
            if(!action.contains("fetch"))
            {
                new bg("fetch").execute();
            }
        }
    }

    class model
    {
        private  String id,date,time,problem,d_advice,feedback_value ,status;

        model(String id, String date, String time, String problem, String d_advice, String feedback_value, String status) {
            this.id = id;
            this.date = date;
            this.time = time;
            this.problem = problem;
            this.d_advice = d_advice;
            this.feedback_value = feedback_value;

            this.status = status;
        }

        public String getId() {
            return id;
        }

        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }

        public String getProblem() {
            return problem;
        }

        public String getD_advice() {
            return d_advice;
        }

        public String getFeedback_value() {
            return feedback_value;
        }

        public String getStatus() {
            return status;
        }
    }

    class adapter extends RecyclerView.Adapter<adapter.MyViewHolder>
    {
       private ArrayList<model> modelArrayList;

        adapter(ArrayList<model> modelArrayList) {
            this.modelArrayList = modelArrayList;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder
        {
            TextView date_time,problem;
            ImageView delete;
            TextView feedback, moreInfo;
            public MyViewHolder(@NonNull View itemView)
            {
                super(itemView);
                date_time=itemView.findViewById(R.id.time_date);
                problem=itemView.findViewById(R.id.problem);
                delete=itemView.findViewById(R.id.delete);
                feedback=itemView.findViewById(R.id.review);
                moreInfo=itemView.findViewById(R.id.moreInfo);
            }
        }
        @NonNull
        @Override
        public adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemview= LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_history_item,parent,false);
            return new MyViewHolder(itemview);
        }

        @Override
        public void onBindViewHolder(@NonNull adapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

            String da=modelArrayList.get(position).getDate()+" - "+modelArrayList.get(position).getTime();
            holder.date_time.setText(da);
            holder.problem.setText(modelArrayList.get(position).getProblem());
            String few= holder.problem.getText().toString();
            String feed_back_value = modelArrayList.get(position).getFeedback_value();
            if(!feed_back_value.isEmpty()){
                holder.feedback.setEnabled(false);
                holder.feedback.setText("");
                holder.feedback.setBackgroundResource(R.drawable.checkbox_on_background);
            }
            holder.delete.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     opp_id_s=modelArrayList.get(position).getId().toString();
                     Toast.makeText(patient_history.this, ""+opp_id_s, Toast.LENGTH_SHORT).show();
                     new bg("delete").execute();
                 }
            });

            holder.feedback.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     opp_id_s=modelArrayList.get(position).getId().toString();
                     feedback_dialog(opp_id_s);
                 }
            });

           ;
            holder.moreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String status=modelArrayList.get(position).getStatus().toString();
                            if(status.contains("done"))
                            {
                                Intent i = new Intent(patient_history.this, doctor_diagnois.class);
//                            String strName = null;

                                i.putExtra("name",name.getText());
                                i.putExtra("opp_id", modelArrayList.get(position).getId().toString());
                                i.putExtra("status",modelArrayList.get(position).getStatus().toString());
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(i);
                            }else {
                                Intent intent = new Intent(getApplicationContext(), medical_history.class);
                                startActivity(intent);
                            }

                }
            });

        }

        @Override
        public int getItemCount() {
            return modelArrayList.size();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), patient_main.class));
        overridePendingTransition(0,0);
    }

    void feedback_dialog(String opp_id) {

        Dialog d = new Dialog(this);
        d.setContentView(R.layout.feedback_dialog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText feed;
        TextView feedSubmit;
        feed = d.findViewById(R.id.feedback);
        feedSubmit = d.findViewById(R.id.feedSubmit);

        feedSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feed_back_s = feed.getText().toString();
                feed_back_s = feed_back_s.replace("'", "''");
                if (TextUtils.isEmpty(feed_back_s)) {
                    toast = Toast.makeText(patient_history.this, "feedback is empty!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                } else {
                    opp_id_s = opp_id;
                    new bg("feedback").execute();
                    toast = Toast.makeText(patient_history.this, "feedback Sent!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                    d.dismiss();
                }
            }
        });

        d.show();
    }
}
