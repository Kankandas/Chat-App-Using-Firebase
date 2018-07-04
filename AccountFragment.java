package com.example.kankan.timepass;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private DatabaseReference myData;
    private StorageReference myStore;
    private FirebaseAuth myAuth;
    private FirebaseUser myUser;
    private ImageView imgProfile;
    private TextView txtProfileName,txtEmail,txtRelation,txtProfileWork,txtProfileSchool,txtProfileCollege;


    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_account, container, false);

        imgProfile=v.findViewById(R.id.imgProfilePhoto);
        txtProfileName=v.findViewById(R.id.txtProfileName);
        txtEmail=v.findViewById(R.id.txtProfileEmail);
        txtRelation=v.findViewById(R.id.txtProfileRelaionship);
        txtProfileWork=v.findViewById(R.id.txtProfileWork);
        txtProfileSchool=v.findViewById(R.id.txtProfileSchool);
        txtProfileCollege=v.findViewById(R.id.txtProfileCollege);





        myAuth=FirebaseAuth.getInstance();
        myUser=myAuth.getCurrentUser();
        myData= FirebaseDatabase.getInstance().getReference();
        myStore= FirebaseStorage.getInstance().getReference();





        myData.child("Profile").child(myUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Profile profile=dataSnapshot.getValue(Profile.class);


                txtProfileName.setText(profile.getName());
                txtEmail.setText("  "+myUser.getEmail());
                txtProfileCollege.setText("  "+profile.getCollege());
                txtProfileSchool.setText("  "+profile.getSchool());
                txtProfileWork.setText("  "+profile.getWork());
                txtRelation.setText("  "+profile.getRelation());

                Glide.with(getActivity()).load(profile.getUrl()).into(imgProfile);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        return v;
    }

}
