package com.jchip.album.activity;

import android.os.Bundle;

import com.jchip.album.R;
import com.jchip.album.data.AlbumData;
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

        List<AlbumData> albums = this.albumDataHandler.queryAll();
        String defaultAlbumName = this.getString(R.string.default_album_name);
        AlbumData album = new AlbumData(defaultAlbumName + (albums.size() + 1));
        albums.add(0, album);

        AlbumViewAdapter viewAdapter = new AlbumViewAdapter(this, R.layout.album_spinner_item, albums);
        albumView.setAdapter(viewAdapter);

        //albumView.setListSelection(0);
        albumView.setText(defaultAlbumName, false);
        albumView.setOnItemClickListener((position) -> this.setAlbumPhotos(albums.get(position)));

        this.setAlbumPhotos(album);
    }

}
