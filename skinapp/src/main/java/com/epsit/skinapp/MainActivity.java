package com.epsit.skinapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG,"R.color.colorPrimary="+ R.color.colorPrimary +"  "+ getResources().getColor(R.color.colorPrimary));
        Log.e(TAG,"R.color.colorAccent="+ R.color.colorAccent +"  "+getResources().getColor(R.color.colorAccent ));
    }
}
