package com.jchip.album.layer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;

public abstract class ListLayer extends DataLayer {

    protected ListView listView;
    private final Map<Integer, View> itemViews = new HashMap<>();

    public void initListView(int listId, int itemId, int itemCount) {
        this.listView = this.findViewById(listId);
        this.listView.setAdapter(new ListViewAdapter(this, itemId, itemCount));
    }

    public class ListViewAdapter extends BaseAdapter {
        protected Context context;
        protected int viewId;
        protected int itemCount;

        private final LayoutInflater inflater;

        public ListViewAdapter(Context context, int viewId, int itemCount) {
            this.context = context;
            this.viewId = viewId;
            this.itemCount = itemCount;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return this.itemCount;
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
            View itemView = itemViews.get(position);
            if (itemView == null) {
                itemView = inflater.inflate(this.viewId, null);
                bindItemView(itemView, position);
                itemViews.put(position, itemView);
            }
            return itemView;
        }
    }

    protected abstract void bindItemView(View itemView, final int position);
}
