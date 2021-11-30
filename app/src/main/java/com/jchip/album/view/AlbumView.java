package com.jchip.album.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

public class AlbumView extends AppCompatAutoCompleteTextView {
    public AlbumView(Context context) {
        super(context);
        this.initListeners();
    }

    public AlbumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initListeners();
    }

    public AlbumView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.initListeners();
    }

    private void initListeners() {
        this.setOnItemClickListener((adapterView, view, position, id) -> {
            TextView textView = (TextView) ((RelativeLayout) view).getChildAt(0);
            AlbumView.this.setText(textView.getText().toString());

            if (AlbumView.this.onItemClickListener != null) {
                AlbumView.this.onItemClickListener.onItemClick(position);
            }
        });
        this.setOnTouchListener((view, motionEvent) -> {
            AlbumView.this.showDropDown();
            Log.d("",""+ AlbumView.this.getListSelection());
            return false;
        });
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // this is how to disable AutoCompleteTextView filter
    @Override
    protected void performFiltering(final CharSequence text, final int keyCode) {
        super.performFiltering("", keyCode);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

//    @Override
//    protected void onFocusChanged(boolean focused, int direction,
//                                  Rect previouslyFocusedRect) {
//        super.onFocusChanged(focused, direction, previouslyFocusedRect);
//        Log.d("", "onFocusChanged ====================================");
//        if (focused && getAdapter() != null) {
//            Log.d("", "onFocusChanged ====focused=======focused===========focused=============");
//            performFiltering(getText(), 0);
//        }
//    }
}
