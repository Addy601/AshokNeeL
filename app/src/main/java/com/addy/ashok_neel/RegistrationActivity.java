package com.addy.ashok_neel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    private EditText etUsermail,etUserpassword;
    private Button btnRegister;
    private TextView tvLogin;
    private TextView rd;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);
        rd=findViewById(R.id.textView2);
        etUsermail= findViewById(R.id.etmail);
        etUserpassword=findViewById(R.id.etUserpassword);
        btnRegister=findViewById(R.id.btnregister);
        tvLogin=findViewById(R.id.tvlogin);
        progressBar=findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null){
            Intent intent = new Intent(RegistrationActivity.this,SecondActivity.class);
            startActivity(intent);
            finish();
        }



        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=etUsermail.getText().toString().trim();
                String password=etUserpassword.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    etUsermail.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    etUsermail.setError("Password is Required");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            myRef.child(fAuth.getCurrentUser().getUid()).setValue(email);
                            Context context = getApplicationContext();
                            Map<String, Object> user = new HashMap<>();
                            user.put("Email", email);
                            user.put("Id",fAuth.getCurrentUser().getUid());
                            db.collection("users").document(fAuth.getUid()).set(user);
                            CharSequence text = "User Created Successful";
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                            Intent intent = new Intent(RegistrationActivity.this,SecondActivity.class);
                            startActivity(intent);
                        } else{
                            Context context = getApplicationContext();
                            CharSequence text = "Error:"+task.getException().getMessage();
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }
                    }
                });

            }
        });



        }
    }


