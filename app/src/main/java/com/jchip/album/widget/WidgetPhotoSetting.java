package com.jchip.album.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jchip.album.R;
import com.jchip.album.activity.AbstractActivity;
import com.jchip.album.data.AlbumData;
import com.jchip.album.data.DataHelper;
import com.jchip.album.data.PhotoData;

import java.util.List;

public class WidgetPhotoSetting extends AbstractActivity {

    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private int resultValue = RESULT_CANCELED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.widget_photo_setting);
    }

    @Override
    public void initContentView() {
        super.initContentView();

        this.appWidgetId = getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        if (this.appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            //finish();
        }

        ListView settingView = (ListView) findViewById(R.id.photo_setting_view);
        ListViewAdapter listViewAdapter = new ListViewAdapter(this);
        settingView.setAdapter(listViewAdapter);

        settingView.setOnItemClickListener((adapterView, view, position, id) -> {
            //saveSharedPreferences(getApplicationContext(), appWidgetId, (String) listViewAdapter.getItem(position));
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

            //         this.albums = DataHelper.getInstance(context).queryPhotoAlbum();
            this.albums = DataHelper.getInstance(context).queryAlbumPhotos();
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
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.widget_photo_setting_item, null);
            TextView albumName = view.findViewById(R.id.album_name);
            albumName.setText(this.albums.get(position).getAlbumName());

            GridLayout photoContainer = view.findViewById(R.id.photo_container);
            Log.d("","this.albums.get(position).getPhotos() size==="+this.albums.get(position).getPhotos().size());
            for (PhotoData photoData : this.albums.get(position).getPhotos()) {
                photo = photoData;
                ImageView photoImage = new ImageView(context);
                photoImage.setMaxWidth(80);
                photoImage.setMaxHeight(80);
                ((AbstractActivity) this.context).setImagePhoto(photoImage);
                photoContainer.addView(photoImage);
            }
            return view;
        }
    }

    protected void saveSharedPreferences(Context context, int appWidgetId, String item) {
//        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(context).edit();
//        prefs.putString(String.valueOf(appWidgetId), item);
//        prefs.commit();
        //this.updateWidget(getApplicationContext());
    }
}