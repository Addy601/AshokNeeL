package com.addy.ashok_neel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* setContentView(R.layout.activity_about_us);*/
        Element addElement = new Element();
        addElement.setTitle("Advertize With Us");
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.aboutusback)
                .setDescription("Ashok_Neel v1.0.0")
                .addGroup("INFO")
                .addItem(new Element().setTitle("Version 1.0"))
                .addItem(addElement)
                .addGroup("Connect With Me")
                .addEmail("technimaticindia@gmail.com")
                .addGitHub("Addy601")
                .addInstagram("pandeyadarsh_7")
                .addYoutube("UCPWiSF4zMmzgFswiPrpkrfA")
                .addItem(createCopyright())
                .create();
        setContentView(aboutPage);
    }
    private Element createCopyright(){
        Element copyright = new Element();
        final String copyrightString = "Copyright 2020 by Adarsh";
        copyright.setTitle(copyrightString);
        copyright.setIcon(R.mipmap.ic_launcher);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                CharSequence text = copyrightString;
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

    return copyright;
    }

}
