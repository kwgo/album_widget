package com.jchip.album.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jchip.album.R;
import com.jchip.album.data.PhotoData;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WidgetPhotoSetting extends AppCompatActivity {

    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private int resultValue = RESULT_CANCELED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_photo_setting);

        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        this.appWidgetId = getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        if (this.appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        ListView settingView = (ListView) findViewById(R.id.widget_setting_view);
        ListViewAdapter listViewAdapter = new ListViewAdapter(getApplicationContext());
        settingView.setAdapter(listViewAdapter);

        settingView.setOnItemClickListener((adapterView, view, position, id) -> {
            saveSharedPreferences(getApplicationContext(), appWidgetId, (String) listViewAdapter.getItem(position));
            updateWidget(getApplicationContext());
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
        return WidgetAlbumProvider.class;
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

        private Map<String, String[]> info;
        private List<String> sortedInfo;

        public ListViewAdapter(Context context) {
            this.context = context;
            this.inflater = (LayoutInflater.from(context));

//            this.info = MainHelper.getISOInfo();
//
//            this.sortedInfo = new ArrayList(this.info.keySet());
//
//            FallHelper.sortCountryInfo(this.context, this.info, this.sortedInfo, FallHelper.COUNTRY);
        }

        @Override
        public int getCount() {
            return this.info.size();
        }

        @Override
        public Object getItem(int position) {
            return this.sortedInfo.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.widget_photo_setting_item, null);
//            TextView textView = view.findViewById(R.id.widget_setting_text);
//            ImageView imageView = view.findViewById(R.id.widget_setting_image);

   //         String item = this.sortedInfo.get(position);
     //       textView.setText(FallUtility.getSourceText(context, item, "string", "short"));
     //       imageView.setImageResource(FallUtility.getSourceId(context, item, "drawable", "flag"));
            return view;
        }
    }

    protected void saveSharedPreferences(Context context, int appWidgetId, String item) {
        SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(context).edit();
        //prefs.clear();
        prefs.putString(String.valueOf(appWidgetId), item);
        prefs.commit();
    }
}