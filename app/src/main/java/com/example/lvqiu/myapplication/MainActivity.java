package com.example.lvqiu.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.baselibrary.BaseActivity;
import com.example.report.ReportActivity;

import java.io.IOException;
import java.util.HashMap;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends BaseActivity {
    final static String TAG="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testOkHttp();
        testRxJava();
        HashMap map=new HashMap();
        map.put("","");
    }


    public void testOkHttp(){

        String url = "http://wwww.baidu.com";
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: " + response.body().string());
            }
        });
    }

    public void testRxJava(){
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("连载1");
                emitter.onNext("连载2");
                emitter.onNext("连载3");
                emitter.onComplete();
            }
        }).map(new Function<String, Integer>() {
            @Override
            public Integer apply(String s) throws Exception {
                return 11;
            }
        }).observeOn(AndroidSchedulers.mainThread())//回调在主线程
        .subscribeOn(Schedulers.io())//执行在io线程
        .subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG,"onSubscribe");
            }

            @Override
            public void onNext(Integer value) {
                Log.d(TAG,"onNext:"+value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG,"onError="+e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG,"onComplete()");
            }
        });
    }

    public void jump(View view) {
        Intent intent=new Intent(this, ReportActivity.class);
        this.startActivity(intent);
    }
}
