package com.example.protectyourchild;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class barcodeScanner extends AppCompatActivity {


    SurfaceView surfaceView;
    CameraSource cameraSource;
    TextView textView;
    BarcodeDetector barcodeDetector;


    FirebaseAuth fAuth;
    private DatabaseReference reference;
    public static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        fAuth=FirebaseAuth.getInstance();
        final String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        surfaceView= (SurfaceView)findViewById(R.id.camerapreview);
        textView = (TextView)findViewById(R.id.scantext);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(this,barcodeDetector)
                .setRequestedPreviewSize(640,480).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    return;
                }

                try {
                    cameraSource.start(surfaceHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

                cameraSource.stop();

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray <Barcode> qrCodes = detections.getDetectedItems();

                if (qrCodes.size() != 0)
                {
                    barcodeDetector.release();
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(100);
                            textView.setText(qrCodes.valueAt(0).displayValue);
                            reference = FirebaseDatabase.getInstance().getReference("Sensors");
                            Date today = new Date();
                            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
                            final String dateToStr = format.format(today);
                            Map<String,String> sensors = new HashMap<>();
                            sensors.put("Sensor",textView.getText().toString() );
                            reference.child(currentuser).push().setValue(sensors);
                            FirebaseMessaging.getInstance().subscribeToTopic(qrCodes.valueAt(0).displayValue); // firebase subscribe to specific arduino
                            Toast.makeText(barcodeScanner.this,""+ textView.getText() + " Sensor Added Successfuly", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),DashBoard.class));

                        }
                    });
                }
            }
        });

    }
}
