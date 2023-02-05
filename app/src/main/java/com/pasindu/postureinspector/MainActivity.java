package com.pasindu.postureinspector;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {

    Button btnRecord, btnReplay, btnSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRecord=findViewById(R.id.btnRecord);
        btnReplay=findViewById(R.id.btnReplay);
        btnSettings=findViewById(R.id.btnSetting);

        if (OpenCVLoader.initDebug()){
            Log.d("OpenCV","Success, OpenCV loaded successfully");
        } else {
            Log.d("OpenCV","Error, OpenCV was not loaded");
        }

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intet=new Intent(MainActivity.this,RecordActivity.class);
                startActivity(intet);
            }
        });
    }
}