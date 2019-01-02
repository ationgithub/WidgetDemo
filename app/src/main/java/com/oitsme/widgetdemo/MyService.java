package com.oitsme.widgetdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.oitsme.widgetdemo.widget.ListWidgetService;

public class MyService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return START_STICKY_COMPATIBILITY;
        //return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy()
    {
        Intent localIntent = new Intent();
        localIntent.setClass(this, ListWidgetService.class); // 销毁时重新启动Service
        this.startService(localIntent);
    }
}
