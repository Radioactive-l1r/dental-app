package com.example.dental;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class doctor_main extends AppCompatActivity
{
    String name_s,date_s,time,feedbacK_s ,Status_s;

    ArrayList<model> modelArrayList;
    RecyclerView rv;
    AdaPTER adaPTER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);

        common.create_pd(doctor_main.this);

        modelArrayList=new ArrayList<>();
        adaPTER=new AdaPTER(modelArrayList);
        rv=findViewById(R.id.rv);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(layoutManager);

        rv.setAdapter(adaPTER);

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
            jsonArr=  common.send_req("c_qry=SELECT opp_id,name,date_,time_,feedback,status FROM appointment");
            modelArrayList.clear();
            for (int i = 0; i < jsonArr.length(); i++)
            {
                JSONObject jsonObj;
                try {
                    jsonObj = jsonArr.getJSONObject(i);

                    String id=jsonObj.getString("opp_id");
                    name_s=jsonObj.getString("name");
                    date_s=jsonObj.getString("date_");
                    time=jsonObj.getString("time_");
                    feedbacK_s=jsonObj.getString("feedback").replace("_","\n");
                    Status_s=jsonObj.getString("status");

                    modelArrayList.add(new model(id,date_s,time,name_s,feedbacK_s, Status_s));

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
            adaPTER.notifyDataSetChanged();
        }
    }

    void open_feedbac(String feedbaa)
    {
        Dialog d=new Dialog(this);
        d.setContentView(R.layout.doc_feed_back_dilaog);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView feed;
        feed=d.findViewById(R.id.feedback);

        if(!TextUtils.isEmpty(feedbaa))
        {
            feed.setText(feedbaa);
        }
        d.show();
    }

    class model {
        private String id,date,time,name,feedback ,status;

        model(String id, String date, String time, String name, String feedback, String status) {
            this.id = id;
            this.date = date;
            this.time = time;
            this.name = name;
            this.feedback = feedback;
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

        public String getName() {
            return name;
        }

        public String getFeedback() {
            return feedback;
        }

        public String getStatus() {
            return status;
        }
    }

    class AdaPTER extends RecyclerView.Adapter<AdaPTER.MyViewHoder>
    {
        private ArrayList<model> modelArrayList;

        AdaPTER(ArrayList<model> modelArrayList) {
            this.modelArrayList = modelArrayList;
        }

        public class MyViewHoder extends RecyclerView.ViewHolder {
            TextView tim_date,proble,feedback,more;
            public MyViewHoder(@NonNull View itemView) {

                super(itemView);
                tim_date=itemView.findViewById(R.id.time_date);
                proble=itemView.findViewById(R.id.problem);
                feedback=itemView.findViewById(R.id.review);
                more=itemView.findViewById(R.id.moreInfo);

            }
        }
        @NonNull
        @Override
        public AdaPTER.MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemview= LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_main_rv_item,parent,false);
            return new AdaPTER.MyViewHoder(itemview);
        }

        @Override
        public void onBindViewHolder(@NonNull AdaPTER.MyViewHoder holder, int position) {

            String da=modelArrayList.get(position).getDate()+" - "+modelArrayList.get(position).getTime();
            holder.tim_date.setText(da);
            holder.proble.setText(modelArrayList.get(position).getName());

            /*open diagnose page*/
            holder.more.setOnClickListener(view -> {

                Intent i = new Intent(doctor_main.this, doctor_diagnois.class);
                i.putExtra("who","doctor");
                i.putExtra("name", modelArrayList.get(position).getName());
                i.putExtra("opp_id", modelArrayList.get(position).getId());
                i.putExtra("status",modelArrayList.get(position).getStatus());
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
            });
            holder.feedback.setOnClickListener(view -> {
                //open feedback dialog
                String fb=modelArrayList.get(position).getFeedback();
                open_feedbac(fb);

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
        startActivity(new Intent(getApplicationContext(), login_signup.class));
        overridePendingTransition(0,0);
    }
}