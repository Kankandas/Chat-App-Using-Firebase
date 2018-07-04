package com.example.kankan.timepass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView txtGoSignup;
    private EditText edtEmail,edtPassword;
    private Button btnLogin;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference myRef;
    private Profile myProfile;
    private boolean isOnline=false;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail=findViewById(R.id.edtLoginEmail);
        edtPassword=findViewById(R.id.edtPasswordLogin);
        btnLogin=findViewById(R.id.btnLogin);

        dialog=new ProgressDialog(LoginActivity.this);

        myProfile=new Profile();
        isOnline=true;

        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        myRef=FirebaseDatabase.getInstance().getReference();


        if(user!=null)
        {

            //UpdateProfile(true);
            startActivity(new Intent(LoginActivity.this,MainActivity.class));

            finish();

        }

        txtGoSignup=findViewById(R.id.txtGoSignUp);

        txtGoSignup.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void letsLogin()
    {
        String email=edtEmail.getText().toString();
        String pass=edtPassword.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(LoginActivity.this,"Please enter valid Email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass))
        {
            Toast.makeText(LoginActivity.this,"Please enter valid Password",Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.setMessage("Logging in...");
        dialog.show();
        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                dialog.cancel();
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.txtGoSignUp:
                startActivity(new Intent(LoginActivity.this,SignupActivity.class));
                finish();
                break;
            case R.id.btnLogin:
                letsLogin();
                break;
        }
    }

}
