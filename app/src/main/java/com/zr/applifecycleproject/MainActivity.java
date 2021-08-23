package com.zr.applifecycleproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.test.AppTest;

import java.util.HashSet;
import java.util.Set;

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


        AppTest a=new AppTest();
        a.getPriority();
        Set<Object> set=new HashSet<>();
        set.add(new App());
        set.add(new App());
    }
}
