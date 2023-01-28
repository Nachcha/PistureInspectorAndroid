package com.pasindu.postureinspector;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnRecord, btnReplay, btnSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRecord=findViewById(R.id.btnRecord);
        btnReplay=findViewById(R.id.btnReplay);
        btnSettings=findViewById(R.id.btnSetting);

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intet=new Intent(MainActivity.this,RecordActivity.class);
                startActivity(intet);
            }
        });
    }
}