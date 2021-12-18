package com.jchip.album.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.jchip.album.data.PhotoData;

import java.util.ArrayList;
import java.util.List;

public class WidgetPhotoSetting extends DataActivity {

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
        private static final int PHOTO_NUMBER = 2;

        public ListViewAdapter(Context context) {
            this.context = context;
            this.inflater = (LayoutInflater.from(context));

            this.albums = new ArrayList<>();
            for (AlbumData albumData : ((DataActivity) context).queryAlbumPhotos()) {
                AlbumData album = new AlbumData(albumData.getAlbumName());
                album.setAlbumId(albumData.getAlbumId());
                this.albums.add(album);

                AlbumData photoAlbum = new AlbumData();
                for (int index = 0; index < albumData.getPhotoSize(); index++) {
                    if (index % PHOTO_NUMBER == 0) {
                        photoAlbum = new AlbumData();
                        this.albums.add(photoAlbum);
                    }
                    photoAlbum.addPhoto(albumData.getPhoto(index));
                }
            }
        }

        @Override
        public int getCount() {
            return this.albums.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = inflater.inflate(R.layout.widget_photo_setting_item, null);
                AlbumData albumData = this.albums.get(position);
                if (albumData.isSaved()) {
                    TextView albumName = view.findViewById(R.id.album_name);
                    albumName.setText(albumData.getAlbumName());
                } else {
                    for (int index = 0; index < albumData.getPhotoSize(); index++) {
                        PhotoData photoData = albumData.getPhoto(index);
                        View photoView = view.findViewById(index % PHOTO_NUMBER == 0 ? R.id.photo_left_view : R.id.photo_right_view);
                        PhotoHelper.setPhotoView(photoView, photoData, R.id.photo_image, R.id.photo_label, 0, 0);
                    }
                }
                view.findViewById(R.id.album_name).setVisibility(albumData.isSaved() ? View.VISIBLE : View.GONE);
                view.findViewById(R.id.photos_view).setVisibility(albumData.isSaved() ? View.GONE : View.VISIBLE);
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