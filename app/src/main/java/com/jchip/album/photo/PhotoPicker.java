package com.jchip.album.photo;

/*
 * Created by Ray on 2017/2/10.
 * RZAlbum
 * 調用入口類
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.jchip.album.photo.common.PhotoConfig;
import com.jchip.album.photo.model.PhotoModel;

import java.util.Collections;
import java.util.List;

public final class PhotoPicker {
    private static final String TAG = PhotoPicker.class.getSimpleName();
    private Intent rzIntent;
    private Bundle rzBundle;

    public static PhotoPicker ofAppName(@NonNull String yourAppName) {
        return new PhotoPicker(yourAppName);
    }

    private PhotoPicker(String yourAppName) {
        rzBundle = new Bundle();
        rzBundle.putString(PhotoConfig.APP_NAME, yourAppName);
    }

    public PhotoPicker setLimitCount(int limitCount) {
        rzBundle.putInt(PhotoConfig.LIMIT_COUNT, limitCount <= 0 ? PhotoConfig.DEFAULT_LIMIT_COUNT : limitCount);
        return this;
    }

    public PhotoPicker setSpanCount(int spanCount) {
        rzBundle.putInt(PhotoConfig.SPAN_COUNT, spanCount <= 0 ? PhotoConfig.DEFAULT_SPAN_COUNT : spanCount);
        return this;
    }

    public PhotoPicker setStatusBarColor(@ColorInt int statusBarColor) {
        rzBundle.putInt(PhotoConfig.STATUS_BAR_COLOR, statusBarColor);
        return this;
    }

    public PhotoPicker setToolBarColor(@ColorInt int toolBarColor) {
        rzBundle.putInt(PhotoConfig.TOOLBAR_COLOR, toolBarColor);
        return this;
    }

    public PhotoPicker setToolBarTitle(String toolBarTitle) {
        rzBundle.putString(PhotoConfig.TOOLBAR_TITLE, toolBarTitle);
        return this;
    }

    public PhotoPicker setPickerColor(@ColorInt int pickColor) {
        rzBundle.putInt(PhotoConfig.PICK_COLOR, pickColor);
        return this;
    }

    public PhotoPicker setDialogIcon(@DrawableRes int resID) {
        rzBundle.putInt(PhotoConfig.DIALOG_ICON, resID);
        return this;
    }

    public PhotoPicker setAllFolderName(String folderName) {
        rzBundle.putString(PhotoConfig.ALL_FOLDER_NAME, folderName);
        return this;
    }

    public PhotoPicker showCamera(boolean isShow) {
        rzBundle.putBoolean(PhotoConfig.SHOW_CAMERA, isShow);
        return this;
    }

    public PhotoPicker showGif(boolean isShow) {
        rzBundle.putBoolean(PhotoConfig.SHOW_GIF, isShow);
        return this;
    }

    public PhotoPicker setPreviewOrientation(int orientation) {
        if (orientation < 0 || orientation > 1) {
            rzBundle.putInt(PhotoConfig.PREVIEW_ORIENTATION, PhotoConfig.ORIENTATION_AUTO);
        } else {
            rzBundle.putInt(PhotoConfig.PREVIEW_ORIENTATION, orientation);
        }
        return this;
    }

    public void start(@NonNull Activity activity, int requestCode) {
        rzIntent = new Intent(activity, PhotoPickerActivity.class);
        rzIntent.putExtras(rzBundle);
        activity.startActivityForResult(rzIntent, requestCode);
    }

    public void start(@NonNull android.app.Fragment fragment, int requestCode) {
        rzIntent = new Intent(fragment.getActivity(), PhotoPickerActivity.class);
        rzIntent.putExtras(rzBundle);
        fragment.startActivityForResult(rzIntent, requestCode);
    }

    public void start(@NonNull Fragment fragment, int requestCode) {
        rzIntent = new Intent(fragment.getActivity(), PhotoPickerActivity.class);
        rzIntent.putExtras(rzBundle);
        fragment.startActivityForResult(rzIntent, requestCode);
    }

    public static List<PhotoModel> parseResult(@NonNull Intent intent) {
        List<PhotoModel> photos = intent.getParcelableArrayListExtra(PhotoConfig.RESULT_PHOTOS);
        if (photos == null) {
            photos = Collections.emptyList();
        }
        return photos;
    }
}
