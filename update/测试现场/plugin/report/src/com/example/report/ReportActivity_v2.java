package com.example.report;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

public class ReportActivity_v2 extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.report_main);
        TextView textView=findViewById(R.id.text);
        textView.setText(R.string.app_name_update);
    }
}
