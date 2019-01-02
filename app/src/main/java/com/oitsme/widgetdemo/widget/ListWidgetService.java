package com.oitsme.widgetdemo.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.oitsme.widgetdemo.LogUtils;

public class ListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        LogUtils.d("RemoteViewsService onGet");
        return new ListRemoteViewsFactory(this, intent);
    }

    @Override
    public void onDestroy() {
        LogUtils.d("RemoteViewsService destory");
        super.onDestroy();
    }
}
