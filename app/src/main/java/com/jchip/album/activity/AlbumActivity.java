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

        this.albums = this.queryAlbums();
        this.reloadAlbumList();
        this.setAlbumPhotos(albums.get(0));

        this.albumView = (AlbumView) findViewById(R.id.album_name_text);
        this.albumView.setAdapter(new AlbumViewAdapter(this, this.albums));
        this.albumView.setEnabled(false);
        this.albumView.setText(this.album.getAlbumName(), false);
        this.albumView.addTextChangedListener(text -> this.onAlbumNameChanged(text));
        this.albumView.setOnItemClickListener((adapterView, view, position, id) -> this.onSelectAlbum(position));

    }

    protected void reloadAlbumList() {
        boolean allSaved = true;
        Log.d("","this.albums size=" +this.albums.size());
        for (AlbumData album : this.albums) {
            Log.d("","album.isSaved()=" +album.isSaved());
            if (!album.isSaved()) {
                allSaved = false;
                break;
            }
        }
        Log.d("","allSaved=" +allSaved);
        if (allSaved) {
            String albumName = this.getString(R.string.default_album_name);
            this.albums.add(0, new AlbumData(albumName + (this.albums.size() + 1)));
        }
    }

    public void showMenu(View view) {
        Context contextThemeWrapper = new ContextThemeWrapper(this, R.style.album_name_menu);
        PopupMenu popupMenu = new PopupMenu(contextThemeWrapper, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.album_name_edit) {
                    albumView.setEnabled(true);
                } else if (item.getItemId() == R.id.album_name_delete) {
                    onDeleteAlbum();
                }
                return true;
            }
        });
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.album_name_menu, popupMenu.getMenu());
        popupMenu.show();
    }

    protected void onAlbumNameChanged(String text) {
        this.album.setAlbumName(text.trim());
        this.updateAlbum();
    }

    private void onSelectAlbum(int position) {
        this.setAlbumPhotos(albums.get(position));
    }

    private void onDeleteAlbum() {
        this.alert(R.string.album_title, R.string.album_alert_delete, () -> {
            if (this.album.isSaved()) {
                this.albums.remove(this.album);
                this.deleteAlbum();
            }
            this.reloadAlbumList();
            this.setAlbumPhotos(albums.get(0));
        });
    }
}
