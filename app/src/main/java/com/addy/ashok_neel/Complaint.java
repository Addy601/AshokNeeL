package com.addy.ashok_neel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Complaint extends AppCompatActivity {
    EditText editTextComplaint;
    TextView textView6;
    TextView textView10;
    TextView textView11;
    Button btnsubmit;
    Button btnUpload;
    Spinner spinner;
    ImageView img3;
    DatabaseReference databaseComplaint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        editTextComplaint=findViewById(R.id.editTextComplaint);
        btnsubmit=findViewById(R.id.btnsubmit);
        spinner=findViewById(R.id.spinner);
        img3=findViewById(R.id.imageView3);
        textView6=findViewById(R.id.textView6);
        textView10=findViewById(R.id.textView10);
        textView11=findViewById(R.id.textView11);
        btnUpload=findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Complaint.this,ComplaintImageUpload.class);
                startActivity(intent);
            }
        });
        databaseComplaint= FirebaseDatabase.getInstance().getReference();
        btnsubmit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComplaint();
            }
        });

    }
    private void addComplaint(){
        String Complaints=editTextComplaint.getText().toString().trim();
        String Identity=spinner.getSelectedItem().toString();
        if (!TextUtils.isEmpty(Complaints)){
           String id = databaseComplaint.push().getKey();
           ComplaintDataStore complaint=new ComplaintDataStore(id,Complaints,Identity);
           databaseComplaint.child(id).setValue(complaint);
           Toast.makeText(this,"Complaint Added Successfully",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"You Should Enter Complaint Before Submitting",Toast.LENGTH_SHORT).show();
        }
    }
}
