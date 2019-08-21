package com.example.baselibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;


public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        TextView textView= (TextView) findViewById(R.id.text);
        textView.setText(""+getSimpleName());
        Log.e(getSimpleName(),"onCreate");
    }

    public String getSimpleName(){
        String name=getClass().getSimpleName();
//        Log.e("getSimpleName",getClass().getSimpleName());
        return name;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(getSimpleName(),"onNewIntent");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(getSimpleName(),"onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(getSimpleName(),"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(getSimpleName(),"onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(getSimpleName(),"onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(getSimpleName(),"onDestroy");
    }
}
