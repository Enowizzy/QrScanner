package com.example.qrscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import eu.livotov.labs.android.camview.ScannerLiveView;
import eu.livotov.labs.android.camview.scanner.decoder.zxing.ZXDecoder;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.VIBRATE;

public class ScannerActivity extends AppCompatActivity {

    private ScannerLiveView camera;
    private TextView scannedTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        if (checkPermission()){
            Toast.makeText(this,"Permission Granted..",Toast.LENGTH_LONG).show();
        }
        else {
            requestPermission();
        }

        scannedTV=(TextView)findViewById(R.id.idTVscanned);
        camera=(ScannerLiveView)findViewById(R.id.camView);

        camera.setScannerViewEventListener(new ScannerLiveView.ScannerViewEventListener() {
            @Override
            public void onScannerStarted(ScannerLiveView scanner) {
                Toast.makeText(ScannerActivity.this,"Scanner started",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onScannerStopped(ScannerLiveView scanner) {
                Toast.makeText(ScannerActivity.this,"Scanner stopped",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onScannerError(Throwable err) {
                Toast.makeText(ScannerActivity.this,"Scanner error",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeScanned(String data) {

                Intent intent = new Intent(ScannerActivity.this, MappingActivity.class);
                intent.putExtra("message", data);
                startActivity(intent);

//                scannedTV.setText(data);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        ZXDecoder decoder = new ZXDecoder();

        decoder.setScanAreaPercent(0.8);

        camera.setDecoder(decoder);

        camera.startScanner();

    }

    @Override
    protected void onPause() {
        camera.stopScanner();
        super.onPause();

    }

    private boolean checkPermission(){
        int camera_permission= ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int vibrate_permission= ContextCompat.checkSelfPermission(getApplicationContext(),VIBRATE);

        return camera_permission== PackageManager.PERMISSION_GRANTED && vibrate_permission==PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        int PERMISSION_REQUEST_CODE=200;
        ActivityCompat.requestPermissions(this, new String[] {CAMERA,VIBRATE},PERMISSION_REQUEST_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            boolean cameraaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean vibrateaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (cameraaccepted & vibrateaccepted) {
                Toast.makeText(ScannerActivity.this, "Permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ScannerActivity.this, "Permission Denied \n You can not use app without providing permission", Toast.LENGTH_LONG).show();

            }
        }
    }
}