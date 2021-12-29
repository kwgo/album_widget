package com.jchip.album.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jchip.album.ActivityAlbumSetting;
import com.jchip.album.ActivityPhotoSetting;
import com.jchip.album.common.AlbumHelper;
import com.jchip.album.common.PhotoHelper;
import com.jchip.album.data.AlbumData;
import com.jchip.album.data.PhotoData;
import com.jchip.album.view.PhotoView;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractActivity extends AppCompatActivity {
    public static final String FRAME_INDEX = "frameIndex";
    public static final String FRAME_ID = "frameId";
    public static final String SHELL_ID = "shellId";

    protected AlbumData album = new AlbumData();
    protected PhotoData photo = new PhotoData();
    protected List<AlbumData> albums = new ArrayList<>();

    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ActivityCallBack activityCallBack;

    private int layer = -1;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        this.setContentView(this.layer, layoutResID);
    }

    public void setContentView(int layer, @LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        this.layer = layer;
        this.initContentView();
    }

    protected void initContentView() {
        this.activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (activityCallBack != null) {
                            activityCallBack.call(data);
                            activityCallBack = null;
                        }
                    }
                });
    }

    public void startActivity(Class<?> clazz) {
        this.startActivity(new Intent(this, clazz));
    }

    public void startActivity(Class<?> clazz, ActivityCallBack activityCallBack) {
        this.activityCallBack = activityCallBack;
        Intent intent = new Intent(this, clazz);
        intent.putExtra(AlbumData.tableName, this.album);
        intent.putExtra(PhotoData.tableName, this.photo);
        this.activityResultLauncher.launch(intent);
    }

    public PhotoView getPhotoView() {
        return new PhotoView(this, this.photo, this.layer);
    }

    public void setPhotoView(View view) {
        PhotoHelper.setPhotoView(this.getPhotoView(), view);
    }

    public void setPhotoImage(ImageView imageView) {
        PhotoHelper.setPhotoImage(this.getPhotoView(), imageView);
    }

    public void setPhotoScale(ImageView imageView) {
        PhotoHelper.setPhotoScale(this.getPhotoView(), imageView);
    }

    public void setPhotoFont(TextView textView) {
        PhotoHelper.setPhotoFont(this.getPhotoView(), textView);
    }

    public void setPhotoFrame(View containerView, View frameView) {
        PhotoHelper.setPhotoFrame(this.getPhotoView(), containerView, frameView);
    }

    public int getFontIndex() {
        return this.getPhotoView().getFontIndex();
    }

    public List<Integer> getFonts() {
        return this.getPhotoView().getFonts();
    }

    public void alert(int titleId, int detailId, Runnable work) {
        AlbumHelper.alert(this, titleId, detailId, work);
    }

    public View getView(int sourceId) {
        return findViewById(sourceId);
    }

    public ImageView getImageView(int sourceId) {
        return (ImageView) findViewById(sourceId);
    }

    public TextView getTextView(int sourceId) {
        return (TextView) findViewById(sourceId);
    }

    public EditText getEditView(int sourceId) {
        return (EditText) findViewById(sourceId);
    }

    public SeekBar getSeekView(int sourceId) {
        return (SeekBar) findViewById(sourceId);
    }

    public FloatingActionButton getButtonView(int sourceId) {
        return (FloatingActionButton) findViewById(sourceId);
    }

    public void updateWidget() {
        AlbumHelper.updateWidget(this, ActivityAlbumSetting.AlbumProvider.class);
        AlbumHelper.updateWidget(this, ActivityPhotoSetting.PhotoProvider.class);
    }

    public void setStatusBarColor(int colorId) {
        AlbumHelper.setStatusBarColor(this, colorId);
    }

    public interface ActivityCallBack {
        void call(Intent intent);
    }
}