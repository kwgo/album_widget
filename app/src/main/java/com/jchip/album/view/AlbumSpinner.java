package com.jchip.album.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.widget.AppCompatSpinner;
import com.jchip.album.R;

public class AlbumSpinner extends AppCompatSpinner {
    private Context context;
    private CharSequence[] items;

    public AlbumSpinner(Context context) {
        super(context);
    }

    public AlbumSpinner(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public AlbumSpinner(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
    }

    @Override
    public final void onFinishInflate() {
        super.onFinishInflate();

        this.context = this.getContext();
        this.items = this.getResources().getTextArray(R.array.album_list);

        this.setAdapter(new WordCardSpinnerAdapter());
        this.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (selectedListener != null) {
//                    selectedListener.onItemSelected(position);
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }

//    private OnItemSelectedListener selectedListener;
//
//    public void setItemSelectedListener(OnItemSelectedListener selectedListener) {
//        this.selectedListener = selectedListener;
//    }
//
//    public interface OnItemSelectedListener {
//        public void onItemSelected(int position);
//    }

    public class WordCardSpinnerAdapter extends ArrayAdapter<CharSequence> {
        public WordCardSpinnerAdapter() {
            super(context, R.layout.album_spinner_item, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(R.layout.album_spinner_view, parent, false);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.album_spinner_item, null);
            return view;
        }
    }
}
