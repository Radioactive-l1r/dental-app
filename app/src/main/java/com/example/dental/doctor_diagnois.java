package com.example.dental;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class doctor_diagnois extends AppCompatActivity {

    String opp_id, name ,status;
    String d_advice , p_problem, sImage;
    TextView name_tv, diagonois_tx, problem_tv, upload;
    EditText advice;
    ImageView pic;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_diagnois);

        common.create_pd(doctor_diagnois.this);

        Bundle extras = getIntent().getExtras();
        opp_id=extras.getString("opp_id");
        name=extras.getString("name");
        status=extras.getString("status");


        name_tv=findViewById(R.id.name);
        name_tv.setText(name);

        pic=findViewById(R.id.pic);

        upload = findViewById(R.id.upload);
        upload.setOnClickListener(view -> {

            pic.setImageBitmap(null);
            Intent intent=new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent,"Select Image"),100);
        });

        advice=findViewById(R.id.advice);
        diagonois_tx=findViewById(R.id.diagnois);
        diagonois_tx.setOnClickListener(view -> {
            /* send to db*/
            d_advice=advice.getText().toString().replace("'", "''").replace("\n","_");
            if(TextUtils.isEmpty(d_advice))
            {
                Toast.makeText(this, "Some fields are empty!", Toast.LENGTH_SHORT).show();
            }else {
                new bg("insert").execute();
            }
        });

        problem_tv=findViewById(R.id.problem);
        new bg("fetch").execute();
        if(status.contains("done"))
        {
            advice.setClickable(false);
            diagonois_tx.setVisibility(View.INVISIBLE);
            upload.setVisibility(View.INVISIBLE);
            new bg("fetch").execute();
        }


    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==100 && resultCode==RESULT_OK && data!=null)
        {
            Uri uri=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,60,stream);
                byte[] bytes=stream.toByteArray();
                sImage= Base64.encodeToString(bytes,Base64.DEFAULT);

                // retrieve
                byte[] bytes2= Base64.decode(sImage,Base64.DEFAULT);
                Bitmap bitmap2= BitmapFactory.decodeByteArray(bytes2,0,bytes2.length);
                pic.setImageBitmap(bitmap2);
                upload.setVisibility(View.INVISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

                    if(action.contains("insert")) {

                        common.send_req("c_qry=UPDATE appointment SET d_advice='" + d_advice + "',status='done' where opp_id='" + opp_id + "'");
                        common.insert_diagnois(sImage,opp_id);
                    }
                    else  if(action.contains("fetch"))
                    {
                        JSONArray jsonArr;
                        jsonArr=  common.send_req("c_qry=SELECT problem,d_advice,img FROM appointment where opp_id='"+opp_id+"'");

                        for (int i = 0; i < jsonArr.length(); i++)
                        {
                            JSONObject jsonObj;
                            try {
                                jsonObj = jsonArr.getJSONObject(i);
                                Log.d("appointments", "jarray: : "+jsonObj);
                                p_problem=jsonObj.getString("problem").replace("_","\n");
                                d_advice=jsonObj.getString("d_advice").replace("_","\n");
                                sImage=jsonObj.getString("img");

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
            advice.setText(d_advice);
            problem_tv.setText(p_problem);
            // retrieve
            if(action.contains("fetch")) {
                byte[] bytes2 = Base64.decode(sImage, Base64.DEFAULT);
                Bitmap bitmap2 = BitmapFactory.decodeByteArray(bytes2, 0, bytes2.length);
                pic.setImageBitmap(bitmap2);
            }
            //on submit go to doctor main
            if(action.contains("insert"))
            {
                toast = Toast.makeText(doctor_diagnois.this, "SUBMITTED!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                startActivity(new Intent(getApplicationContext(), doctor_main.class));
                overridePendingTransition(0,0);
            }
        }
    }
}