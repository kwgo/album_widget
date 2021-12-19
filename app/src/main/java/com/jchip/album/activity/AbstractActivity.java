package com.jchip.album.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractActivity extends AppCompatActivity {
    public static final String FRAME_INDEX = "frameIndex";
    public static final String FRAME_RESOURCE = "frameResource";

    protected AlbumData album = new AlbumData();
    protected PhotoData photo = new PhotoData();

    protected List<AlbumData> albums = new ArrayList<>();
    //protected List<PhotoData> photos = new ArrayList<>();

    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ActivityCallBack activityCallBack;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        this.initContentView();
    }

    public void initContentView() {
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

    public void setPhotoView(View view, boolean label, boolean frame) {
        PhotoHelper.setPhotoView(this, view, this.photo, label, frame);
    }

    public void setPhotoImage(ImageView imageView) {
        PhotoHelper.setPhotoImage(imageView, this.photo);
    }

    public void setPhotoImage(ImageView imageView, int maxSize) {
        PhotoHelper.setPhotoImage(imageView, this.photo, maxSize);
    }

    public void setPhotoScale(ImageView imageView) {
        PhotoHelper.setPhotoScale(imageView, this.photo);
    }

    public void setPhotoLabel(TextView textView) {
        PhotoHelper.setPhotoLabel(this, textView, this.photo);
    }

    public void setPhotoFont(TextView textView) {
        PhotoHelper.setPhotoFont(this, textView, this.photo);
    }

    public void setPhotoFrame(View view) {
        PhotoHelper.setPhotoFrame(view, this.photo);
    }

    public void setFontLocation(TextView textView) {
        PhotoHelper.setFontLocation(textView, this.photo);
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

    public ListView getListView(int sourceId) {
        return (ListView) findViewById(sourceId);
    }

    public FloatingActionButton getButtonView(int sourceId) {
        return (FloatingActionButton) findViewById(sourceId);
    }

    public boolean isPortrait() {
        int orientation = this.getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public boolean isLandscape() {
        return !this.isPortrait();
    }

    public int dp2px(int dp) {
        return AlbumHelper.dp2px(this, dp);
    }

    public interface ActivityCallBack {
        public void call(Intent intent);
    }

}