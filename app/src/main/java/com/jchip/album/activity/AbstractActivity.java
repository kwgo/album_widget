package com.jchip.album.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import com.jchip.album.data.AlbumData;
import com.jchip.album.data.PhotoData;
import com.jchip.album.model.AlbumModel;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractActivity extends AppCompatActivity {
    public static final String ALBUM_MODEL = "albumModel";

    private AlbumModel albumModel;

    protected AlbumData album;
    protected PhotoData photo;

    protected List<AlbumData> albums = new ArrayList<>();
    protected List<PhotoData> photos = new ArrayList<>();

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
    }

    public void startActivity(Class<?> clazz) {
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