package com.example.protectyourchild;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Pool extends AppCompatActivity implements View.OnClickListener {

    private CardView add_device_card , account_card , events_card , armed_card , disarmed_card, help_card;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    TextView textView;
    public static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pool);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();



        add_device_card = (CardView) findViewById(R.id.add_device);
        account_card = (CardView) findViewById(R.id.account);
        events_card = (CardView)findViewById(R.id.events);
        armed_card = (CardView)findViewById(R.id.armed);
        disarmed_card=(CardView)findViewById(R.id.disarmed);
        help_card = (CardView) findViewById(R.id.help);

        //add Click listener to cards
        add_device_card.setOnClickListener(this);
        account_card.setOnClickListener(this);
        events_card.setOnClickListener(this);
        armed_card.setOnClickListener(this);
        disarmed_card.setOnClickListener(this);
        help_card.setOnClickListener(this);

        textView = findViewById(R.id.profileName);


        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (e!=null){
                    Log.d(TAG,"Error:"+e.getMessage());
                }
                else {
                    //phone.setText(documentSnapshot.getString("phone"));
                    textView.setText(documentSnapshot.getString("fName"));
                    //email.setText(documentSnapshot.getString("email"));
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()){
            case R.id.add_device : i = new Intent(this,barcodeScanner.class);startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);break;

            case R.id.events : i = new Intent(this,EventHistory.class); startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);break;

            case R.id.account : i = new Intent(this,Account.class); startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);break;

            case R.id.armed : i = new Intent(this,Armed.class); startActivity(i); //FirebaseMessaging.getInstance().subscribeToTopic();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);break;

            case R.id.help : i = new Intent(this,About.class); startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);break;

            case R.id.disarmed : i = new Intent(this, Disarmed.class); startActivity(i); //FirebaseMessaging.getInstance().subscribeToTopic();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);break;

        }

    }
}
