package com.jchip.album.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

public class AlbumNameView extends AppCompatAutoCompleteTextView {
    public AlbumNameView(Context context) {
        super(context);
    }

    public AlbumNameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlbumNameView(Context context, AttributeSet attrs, int defStyle) {
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
            this.clearFocus();
        });
    }

    @Override
    protected void performFiltering(final CharSequence text, final int keyCode) {
    }

//    @Override
//    public boolean enoughToFilter() {
//        return true;
//    }

    public void addTextChangedListener(TextChangedListener textChangedListener) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                if (isEnabled() && textChangedListener != null) {
                    textChangedListener.textChanged(text.toString().trim());
                }
            }
        };
        super.addTextChangedListener(textWatcher);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        this.setCursorVisible(focused);
        this.setSelection(this.getText().length());
        if (!focused) {
            this.setEnabled(false);
            InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(AlbumNameView.this.getWindowToken(), 0);
        } else {
            InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onEditorAction(int actionCode) {
        super.onEditorAction(actionCode);
        if (actionCode == EditorInfo.IME_ACTION_DONE || actionCode == EditorInfo.IME_ACTION_SEARCH || actionCode == EditorInfo.IME_ACTION_GO || actionCode == EditorInfo.IME_ACTION_NEXT || actionCode == EditorInfo.IME_NULL) {
            this.clearFocus();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.editable) {
            this.showDropDown();
        }
        return super.onTouchEvent(motionEvent);
    }

    private boolean editable = true;

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        new Handler().postDelayed(() -> {
            this.editable = enabled;
            if (enabled) {
                requestFocus();
            }
            this.setCursorVisible(enabled);
        }, 100);
    }

    public interface TextChangedListener {
        public void textChanged(String text);
    }

}
