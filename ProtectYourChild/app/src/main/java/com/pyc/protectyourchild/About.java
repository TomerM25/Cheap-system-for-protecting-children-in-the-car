package com.pyc.protectyourchild;

import androidx.appcompat.app.AppCompatActivity;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;


public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Element adsElement = new Element();
        adsElement.setTitle("Advertise here");

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.pyc)
                .setDescription("Protect Your Child")
                .addItem(new Element().setTitle("Version 1.0"))
                .addItem(adsElement)
                .addGroup("Connect with us")
                .addEmail("protectyourchild20@gmail.com")
                .addFacebook("ProtectYourChild")
                .addTwitter("Our Twitter")
                .addInstagram("Our Instagram")
                .addPlayStore("https://play.google.com/store/apps/details?id=com.pyc.protectyourchild&hl=en")
                .addGitHub("Protect Your Child")
                .addYoutube("ProtectYourChild")
                .addItem(createBack())
                .addItem(createCopyright())
                .create();

        setContentView(aboutPage);
    }
    private Element createCopyright() {
        Element copyright = new Element();
        String copyrightString = String.format("Copyright %d by O&T", Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(copyrightString);
        copyright.setIcon(R.mipmap.ic_launcher);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(About.this,copyrightString,Toast.LENGTH_SHORT).show();

            }
        });
        return copyright;
    }
    private Element createBack() {
        Element signOut = new Element();
        signOut.setIcon(R.drawable.back);
        String BackString = String.format("Back To DashBoard");
        signOut.setTitle(BackString);


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashBoard.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
        return signOut;
    }
    public void goBack(View view){
        startActivity(new Intent(getApplicationContext(), DashBoard.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}