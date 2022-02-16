package com.jchip.album.layer;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;

import com.jchip.album.ActivityAbout;
import com.jchip.album.ActivitySlideshow;
import com.jchip.album.R;
import com.jchip.album.data.AlbumData;
import com.jchip.album.view.AlbumNameAdapter;
import com.jchip.album.view.AlbumNameView;
import com.jchip.album.view.AlbumView;
import com.jchip.album.view.PhotoViewConfig;

import java.util.List;

public class AlbumLayer extends PhotoLayer {
    private AlbumNameView albumNameView;
    private List<AlbumView> albums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(PhotoViewConfig.LAYER_ALBUM_PHOTO, R.layout.album_photo_layer);
    }

    @Override
    public void initContentView() {
        super.initContentView();

        this.settingData = this.querySetting();
        if (!this.settingData.isSaved()) {
            int backgroundColor = this.getResources().getColor(R.color.background);
            this.settingData.setBackgroundColor(backgroundColor);
            this.settingData.setSlideSpeed(PhotoViewConfig.DEFAULT_SLIDESHOW_SPEED);
            this.settingData.setFrameIndex(PhotoViewConfig.DEFAULT_FRAME_ID);
            this.saveSetting();
        } else {
            this.getView(R.id.album_photo_view).setBackgroundColor(this.settingData.getBackgroundColor());
        }

        this.setAlbumPhotos(new AlbumView(this, new AlbumData(), this.layer));

        this.getView(R.id.album_name_menu).setOnClickListener(this::showMenu);
    }

    @Override
    protected void postContentView() {
        this.albums = this.queryAlbums();
        this.setAlbumPhotos(this.reloadAlbumList());

        this.albumNameView = findViewById(R.id.album_name_text);
        this.albumNameView.setAdapter(new AlbumNameAdapter(this, this.albums));
        this.albumNameView.setEnabled(false);
        this.albumNameView.setOnItemClickListener((adapterView, view, position, id) -> this.onSelectAlbum(position));
        this.albumNameView.setText(this.album.getAlbumName(), false);
        this.albumNameView.addTextChangedListener(this::onAlbumNameChanged);
    }

    protected AlbumView reloadAlbumList() {
        boolean allSaved = true;
        for (AlbumView album : this.albums) {
            if (!album.isSaved()) {
                allSaved = false;
                break;
            }
        }
        if (allSaved) {
            String albumNamePrefix = this.getString(R.string.album_default_name);
            int number = this.albums.size() > 0 ? this.albums.get(0).getAlbumId() + 1 : 1;
            AlbumView albumView = new AlbumView(this, this.layer);
            albumView.setAlbumName(albumNamePrefix + number);
            this.albums.add(0, albumView);
        }
        return this.albums.size() > 0 ? this.albums.get(0) : new AlbumView(this, this.layer);
    }

    public void showMenu(View view) {
        Context contextThemeWrapper = new ContextThemeWrapper(this, R.style.album_name_menu);
        PopupMenu popupMenu = new PopupMenu(contextThemeWrapper, view);
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.album_name_edit) {
                albumNameView.setEnabled(true);
            } else if (item.getItemId() == R.id.album_name_delete) {
                this.onDeleteAlbum();
            } else if (item.getItemId() == R.id.album_slideshow) {
                this.onSlideshow();
            } else if (item.getItemId() == R.id.album_about) {
                this.onAlbumAbout();
            }
            return true;
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

    private void removeAlbum() {
        if (this.album.isSaved()) {
            this.albums.remove(this.album);
            this.deleteAlbum();
        }

        AlbumView albumView = this.reloadAlbumList();
        this.setAlbumPhotos(albumView);
        this.albumNameView.setText(albumView.getAlbumName());
    }

    private void onDeleteAlbum() {
        if (this.album.isSaved()) {
            this.alert(R.string.album_title, R.string.album_alert_delete, () -> {
                if (this.existAlbumWidget()) {
                    this.alert(R.string.album_title, R.string.album_alert_link, this::removeAlbum);
                } else {
                    removeAlbum();
                }
            });
        }
    }

    private void onSlideshow() {
        this.startActivity(ActivitySlideshow.class, null);
    }

    private void onAlbumAbout() {
        this.startActivity(ActivityAbout.class);
    }
}
