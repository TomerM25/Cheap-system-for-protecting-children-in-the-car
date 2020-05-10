package com.example.loginactivity;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "TAG";
    TextView fullName,email,phone;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    private CardView carCard,poolCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //phone = findViewById(R.id.profilePhone);
        fullName = findViewById(R.id.profileName);
        //email = findViewById(R.id.profileEmail);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        carCard=(CardView)findViewById(R.id.car_card);
        poolCard=(CardView)findViewById(R.id.pool_card);

        carCard.setOnClickListener(this);
        poolCard.setOnClickListener(this);


        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (e!=null){
                    Log.d(TAG,"Error:"+e.getMessage());
                }
                else {
                    //phone.setText(documentSnapshot.getString("phone"));
                    fullName.setText(documentSnapshot.getString("fName"));
                    //email.setText(documentSnapshot.getString("email"));
                }
            }
        });
    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }

    @Override
    public void onClick(View view) {

        Intent i;

        switch(view.getId())
        {
            case R.id.pool_card:i= new Intent(this,Pool.class);startActivity(i);break;
            case R.id.car_card:i=  new Intent(this,Car.class);startActivity(i);break;
            default:break;
        }



    }
}
