package com.example.lvqiu.myapplication;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.util.Iterator;
import java.util.List;

public class MyApplication extends Application {
    public final static String TAG="MyApplication";
    public static String var="";

    @Override
    public void onCreate() {
        Log.e(TAG,"onCreate");
        super.onCreate();
        if (var.equals("")){
            var=""+getAppName(this);
            Log.e("onCreate",var);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        Log.e(TAG,"attachBaseContext");
        super.attachBaseContext(base);
    }

    /**
     * 获取当前进程的名字，一般就是当前app的包名
     *
     * @param context 当前上下文
     * @return 返回进程的名字
     */
    public static String getAppName(Context context)
    {
        int pid = android.os.Process.myPid(); // Returns the identifier of this process
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List list = activityManager.getRunningAppProcesses();
        Iterator i = list.iterator();
        while (i.hasNext())
        {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try
            {
                if (info.pid == pid)
                {
                    // 根据进程的信息获取当前进程的名字
                    return info.processName;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        // 没有匹配的项，返回为null
        return null;
    }
}
