package com.jchip.album.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import androidx.appcompat.widget.PopupMenu;

import com.jchip.album.R;
import com.jchip.album.data.AlbumData;
import com.jchip.album.data.AlbumDataHandler;
import com.jchip.album.view.AlbumView;
import com.jchip.album.view.AlbumViewAdapter;

public class AlbumActivity extends PhotoActivity {

    private ScaleType[] scaleTypies = new ScaleType[]{ScaleType.CENTER_CROP, ScaleType.FIT_CENTER, ScaleType.FIT_XY};
    private int scaleTypeIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.album_layer);
    }

    @Override
    public void initContentView() {
        super.initContentView();

        this.setLayer(LAYER_ALBUM);

        AlbumView albumView = (AlbumView) findViewById(R.id.album_name_text);

        this.albums = AlbumDataHandler.getInstance(this).queryAlbums();
        String albumName = this.getString(R.string.default_album_name) + (this.albums.size() + 1);
        AlbumData album = new AlbumData(albumName);
        this.albums.add(0, album);
        this.setAlbumPhotos(album);

        albumView.setAdapter(new AlbumViewAdapter(this, this.albums));
        albumView.setListSelection(0);
        albumView.setText(albumName, false);

        albumView.setOnFocusChangeListener((view, focused) -> {
            Log.d("", "focused focused focused focused focused");
            if (!focused && AlbumActivity.this.album != null) {
                String albumText = albumView.getText().toString();
                if (this.isEmpty(albumText)) {
                    albumView.setText(AlbumActivity.this.album.getAlbumName(), false);
                } else {
                    AlbumActivity.this.album.setAlbumName(albumText.trim());
                }
            }
        });

        albumView.setOnItemClickListener((adapterView, view, position, id) -> {
            albumView.clearFocus();

            Log.d("", "setOnItemClickListener  setOnItemClickListener setOnItemClickListener");

            this.setAlbumPhotos(AlbumActivity.this.album = AlbumActivity.this.albums.get(position));
            //AlbumActivity.this.album.setAlbumName(albumView.getText().toString());
            Log.d("", "select text ==== " + albumView.getText().toString());

        });

        findViewById(R.id.album_name_menu).setOnClickListener((v) -> this.showMenu(v));
        findViewById(R.id.photo_fit).setOnClickListener((v) -> this.fitPhoto(v));

    }

    public void fitPhoto(View v) {
        this.scaleTypeIndex = ++this.scaleTypeIndex % this.scaleTypies.length;
        ((ImageView) findViewById(R.id.photo_image)).setScaleType(this.scaleTypies[this.scaleTypeIndex]);
    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v, Gravity.END);
        //    popupWindow.showAsDropDown(View anchor, int xoff, int yoff, int gravity) l
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.album_name_menu, popup.getMenu());
        popup.show();
    }

    //    @Override
    //implements PopupMenu.OnMenuItemClickListener {
//    public boolean onMenuItemClick(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.archive:
//                archive(item);
//                return true;
//            case R.id.delete:
//                delete(item);
//                return true;
//            default:
//                return false;
//        }
//    }
    public void showMenu0(Activity context) {
        final ViewGroup root = (ViewGroup) context.findViewById(android.R.id.content);

        final View view = new View(context);
        view.setLayoutParams(new ViewGroup.LayoutParams(1, 1));
        view.setBackgroundColor(Color.TRANSPARENT);

        root.addView(view);
float x=0; float y=0;
        view.setX(x);
        view.setY(y);



        PopupMenu popupMenu = new PopupMenu(context, view, Gravity.CENTER);

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                root.removeView(view);
            }
        });

        popupMenu.show();
    }
}
