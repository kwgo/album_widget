package com.jchip.album.activity;

import android.os.Bundle;

import com.jchip.album.R;
import com.jchip.album.data.AlbumData;
import com.jchip.album.view.AlbumView;
import com.jchip.album.view.AlbumViewAdapter;


public class AlbumActivity extends PhotoActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.album_layer);
    }

    @Override
    public void initContentView() {
        super.initContentView();


//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
//        AutoCompleteTextView textView = (AutoCompleteTextView)
//                findViewById(R.id.album_name);
//        textView.setAdapter(adapter);


        // autocompletetextview is in activity_main.xml
        AlbumView albumView = (AlbumView) findViewById(R.id.album_name);

//        albumView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
//                TextView textView = (TextView) ((RelativeLayout) view).getChildAt(0);
//                albumView.setText(textView.getText().toString());
//            }
//        });
//
//        albumView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                albumView.showDropDown();
//                return false;
//            }
//        });

        // add the listener so it will tries to suggest while the user types
        // albumView.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this));

        // ObjectItemData has no value at first
        //   MyObject[] ObjectItemData = new MyObject[]{new MyObject("aaa"), new MyObject("bbb"), new MyObject("vvv")};
        AlbumData[] albums = new AlbumData[]{new AlbumData("aaa"), new AlbumData("bbb"), new AlbumData("vvv")};

        // set the custom ArrayAdapter
        AlbumViewAdapter viewAdapter = new AlbumViewAdapter(this, R.layout.album_spinner_item, albums);
        albumView.setAdapter(viewAdapter);
    }

}
