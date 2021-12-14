package com.jchip.album.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jchip.album.common.AlbumHelper;
import com.jchip.album.common.ImageHelper;
import com.jchip.album.data.AlbumData;
import com.jchip.album.data.DataHelper;
import com.jchip.album.data.PhotoData;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractActivity extends AppCompatActivity {
    public static final String ALBUM_MODEL = "albumModel";
    public static final String FRAME_INDEX = "frameIndex";
    public static final String FRAME_RESOURCE = "frameResource";

    protected AlbumData album = new AlbumData();
    protected PhotoData photo = new PhotoData();

    protected List<AlbumData> albums = new ArrayList<>();
    protected List<PhotoData> photos = new ArrayList<>();

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
        Log.d("", "put into Intent ..............." + this.photo);
        this.activityResultLauncher.launch(intent);
    }

    protected void setViewPhoto(ImageView imageView) {
        if (this.photo.getPhotoPath() != null && !this.photo.getPhotoPath().trim().isEmpty()) {
            Bitmap bitmap = AlbumHelper.loadBitmap(this.photo.getPhotoPath());
            bitmap = ImageHelper.convertBitmap(bitmap, this.photo.getRotationIndex(), this.photo.getFlipIndex());
            imageView.setImageBitmap(bitmap);
        }
    }

    protected void setViewFont(TextView textView) {
        textView.setText(this.photo.getFontText());
        textView.setTextColor(this.photo.getFontColor());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.photo.getFontSize());
        this.setViewLocation(textView);
    }

    protected void setViewText(TextView textView) {
        textView.setText(this.photo.getFontText());
    }

    protected void setViewLocation(View view) {
        int fontLocation = this.photo.getFontLocation();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_START);
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(fontLocation % 2 == 0 ? RelativeLayout.ALIGN_PARENT_START : RelativeLayout.ALIGN_PARENT_END);
        layoutParams.addRule(fontLocation / 2 == 0 ? RelativeLayout.ALIGN_PARENT_TOP : RelativeLayout.ALIGN_PARENT_BOTTOM);
        view.setLayoutParams(layoutParams);
    }

    public void alert(int titleId, int detailId, Runnable work) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(titleId);
        alert.setMessage(detailId);
        // alert.setNegativeButton(android.R.string.no, null)
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                work.run();
                dialog.dismiss();
            }
        }).show();
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

    //public boolean isEmpty(List<?> list) {
    //      return list == null || list.isEmpty();
    // }

    public boolean isEmptyText(String text) {
        return text == null || text.trim().isEmpty();
    }

    //   public boolean isEmpty(AbstractData data) {
    //       return data == null;
    //   }

    public boolean isPortrait() {
        int orientation = this.getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public boolean isLandscape() {
        return !this.isPortrait();
    }

    public int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public interface ActivityCallBack {
        public void call(Intent intent);
    }

    protected void setAlbumName(String name) {
        this.album.setAlbumName(name);
        if (!this.photos.isEmpty()) {
            DataHelper.getInstance(this).saveAlbum(this.album);
        }
    }

    protected void deleteAlbum() {
        //DataHelper.getInstance(this).deleteAlbum(this.album);
    }
}