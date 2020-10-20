package com.addy.ashok_neel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import static android.os.Environment.DIRECTORY_DOWNLOADS;



public class Maintenance extends AppCompatActivity {
private Button btn2;
private FirebaseAuth firebaseAuth;
FirebaseStorage firebaseStorage;
StorageReference storageReference;
StorageReference ref;
FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);
        btn2=findViewById(R.id.button2);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
            }
        });
    }
    public void download(){
        String document= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference docRef = db.collection("users").document(document);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                       String Link = document.getString("Maintenance");
                       if (Link==null) {
                           Toast.makeText(Maintenance.this,"NO MAINTENANCE FILE EXISTS CONTACT ADARSH!",Toast.LENGTH_SHORT).show();
                       }
                        downloadFiles(Maintenance.this, "Maintenance", ".pdf", DIRECTORY_DOWNLOADS,Link);
                        Toast.makeText(Maintenance.this,"Download Started Check Your Notification",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Maintenance.this,"Document Not Found",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Maintenance.this,"Task Failed",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Maintenance.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });
        /*db.collection("users").document(document).collection("Maintenance")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String link=queryDocumentSnapshots.getDocuments().toString();
                downloadFiles(Maintenance.this, "Maintenance", ".pdf", DIRECTORY_DOWNLOADS,link);
                Toast.makeText(Maintenance.this,"Download Started Check Your Notification",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Maintenance.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();

            }
        });
*/
       /* storageReference=FirebaseStorage.getInstance().getReference("Maintenance/");
        ref=storageReference.child("new resume.pdf");
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url=uri.toString();

            downloadFiles(Maintenance.this,"Maintenance",".pdf",DIRECTORY_DOWNLOADS,url);
            Toast.makeText(Maintenance.this,"Downoad Started Check Your Notification",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Maintenance.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();

            }
        });*/

    }

    public long downloadFiles(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {


            DownloadManager downloadmanager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            if(url==null){
                Toast.makeText(Maintenance.this,"NO MAINTENANCE FILE EXISTS CONTACT ADARSH!",Toast.LENGTH_SHORT).show();
            }

                Uri uri = Uri.parse(url);
                DownloadManager.Request request = new DownloadManager.Request(uri);

                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);


                return downloadmanager.enqueue(request);

    }

}
