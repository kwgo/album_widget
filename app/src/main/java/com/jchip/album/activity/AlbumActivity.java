package com.jchip.album.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;

import com.jchip.album.R;
import com.jchip.album.data.AlbumData;
import com.jchip.album.data.AlbumDataHandler;
import com.jchip.album.view.AlbumView;
import com.jchip.album.view.AlbumViewAdapter;

public class AlbumActivity extends PhotoActivity {
    private AlbumView albumView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.album_layer);
    }

    @Override
    public void initContentView() {
        super.initContentView();

        this.setLayer(LAYER_ALBUM);

        this.albumView = (AlbumView) findViewById(R.id.album_name_text);

        this.albums = AlbumDataHandler.getInstance(this).queryAlbums();
        String albumName = this.getString(R.string.default_album_name) + (this.albums.size() + 1);
        AlbumData album = new AlbumData(albumName);
        this.albums.add(0, album);
        this.setAlbumPhotos(album);

        this.albumView.setAdapter(new AlbumViewAdapter(this, this.albums));
        this.albumView.setListSelection(0);
        this.albumView.setText(albumName, false);
        this.albumView.setEnabled(false);

        this.albumView.setOnFocusChangeListener((view, focused) -> {
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

        this.albumView.setOnItemClickListener((adapterView, view, position, id) -> {
            this.albumView.clearFocus();

            Log.d("", "setOnItemClickListener  setOnItemClickListener setOnItemClickListener");

            this.setAlbumPhotos(AlbumActivity.this.album = AlbumActivity.this.albums.get(position));
            //AlbumActivity.this.album.setAlbumName(albumView.getText().toString());
            Log.d("", "select text ==== " + albumView.getText().toString());

        });

        findViewById(R.id.album_name_menu).setOnClickListener((v) -> this.showMenu(v));

    }

    public void showMenu(View view) {
        Context wrapper = new ContextThemeWrapper(this, R.style.album_name_menu);
        PopupMenu popupMenu = new PopupMenu(wrapper, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.album_name_edit) {
                    albumView.setEnabled(true);
                } else if (item.getItemId() == R.id.album_name_delete) {
                    alertDeletion(() -> onDeleteAlbum());
                }
                return true;
            }
        });
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.album_name_menu, popupMenu.getMenu());
        popupMenu.show();
    }

    private void onDeleteAlbum() {

    }
}
