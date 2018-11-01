package com.dc.chandra.nitchatzz;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dc.chandra.nitchatzz.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText uname,uemail,upass,uphnno;
    private Spinner sigintype;

    private Button registerbt;
    private TextView alreadysignup;

    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUIviews();

        firebaseAuth= FirebaseAuth.getInstance();

        registerbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    //// enter the login details to databse;
                    String user_email=uemail.getText().toString().trim();
                    String user_pass=upass.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email,user_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){

                                Toast.makeText(MainActivity.this, "Successfully Registered, Upload complete!", Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(MainActivity.this,LoginActivity.class));
                            }else{

                                Toast.makeText(MainActivity.this, "Registration unsuccesful!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

        alreadysignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });


    }

    private void setupUIviews(){
        uname=(EditText) findViewById(R.id.et_username);
        uemail=(EditText) findViewById(R.id.et_email);
        upass=(EditText) findViewById(R.id.et_password);
        uphnno=(EditText) findViewById(R.id.et_phoneno);

        sigintype=(Spinner) findViewById(R.id.spinner_signup);
        registerbt=(Button)findViewById(R.id.bt_go);
        alreadysignup=(TextView)findViewById(R.id.tv_alreadysignup);

        return;
    }
    private boolean validate(){
        boolean result=false;
        String user_email = uemail.getText().toString().trim();
        String user_password = upass.getText().toString().trim();
        String user_name=uname.getText().toString().trim();


        if(user_email.isEmpty() || user_name.isEmpty() || user_password.isEmpty() ){
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        }else{
            result=true;
        }

        return result;
    }


}
