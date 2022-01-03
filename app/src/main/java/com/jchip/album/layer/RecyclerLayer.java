package com.jchip.album.layer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jchip.album.R;

import java.util.HashMap;
import java.util.Map;

public abstract class RecyclerLayer extends DataLayer {
    private final static int COLUMN_NUMBER = 1;

    protected RecyclerView recyclerView;

    private Map<Integer, View> itemViews = new HashMap<>();

    public void initRecyclerView(int recyclerId, int itemId, int itemCount) {
        this.initRecyclerView(recyclerId, itemId, itemCount, COLUMN_NUMBER);
    }

    public void initRecyclerView(int recyclerId, int itemId, int itemCount, int spanCount) {
        this.recyclerView = (RecyclerView) this.findViewById(recyclerId);
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount, GridLayoutManager.VERTICAL, false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return RecyclerLayer.this.getSpanSize(position);
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(this.getDrawable(R.drawable.album_view_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        RecyclerViewAdapter viewAdapter = new RecyclerViewAdapter(this, itemId, itemCount);
        recyclerView.setAdapter(viewAdapter);
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        protected Context context;
        protected int viewId;
        protected int itemCount;

        public RecyclerViewAdapter(Context context, int viewId, int itemCount) {
            this.context = context;
            this.viewId = viewId;
            this.itemCount = itemCount;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(this.viewId, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            View itemView = itemViews.get(position);
            if (itemView == null) {
                holder.bindView(position);
                itemViews.put(position, holder.getItemView());
            }
        }

        @Override
        public int getItemCount() {
            return this.itemCount;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private View itemView;

            public void bindView(int position) {
                bindItemView(this.itemView, position);
            }

            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
            }

            public View getItemView() {
                return this.itemView;
            }
        }
    }

    protected abstract void bindItemView(View itemView, final int position);

    protected int getSpanSize(final int position) {
        return 1;
    }
}
