package com.jchip.album.activity;

import android.os.Bundle;
import android.util.Log;

import com.jchip.album.R;
import com.jchip.album.data.AlbumData;
import com.jchip.album.data.AlbumDataHandler;
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

        AlbumView albumView = (AlbumView) findViewById(R.id.album_name);

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
                if (albumText != null && !albumText.trim().isEmpty()) {
                    AlbumActivity.this.album.setAlbumName(albumText.trim());
                } else {
                    albumView.setText(AlbumActivity.this.album.getAlbumName(), false);
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
    }
}
