package com.example.kankan.timepass;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class Pro extends AppCompatActivity {

    private String thisUserID,name,image,school,college,relation;
    private DatabaseReference myData;
    private StorageReference myStore;
    private FirebaseAuth myAuth;
    private FirebaseUser myUser;
    private ImageView imgProfile,imgOnline;
    private TextView txtProfileName,txtEmail,txtRelation,txtProfileWork,txtProfileSchool,txtProfileCollege;
    private Button btnAdd;
    private Profile profile;
    private List<String> idList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro);

        Bundle bundle=getIntent().getExtras();

        thisUserID=bundle.getString("ID_PRO");

        imgProfile=findViewById(R.id.imgProfilePhotoForProActivity);
        txtProfileName=findViewById(R.id.txtProfileNameForProActivity);
        txtEmail=findViewById(R.id.txtProfileEmailForProActivity);
        txtRelation=findViewById(R.id.txtProfileRelaionshipForProActivity);
        txtProfileWork=findViewById(R.id.txtProfileWorkForProActivity);
        txtProfileSchool=findViewById(R.id.txtProfileSchoolForProActivity);
        txtProfileCollege=findViewById(R.id.txtProfileCollegeForProActivity);
        imgOnline=findViewById(R.id.isOnlineForProActivity);

        myAuth=FirebaseAuth.getInstance();
        myUser=myAuth.getCurrentUser();
        myData= FirebaseDatabase.getInstance().getReference();
        myStore= FirebaseStorage.getInstance().getReference();

        setData();
        getOnline();

    }

    private void getOnline()
    {
        myData.child("Payal").child(thisUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String get=dataSnapshot.child("isOnline").getValue().toString();
                if(get.equals("Yes"))
                {
                    imgOnline.setBackgroundResource(R.drawable.online);
                }
                else {
                    imgOnline.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void setData()
    {
        myData.child("Profile").child(thisUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                profile=dataSnapshot.getValue(Profile.class);
                txtProfileName.setText(profile.getName());
                txtEmail.setText("  "+profile.getEmail());
                txtProfileCollege.setText("  "+profile.getCollege());
                txtProfileSchool.setText("  "+profile.getSchool());
                txtProfileWork.setText("  "+profile.getWork());
                txtRelation.setText("  "+profile.getRelation());

                Glide.with(Pro.this).load(profile.getUrl()).into(imgProfile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                myData.child("Payal").child(myUser.getUid()).child("isOnline").setValue("Yes");
            }
        },2000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        myData.child("Payal").child(myUser.getUid()).child("isOnline").setValue(ServerValue.TIMESTAMP);

    }
}
