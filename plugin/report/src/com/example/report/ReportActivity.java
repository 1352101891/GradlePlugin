package com.example.report;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.baselibrary.BaseActivity;


public class ReportActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_main);
        TextView textView= (TextView) findViewById(R.id.text);
        textView.setText("上报插件");
    }

    public void jump(View view){
        Intent intent=new Intent(this,ReportActivity_v2.class);
        this.startActivity(intent);
    }
}
