package com.jchip.album.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;

import com.jchip.album.R;
import com.jchip.album.common.AlbumHelper;
import com.jchip.album.data.AlbumData;
import com.jchip.album.data.PhotoData;
import com.rayzhang.android.rzalbum.RZAlbum;
import com.rayzhang.android.rzalbum.model.AlbumPhoto;

import java.util.ArrayList;
import java.util.List;


public class PhotoActivity extends AbstractActivity {
    private static final int ALBUM_REQUEST_CODE = 1;

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
        /**
         * @param ofAppName             : (required)
         * @param setLimitCount         : (choose)   (default:5)
         * @param setSpanCount          : (choose)   (default:3)
         * @param setStatusBarColor     : (choose)   (default:#ff673ab7)
         * @param setToolBarColor       : (choose)   (default:#ff673ab7)
         * @param setToolBarTitle       : (choose)   (default:RZAlbum)
         * @param setPickColor          : (choose)   (default:#ffffc107)
         * @param setPreviewOrientation : (choose)   (default:ORIENTATION_AUTO)
         * @param setAllFolderName      : (choose)   (default:All Photos)
         * @param setDialogIcon         : (choose)   (default:none)
         * @param showCamera            : (choose)   (default:true)
         * @param showGif               : (choose)   (default:true)
         * @param start                 : (required)
         */
        RZAlbum.ofAppName("RZ - Album")
                .setLimitCount(12)
                .setSpanCount(3)
                .setStatusBarColor(Color.parseColor("#AD1457"))
                .setToolBarColor(Color.parseColor("#D81B60"))
                .setToolBarTitle("Album")
                .setPickerColor(0x000000)
                //.setPickerColor(18);
                //.setPickColor(Color.argb(255, 153, 51, 255))
                //.setDialogIcon(R.drawable.ic_bird_shape_30_3dp)
                .setDialogIcon(R.drawable.album_button)

                //    .setPreviewOrientation(RZAlbum.ORIENTATION_PORTRATI)
                .setAllFolderName("Photos")
                .showCamera(true)
                .showGif(false)
                .start(this, ALBUM_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.d("RZAlbum", "requestCode:::::::::::::::" + requestCode);
            switch (requestCode) {
                case ALBUM_REQUEST_CODE:
                    List<AlbumPhoto> albumPhotos = RZAlbum.parseResult(data);
                    Log.d("RZAlbum", "albumPhotos size:::::::::::::::" + albumPhotos.size());
                    if (albumPhotos != null && !albumPhotos.isEmpty()) {
                        if (this.album != null) {
                            if (this.album.getAlbumId() <= 0) {
                                Log.d("RZAlbum", "add new  album:::::::::::::::"  );
                                this.album = this.albumDataHandler.insert(this.album);
                            }
                            int albumId = this.album.getAlbumId();
                            Log.d("RZAlbum", " new  album id :::::::::::::::" +albumId );
                            if (albumId > 0) {
                                Log.d("RZAlbum", " new  album created :::::::::::::::" +albumId );
                                for (AlbumPhoto albumPhoto : albumPhotos) {
                                    PhotoData photo = new PhotoData(albumId, albumPhoto.getPhotoPath());
                                    Log.d("RZAlbum", " for loop photo ====" );
                                    if (!this.photos.contains(photo)) {
                                        Log.d("RZAlbum", " save  photo ====" + photo.getPhotoPath());
                                        photo = this.photoDataHandler.insert(photo);
                                        Log.d("RZAlbum", " saved  photo  id====" + photo.getPhotoId());
                                        this.photos.add(photo);
                                    }
                                }
                                if (this.photo == null && !this.photos.isEmpty()) {
                                    this.photo = this.photos.get(0);
                                    this.setAlbumPhoto(this.photo);
                                }
                            }
                        }
                    }
                    break;
            }
        }
    }

    public void setAlbumPhotos(AlbumData album) {
        Log.d("AlbumData", "AlbumData====" + album);

        this.album = album;

        if (album.getAlbumId() > 0) {
            this.photos = this.photoDataHandler.queryAll(album.getAlbumId());
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
}