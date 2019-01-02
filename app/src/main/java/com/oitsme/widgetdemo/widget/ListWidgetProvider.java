package com.oitsme.widgetdemo.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.oitsme.widgetdemo.LogUtils;
import com.oitsme.widgetdemo.R;
import com.oitsme.widgetdemo.Utils;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class ListWidgetProvider extends AppWidgetProvider {
    private static final String TAG = "WIDGET";
    public static final String REFRESH_WIDGET = "com.zhpan.REFRESH_WIDGET";
    public static final String COLLECTION_VIEW_ACTION = "com.zhpan.COLLECTION_VIEW_ACTION";
    public static final String COLLECTION_VIEW_EXTRA = "com.zhpan.COLLECTION_VIEW_EXTRA";
    private static Handler mHandler=new Handler();

    private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            hideLoading(Utils.getContext());
//            Toast.makeText(Utils.getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
            Log.e("ListWidgetProvider","刷新成功");
        }
    };

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ComponentName thisWidget = new ComponentName(context,ListWidgetProvider.class);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget);
        Intent intent = new Intent(context, ListWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[0]);
        remoteViews.setRemoteAdapter(R.id.lv_device, intent);
        // 点击列表触发事件  注意：Intent指定的类
        Intent clickIntent = new Intent(context, ListWidgetProvider.class);
        // 设置Action，方便在onReceive中区别点击事件
        clickIntent.setAction(COLLECTION_VIEW_ACTION);
        clickIntent.setData(Uri.parse(clickIntent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent pendingIntentTemplate = PendingIntent.getBroadcast(
                context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.lv_device, pendingIntentTemplate);
        // 刷新按钮
        final Intent refreshIntent = new Intent(context, ListWidgetProvider.class);
        refreshIntent.setAction(REFRESH_WIDGET);
        final PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context, 0, refreshIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.tv_refresh, refreshPendingIntent);
        // 更新Wdiget
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);

        //        for (int appWidgetId : appWidgetIds) {
//            // 获取AppWidget对应的视图
//            Intent btIntent = new Intent(context, ListWidgetProvider.class);
//            btIntent.setAction(REFRESH_WIDGET);
//            PendingIntent btPendingIntent = PendingIntent.getBroadcast(context, 0, btIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            remoteViews.setOnClickPendingIntent(R.id.tv_refresh, btPendingIntent);
            // (01) intent: 对应启动 ListWidgetService(RemoteViewsService) 的intent
            // (02) setRemoteAdapter: 设置 ListView的适配器
//            Intent serviceIntent = new Intent(context, ListWidgetService.class);
//            remoteViews.setRemoteAdapter(R.id.lv_device, serviceIntent);
            // 设置响应 “ListView” 的intent模板
            // 说明：“集合控件(如GridView、ListView、StackView等)”中包含很多子元素。
            //     它们不能像普通的按钮一样通过 setOnClickPendingIntent 设置点击事件，必须先通过两步。
            //        (01) 通过 setPendingIntentTemplate 设置 “intent模板”，这是比不可少的！
            //        (02) 然后在处理该“集合控件”的RemoteViewsFactory类的getViewAt()接口中 通过 setOnClickFillInIntent 设置“集合控件的某一项的数据”
//            Intent listIntent =  new Intent(context, ListWidgetService.class);
//            listIntent.setAction(COLLECTION_VIEW_ACTION);
//            listIntent.setData(Uri.parse(listIntent.toUri(Intent.URI_INTENT_SCHEME)));
//            listIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[0]);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, listIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            remoteViews.setPendingIntentTemplate(R.id.lv_device, pendingIntent);
//        }
//        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
//        super.onUpdate(context, appWidgetManager, appWidgetIds);

//        final int counter = appWidgetIds.length;=1
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e("ListWidgetProvider",action);
        if (action.equals(COLLECTION_VIEW_ACTION)) {
            // 接受“ListView”的点击事件的广播
            Log.e("ListWidgetProvider","点击...");
            int type = intent.getIntExtra("Type", 0);
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            int index = intent.getIntExtra(COLLECTION_VIEW_EXTRA, 0);
            switch (type) {
                case 0:
//                    Toast.makeText(context, "item" + index, Toast.LENGTH_SHORT).show();
                    Log.e("ListWidgetProvider","item");
                    break;
                case 1:
//                    Toast.makeText(context, "lock"+index, Toast.LENGTH_SHORT).show();
                    Log.e("ListWidgetProvider","lock");
                    break;
                case 2:
//                    Toast.makeText(context, "unlock"+index, Toast.LENGTH_SHORT).show();
                    Log.e("ListWidgetProvider","unlock");
                    break;
            }
        } else if (action.equals(REFRESH_WIDGET)) {
            // 接受“bt_refresh”的点击事件的广播
            Log.e("ListWidgetProvider","刷新...");
//            Toast.makeText(context, "刷新...", Toast.LENGTH_SHORT).show();
            final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            final ComponentName cn = new ComponentName(context,ListWidgetProvider.class);
            ListRemoteViewsFactory.refresh();
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn),R.id.lv_device);
            mHandler.postDelayed(runnable,500);
            showLoading(context);
        }
        super.onReceive(context, intent);
    }

    private void showLoading(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget);
        remoteViews.setViewVisibility(R.id.tv_refresh, View.VISIBLE);
        remoteViews.setViewVisibility(R.id.progress_bar, View.VISIBLE);

        remoteViews.setTextViewText(R.id.tv_refresh, "正在刷新...");
        refreshWidget(context, remoteViews, false);
    }
    private void hideLoading(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.layout_widget);
        remoteViews.setViewVisibility(R.id.progress_bar, View.GONE);

        remoteViews.setTextViewText(R.id.tv_refresh, "刷新");
        refreshWidget(context, remoteViews, false);
    }
    private void refreshWidget(Context context, RemoteViews remoteViews, boolean isList) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName componentName = new ComponentName(context, ListWidgetProvider.class);
        appWidgetManager.updateAppWidget(componentName, remoteViews);
        if (isList)
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(componentName), R.id.lv_device);
    }
}
