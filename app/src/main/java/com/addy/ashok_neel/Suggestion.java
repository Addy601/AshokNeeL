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

public class Suggestion extends AppCompatActivity {
    EditText editTextSuggestion;
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
        setContentView(R.layout.activity_suggestion);
        editTextSuggestion=findViewById(R.id.editTextComplaint);
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
                Intent intent = new Intent(Suggestion.this,SuggestionImageUpload.class);
                startActivity(intent);
            }
        });
        databaseComplaint= FirebaseDatabase.getInstance().getReference();
        btnsubmit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSuggestion();
            }
        });

    }
    private void addSuggestion(){
        String Suggestions=editTextSuggestion.getText().toString().trim();
        String Identity=spinner.getSelectedItem().toString();
        if (!TextUtils.isEmpty(Suggestions)){
            String id = databaseComplaint.push().getKey();
            ComplaintDataStore suggestion=new ComplaintDataStore(id,Suggestions,Identity);
            databaseComplaint.child(id).setValue(suggestion);
            Toast.makeText(this,"Suggestion Added Successfully",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"You Should Enter Suggestion Before Submitting",Toast.LENGTH_SHORT).show();

    }

    }
}
