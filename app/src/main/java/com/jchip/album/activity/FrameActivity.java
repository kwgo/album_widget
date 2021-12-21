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
        super.setContentView(R.layout.frame_layer);
        super.setStatusBarColor(android.R.color.transparent);
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
            intent.putExtra(FRAME_LOOK, (Integer) listViewAdapter.getLook(position));
            this.setResult(RESULT_OK, intent);
            this.finish();
        });
        this.getView(R.id.frame_setting_view).setOnClickListener((v) -> this.finish());
    }

    public class ListViewAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        private List<Integer> frames;
        private List<Integer> frameLooks;

        public ListViewAdapter(Context context) {
            this.context = context;
            this.inflater = (LayoutInflater.from(context));

            this.frames = new ArrayList<>();
            this.frameLooks = new ArrayList<>();
            for (int index = 0; index < MAX_FRAME_NUMBER; index++) {
                String sourceFrame = "frame_" + index;
                int sourceId = context.getResources().getIdentifier(sourceFrame, "drawable", context.getPackageName());
                if (sourceId > 0) {
                    this.frames.add(sourceId);
                    sourceFrame = "frame_look_" + index;
                    sourceId = context.getResources().getIdentifier(sourceFrame, "drawable", context.getPackageName());
                    this.frameLooks.add(sourceId);
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

        public Object getLook(int position) {
            return this.frameLooks.get(position);
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