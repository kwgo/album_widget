package com.jchip.album.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jchip.album.R;
import com.jchip.album.common.PhotoHelper;
import com.jchip.album.photo.adapter.itemdecoration.RecycleItemDecoration;

public abstract class RecyclerActivity extends AbstractActivity {
    private final static int COLUMN_NUMBER = 1;

    protected RecyclerView recyclerView;

    public void initRecyclerView(int recyclerId, int itemId, int itemCount) {
        this.recyclerView = (RecyclerView) this.findViewById(recyclerId);
        GridLayoutManager layoutManager = new GridLayoutManager(this, COLUMN_NUMBER, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);
        int color = this.getResources().getColor(R.color.material_blue_grey_800);
        recyclerView.addItemDecoration(new RecycleItemDecoration(PhotoHelper.dpToPx(1), color));
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
            holder.bind(position);
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

            public void bind(int position) {
                bindItemView(this.itemView, position);
            }

            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
            }
        }
    }

    protected abstract void bindItemView(View itemView, int position);
}
