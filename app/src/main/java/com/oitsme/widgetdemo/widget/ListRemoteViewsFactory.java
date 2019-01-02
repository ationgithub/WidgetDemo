package com.oitsme.widgetdemo.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.oitsme.widgetdemo.Device;
import com.oitsme.widgetdemo.R;
import com.oitsme.widgetdemo.jsoup.FormattingVisitor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeTraversor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.oitsme.widgetdemo.widget.ListWidgetProvider.COLLECTION_VIEW_ACTION;

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final static String TAG="WidgetFactory";
    private Context mContext;
    private int mAppWidgetId;
    private static List<Device> mDevices;

    Document doc = null;
    private static final String userAgent = "Mozilla/5.0 (jsoup)";
    private static final int timeout = 5 * 1000;
    /**
     * 构造ListRemoteViewsFactory
     */
    public ListRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        Log.e(TAG,"构造"+mAppWidgetId);
    }

    @Override
    public RemoteViews getViewAt(int position) {
        //  HashMap<String, Object> map;
        // 获取 item_widget_device.xml 对应的RemoteViews
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.item_widget_device);
        Log.e(TAG,""+position);

        // 设置 第position位的“视图”的数据  rv.setImageViewResource(R.id.iv_lock, ((Integer) map.get(IMAGE_ITEM)).intValue());
        Device device = mDevices.get(position);
        rv.setTextViewText(R.id.tv_name, device.getName());

        // 设置 第position位的“视图”对应的响应事件
        Intent fillInIntent =  new Intent();
        fillInIntent.putExtra("Type", 0);
        fillInIntent.putExtra(COLLECTION_VIEW_ACTION, position);
        rv.setOnClickFillInIntent(R.id.rl_widget_device, fillInIntent);

        Intent lockIntent = new Intent();
        lockIntent.putExtra("Type", 1);
        lockIntent.putExtra(COLLECTION_VIEW_ACTION, position);
        rv.setOnClickFillInIntent(R.id.iv_lock, lockIntent);

        Intent unlockIntent = new Intent();
        unlockIntent.putExtra("Type", 2);
        unlockIntent.putExtra(COLLECTION_VIEW_ACTION, position);
        rv.setOnClickFillInIntent(R.id.iv_unlock, unlockIntent);

        return rv;
    }


    /**
     * 初始化ListView的数据
     */
    private void initListViewData() {
        mDevices = new ArrayList<>();
//        mDevices.add(new Device("Hello", 0));
//        mDevices.add(new Device("Oitsme", 1));
//        mDevices.add(new Device("Hi", 0));
//        mDevices.add(new Device("Hey", 1));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    doc = Jsoup.connect("http://www.baidu.com/").userAgent(userAgent).timeout(timeout).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                String plainText = getPlainText(doc);
//                Log.e("plainText",doc.outerHtml());
                Elements elements = doc.select("a[href]"); // get each element that matches the CSS selector
                for (Element element : elements) {
                    String plainText = getPlainText(element); // format that element to plain text
                    Log.e("plainText",plainText);
                    mDevices.add(new Device(plainText, 0));
                }
            }
        }).start();
    }
    private static int i;
    public static void refresh(){
        i++;
        mDevices.add(new Device("Refresh"+i, 1));
    }
    public String getPlainText(Element element) {
        //自定义一个NodeVisitor - FormattingVisitor
        FormattingVisitor formatter = new FormattingVisitor();
        //使用NodeTraversor来装载FormattingVisitor
        NodeTraversor traversor = new NodeTraversor(formatter);
        //进行遍历
        traversor.traverse(element);
        return formatter.toString();
    }

    @Override
    public void onCreate() {
        Log.e(TAG,"onCreate");
        // 初始化“集合视图”中的数据
        initListViewData();
    }

    @Override
    public int getCount() {
        // 返回“集合视图”中的数据的总数
        return mDevices.size();
    }

    @Override
    public long getItemId(int position) {
        // 返回当前项在“集合视图”中的位置
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        // 只有一类 ListView
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
        mDevices.clear();
    }
}
