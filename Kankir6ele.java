package com.example.kankan.timepass;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class Kankir6ele extends AppCompatActivity implements View.OnClickListener {
    private Button btnKanki;
    private EditText edtclg,edtSchool,edtName,edtWork;
    private Spinner spinner;
    private ImageView imgKanki;
    private DatabaseReference myDatabase;
    private StorageReference myStore;
    private FirebaseAuth myAuth;
    private FirebaseUser myUser;
    private Uri uri;
    private List<String > list;
    private static final int GALARY_KANKAN=111;
    private String relation="";
    private ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kankir6ele);

        list=new ArrayList<>();

        dialog=new ProgressDialog(Kankir6ele.this);

        btnKanki=findViewById(R.id.btnKankiUpload);
        edtclg=findViewById(R.id.edtKankiCollege);
        edtName=findViewById(R.id.edtKankiName);
        edtSchool=findViewById(R.id.edtKankiSchool);
        edtWork=findViewById(R.id.edtKankiWork);

        spinner=findViewById(R.id.spinKanki);

        imgKanki=findViewById(R.id.imgKanki);

        myDatabase=FirebaseDatabase.getInstance().getReference();
        myStore=FirebaseStorage.getInstance().getReference();
        myAuth=FirebaseAuth.getInstance();
        myUser=myAuth.getCurrentUser();

        list.add("Relationship");
        list.add("Single");
        list.add("In a relation");
        list.add("Marriage");
        list.add("Divorce");


        ArrayAdapter<String> adapter=new ArrayAdapter<String >(this,android.R.layout.simple_dropdown_item_1line,list);
        spinner.setAdapter(adapter);

        btnKanki.setOnClickListener(this);
        imgKanki.setOnClickListener(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                relation=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALARY_KANKAN && resultCode==RESULT_OK)
        {
            uri=data.getData();
            try {
                if (uri != null) {
                    Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    RoundedBitmapDrawable drawable= RoundedBitmapDrawableFactory.create(getResources(),bm);
                    drawable.setCircular(true);
                    imgKanki.setImageDrawable(drawable);

                }
            }
            catch (Exception e)
            {

            }
        }
    }

    private void letsUploadData()
    {
        final String name=edtName.getText().toString();
        final String work=edtWork.getText().toString();
        final String school=edtSchool.getText().toString();
        final String college=edtclg.getText().toString();
        final String email=myUser.getEmail();
        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(Kankir6ele.this,"Enter valid name",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(work))
        {
            Toast.makeText(Kankir6ele.this,"Enter valid work place ",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(school))
        {
            Toast.makeText(Kankir6ele.this,"Enter valid School",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(relation)|| relation.equals("Relationship"))
        {
            Toast.makeText(Kankir6ele.this,"Enter valid Relation status",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(college))
        {
            Toast.makeText(Kankir6ele.this,"Enter valid college",Toast.LENGTH_SHORT).show();
            return;
        }
        try {

            if (uri == null) {
                Toast.makeText(Kankir6ele.this, "Please Choose an Image", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        catch (Exception e)
        {
            Toast.makeText(Kankir6ele.this, "Please Choose an Image", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.setMessage("Uploading profile please wait...");
        dialog.show();
        StorageReference ref=myStore.child("Profile Photo"+System.currentTimeMillis());
        ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String url=taskSnapshot.getDownloadUrl().toString();
                String DeviceToken= FirebaseInstanceId.getInstance().getToken();
                Profile profile=new Profile(name,relation,work,school,college,email,url,"Yes",myUser.getUid(),DeviceToken);

                //OnLine onLine=new OnLine("Yes",myUser.getUid());
                //myDatabase.child("Sachin").child(myUser.getUid()).setValue(onLine);

                myDatabase.child("Profile").child(myUser.getUid()).setValue(profile);
                dialog.dismiss();
                startActivity(new Intent(Kankir6ele.this,MainActivity.class));
                finish();

            }
        });

    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imgKanki:
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALARY_KANKAN);
                break;
            case R.id.btnKankiUpload:
                letsUploadData();
                break;
        }
    }
}
