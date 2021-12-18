package com.jchip.album.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jchip.album.R;
import com.jchip.album.activity.DataActivity;
import com.jchip.album.common.PhotoHelper;
import com.jchip.album.data.AlbumData;
import com.jchip.album.data.DataHelper;
import com.jchip.album.data.PhotoData;
import com.jchip.album.data.WidgetData;

import java.util.List;

public class WidgetAlbumSetting extends DataActivity {

    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private int resultValue = RESULT_CANCELED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.widget_album_setting);
    }

    @Override
    public void initContentView() {
        super.initContentView();

        this.appWidgetId = getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        if (this.appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            //finish();
        }

        ListView settingView = (ListView) findViewById(R.id.album_setting_view);
        ListViewAdapter listViewAdapter = new ListViewAdapter(this);
        settingView.setAdapter(listViewAdapter);

        settingView.setOnItemClickListener((adapterView, view, position, id) -> {

            Log.d("", "photoView  OnClick  ===========================");
            Log.d("", "photoView  appWidgetId  ===========================" + appWidgetId);
            //Log.d("","photoView  photoData  ===========================" + photoData.getAlbumId());
            Log.d("", "photoView  OnClick  ===========================");
            AlbumData albumData = (AlbumData) listViewAdapter.getItem(position);
            saveWidget(this, appWidgetId, albumData.getAlbumId());
            finish();
        });
    }

    private void notifyWidget(int value) {
        Intent intent = new Intent();
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, this.appWidgetId);
        setResult(value, intent);
    }

    private void updateWidget(Context context) {
        notifyWidget(resultValue = RESULT_OK);

        Intent updateIntent = new Intent(context, this.gerProviderClass());
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, this.appWidgetId);
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{this.appWidgetId});
        context.sendBroadcast(updateIntent);
    }

    protected Class gerProviderClass() {
        return WidgetPhotoProvider.class;
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
            setResult(RESULT_CANCELED);
        }
        super.onDestroy();
    }

    public class ListViewAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        private List<AlbumData> albums;

        public ListViewAdapter(Context context) {
            this.context = context;
            this.inflater = (LayoutInflater.from(context));

            this.albums = ((DataActivity) context).queryPhotoAlbum();
        }

        @Override
        public int getCount() {
            return this.albums.size();
        }

        @Override
        public Object getItem(int position) {
            return this.albums.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = inflater.inflate(R.layout.widget_album_setting_item, null);
                AlbumData albumData = this.albums.get(position);
                TextView albumName = view.findViewById(R.id.album_name);
                albumName.setText(albumData.getAlbumName());
                PhotoData photoData = albumData.getPhoto(0);
                View photoView = view.findViewById(R.id.photo_view);
                PhotoHelper.setPhotoView(photoView, photoData, R.id.photo_image, R.id.photo_label, 0, 0);
            }
            return view;
        }

    }

    protected void saveWidget(Context context, int appWidgetId, int albumId) {
        Log.d("", "saveWidget 888888888888888888888888888");
        //WidgetData widgetData = DataHelper.getInstance(context).queryWidget(appWidgetId);
        WidgetData widgetData = new WidgetData();
        widgetData.setWidgetId(appWidgetId);
        widgetData.setAlbumId(albumId);
        DataHelper.getInstance(context).saveWidget(widgetData);
        this.updateWidget(context);
    }
}