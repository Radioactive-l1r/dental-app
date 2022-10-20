package com.example.dental;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DepartmentInfo extends AppCompatActivity {

    TextView deptInfo, headerInfo;
    private AdapterViewFlipper adapterViewFlipper;
    int[] images;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_info);

        sp = getSharedPreferences("deptInfos", MODE_PRIVATE);
        fillDeptInfo();
        String info = getIntent().getStringExtra("dept");

        headerInfo = findViewById(R.id.headerInfo);
        headerInfo.setText(""+info);

        String infoValue;
        if(info.equals("Orthodontics")){
            images = new int[]{R.drawable.ortho_1, R.drawable.ortho_2, R.drawable.ortho_3, R.drawable.ortho_4, R.drawable.ortho_5};
        }else if(info.equals("Oral Medicine and Radiology")){
            images = new int[]{R.drawable.oralrad_1, R.drawable.oralrad_2, R.drawable.oralrad_3, R.drawable.oralrad_4};
        }else if(info.equals("Public Health Dentistry")){
            images = new int[]{R.drawable.phd_1, R.drawable.phd_2};
        }else if(info.equals("Prosthodontics")){
            images = new int[]{R.drawable.prostho_1, R.drawable.prostho_2, R.drawable.prostho_3, R.drawable.prostho_4, R.drawable.prostho_5};
        }else if(info.equals("Oral Pathology")){
            images = new int[]{R.drawable.oralpath_1, R.drawable.oralpath_2};
        }else if(info.equals("Oral Maxillofacial Surgery")){
            images = new int[]{R.drawable.oralsurj_1, R.drawable.oralsurj_2, R.drawable.oralsurj_3, R.drawable.oralsurj4};
        }else if(info.equals("Pedodontics")){
            images = new int[]{R.drawable.pedo_1, R.drawable.pedo_2, R.drawable.pedo_3, R.drawable.pedo_4, R.drawable.pedo_5};
        }else if(info.equals("Conservative and Endodontics")){
            images = new int[]{R.drawable.endo_1, R.drawable.endo_2, R.drawable.endo_3, R.drawable.endo_4};
        }else {
            images = new int[]{R.drawable.perio_1, R.drawable.perio_2, R.drawable.perio_3};
        }

        String deptInfos = sp.getString(info, "");
        deptInfo = findViewById(R.id.dept_info);
        deptInfo.setText(""+deptInfos);

        adapterViewFlipper = findViewById(R.id.AdapterViewFlipper);
        CustomAdapter adapter = new CustomAdapter(this, images);
        adapterViewFlipper.setAdapter(adapter);
        adapterViewFlipper.setFlipInterval(3000);
        adapterViewFlipper.setAutoStart(true);
    }

    void fillDeptInfo(){
        sp = getSharedPreferences("deptInfos", MODE_PRIVATE);

        sp.edit().putString("Orthodontics", "Orthodontics is a dentistry specialty that addresses the diagnosis, prevention, management, and correction of mal-positioned teeth and jaws, and misaligned bite patterns. It may also address the modification of facial growth, known as dentofacial orthopedics.").commit();
        sp.edit().putString("Oral Medicine and Radiology", "Oral Medicine is that branch of dentistry which deals with the study of oral and maxillofacial diseases, oral manifestation of systemic diseases, oral diseases causing systemic diseases, their diagnosis by modern scientific method and their medical management.").commit();
        sp.edit().putString("Public Health Dentistry", "Dental Public Health (DPH) is a para-clinical specialty of dentistry that deals with the prevention of oral disease and promotion of oral health. Dental public health is involved in the assessment of key dental health needs and coming up with effective solutions to improve the dental health of populations rather than individuals.").commit();
        sp.edit().putString("Prosthodontics", "Prosthodontics is one of the branches of dentistry that deals with the replacement of missing teeth and the associated soft and hard tissues by prostheses (crowns, bridges, dentures) which may be fixed or removable, or may be supported and retained by implants.").commit();
        sp.edit().putString("Oral Pathology", "Oral pathology is a specialized field of pathology dealing with the diseases of oral cavity and its associated structures (i.e., teeth, tongue; bony tissue; joint; lymph nodes; nerves; structures in neck and blood vessels of the ear, nose and throat ). The qualified doctors of this domain are called oral and maxillofacial pathologists.").commit();
        sp.edit().putString("Oral Maxillofacial Surgery", "Oral and maxillofacial surgery is a surgical specialty focusing on reconstructive surgery of the face, facial trauma surgery, the oral cavity, head and neck, mouth, and jaws, as well as facial cosmetic surgery/facial plastic surgery including cleft lip and cleft palate surgery.").commit();
        sp.edit().putString("Pedodontics", "pedodontics, also spelled paedodontics, dental specialty that deals with the care of children’s teeth. The pedodontist is extensively concerned with prevention, which includes instruction in proper diet, use of fluoride, and practice of oral hygiene.").commit();
        sp.edit().putString("Conservative and Endodontics", "Conservative Dentistry and Endodontics is the branch of dentistry which deals with the treatment of caries, malformed, discolored, unesthetic, or fractured teeth and Endodontics deals with treatment of pulpal and periapical diseases and dental pain management.").commit();
        sp.edit().putString("Periodontics", "Periodontics is the branch of dentistry that focuses on the health of your gums and jawbone — the tissues that support your teeth. A gum specialist is called a periodontist. After going to a four-year dental school, they receive three additional years of focused training in periodontics").commit();
    }

    class CustomAdapter extends BaseAdapter{

        Context context;
        int[] images;
        LayoutInflater inflater;

        public CustomAdapter(Context context, int[] images){
            this.context = context;
            this.images = images;
            inflater = (LayoutInflater.from(context));
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            view = inflater.inflate(R.layout.flipper_items, null);
            ImageView img = view.findViewById(R.id.infoImg);
            img.setImageResource(images[position]);
            return view;
        }
    }
}

