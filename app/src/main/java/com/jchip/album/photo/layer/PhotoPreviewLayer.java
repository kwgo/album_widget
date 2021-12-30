package com.jchip.album.photo.layer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.jchip.album.R;
import com.jchip.album.common.AlbumHelper;
import com.jchip.album.photo.common.PhotoConfig;
import com.jchip.album.photo.model.PhotoModel;
import com.jchip.album.photo.view.PhotoNumberView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Photo preview activity
 */

public class PhotoPreviewLayer extends AppCompatActivity implements PhotoFragPreview.OnNumberViewClickListener {
    private TextView indexText;
    private PhotoNumberView photoNumberView;

    private ArrayList<PhotoModel> allPhotos;
    private ArrayList<PhotoModel> selectedPhotos, deletePhotos;
    private int pickColor = PhotoConfig.DEFAULT_PICK_COLOR;
    private int limitCount = PhotoConfig.DEFAULT_LIMIT_COUNT;
    private int currentItem = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_preview_layer);
        AlbumHelper.setStatusBarColor(this, android.R.color.transparent);

        allPhotos = getIntent().getParcelableArrayListExtra(PhotoConfig.PREVIEW_ALL_PHOTOS);
        selectedPhotos = getIntent().getParcelableArrayListExtra(PhotoConfig.PREVIEW_ADD_PHOTOS);
        deletePhotos = new ArrayList<>();

        currentItem = getIntent().getIntExtra(PhotoConfig.PREVIEW_ITEM_POSITION, 0);
        pickColor = getIntent().getIntExtra(PhotoConfig.PREVIEW_PICK_COLOR, PhotoConfig.DEFAULT_PICK_COLOR);
        limitCount = getIntent().getIntExtra(PhotoConfig.PREVIEW_LIMIT_COUNT, PhotoConfig.DEFAULT_LIMIT_COUNT);

        // int orientation = getIntent().getIntExtra(PhotoConfig.PREVIEW_ORIENTATION, PhotoConfig.DEFAULT_ORIENTATION);
        // if (orientation != PhotoConfig.ORIENTATION_AUTO) {
        //     setRequestedOrientation(orientation == 0 ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // }

        setViewPager();

        findViewById(R.id.photo_preview_view).setOnClickListener((view) -> finishPhotoPreview());
    }

    private void setViewPager() {
        ViewPager viewPager = findViewById(R.id.photo_view_pager);
        RelativeLayout bottomView = findViewById(R.id.photo_bottom_view);
        // bottomView.setBackgroundColor(Color.argb(200, 255, 255, 255));
        // findViewById(R.id.photo_back_button).setOnClickListener((view) -> finishPreviewPhoto());

        indexText = findViewById(R.id.photo_index_text);
        photoNumberView = findViewById(R.id.photo_number_view);

        indexText.setText(String.format("%d/%d", currentItem + 1, allPhotos.size()));
        photoNumberView.setNumber(selectedPhotos.size());
        photoNumberView.setPickColor(pickColor);

        FragPreviewAdapter fragPreviewAdapter = new FragPreviewAdapter(getSupportFragmentManager(),
                allPhotos, selectedPhotos, limitCount, new WeakReference<>(this));
        viewPager.setAdapter(fragPreviewAdapter);
        viewPager.setCurrentItem(currentItem, false);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                indexText.setText(String.format("%d/%d", position + 1, allPhotos.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void finishPhotoPreview() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(PhotoConfig.PREVIEW_ADD_PHOTOS, selectedPhotos);
        intent.putParcelableArrayListExtra(PhotoConfig.PREVIEW_DELETE_PHOTOS, deletePhotos);
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onNumberViewClick(View view, PhotoModel photo, boolean isAdd) {
        if (isAdd) {
            deletePhotos.remove(photo);
            allPhotos.get(currentItem).setPickNumber(photo.getPickNumber());
        } else {
            deletePhotos.add(photo);
            allPhotos.get(currentItem).setPickNumber(0);
        }
        for (int i = 0; i < selectedPhotos.size(); i++) {
            selectedPhotos.get(i).setPickNumber(i + 1);
            if (allPhotos.contains(selectedPhotos.get(i))) {
                int index = allPhotos.indexOf(selectedPhotos.get(i));
                allPhotos.get(index).setPickNumber(selectedPhotos.get(i).getPickNumber());
            }
        }
        photoNumberView.setNumber(selectedPhotos.size());
        photoNumberView.setPickColor(pickColor);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishPhotoPreview();
    }

    private static class FragPreviewAdapter extends FragmentStatePagerAdapter {
        private ArrayList<PhotoModel> allPhotos;
        private ArrayList<PhotoModel> addPhotos;
        private WeakReference<PhotoPreviewLayer> weakReference;
        private int limitCount;

        private FragPreviewAdapter(FragmentManager manager, ArrayList<PhotoModel> allPhotos, ArrayList<PhotoModel> addPhotos,
                                   int limitCount, WeakReference<PhotoPreviewLayer> weakReference) {
            super(manager);
            this.allPhotos = allPhotos;
            this.addPhotos = addPhotos;
            this.weakReference = weakReference;
            this.limitCount = limitCount;
        }

        @Override
        public Fragment getItem(int position) {
            PhotoFragPreview photoFragPreview = PhotoFragPreview.instance(allPhotos.get(position), addPhotos, limitCount);
            photoFragPreview.setOnNumberViewClickListener(weakReference.get());
            return photoFragPreview;
        }

        @Override
        public int getCount() {
            return allPhotos == null ? 0 : allPhotos.size();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }
    }
}
