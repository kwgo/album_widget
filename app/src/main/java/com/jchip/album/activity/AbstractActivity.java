package com.jchip.album.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.jchip.album.R;
import com.jchip.album.data.AbstractData;
import com.jchip.album.data.AlbumData;
import com.jchip.album.data.PhotoData;
import com.jchip.album.model.AlbumModel;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractActivity extends AppCompatActivity {
    public static final String ALBUM_MODEL = "albumModel";
    public static final String FRAME_RESOURCE = "frameResource";

    private AlbumModel albumModel;

    protected AlbumData album;
    protected PhotoData photo;

    protected List<AlbumData> albums = new ArrayList<>();
    protected List<PhotoData> photos = new ArrayList<>();

    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ActivityCallBack activityCallBack;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.albumModel = (AlbumModel) bundle.getSerializable(ALBUM_MODEL);
        } else {
            this.albumModel = new AlbumModel();
        }

        this.activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (activityCallBack != null) {
                            activityCallBack.call(data);
                        }
                    }
                });
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        this.initContentView();
    }

    public void initContentView() {
    }

    public void startActivity(Class<?> clazz) {
        this.startActivity(new Intent(this, clazz));
    }

    public void startActivity(Class<?> clazz, ActivityCallBack activityCallBack) {
        Log.d("activityCallBack", "activityCallBack=" + activityCallBack);
        this.activityCallBack = activityCallBack;
        Log.d("activityCallBack", "activityCallBack===00000000=" + activityCallBack);
        Intent intent = new Intent(this, clazz);
        Log.d("activityCallBack", "999999999999999999999999");
        this.activityResultLauncher.launch(intent);
        Log.d("activityCallBack", "88888888888888888888888888");
    }

    public void setVisibility(View view, boolean show, boolean gone) {
        view.setVisibility(show ? View.VISIBLE : gone ? View.GONE : View.INVISIBLE);
    }

    public void alertDeletion(Runnable work) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.app_name);
        alert.setMessage(R.string.album_alert_delete);
        //.setNegativeButton(android.R.string.no, null)
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                work.run();
                dialog.dismiss();
            }
        }).show();
    }

    public boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    public boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    public boolean isEmpty(AbstractData data) {
        return data == null;
    }

    public boolean isPortrait() {
        int orientation = this.getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public boolean isLandscape() {
        return !this.isPortrait();
    }

    public interface ActivityCallBack {
        public void call(Intent intent);
    }
}