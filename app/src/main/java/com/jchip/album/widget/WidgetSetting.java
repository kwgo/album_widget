package com.jchip.album.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.jchip.album.ActivityAlbum;
import com.jchip.album.common.AlbumHelper;
import com.jchip.album.data.DataHelper;
import com.jchip.album.data.WidgetData;
import com.jchip.album.layer.ListLayer;

public abstract class WidgetSetting extends ListLayer {
    protected int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    protected int resultValue = RESULT_CANCELED;
    protected boolean appStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
    }

    @Override
    public void initContentView() {
        super.initContentView();

        this.appWidgetId = getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        if (this.appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    protected void notifyWidget(int value) {
        Intent intent = new Intent();
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, this.appWidgetId);
        setResult(value, intent);

        this.exitSetting();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (resultValue == RESULT_CANCELED) {
            notifyWidget(RESULT_CANCELED);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (resultValue == RESULT_CANCELED) {
            notifyWidget(RESULT_CANCELED);
        }
    }

    @Override
    protected void onDestroy() {
        if (resultValue == RESULT_CANCELED) {
            notifyWidget(RESULT_CANCELED);
        }
        super.onDestroy();
    }

    protected void updateWidget(Class<?> provider) {
        notifyWidget(resultValue = RESULT_OK);

        Intent intent = new Intent(this, provider);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, this.appWidgetId);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{this.appWidgetId});
        this.sendBroadcast(intent);
    }

    protected void saveWidget(WidgetData widgetData) {
        widgetData.setWidgetId(this.appWidgetId);
        DataHelper.getInstance(this).saveWidget(widgetData);
    }

    protected void startApp() {
        this.appStarted = true;
        this.startActivity(new Intent(this, ActivityAlbum.class));
    }

    protected void exitSetting() {
        if (!appStarted) {
            AlbumHelper.exitApp(this);
        }
    }
}