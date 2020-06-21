package com.example.protectyourchild;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class splashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;

    Animation topAnim,bottomAnim;
    ImageView logo,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        //Animation
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Hooks
        logo = findViewById(R.id.logo);
        name = findViewById(R.id.name);

        logo.setAnimation(bottomAnim);
        name.setAnimation(topAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splashScreen.this,Login.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}
