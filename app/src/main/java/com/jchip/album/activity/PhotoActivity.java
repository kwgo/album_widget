package com.jchip.album.activity;

import android.content.Intent;
import android.util.Log;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.jchip.album.R;
import com.lling.photopicker.PhotoPickerActivity;

public class PhotoActivity extends AbstractActivity {
    private static final int PICK_PHOTO = 1;

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


        int selectedMode = PhotoPickerActivity.MODE_MULTI;
        boolean showCamera = true;
        int maxNum = PhotoPickerActivity.DEFAULT_NUM;

        Intent intent = new Intent(this, PhotoPickerActivity.class);
        intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, showCamera);
        intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, selectedMode);
        intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, maxNum);
        startActivityForResult(intent, PICK_PHOTO);

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
}