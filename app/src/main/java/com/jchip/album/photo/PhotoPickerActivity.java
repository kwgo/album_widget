package com.jchip.album.photo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jchip.album.R;
import com.jchip.album.photo.adapter.MultiAdapter;
import com.jchip.album.photo.adapter.itemdecoration.RecycleItemDecoration;
import com.jchip.album.photo.adapter.listener.OnMultiItemClickListener;
import com.jchip.album.photo.common.PhotoConfig;
import com.jchip.album.photo.model.FolderModel;
import com.jchip.album.photo.model.PhotoModel;
import com.jchip.album.photo.utils.PhotoScanner;
import com.jchip.album.photo.utils.AnimationHelper;
import com.jchip.album.photo.utils.DisplayUtils;
import com.jchip.album.photo.utils.FileProviderUtils;
import com.jchip.album.photo.utils.MainHandler;
import com.jchip.album.photo.utils.Utils;
import com.jchip.album.photo.view.PreviewPhotoActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PhotoPickerActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = PhotoPickerActivity.class.getSimpleName();
    private final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private final int PERMISSION_REQUEST_STORAGE = 6666;
    private final int PERMISSION_REQUEST_CAMERA = PERMISSION_REQUEST_STORAGE + 1;
    private final int ACTIVITY_REQUEST_CAMERA = 7777;
    private final int ACTIVITY_REQUEST_PREVIEW = ACTIVITY_REQUEST_CAMERA + 1;

    private String appName = PhotoConfig.APP_NAME;
    private String toolBarTitle = PhotoConfig.TOOLBAR_TITLE;
    private int spanCount = PhotoConfig.DEFAULT_SPAN_COUNT;
    private int limitCount = PhotoConfig.DEFAULT_LIMIT_COUNT;
    private int statusBarColor = PhotoConfig.DEFAULT_STATUS_BAR_COLOR;
    private int toolBarColor = PhotoConfig.DEFAULT_TOOLBAR_COLOR;
    private int pickColor = PhotoConfig.DEFAULT_PICK_COLOR;
    private boolean showCamera = PhotoConfig.DEFAULT_SHOW_CAMERA;
    private boolean showGif = PhotoConfig.DEFAULT_SHOW_GIF;
    private int orientation = PhotoConfig.DEFAULT_ORIENTATION;
    private String folderName = "";
    private int dialogIcon = -1;

    private RecyclerView.LayoutManager layoutManager;
    private MultiAdapter<PhotoModel> multiAdapter;
    private FloatingActionButton functionButton, folderButton, doneButton;

    private BottomSheetDialog bottomSheetDialog;
    private RecyclerView bottomRecyclerView;
    private MultiAdapter<FolderModel> bottomAdapter;

    // 相簿資料夾
    private List<FolderModel> mFolderModels;
    // 紀錄選擇到的Photo
    private List<PhotoModel> addPhotos;
    // 儲存照片的路徑
    private String mCameraPath;

    private ExecutorService mSingleExecutor;
    private Runnable scanAlbumRunnable = new Runnable() {
        @Override
        public void run() {
            folderName = folderName.isEmpty() ? getResources().getString(R.string.rz_album_all_folder_name) : folderName;
            mFolderModels = PhotoScanner.instances(pickColor, showGif).getPhotoAlbum(PhotoPickerActivity.this, folderName);
            MainHandler.instances().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isFinishing()) {
                        mFolderModels.clear();
                        mFolderModels = null;
                    } else {
                        showAlbum(0);
                    }
                }
            }, 200L);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_layer);

        DisplayUtils.instance(this.getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            appName = bundle.getString(PhotoConfig.APP_NAME, PhotoConfig.DEFAULT_APP_NAME);
            limitCount = bundle.getInt(PhotoConfig.LIMIT_COUNT, PhotoConfig.DEFAULT_LIMIT_COUNT);
            spanCount = bundle.getInt(PhotoConfig.SPAN_COUNT, PhotoConfig.DEFAULT_SPAN_COUNT);
            statusBarColor = bundle.getInt(PhotoConfig.STATUS_BAR_COLOR, PhotoConfig.DEFAULT_STATUS_BAR_COLOR);
            toolBarTitle = bundle.getString(PhotoConfig.TOOLBAR_TITLE, PhotoConfig.DEFAULT_TOOLBAR_TITLE);
            toolBarColor = bundle.getInt(PhotoConfig.TOOLBAR_COLOR, PhotoConfig.DEFAULT_TOOLBAR_COLOR);
            showCamera = bundle.getBoolean(PhotoConfig.SHOW_CAMERA, PhotoConfig.DEFAULT_SHOW_CAMERA);
            showGif = bundle.getBoolean(PhotoConfig.SHOW_GIF, PhotoConfig.DEFAULT_SHOW_GIF);
            orientation = bundle.getInt(PhotoConfig.PREVIEW_ORIENTATION, PhotoConfig.ORIENTATION_AUTO);
            pickColor = bundle.getInt(PhotoConfig.PICK_COLOR, PhotoConfig.DEFAULT_PICK_COLOR);
            folderName = bundle.getString(PhotoConfig.ALL_FOLDER_NAME, "");
            dialogIcon = bundle.getInt(PhotoConfig.DIALOG_ICON, -1);
        }

        mSingleExecutor = Executors.newSingleThreadExecutor();
        addPhotos = new ArrayList<>();
        setupView();
        requestScanPhotos();
    }

    private void setupView() {
        Utils.setStatusBarColor(this, statusBarColor);
        Toolbar mToolBar = findViewById(R.id.mToolBar);
        mToolBar.setTitle(toolBarTitle);
        mToolBar.setTitleTextColor(Color.WHITE);
        mToolBar.setBackgroundColor(toolBarColor);
        mToolBar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        if (showCamera) {
            mToolBar.getMenu().add(0, 0, 0, getResources().getString(R.string.rz_album_menu_camera))
                    .setIcon(R.drawable.ic_camera).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == 0) {
                        requestOpenCamera();
                        return true;
                    }
                    return false;
                }
            });
        }

        RecyclerView recyclerView = findViewById(R.id.mRZRecyclerView);
        layoutManager = new GridLayoutManager(this, spanCount, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);
        int itemSize = 1;
        recyclerView.addItemDecoration(new RecycleItemDecoration(itemSize, Color.argb(255, 255, 255, 255)));
        int itemWH = (DisplayUtils.screenW - (itemSize * (spanCount - 1))) / spanCount;
        multiAdapter = new MultiAdapter<>(null, itemWH);
        recyclerView.setAdapter(multiAdapter);

        multiAdapter.setOnItemClickListener(new OnMultiItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, View view, int viewPosition, int itemPosition) {
                if (viewPosition == 0) {
                    photoPreview(itemPosition);
                } else {
                    photoPick(itemPosition);
                }
            }
        });

        functionButton = findViewById(R.id.mFabMultiBut);
        folderButton = findViewById(R.id.mFabFolderBut);
        doneButton = findViewById(R.id.mFabDoneBut);
        functionButton.setAlpha(.8f);
        functionButton.setOnClickListener(this);
        folderButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);

        // Bottom Adapter
        bottomRecyclerView = new RecyclerView(this);
        bottomRecyclerView.setBackgroundColor(Color.argb(255, 255, 255, 255));
        bottomRecyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        bottomRecyclerView.setLayoutManager(manager);
        bottomRecyclerView.setHasFixedSize(true);
        bottomRecyclerView.addItemDecoration(new RecycleItemDecoration(1, Color.LTGRAY));
        bottomAdapter = new MultiAdapter<>(null);
        bottomRecyclerView.setAdapter(bottomAdapter);
    }

    private void requestScanPhotos() {
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionResult = ContextCompat.checkSelfPermission(this, PERMISSION_READ_EXTERNAL_STORAGE);
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_READ_EXTERNAL_STORAGE)) {
                    // 第一次被使用者拒絕後，這邊做些解釋的動作
                    showDescriptionDialog(1);
                } else {
                    // 第一次詢問
                    ActivityCompat.requestPermissions(this,
                            new String[]{PERMISSION_READ_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_STORAGE);
                }
            } else {
                mSingleExecutor.execute(scanAlbumRunnable);
            }
        } else {
            mSingleExecutor.execute(scanAlbumRunnable);
        }
    }

    private void requestOpenCamera() {
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionResult = ContextCompat.checkSelfPermission(this, PERMISSION_CAMERA);
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_CAMERA)) {
                    showDescriptionDialog(2);
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{PERMISSION_CAMERA},
                            PERMISSION_REQUEST_CAMERA);
                }
            } else {
                openCamera();
            }
        } else {
            openCamera();
        }
    }

    private void showAlbum(int index) {
        multiAdapter.resetData(mFolderModels.get(index).getFolderPhotos());
        layoutManager.scrollToPosition(0);
        //changePhotoStatus();
    }

    private void openCamera() {
        // 打開手機的照相機
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判斷手機上是否有可以啟動照相機的應用程式
        if (camera.resolveActivity(getPackageManager()) != null) {
            File photoFile;
            // 照片命名
            String uuid = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String fileName = String.format(Locale.TAIWAN, "JPEG_%s.jpg", uuid);
            // 建立目錄
            File dcimFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            // 如果目錄不存在就建立
            if (!dcimFile.exists()) {
                boolean isCreate = dcimFile.mkdirs();
            }
            // 建立檔案(存放的位置, 檔名)
            // 拍照時，將拍得的照片先保存在指定的資料夾中(未缩小)
            photoFile = new File(dcimFile, fileName);

            // 照片存放路徑
            mCameraPath = photoFile.getAbsolutePath();
            // 拍照適配Android7.0 up
            Uri fileUri = FileProviderUtils.getUriForFile(this, photoFile);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProviderUtils.grantPermissions(this, camera, fileUri, true);
            }
            // 2017-06-11 更改
            // 如果指定了圖片uri，data就没有數據，如果没有指定uri，則data就返回有數據
            // 指定圖片输出位置，若無這句則拍照後，圖片會放入內存中，由於占用内存太大導致無法剪切或者剪切後無法保存
            //camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            camera.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(camera, ACTIVITY_REQUEST_CAMERA);
        } else {
            Toast.makeText(this, getResources().getString(R.string.rz_album_camera_not_found), Toast.LENGTH_SHORT).show();
        }
    }

    private void photoPreview(int itemPosition) {
        Intent preview = new Intent(this, PreviewPhotoActivity.class);
        preview.putParcelableArrayListExtra(PhotoConfig.PREVIEW_ADD_PHOTOS, (ArrayList<? extends Parcelable>) addPhotos);
        preview.putParcelableArrayListExtra(PhotoConfig.PREVIEW_ALL_PHOTOS, (ArrayList<? extends Parcelable>) multiAdapter.getDatas());
        preview.putExtra(PhotoConfig.PREVIEW_ITEM_POSITION, itemPosition);
        preview.putExtra(PhotoConfig.PREVIEW_PICK_COLOR, pickColor);
        preview.putExtra(PhotoConfig.PREVIEW_LIMIT_COUNT, limitCount);
        preview.putExtra(PhotoConfig.PREVIEW_ORIENTATION, orientation);
        startActivityForResult(preview, ACTIVITY_REQUEST_PREVIEW);
        overridePendingTransition(0, 0);
    }

    private void photoPick(int itemPosition) {
        PhotoModel photo = multiAdapter.getDatas().get(itemPosition);
        if (photo.getPickNumber() == 0) {
            if (addPhotos.size() == limitCount) {
                Toast.makeText(PhotoPickerActivity.this, String.format(Locale.TAIWAN, getResources().getString(R.string.rz_album_limit_count), limitCount),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            photo.setPickNumber(addPhotos.size() + 1);
            addPhotos.add(photo);
            multiAdapter.notifyItemChanged(itemPosition);
        } else {
            if (addPhotos.size() == 0) return;
            addPhotos.remove(photo);
            photo.setPickNumber(0);
            multiAdapter.notifyItemChanged(itemPosition);
            changePhotoPickNumber();
        }
    }

    private void changePhotoPickNumber() {
        // 改變item status & index
        for (int i = 0; i < addPhotos.size(); i++) {
            addPhotos.get(i).setPickNumber(i + 1);
            int index = multiAdapter.getDatas().indexOf(addPhotos.get(i));
            if (index != -1) {
                multiAdapter.getDatas().get(index).setPickNumber(i + 1);
                multiAdapter.notifyItemChanged(index);
            }
        }
    }

    private void showDescriptionDialog(final int type) {
        String title = type == 1 ? getResources().getString(R.string.rz_album_dia_read_description) : getResources().getString(R.string.rz_album_dia_camera_description);
        String msg = type == 1 ? getResources().getString(R.string.rz_album_dia_read_message) : getResources().getString(R.string.rz_album_dia_camera_message);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(R.string.rz_album_dia_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 再一次請求
                        ActivityCompat.requestPermissions(PhotoPickerActivity.this,
                                type == 1 ? new String[]{PERMISSION_READ_EXTERNAL_STORAGE} : new String[]{PERMISSION_CAMERA},
                                type == 1 ? PERMISSION_REQUEST_STORAGE : PERMISSION_REQUEST_CAMERA);
                    }
                })
                .setNegativeButton(R.string.rz_album_dia_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (type == 1) PhotoPickerActivity.this.finish();
                    }
                });
        if (dialogIcon != -1) builder.setIcon(dialogIcon);

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(pickColor);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(pickColor);
            }
        });
        dialog.show();
    }

    private void showDeniedDialog(final int type) {
        String title = type == 1 ? getResources().getString(R.string.rz_album_dia_read_description_denied) : getResources().getString(R.string.rz_album_dia_camera_description_denied);
        String msg = type == 1 ?
                String.format(Locale.TAIWAN, getResources().getString(R.string.rz_album_dia_read_message_denied), appName) :
                String.format(Locale.TAIWAN, getResources().getString(R.string.rz_album_dia_camera_message_denied), appName);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(R.string.rz_album_dia_go_setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        Utils.goAppSettingPage(PhotoPickerActivity.this);
                        if (type == 1) PhotoPickerActivity.this.finish();
                    }
                });
        if (dialogIcon != -1) builder.setIcon(dialogIcon);

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(pickColor);
            }
        });
        dialog.show();
    }

    public void fatButHideAnimation(View view) {
        if (view.isSelected()) {
            AnimationHelper.hideFabButAnimation(folderButton, true);
            AnimationHelper.hideFabButAnimation(doneButton, false);
        } else {
            AnimationHelper.showFabButAnimation(folderButton, true);
            AnimationHelper.showFabButAnimation(doneButton, false);
        }
        view.setSelected(!view.isSelected());
    }

    private void showBottomDialog() {
        if (mFolderModels.get(0).getFolderPhotos().size() == 0) return;
        if (bottomSheetDialog == null) {
            bottomSheetDialog = new BottomSheetDialog(this);
            bottomAdapter.resetData(mFolderModels);
            bottomAdapter.setOnItemClickListener(new OnMultiItemClickListener() {
                @Override
                public void onItemClick(RecyclerView.ViewHolder viewHolder, View view, int viewPosition, int itemPosition) {
                    showAlbum(itemPosition);
                    for (int i = 0; i < bottomAdapter.getItemCount(); i++) {
                        bottomAdapter.getDatas().get(i).setCheck(false);
                    }
                    bottomAdapter.getDatas().get(itemPosition).setCheck(true);
                    bottomAdapter.notifyItemRangeChanged(0, bottomAdapter.getItemCount());
                    bottomSheetDialog.dismiss();
                }
            });
            bottomSheetDialog.setContentView(bottomRecyclerView);
            bottomSheetDialog.setCancelable(true);
            bottomSheetDialog.setCanceledOnTouchOutside(true);
        }
        bottomSheetDialog.show();
    }

    @Override
    public void onClick(View view) {
        int tag = view.getId();
        if (tag == R.id.mFabMultiBut) {
            fatButHideAnimation(functionButton);
        } else if (tag == R.id.mFabFolderBut) {
            showBottomDialog();
            fatButHideAnimation(functionButton);
        } else if (tag == R.id.mFabDoneBut) {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(PhotoConfig.RESULT_PHOTOS, (ArrayList<? extends Parcelable>) addPhotos);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int permissionResult = grantResults[0];
        switch (requestCode) {
            case PERMISSION_REQUEST_STORAGE:
                if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                    mSingleExecutor.execute(scanAlbumRunnable);
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                        showDescriptionDialog(1);
                    } else {
                        showDeniedDialog(1);
                    }
                }
                break;
            case PERMISSION_REQUEST_CAMERA:
                if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                        showDescriptionDialog(2);
                    } else {
                        showDeniedDialog(2);
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ACTIVITY_REQUEST_CAMERA:
                    // 拍照完後透過掃描可在手機端發現剛拍完照的檔案 參考資料 : https://www.jianshu.com/p/bc8b04bffddf
                    MediaScannerConnection.scanFile(this, new String[]{mCameraPath}, new String[]{PhotoConfig.JPEG},
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, final Uri uri) {
                                    mSingleExecutor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            final PhotoModel photo = PhotoScanner.instances(pickColor, showGif)
                                                    .getSinglePhoto(PhotoPickerActivity.this, uri);
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent result = new Intent();
                                                    if (photo != null) {
                                                        ArrayList<PhotoModel> list = new ArrayList<>();
                                                        list.add(photo);
                                                        result.putParcelableArrayListExtra(PhotoConfig.RESULT_PHOTOS, list);
                                                    }
                                                    setResult(RESULT_OK, result);
                                                    finish();
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                    break;
                case ACTIVITY_REQUEST_PREVIEW:
                    if (data != null) {
                        addPhotos = data.getParcelableArrayListExtra(PhotoConfig.PREVIEW_ADD_PHOTOS);
                        List<PhotoModel> deletePhotos = data.getParcelableArrayListExtra(PhotoConfig.PREVIEW_DELETE_PHOTOS);
                        // delete
                        for (int i = 0; i < deletePhotos.size(); i++) {
                            int index = multiAdapter.getDatas().indexOf(deletePhotos.get(i));
                            multiAdapter.getDatas().get(index).setPickNumber(0);
                            multiAdapter.notifyItemChanged(index);
                        }
                        // add
                        for (int j = 0; j < addPhotos.size(); j++) {
                            PhotoModel photo = addPhotos.get(j);
                            int index = multiAdapter.getDatas().indexOf(photo);
                            if (index != -1) {
                                multiAdapter.getDatas().get(index).setPickNumber(photo.getPickNumber());
                                multiAdapter.notifyItemChanged(index);
                            }
                        }
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (!mSingleExecutor.isShutdown()) {
            mSingleExecutor.shutdown();
            mSingleExecutor = null;
        }
        super.onDestroy();
    }
}
