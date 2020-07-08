package com.pyc.protectyourchild;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class DashBoard extends AppCompatActivity implements View.OnClickListener {

    private CardView add_device_card , account_card , events_card , armed_card , disarmed_card, help_card,profile_card;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;
    String userId;
    private int STORAGE_PREMISSION_CODE =1;
    TextView textView;
    ImageView imageView;

    public static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        requestCameraPermission();

        NotificationManager mNotificationManager = (NotificationManager)DashBoard.this.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            // Check if the notification policy access has been granted for the app.
            if (!mNotificationManager.isNotificationPolicyAccessGranted()) {
                Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivity(intent);
            }
        }

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();


        profile_card=(CardView) findViewById(R.id.profileCard);
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
        profile_card.setOnClickListener(this);

        textView = findViewById(R.id.profileName);

        imageView = findViewById(R.id.profileImageView);
        registerForContextMenu(profile_card);

        StorageReference profileRef =storageReference.child("users/"+userId+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageView);
            }
        });




        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (e!=null){
                    Log.d(TAG,"Error:"+e.getMessage());
                }
                else {
                    textView.setText(documentSnapshot.getString("fName"));
                }
            }
        });



    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.user_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item){
        switch (item.getItemId())
        {
            case R.id.logout:
                logout(); break;
            case R.id.change_picture:
                change_picture();break;
            default:return false;
        }
                return super.onContextItemSelected(item);
        }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()){
            case R.id.add_device :
                i = new Intent(this,barcodeScanner.class);startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left); break;

            case R.id.events : i = new Intent(this,EventHistory.class); startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);break;

            case R.id.account : i = new Intent(this,Account.class); startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);break;

            case R.id.armed : i = new Intent(this, Activate.class); startActivity(i); //FirebaseMessaging.getInstance().subscribeToTopic();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);break;

            case R.id.help : i = new Intent(this,About.class); startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);break;

            case R.id.disarmed : i = new Intent(this, Deactivate.class); startActivity(i); //FirebaseMessaging.getInstance().subscribeToTopic();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);break;

        }

    }
    private void requestCameraPermission() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {


                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
    public void change_picture ()
    {
        Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(openGalleryIntent,1000);
    }
    @Override
    protected void onActivityResult (int requestCode, int resultCode, @androidx.annotation.Nullable Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==1000){
            if(resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                handleUpload(imageUri);
            }
        }
    }
    //Upload profile picture to firebase
    private void handleUpload (Uri image)
    {
        StorageReference reference = storageReference.child("users/"+userId+"/profile.jpg");
        reference.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(imageView);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DashBoard.this,"Faild Uploadeding Image",Toast.LENGTH_SHORT).show();

            }
        });

    }
}
