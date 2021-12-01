package com.jchip.album.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.jchip.album.R;
import com.jchip.album.common.AlbumHelper;
import com.jchip.album.common.GestureHelper;
import com.jchip.album.data.AlbumData;
import com.jchip.album.data.AlbumDataHandler;
import com.jchip.album.data.PhotoData;
import com.rayzhang.android.rzalbum.RZAlbum;
import com.rayzhang.android.rzalbum.model.AlbumPhoto;

import java.util.ArrayList;
import java.util.List;


public class PhotoActivity extends LayerActivity {


    private ImageView albumPhotoView;

    @Override
    public void initContentView() {
        super.initContentView();

        this.albumPhotoView = this.findViewById(R.id.album_photo);

        GestureHelper.setViewGesture(this.albumPhotoView, () -> slipPhoto(+1), () -> slipPhoto(-1));

        this.findViewById(R.id.album_photo_add).setOnClickListener((e) -> onSelectPhotos());
    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layer_photo);
////        if (this.isLandscape()) {
////            View view = this.findViewById(R.id.album_photo_view);
////            view.setRotation(90);
////        }
//    }


    private void onSelectPhotos() {
        Log.d("", "onSelectPhotos ==============================");

        AlbumHelper.selectPhotos(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.d("", "requestCode:::::::::::::::" + requestCode);
            switch (requestCode) {
                case AlbumHelper.ALBUM_REQUEST_CODE:
                    List<AlbumPhoto> albumPhotos = RZAlbum.parseResult(data);
                    Log.d("", "albumPhotos size:::::::::::::::" + albumPhotos.size());
                    if (albumPhotos != null && !albumPhotos.isEmpty()) {
                        this.handleSelectedPhotos(albumPhotos);
                    }
                    break;
            }
        }
    }

    public void setAlbumPhotos(AlbumData album) {
        Log.d("AlbumData", "AlbumData====" + album);
        if (album.getAlbumId() > 0) {
            this.photos = AlbumDataHandler.getInstance(this).queryPhotos(album.getAlbumId());
            this.setAlbumPhoto(this.photos.isEmpty() ? null : this.photos.get(0));
        } else {
            this.photos = new ArrayList<>();
            this.setAlbumPhoto(null);
        }
    }

    public void setAlbumPhoto(PhotoData photo) {
        this.photo = photo;
        if (photo != null) {
            Bitmap bitmap = AlbumHelper.loadBitmap(photo.getPhotoPath());
            this.albumPhotoView.setImageBitmap(bitmap);
        } else {
            this.albumPhotoView.setImageBitmap(null);
        }
    }

    protected boolean slipPhoto(int offset) {
        if (this.photo != null) {
            int postion = this.photos.indexOf(this.photo);
            postion += offset;
            if (postion >= 0 && postion < this.photos.size()) {
                this.setAlbumPhoto(this.photos.get(postion));
                return true;
            }
        }
        return false;
    }

    private void handleSelectedPhotos(List<AlbumPhoto> albumPhotos) {
        if (this.album != null) {
            if (this.album.getAlbumId() <= 0) {
                this.album = AlbumDataHandler.getInstance(this).createAlbum(this.album);
            }
            int albumId = this.album.getAlbumId();
            Log.d("", " new  album id :::::::::::::::" + albumId);
            if (albumId > 0) {
                for (AlbumPhoto albumPhoto : albumPhotos) {
                    PhotoData photo = new PhotoData(albumId, albumPhoto.getPhotoPath());
                    if (!this.photos.contains(photo)) {
                        this.photos.add(photo = AlbumDataHandler.getInstance(this).createPhoto(photo));
                        Log.d("", " saved  photo  id====" + photo.getPhotoId());
                        if (this.photo == null) {
                            this.setAlbumPhoto(this.photo = photo);
                        }
                    }
                }
            }
        }
    }
}