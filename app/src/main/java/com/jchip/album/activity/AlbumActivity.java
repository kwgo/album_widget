package com.jchip.album.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jchip.album.R;
import com.jchip.album.data.MyObject;
import com.jchip.album.view.AutocompleteCustomArrayAdapter;
import com.jchip.album.view.CustomAutoCompleteView;

public class AlbumActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_layer);

//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
//        AutoCompleteTextView textView = (AutoCompleteTextView)
//                findViewById(R.id.album_name);
//        textView.setAdapter(adapter);


        // autocompletetextview is in activity_main.xml
        CustomAutoCompleteView myAutoComplete = (CustomAutoCompleteView) findViewById(R.id.album_name);

        myAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {

                RelativeLayout rl = (RelativeLayout) arg1;
                TextView tv = (TextView) rl.getChildAt(0);
                myAutoComplete.setText(tv.getText().toString());

            }

        });

        myAutoComplete.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                myAutoComplete.showDropDown();
                return false;
            }
        });

        // add the listener so it will tries to suggest while the user types
       // myAutoComplete.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this));

        // ObjectItemData has no value at first
        MyObject[] ObjectItemData = new MyObject[] { new MyObject("aaa"),  new MyObject("bbb"), new MyObject("vvv")   };

        // set the custom ArrayAdapter
        AutocompleteCustomArrayAdapter myAdapter = new AutocompleteCustomArrayAdapter(this, R.layout.list_view_row, ObjectItemData);
        myAutoComplete.setAdapter(myAdapter);
    }

    private static final String[] COUNTRIES = new String[] {
            "Belgium", "France", "Italy", "Germany", "Spain"
    };
}
