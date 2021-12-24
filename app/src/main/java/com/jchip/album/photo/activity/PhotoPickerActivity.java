package com.jchip.album.photo.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jchip.album.R;
import com.jchip.album.common.AlbumHelper;
import com.jchip.album.photo.adapter.MultiAdapter;
import com.jchip.album.photo.adapter.itemdecoration.RecycleItemDecoration;
import com.jchip.album.photo.adapter.listener.OnMultiItemClickListener;
import com.jchip.album.photo.common.PhotoConfig;
import com.jchip.album.photo.model.FolderModel;
import com.jchip.album.photo.model.PhotoModel;
import com.jchip.album.photo.utils.AnimationHelper;
import com.jchip.album.photo.utils.MainHandler;
import com.jchip.album.photo.utils.PhotoScanner;
import com.jchip.album.photo.utils.PhotoUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PhotoPickerActivity extends AppCompatActivity {
    private final int PERMISSION_REQUEST_STORAGE = 6666;
    private final int ACTIVITY_REQUEST_PREVIEW = 7778;

    private int spanCount = PhotoConfig.DEFAULT_SPAN_COUNT;
    private int limitCount = PhotoConfig.DEFAULT_LIMIT_COUNT;
    private int pickColor = PhotoConfig.DEFAULT_PICK_COLOR;
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

    private List<FolderModel> folderModels;
    private List<PhotoModel> selectedPhotos;

    private ExecutorService singleExecutor;
    private Runnable scanPhotoRunnable = new Runnable() {
        @Override
        public void run() {
            folderName = folderName.isEmpty() ? getResources().getString(R.string.photo_all_folder) : folderName;
            folderModels = PhotoScanner.instances(pickColor, showGif).getPhotoAlbum(PhotoPickerActivity.this, folderName);
            MainHandler.instances().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isFinishing()) {
                        folderModels.clear();
                        folderModels = null;
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
        setContentView(R.layout.photo_picker_layer);
        AlbumHelper.setStatusBarColor(this, Color.TRANSPARENT);

        PhotoUtils.instance(this.getApplicationContext());

        singleExecutor = Executors.newSingleThreadExecutor();
        selectedPhotos = new ArrayList<>();
        setupView();
        requestScanPhotos();
    }

    private void setupView() {
        RecyclerView recyclerView = findViewById(R.id.photo_grid_view);
        layoutManager = new GridLayoutManager(this, spanCount, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);
        int gapSize = 3;
        recyclerView.addItemDecoration(new RecycleItemDecoration(gapSize, Color.TRANSPARENT));

        int screenWidth = PhotoUtils.screenW - PhotoUtils.dp2px(20) - 2;
        int itemSize = (screenWidth - (gapSize * (spanCount - 1))) / spanCount;

        multiAdapter = new MultiAdapter<>(null, itemSize);
        multiAdapter.setOnItemClickListener((viewHolder, view, viewPosition, itemPosition) -> {
            if (viewPosition == 0) {
                photoPreview(itemPosition);
            } else {
                photoPick(itemPosition);
            }
        });
        recyclerView.setAdapter(multiAdapter);

        functionButton = findViewById(R.id.photo_function_button);
        functionButton.setOnClickListener(v -> fatButHideAnimation(functionButton));
        folderButton = findViewById(R.id.photo_folder_button);
        folderButton.setOnClickListener(v -> {
            showBottomDialog();
            fatButHideAnimation(functionButton);
        });
        doneButton = findViewById(R.id.photo_done_button);
        doneButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(PhotoConfig.RESULT_PHOTOS, (ArrayList<? extends Parcelable>) selectedPhotos);
            setResult(RESULT_OK, intent);
            finish();
        });

        // Bottom Adapter
        bottomRecyclerView = new RecyclerView(this);
        //bottomRecyclerView.setBackgroundColor(Color.argb(255, 255, 255, 255));
        bottomRecyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        bottomRecyclerView.setLayoutManager(manager);
        bottomRecyclerView.setHasFixedSize(true);
        bottomRecyclerView.addItemDecoration(new RecycleItemDecoration(1, Color.LTGRAY));
        bottomAdapter = new MultiAdapter<>(null);
        bottomRecyclerView.setAdapter(bottomAdapter);

        findViewById(R.id.photo_picker_view).setOnClickListener((v) -> this.finish());
    }

    private void requestScanPhotos() {
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDescriptionDialog(1);
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_STORAGE);
                }
            } else {
                singleExecutor.execute(scanPhotoRunnable);
            }
        } else {
            singleExecutor.execute(scanPhotoRunnable);
        }
    }

    private void showAlbum(int index) {
        multiAdapter.resetData(folderModels.get(index).getFolderPhotos());
        layoutManager.scrollToPosition(0);
        //changePhotoStatus();
    }

    private void photoPreview(int itemPosition) {
        Intent preview = new Intent(this, PhotoPreviewActivity.class);
        preview.putParcelableArrayListExtra(PhotoConfig.PREVIEW_ADD_PHOTOS, (ArrayList<? extends Parcelable>) selectedPhotos);
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
            if (selectedPhotos.size() == limitCount) {
                Toast.makeText(PhotoPickerActivity.this, String.format(getResources().getString(R.string.photo_limit_count), limitCount),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            photo.setPickNumber(selectedPhotos.size() + 1);
            selectedPhotos.add(photo);
            multiAdapter.notifyItemChanged(itemPosition);
        } else {
            if (selectedPhotos.size() == 0) return;
            selectedPhotos.remove(photo);
            photo.setPickNumber(0);
            multiAdapter.notifyItemChanged(itemPosition);
            changePhotoPickNumber();
        }
    }

    private void changePhotoPickNumber() {
        // 改變item status & index
        for (int i = 0; i < selectedPhotos.size(); i++) {
            selectedPhotos.get(i).setPickNumber(i + 1);
            int index = multiAdapter.getDatas().indexOf(selectedPhotos.get(i));
            if (index != -1) {
                multiAdapter.getDatas().get(index).setPickNumber(i + 1);
                multiAdapter.notifyItemChanged(index);
            }
        }
    }

    private void showDescriptionDialog(final int type) {
        String title = getResources().getString(R.string.dialog_permission_title);
        String message = getResources().getString(R.string.dialog_permission_description);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.dialog_permission_ok, (dialog, which) -> {
                    ActivityCompat.requestPermissions(PhotoPickerActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
                })
                .setNegativeButton(R.string.dialog_permission_cancel, (dialog, which) -> {
                    dialog.dismiss();
                    PhotoPickerActivity.this.finish();
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
        String title = getResources().getString(R.string.dialog_deny_title);
        String description = getResources().getString(R.string.dialog_deny_description);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(description)
                .setPositiveButton(R.string.dialog_deny_go_setting, (dialog, which) -> {
                    dialog.dismiss();

                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.fromParts("package", this.getPackageName(), null));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.startActivity(intent);

                    if (type == 1) PhotoPickerActivity.this.finish();
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
        if (folderModels.get(0).getFolderPhotos().size() == 0) return;
        if (bottomSheetDialog == null) {
            bottomSheetDialog = new BottomSheetDialog(this);
            bottomAdapter.resetData(folderModels);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int permissionResult = grantResults[0];
        switch (requestCode) {
            case PERMISSION_REQUEST_STORAGE:
                if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                    singleExecutor.execute(scanPhotoRunnable);
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                        showDescriptionDialog(1);
                    } else {
                        showDeniedDialog(1);
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
                case ACTIVITY_REQUEST_PREVIEW:
                    if (data != null) {
                        selectedPhotos = data.getParcelableArrayListExtra(PhotoConfig.PREVIEW_ADD_PHOTOS);
                        List<PhotoModel> deletePhotos = data.getParcelableArrayListExtra(PhotoConfig.PREVIEW_DELETE_PHOTOS);
                        // delete
                        for (int i = 0; i < deletePhotos.size(); i++) {
                            int index = multiAdapter.getDatas().indexOf(deletePhotos.get(i));
                            multiAdapter.getDatas().get(index).setPickNumber(0);
                            multiAdapter.notifyItemChanged(index);
                        }
                        // add
                        for (int j = 0; j < selectedPhotos.size(); j++) {
                            PhotoModel photo = selectedPhotos.get(j);
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
        if (!singleExecutor.isShutdown()) {
            singleExecutor.shutdown();
            singleExecutor = null;
        }
        super.onDestroy();
    }
}
