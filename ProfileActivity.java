
package com.example.kankan.timepass;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

public class ProfileActivity extends AppCompatActivity {
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
    private boolean present=false;
    private boolean btn=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Bundle bundle=getIntent().getExtras();

        thisUserID=bundle.getString("ID");
        name=bundle.getString("NAME");
        school=bundle.getString("SCHOOL");
        college=bundle.getString("COLLEGE");
        relation=bundle.getString("RELATION");


        idList=new ArrayList<>();


        imgProfile=findViewById(R.id.imgProfilePhotoForPofileActivity);
        txtProfileName=findViewById(R.id.txtProfileNameForProfileActivity);
        txtEmail=findViewById(R.id.txtProfileEmailForProfileActivity);
        txtRelation=findViewById(R.id.txtProfileRelaionshipForProfileActivity);
        txtProfileWork=findViewById(R.id.txtProfileWorkForProfileActivity);
        txtProfileSchool=findViewById(R.id.txtProfileSchoolForProfileActivity);
        txtProfileCollege=findViewById(R.id.txtProfileCollegeForProfileActivity);
        btnAdd=findViewById(R.id.btnAddToChatList);
        imgOnline=findViewById(R.id.isOnlineForProfileActivity);

        myAuth=FirebaseAuth.getInstance();
        myUser=myAuth.getCurrentUser();
        myData= FirebaseDatabase.getInstance().getReference();
        myStore= FirebaseStorage.getInstance().getReference();

        setData();

        checkTheList();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myData.child("Chat List").child(myUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot snapshot:dataSnapshot.getChildren())
                        {
                            Profile profile=snapshot.getValue(Profile.class);
                            if(profile.getUserID().toString().equals(thisUserID))
                            {
                                btn=true;
                            }
                        }
                        if(!btn)
                        {
                            myData.child("Chat List").child(myUser.getUid()).child(thisUserID).setValue(profile);

                            Toast.makeText(ProfileActivity.this,"Successfully  Added",Toast.LENGTH_SHORT).show();
                        }
                        else {

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });


    }
    private void checkTheList()
    {
        myData.child("Chat List").child(myUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Profile profile=snapshot.getValue(Profile.class);
                    if(profile.getUserID().toString().equals(thisUserID))
                    {
                        present=true;
                    }
                }
                if(!present)
                {
                    btnAdd.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(ProfileActivity.this,"Already Added",Toast.LENGTH_SHORT).show();
                    btnAdd.setVisibility(View.INVISIBLE);

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
                String isOnlie=profile.getIsOnline();
                txtProfileName.setText(profile.getName());
                txtEmail.setText("  "+profile.getEmail());
                txtProfileCollege.setText("  "+profile.getCollege());
                txtProfileSchool.setText("  "+profile.getSchool());
                txtProfileWork.setText("  "+profile.getWork());
                txtRelation.setText("  "+profile.getRelation());

                Glide.with(ProfileActivity.this).load(profile.getUrl()).into(imgProfile);

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
                Toast.makeText(ProfileActivity.this,"onStart ChatActivity",Toast.LENGTH_SHORT).show();
            }
        },2000);
    }

    @Override
    protected void onStop() {

        super.onStop();

        myData.child("Payal").child(myUser.getUid()).child("isOnline").setValue(ServerValue.TIMESTAMP);
        Toast.makeText(ProfileActivity.this,"onStop ChatActivity",Toast.LENGTH_SHORT).show();
    }
}
