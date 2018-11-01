package com.dc.chandra.nitchatzz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText email, password ;

    String EmailHolder, PasswordHolder;
    Button Login;//,ForgotPassword;
    TextView ForgotPassword;
    Boolean EditTextEmptyCheck;
    ProgressDialog progressDialog;


    FirebaseAuth firebaseAuth;

    ///google sigin
                            public static final String TAG = "LoginActivity";
                            public static final int RequestSignInCode = 7;
                            //public GoogleApiClient googleApiClient;
                            //com.google.android.gms.common.SignInButton googlesignInBtn;
                            TextView LoginUserName, LoginUserEmail;



                            @Override
                            protected void onCreate(Bundle savedInstanceState) {
                                super.onCreate(savedInstanceState);
                                setContentView(R.layout.activity_login);

                                email=(EditText)findViewById(R.id.et_Email_login);
                                password=(EditText)findViewById(R.id.et_password);

                                Login=(Button)findViewById(R.id.bt_go);
                                ForgotPassword=(TextView) findViewById(R.id.ForgotPasswordBtn);

                                //googlesignInBtn=(SignInButton) findViewById(R.id.googleSignInBtn);

                                progressDialog=new ProgressDialog(LoginActivity.this);

                                firebaseAuth=FirebaseAuth.getInstance();

                                // Creating and Configuring Google Sign In object.
        /*GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Creating and Configuring Google Api Client.
        googleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
        */

                                if(firebaseAuth.getCurrentUser()!=null){
                                    Loginexist();
                                }


                                Login.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(Validate())
                                        {
                                            LoginFunction();
                                        }
                                        else {
                                            Toast.makeText(LoginActivity.this, "Please Fill All the Fields", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

        /*googlesignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });
        */

                                ForgotPassword.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        EmailHolder = email.getText().toString().trim();
                                        if(TextUtils.isEmpty(EmailHolder)||!android.util.Patterns.EMAIL_ADDRESS.matcher(EmailHolder).matches() ){
                                            Toast.makeText(LoginActivity.this, "Enter a valid Email", Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            //finish();
                                            progressDialog.setMessage("Please Wait");
                                            progressDialog.show();
                                            send_email();
                                            progressDialog.dismiss();
                                            Toast.makeText(LoginActivity.this, "Reset Link sent to your Mail", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });


                            }

    /*@Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }
    */

                            public boolean Validate(){
                                EmailHolder = email.getText().toString().trim();
                                PasswordHolder = password.getText().toString().trim();

                                if(TextUtils.isEmpty(EmailHolder)||!android.util.Patterns.EMAIL_ADDRESS.matcher(EmailHolder).matches() ){
                                    Toast.makeText(LoginActivity.this, "Enter a valid Email", Toast.LENGTH_LONG).show();
                                    return false;
                                }
                                if(TextUtils.isEmpty(PasswordHolder)){
                                    Toast.makeText(LoginActivity.this, "Enter a valid Password", Toast.LENGTH_LONG).show();
                                    return false;
                                }


                                return true ;
                            }

                            public void LoginFunction(){

                                progressDialog.setMessage("Please Wait");
                                progressDialog.show();
                                progressDialog.setCanceledOnTouchOutside(false);

                                Log.d(TAG, "signIn:" + email);

                                // Calling  signInWithEmailAndPassword function with firebase object and passing EmailHolder and PasswordHolder inside it.
                                firebaseAuth.signInWithEmailAndPassword(EmailHolder, PasswordHolder)
                                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {

                                                // If task done Successful.
                                                if(task.isSuccessful()){

                                                    // Closing the current Login Activity.
                                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                                                    //DatabaseReference db = firebase.database.getInstance().getReference();
                                                    final Query FaRef = db.child("fa").orderByChild("email").equalTo(user.getEmail());
                                                    final Query ParRef = db.child("parent").orderByChild("email").equalTo(user.getEmail());


                                                    Log.d(TAG, "signInWithEmail:success");
                                                    final String email = user.getEmail();
                                                    Log.d(TAG, "Email :" + email);
                                                    Log.d(TAG, "faref :" + FaRef);

                                                    ParRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            Log.d(TAG, "chandraEmail :" + email);
                                                            if (dataSnapshot.hasChildren()) {
                                                                Log.d(TAG, "parent exist");
                                                                ParExist();
                                                                progressDialog.dismiss();
                                                            } else {
                                                                Log.d(TAG, "parent does not exist");

                                                                FaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        if (dataSnapshot.hasChildren()) {
                                                                            Log.d(TAG, "fa exist");
                                                                            faExist();
                                                                            progressDialog.dismiss();
                                                                        } else {
                                                                            Log.d(TAG, "Faculty does not exist");
                                                                            firebaseAuth.signOut();
                                                                            Toast.makeText(LoginActivity.this, "No user available with entered details", Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }
                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {
                                                                        Log.d(TAG, "In onCancelled Faculty");
                                                                    }
                                                                });


                                                                //Toast.makeText(LoginActivity.this, "No user available with entered details", Toast.LENGTH_LONG).show();



                                                            }
                                                        }
                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                            Log.d(TAG, "In onCancelled parent");
                                                        }
                                                    });
                                                    //Toast.makeText(LoginActivity.this, "No user available with entered details", Toast.LENGTH_LONG).show();


                                                }
                                                else {
                                                    // Hiding the progress dialog.
                                                    progressDialog.dismiss();
                                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                                    // Showing toast message when email or password not found in Firebase Online database.
                                                    Toast.makeText(LoginActivity.this, "Email or Password Not found, Please Try Again", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                            }


    public void Loginexist(){
        Log.d(TAG, "login exist");
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference FaRef = db.child("fa");
        DatabaseReference ParRef = db.child("parent");
        final String uid = user.getUid();
        ParRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(uid).exists()) {
                    Log.d(TAG, "parent exist");
                    ParExist();
                    return;
                } else {
                    Log.d(TAG, "parent does not exist");
                    //Toast.makeText(LoginActivity.this, "User doesn't exist", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "In onCancelled parent");
            }
        });
        FaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(uid).exists()) {
                    Log.d(TAG, "Faculty exist");
                    faExist();
                    return;
                } else {
                    Log.d(TAG, "Faculty does not exist");
                    //Toast.makeText(LoginActivity.this, "User doesn't exist", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "In onCancelled Faculty");
            }
        });

        //Toast.makeText(LoginActivity.this, "No user available with entered details", Toast.LENGTH_LONG).show();

    }


    public void ParExist(){
        Log.d(TAG, "In parent Exists");
        Log.d(TAG, "Start parent Intent");
        finish();
        Intent intent = new Intent(LoginActivity.this, ParentActivity.class);
        startActivity(intent);

    }

    public void faExist(){
        Log.d(TAG, "Start fa Exists");
        Log.d(TAG, "Start fa Intent");
        finish();
        Intent intent = new Intent(LoginActivity.this, FaActivity.class);
        startActivity(intent);


    }


    void send_email() {
        Log.d(TAG, "In send email");

        firebaseAuth.fetchProvidersForEmail(EmailHolder).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                Log.d(TAG, "");
                if (task.isSuccessful()) {
                    if (task.getResult().getProviders().size() == 1) {
                        Log.d(TAG, "user exist sending mail");
                        firebaseAuth.sendPasswordResetEmail(EmailHolder).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent." + EmailHolder);
                                    Toast.makeText(LoginActivity.this, "Reset Link sent to your Mail", Toast.LENGTH_LONG).show();
                                } else {
                                    Log.d(TAG, "Email sending failed");
                                    Toast.makeText(LoginActivity.this, "Can't send Email check Connectivity", Toast.LENGTH_LONG).show();

                                }
                            }
                        });

                    } else {
                        Log.d(TAG, "user doesn't exist");
                        Toast.makeText(LoginActivity.this, "User doesn't exist", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d(TAG, "user doesn't exist");
                    Toast.makeText(LoginActivity.this, "Email Not Registered", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    /*public void googleSignIn(){
        Intent AuthIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(AuthIntent, RequestSignInCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestSignInCode){
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (googleSignInResult.isSuccess()){
                GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();
                FirebaseUserAuth(googleSignInAccount);
            }

        }
    }

    private void FirebaseUserAuth(GoogleSignInAccount googleSignInAccount) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);

        Toast.makeText(LoginActivity.this,""+ authCredential.getProvider(),Toast.LENGTH_LONG).show();

        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task AuthResultTask) {

                        if (AuthResultTask.isSuccessful()){

                            Intent intent = new Intent(LoginActivity.this, parentActivity.class);
                            startActivity(intent);
                            /*
                            // Getting Current Login user details.
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            // Showing Log out button.
                            SignOutButton.setVisibility(View.VISIBLE);
                            // Hiding Login in button.
                            signInButton.setVisibility(View.GONE);
                            // Showing the TextView.
                            LoginUserEmail.setVisibility(View.VISIBLE);
                            LoginUserName.setVisibility(View.VISIBLE);
                            // Setting up name into TextView.
                            LoginUserName.setText("NAME =  "+ firebaseUser.getDisplayName().toString());
                            // Setting up Email into TextView.
                            LoginUserEmail.setText("Email =  "+ firebaseUser.getEmail().toString());

                        }else {
                            Toast.makeText(LoginActivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }*/

}