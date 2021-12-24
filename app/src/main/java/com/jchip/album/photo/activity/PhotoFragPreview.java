package com.jchip.album.photo.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.jchip.album.R;
import com.jchip.album.photo.base.BaseLazyFragment;
import com.jchip.album.photo.model.PhotoModel;
import com.jchip.album.photo.view.PhotoNumberView;
import com.jchip.album.photo.view.ZoomImageView;

import java.util.ArrayList;
import java.util.Locale;

//import android.support.annotation.Nullable;

/**
 * Photo preview
 */

public class PhotoFragPreview extends BaseLazyFragment {
    private static final String FRAG_ALBUM_PHOTO = "FRAG_ALBUM_PHOTO";
    private static final String FRAG_ALBUM_PHOTOS = "FRAG_ALBUM_PHOTOS";
    private static final String FRAG_ALBUM_LIMIT_COUNT = "FRAG_ALBUM_LIMIT_COUNT";

    private FrameLayout rzFrameLayout;
    private ProgressBar rzProgressBar;
    private PhotoNumberView rzNumberView;

    private PhotoModel photo;
    private ArrayList<PhotoModel> addPhotos;
    private int limitCount;
    private OnNumberViewClickListener listener;

    public static PhotoFragPreview instance(PhotoModel photo, ArrayList<PhotoModel> addPhotos, int limitCount) {
        PhotoFragPreview photoFragPreview = new PhotoFragPreview();
        Bundle bundle = new Bundle();
        bundle.putParcelable(FRAG_ALBUM_PHOTO, photo);
        bundle.putParcelableArrayList(FRAG_ALBUM_PHOTOS, addPhotos);
        bundle.putInt(FRAG_ALBUM_LIMIT_COUNT, limitCount);
        photoFragPreview.setArguments(bundle);
        return photoFragPreview;
    }

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photo_fragment_preview, container, false);
        setup(view);
        return view;
    }

    private void setup(View view) {
        if (getArguments() != null) {
            photo = getArguments().getParcelable(FRAG_ALBUM_PHOTO);
            addPhotos = getArguments().getParcelableArrayList(FRAG_ALBUM_PHOTOS);
            limitCount = getArguments().getInt(FRAG_ALBUM_LIMIT_COUNT);
        }

        rzFrameLayout = view.findViewById(R.id.rzFrameLayout);
        rzProgressBar = view.findViewById(R.id.rzProgressBar);
        rzNumberView = view.findViewById(R.id.rzNumberView);

        isPrepared = true;
    }

    @Override
    protected void loadData() {
        if (!isVisiable || !isPrepared || isLoadData) {
            if (isLoadData) {
                // update number
                rzNumberView.setNumber(photo.getPickNumber());
                rzNumberView.setPickColor(photo.getPickColor());
            }
            return;
        }

        final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        String photoPath = photo.getPhotoPath();

        if (getActivity() != null) {
            if (photoPath.endsWith(".gif") || photoPath.endsWith(".GIF")) {
                ImageView mImgView = new ImageView(getContext());
                mImgView.setLayoutParams(lp);
                Glide.with(getActivity())
                        .asGif()
                        .load(photoPath)
                        .into(mImgView);
                rzFrameLayout.addView(mImgView, 0);
            } else {
                ZoomImageView mZoomView = new ZoomImageView(getContext());
                mZoomView.setLayoutParams(lp);
                Glide.with(getActivity())
                        .asBitmap()
                        .load(photoPath)
                        .into(mZoomView);
                rzFrameLayout.addView(mZoomView, 0);
            }
        }
        rzNumberView.setNumber(photo.getPickNumber());
        rzNumberView.setPickColor(photo.getPickColor());

        rzNumberView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAdd = true;
                if (photo.getPickNumber() == 0) {
                    if (addPhotos.size() == limitCount) {
                        Toast.makeText(getActivity(), String.format(Locale.TAIWAN,
                                getResources().getString(R.string.rz_album_limit_count), limitCount),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    photo.setPickNumber(addPhotos.size() + 1);
                    addPhotos.add(photo);
                } else {
                    if (addPhotos.size() == 0) return;
                    addPhotos.remove(photo);
                    photo.setPickNumber(0);
                    isAdd = false;
                }
                rzNumberView.setNumber(photo.getPickNumber());
                rzNumberView.setPickColor(photo.getPickColor());
                if (listener != null) listener.onNumberViewClick(v, photo, isAdd);
            }
        });
        rzProgressBar.setVisibility(View.GONE);
        isLoadData = true;
    }

    @Override
    protected void unLoadData() {

    }

    public interface OnNumberViewClickListener {
        void onNumberViewClick(View view, PhotoModel photo, boolean isAdd);
    }

    public void setOnNumberViewClickListener(OnNumberViewClickListener listener) {
        this.listener = listener;
    }
}
