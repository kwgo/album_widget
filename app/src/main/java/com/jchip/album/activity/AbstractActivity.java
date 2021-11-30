package com.jchip.album.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import com.jchip.album.ActivityAlbum;
import com.jchip.album.ActivityFont;
import com.jchip.album.ActivityFrame;
import com.jchip.album.ActivityPhoto;
import com.jchip.album.data.AlbumData;
import com.jchip.album.data.AlbumDataHandler;
import com.jchip.album.data.PhotoData;
import com.jchip.album.data.PhotoDataHandler;
import com.jchip.album.model.AlbumModel;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractActivity extends AppCompatActivity {
    public static final String ALBUM_MODEL = "albumModel";

    public static final String ALBUM_LAYER = "albumLayer";
    public static final String PHOTO_LAYER = "photoLayer";
    public static final String FRAME_LAYER = "frameLayer";
    public static final String FONT_LAYER = "fontLayer";

    private AlbumModel albumModel;

    protected AlbumDataHandler albumDataHandler;
    protected PhotoDataHandler photoDataHandler;

    protected AlbumData album;
    protected PhotoData photo;
    protected List<PhotoData> photos =  new ArrayList<>();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.albumModel = (AlbumModel) bundle.getSerializable(ALBUM_MODEL);
        } else {
            this.albumModel = new AlbumModel();
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        this.initContentView();
    }

    public void initContentView() {
        if (this.albumDataHandler == null) {
            this.albumDataHandler = new AlbumDataHandler(this);
        }

        if (this.photoDataHandler == null) {
            this.photoDataHandler = new PhotoDataHandler(this);
        }
    }

    public void openLayer(String layer) {
        switch (layer) {
            case ALBUM_LAYER:
                this.startActivity(ActivityAlbum.class);
                break;
            case PHOTO_LAYER:
                this.startActivity(ActivityPhoto.class);
                break;
            case FRAME_LAYER:
                this.startActivity(ActivityFrame.class);
                break;
            case FONT_LAYER:
                this.startActivity(ActivityFont.class);
                break;

        }
    }

    private void startActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra(ALBUM_MODEL, this.albumModel);
        this.startActivity(intent);
    }

    public boolean isPortrait() {
        int orientation = this.getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public boolean isLandscape() {
        return !this.isPortrait();
    }


}