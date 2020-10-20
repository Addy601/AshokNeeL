package com.addy.ashok_neel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private SignInButton signInButton;
    public FirebaseAuth fAuth;
    private GoogleSignInClient googleSignInClient;
    private GoogleApiClient  googleApiClient;
    private static final int SIGN_IN=1;
    private EditText Uemail;
    private EditText UPassword;
    private Button Login;
    private TextView tvregister;
    private TextView tv3;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");

   /* FirebaseAuth.AuthStateListener mAuthListener;*/

    /*public void Validate(String NameUser,String PasswordUser){
        if((NameUser.equals("ADDY")) && (PasswordUser.equals("18"))){
            Intent intent=new Intent(MainActivity.this,SecondActivity.class);
            startActivity(intent);
        }
        }*/
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = fAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        } else {
           Toast.makeText(this,"Please Login Again!",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        FirebaseMessaging.getInstance().subscribeToTopic("NEWS")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successful";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }

                    }
                });
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("MyNotifications","MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

        // Build a GoogleSignInClient with the options specified by gso.

        if (mGoogleSignInClient == null){
            Intent intent=new Intent(MainActivity.this,SecondActivity.class);
            startActivity(intent); }
        signInButton=findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SignInIntent= mGoogleSignInClient.getSignInIntent();
                startActivityForResult(SignInIntent,SIGN_IN);
              /*  Intent intent=Auth.GOOGLE_SIGN_IN_API.getSignInIntent(googleApiClient);
                startActivityForResult(intent,SIGN_IN);*/

            }
        });
        fAuth=FirebaseAuth.getInstance();
        tv3=findViewById(R.id.textView3);
        Uemail=findViewById(R.id.etmail);
        UPassword=findViewById(R.id.etPassword );
        Login=(Button)findViewById(R.id.btn_login);
        tvregister=(TextView)findViewById(R.id.tvRegister);
        tvregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Validate(Name.getText().toString(),Password.getText().toString());*/
                final String email=Uemail.getText().toString().trim();
                String password=UPassword.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    Uemail.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    UPassword.setError("Password is Required");
                    return;
                }
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            String Uid =fAuth.getUid();
                            myRef.child(fAuth.getCurrentUser().getUid()).setValue(email);
                            Context context = getApplicationContext();
                            Map<String, Object> user = new HashMap<>();
                            user.put("Email", email);
                            user.put("Id",fAuth.getCurrentUser().getUid());
                            db.collection("users").document(fAuth.getUid()).set(user);
                            CharSequence text = "Logged In  Successfully";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                            Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Context context = getApplicationContext();
                            CharSequence text = "Error:" + task.getException().getMessage();
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                        }
                    });
                }
            });
        }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data); 
            if(result.isSuccess()){
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                finish();
            }else{
                Toast.makeText(this,"Login Failed",Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
