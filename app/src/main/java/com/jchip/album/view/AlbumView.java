package com.jchip.album.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

public class AlbumView extends AppCompatAutoCompleteTextView {
    public AlbumView(Context context) {
        super(context);
    }

    public AlbumView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlbumView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        super.setOnItemClickListener((adapterView, view, position, id) -> {
            TextView textView = (TextView) ((RelativeLayout) view).getChildAt(0);
            this.setText(textView.getText().toString());
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(adapterView, view, position, id);
            }
        });
    }

    // this is how to disable AutoCompleteTextView filter
    @Override
    protected void performFiltering(final CharSequence text, final int keyCode) {
        super.performFiltering("", keyCode);
    }

//    @Override
//    public boolean enoughToFilter() {
//        return true;
//    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        Log.d("", "onFocusChanged ============onFocusChanged===============");
        AlbumView.this.setCursorVisible(focused);
        if (!focused) {
            InputMethodManager manager = (InputMethodManager) AlbumView.this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(AlbumView.this.getWindowToken(), 0);
        }
    }

    @Override
    public void onEditorAction(int actionCode) {
        super.onEditorAction(actionCode);
        Log.d("", "onEditorAction actionId ====================================" + actionCode);
        if (actionCode == EditorInfo.IME_ACTION_DONE || actionCode == EditorInfo.IME_ACTION_SEARCH || actionCode == EditorInfo.IME_ACTION_GO || actionCode == EditorInfo.IME_ACTION_NEXT || actionCode == EditorInfo.IME_NULL) {
            this.clearFocus();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.showDropDown();
        Log.d("", "AlbumView.this.getListSelection()===" + AlbumView.this.getListSelection());
        return super.onTouchEvent(motionEvent);
    }

}
