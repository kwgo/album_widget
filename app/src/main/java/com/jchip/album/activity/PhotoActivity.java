package com.jchip.album.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.jchip.album.R;
import com.jchip.album.common.AlbumHelper;
import com.jchip.album.data.AlbumData;
import com.jchip.album.data.AlbumDataHandler;
import com.jchip.album.data.PhotoData;
import com.rayzhang.android.rzalbum.RZAlbum;
import com.rayzhang.android.rzalbum.model.AlbumPhoto;

import java.util.ArrayList;
import java.util.List;


public class PhotoActivity extends AbstractActivity {


    private ImageView albumPhotoView;

    @Override
    public void initContentView() {
        super.initContentView();

        this.albumPhotoView = this.findViewById(R.id.album_photo);

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

        this.album = album;

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

    private void handleSelectedPhotos(List<AlbumPhoto> albumPhotos) {
        Log.d("", "albumPhotos size:::::::::::::::" + albumPhotos.size());
        Log.d("", "this.album================" + this.album);
        if (this.album != null) {
            if (this.album.getAlbumId() <= 0) {
                Log.d("", "add new  album:::::::::::::::");
                this.album = AlbumDataHandler.getInstance(this).createAlbum(this.album);
            }
            int albumId = this.album.getAlbumId();
            Log.d("", " new  album id :::::::::::::::" + albumId);
            if (albumId > 0) {
                Log.d("", " new  album created :::::::::::::::" + albumId);
                for (AlbumPhoto albumPhoto : albumPhotos) {
                    PhotoData photo = new PhotoData(albumId, albumPhoto.getPhotoPath());
                    Log.d("", " for loop photo ====");
                    if (!this.photos.contains(photo)) {
                        Log.d("", " save  photo ====" + photo.getPhotoPath());
                        this.photos.add(this.photo = AlbumDataHandler.getInstance(this).createPhoto(photo));
                        Log.d("", " saved  photo  id====" + photo.getPhotoId());
                    }
                }
                Log.d("", " set  photo  id====" + this.photo.getPhotoId());
                if (this.photo != null && !this.photos.isEmpty()) {
                    this.setAlbumPhoto(this.photo = this.photos.get(0));
                    Log.d("RZAlbum", "full view set  photo  id====" + photo.getPhotoId());
                }
            }
        }
    }
}