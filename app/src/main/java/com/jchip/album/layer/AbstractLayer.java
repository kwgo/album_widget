package com.jchip.album.layer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
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
import com.jchip.album.common.AlbumHelper;
import com.jchip.album.common.PhotoHelper;
import com.jchip.album.data.AlbumData;
import com.jchip.album.data.PhotoData;
import com.jchip.album.view.AlbumView;
import com.jchip.album.view.PhotoView;

public abstract class AbstractLayer extends AppCompatActivity {
    protected int layer = -1;

    protected AlbumView album;
    protected PhotoView photo;

    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ActivityCallBack activityCallBack;

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
        this.album = new AlbumView(this, this.layer);
        this.photo = new PhotoView(this, this.layer);

        this.activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (activityCallBack != null) {
                            activityCallBack.call(result.getData());
                            activityCallBack = null;
                        }
                    }
                }
        );

        this.initContentView();
    }

    protected void initContentView() {
    }

    public void startActivity(Class<?> clazz) {
        this.startActivity(new Intent(this, clazz));
    }

    public void startActivity(Class<?> clazz, ActivityCallBack activityCallBack) {
        this.activityCallBack = activityCallBack;
        Intent intent = new Intent(this, clazz);
        intent.putExtra(AlbumData.tableName, this.album.getAlbumData());
        intent.putExtra(PhotoData.tableName, this.photo.getPhotoData());
        this.activityResultLauncher.launch(intent);
    }

    public void setPhotoView(View view) {
        PhotoHelper.setPhotoView(this.photo, view);
    }

    public void setPhotoFrame(View containerView, View frameView) {
        PhotoHelper.setPhotoFrame(this.photo, containerView, frameView);
    }

    public void setPhotoImage(ImageView imageView) {
        PhotoHelper.setPhotoImage(this.photo, imageView);
    }

    public void setPhotoScale(ImageView imageView) {
        PhotoHelper.setPhotoScale(this.photo, imageView);
    }

    public void setPhotoFont(TextView textView) {
        PhotoHelper.setPhotoFont(this.photo, textView);
    }

    public Rect getViewRect(int sourceId) {
        View view = findViewById(sourceId);
        return new Rect(0, 0, view.getWidth(), view.getHeight());
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
        //AlbumHelper.updateWidget(this, ActivityAlbumSetting.AlbumProvider.class);
        //AlbumHelper.updateWidget(this, ActivityPhotoSetting.PhotoProvider.class);
    }

    public void setStatusBarColor(int colorId) {
        AlbumHelper.setStatusBarColor(this, colorId);
    }

    public void alert(int titleId, int detailId, Runnable work) {
        AlbumHelper.alert(this, titleId, detailId, work);
    }

    public interface ActivityCallBack {
        void call(Intent intent);
    }
}