package com.jchip.album.photo.adapter;

import android.content.Context;
import android.os.Build;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jchip.album.photo.adapter.base.BaseViewHolder;
import com.jchip.album.photo.adapter.factory.IItemType;
import com.jchip.album.photo.adapter.factory.ItemTypeFactory;
import com.jchip.album.photo.adapter.listener.OnMultiItemClickListener;
import com.jchip.album.photo.adapter.listener.OnMultiItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

public class MultiAdapter<T extends IItemType> extends RecyclerView.Adapter<BaseViewHolder<T>> {
    private Context context;
    private List<T> data;
    private ItemTypeFactory itemTypeFactory;

    private static final int BASE_ITEM_TYPE_HEADER = 800000;
    private static final int BASE_ITEM_TYPE_FOOTER = 900000;
    private SparseArray<View> headerViews;
    private SparseArray<View> footerViews;

    private OnMultiItemClickListener clickListener;
    private OnMultiItemLongClickListener longClickListener;

    public MultiAdapter(@Nullable List<T> list) {
        data = new ArrayList<>();
        if (list != null) data.addAll(list);
        itemTypeFactory = ItemTypeFactory.instance(0);
        headerViews = new SparseArray<>();
        footerViews = new SparseArray<>();
    }

    public MultiAdapter(@Nullable List<T> list, int wh) {
        data = new ArrayList<>();
        if (list != null) data.addAll(list);
        itemTypeFactory = ItemTypeFactory.instance(wh);
        headerViews = new SparseArray<>();
        footerViews = new SparseArray<>();
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public BaseViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (headerViews.get(viewType) != null) {
            return new HeaderViewHolder(headerViews.get(viewType));
        } else if (footerViews.get(viewType) != null) {
            return new FooterViewHolder(footerViews.get(viewType));
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return itemTypeFactory.createViewHolder(viewType, view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BaseViewHolder<T> holder, int position) {
        if (isHeaderViewPosition(position) || isFooterViewPosition(position)) return;

        final int finalPosition = position - getHeadersCount();
        holder.bindViewData(context, data.get(finalPosition), finalPosition);
        if (clickListener != null) {
            for (int i = 0; i < holder.getClickViews().length; i++) {
                final int viewPosition = i;
                holder.getClickViews()[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickListener.onItemClick(holder, v, viewPosition, finalPosition);
                    }
                });
            }
        }
        if (longClickListener != null) {
            for (int i = 0; i < holder.getLongClickViews().length; i++) {
                final int viewPosition = i;
                holder.getLongClickViews()[i].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        longClickListener.onItemLongClick(holder, v, viewPosition, finalPosition);
                        return true;
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + data.size() + getFootersCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPosition(position)) {
            return headerViews.keyAt(position);
        } else if (isFooterViewPosition(position)) {
            return footerViews.keyAt(position - getHeadersCount() - data.size());
        }
        return data.get(position - getHeadersCount()).itemLayout();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if (headerViews.get(viewType) != null || footerViews.get(viewType) != null) {
                        return ((GridLayoutManager) layoutManager).getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    private class HeaderViewHolder extends BaseViewHolder {

        HeaderViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public View[] getClickViews() {
            return new View[0];
        }

        @Override
        public View[] getLongClickViews() {
            return new View[0];
        }

        @Override
        public void bindViewData(Context context, IItemType data, int itemPosition) {

        }
    }

    private class FooterViewHolder extends BaseViewHolder {

        FooterViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public View[] getClickViews() {
            return new View[0];
        }

        @Override
        public View[] getLongClickViews() {
            return new View[0];
        }

        @Override
        public void bindViewData(Context context, IItemType data, int itemPosition) {

        }
    }

    private int getHeadersCount() {
        return headerViews.size();
    }

    private int getFootersCount() {
        return footerViews.size();
    }

    private boolean isHeaderViewPosition(int position) {
        return position < getHeadersCount();
    }

    private boolean isFooterViewPosition(int position) {
        return position >= getHeadersCount() + data.size();
    }

    public void addHeaderView(View view) {
        headerViews.put(headerViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    public void addFooterView(View view) {
        footerViews.put(footerViews.size() + BASE_ITEM_TYPE_FOOTER, view);
    }

    public void addData(T t) {
        int finalIndex = getHeadersCount() + data.size() - 1;
        addData(finalIndex, t);
    }

    public void addData(int index, T t) {
        int finalIndex = getHeadersCount() + index - 1;
        data.add(finalIndex, t);
        notifyItemInserted(finalIndex);
    }

    public void addDatas(@NonNull List<T> list) {
        int finalIndex = getHeadersCount() + data.size() - 1;
        addDatas(finalIndex, list);

    }

    public void addDatas(int index, @NonNull List<T> list) {
        int finalIndex = getHeadersCount() + index - 1;
        data.addAll(finalIndex, list);
        notifyItemInserted(finalIndex);
    }

    public void removeData(int index) {
        int finalIndex = getHeadersCount() + index - 1;
        data.remove(finalIndex);
        notifyItemRemoved(finalIndex);
    }

    public void clear() {
        data.clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            headerViews.removeAtRange(0, headerViews.size());
            footerViews.removeAtRange(0, footerViews.size());
        } else {
            for (int i = 0; i < headerViews.size(); i++) {
                headerViews.removeAt(i);
            }
            for (int i = 0; i < footerViews.size(); i++) {
                footerViews.removeAt(i);
            }
        }
        notifyDataSetChanged();
    }

    public void resetData(@NonNull List<T> list) {
        if (data.size() > 0) data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return data;
    }

    public void setOnItemClickListener(OnMultiItemClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnItemLongClickListener(OnMultiItemLongClickListener listener) {
        this.longClickListener = listener;
    }
}
