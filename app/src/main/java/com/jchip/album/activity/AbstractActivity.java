package com.jchip.album.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jchip.album.ActivityAlbum;
import com.jchip.album.ActivityFont;
import com.jchip.album.ActivityFrame;
import com.jchip.album.ActivityPhoto;
import com.jchip.album.model.AlbumModel;

public class AbstractActivity extends AppCompatActivity {
    public static final String ALBUM_MODEL = "albumModel";

    public static final String ALBUM_LAYER = "albumLayer";
    public static final String PHOTO_LAYER = "photoLayer";
    public static final String FRAME_LAYER = "frameLayer";
    public static final String FONT_LAYER = "fontLayer";

    private AlbumModel albumModel;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.albumModel = (AlbumModel) bundle.getSerializable(ALBUM_MODEL);
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


}