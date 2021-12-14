package com.jchip.album.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.jchip.album.R;

import java.util.ArrayList;
import java.util.List;

public class FrameActivity extends AbstractActivity {
    private static final int MAX_FRAME_NUMBER = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layer);
    }

    @Override
    public void initContentView() {
        super.initContentView();

        ListView frameView = this.getListView(R.id.frame_list_view);
        ListViewAdapter listViewAdapter = new ListViewAdapter(getApplicationContext());
        frameView.setAdapter(listViewAdapter);
        frameView.setOnItemClickListener((adapterView, view, position, id) -> {
            Intent intent = new Intent();
            intent.putExtra(FRAME_INDEX, position);
            intent.putExtra(FRAME_RESOURCE, (Integer) listViewAdapter.getItem(position));
            this.setResult(RESULT_OK, intent);
            this.finish();
        });
        this.getView(R.id.frame_setting_view).setOnClickListener((v) -> this.finish());
    }

    public class ListViewAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        private List<Integer> frames;

        public ListViewAdapter(Context context) {
            this.context = context;
            this.inflater = (LayoutInflater.from(context));

            this.frames = new ArrayList<>();
            for (int index = 0; index < MAX_FRAME_NUMBER; index++) {
                String sourceName = "frame_" + index;
                int sourceId = context.getResources().getIdentifier(sourceName, "drawable", context.getPackageName());
                if (sourceId > 0) {
                    this.frames.add(sourceId);
                }
            }
        }

        @Override
        public int getCount() {
            return this.frames.size();
        }

        @Override
        public Object getItem(int position) {
            return this.frames.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.frame_list_item, null);
            ImageView imageView = view.findViewById(R.id.frame_item_image);
            imageView.setImageResource(frames.get(position));
            return view;
        }
    }
}