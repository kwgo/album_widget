package com.jchip.album.activity;

import android.os.Bundle;
import android.util.Log;

import com.jchip.album.R;
import com.jchip.album.data.AlbumData;
import com.jchip.album.data.AlbumDataHandler;
import com.jchip.album.view.AlbumView;
import com.jchip.album.view.AlbumViewAdapter;

import java.util.List;


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

        List<AlbumData> albums = AlbumDataHandler.getInstance(this).queryAlbums();
        String albumName = this.getString(R.string.default_album_name) + (albums.size() + 1);
        AlbumData album = new AlbumData(albumName);
        albums.add(0, album);
        this.setAlbumPhotos(album);

        albumView.setAdapter(new AlbumViewAdapter(this, albums));
        albumView.setListSelection(0);
        albumView.setText(albumName, false);
        albumView.setOnItemClickListener((position) -> {
            AlbumData albumData = albums.get(position);
            albumData.setAlbumName(albumView.getText().toString());
            Log.d("", "select text ==== " + albumView.getText().toString());
            this.setAlbumPhotos(albumData);
        });
    }

}
