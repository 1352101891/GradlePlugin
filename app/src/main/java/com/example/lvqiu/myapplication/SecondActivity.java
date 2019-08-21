package com.example.lvqiu.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.baselibrary.BaseActivity;

public class SecondActivity extends BaseActivity {
    final static String TAG="SecondActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void jump(View view) {
        Intent intent=new Intent(this,ThirdActivity.class);
        this.startActivity(intent);
    }
}
