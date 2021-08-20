package com.zr.applifecycleproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.app.DialogCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private View btShowDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("=====","=====setContentView");
        btShowDialog = findViewById(R.id.btShowDialog);
        btShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDialog dialog=new AppCompatDialog(MainActivity.this);
                dialog.show();
            }
        });
    }
}
