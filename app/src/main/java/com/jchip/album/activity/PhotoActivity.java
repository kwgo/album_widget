package com.jchip.album.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.util.Log;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.jchip.album.R;
import com.rayzhang.android.rzalbum.RZAlbum;
import com.rayzhang.android.rzalbum.model.AlbumPhoto;

import java.util.List;


public class PhotoActivity extends AbstractActivity {
    private static final int REQUEST_RZALBUM = 2;
    private static final int RZALBUM_REQUESTCODE = 1;


    @Override
    public void initContentView() {
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

    //    verifyStoragePermissions(this);
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
        RZAlbum.ofAppName("RZAlbum").start(this, REQUEST_RZALBUM);
        /**
         * Or Like this
         */
        RZAlbum.ofAppName("RZAlbum")
                .setLimitCount(2)
                .setSpanCount(3)
                .setStatusBarColor(Color.parseColor("#AD1457"))
                .setToolBarColor(Color.parseColor("#D81B60"))
                .setToolBarTitle("Album")
            //    .setPickColor(Color.argb(255, 153, 51, 255))
                //.setDialogIcon(R.drawable.ic_bird_shape_30_3dp)
                .setDialogIcon(R.drawable.album_button)
                //    .setPreviewOrientation(RZAlbum.ORIENTATION_PORTRATI)
                .setAllFolderName("Photos")
                .showCamera(false)
                .showGif(false)
                .start(this, REQUEST_RZALBUM);

//        int selectedMode = PhotoPickerActivity.MODE_MULTI;
//        boolean showCamera = true;
//        int maxNum = PhotoPickerActivity.DEFAULT_NUM;
//
//        Intent intent = new Intent(this, PhotoPickerActivity.class);
//        intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, showCamera);
//        intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, selectedMode);
//        intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, maxNum);
//        startActivityForResult(intent, PICK_PHOTO);

//        registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    Log.d("","result comming ...................");
//                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
//                        Intent data = result.getData();
//                        // ...
//                    }
//                }
//        ).launch(intent);

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case RZALBUM_REQUESTCODE:
//                    List<AlbumPhoto> paths = RZAlbum.parseResult(data);
//                    Log.d("RZAlbum", "Photos:" + paths);
//                    break;
//            }
//        }
//    }


    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}