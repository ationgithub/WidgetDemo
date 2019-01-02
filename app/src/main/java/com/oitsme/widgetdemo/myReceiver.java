package com.oitsme.widgetdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.oitsme.widgetdemo.widget.ListWidgetService;

public class myReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        context.startService(new Intent(context, ListWidgetService.class));
    }
}
