package com.jchip.album.layer;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.jchip.album.R;
import com.jchip.album.data.AlbumData;
import com.jchip.album.data.WidgetData;
import com.jchip.album.view.AlbumView;
import com.jchip.album.view.PhotoView;
import com.jchip.album.view.PhotoViewConfig;
import com.jchip.album.widget.WidgetProvider;

public class SlideshowLayer extends DataLayer {
    private static int[] slidePeriods = {
            0, 1, 2, 3, 4, 5,
            1 * 10, 2 * 10, 3 * 10, 4 * 10, 5 * 10,
            1 * 60, 2 * 60, 3 * 60, 4 * 60, 5 * 60,
            10 * 60, 20 * 60, 30 * 60, 40 * 60, 50 * 60,
            1 * 360, 2 * 360, 3 * 360, 4 * 360, 5 * 360, 6 * 360, 12 * 360, 24 * 360
    };

    private static int photoIndex = 0;
    private boolean isAlbum = true;
    private CountDownTimer slideTimer;
    private int slideSpeed = PhotoViewConfig.DEFAULT_SLIDESHOW_SPEED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(PhotoViewConfig.LAYER_PHOTO_SLIDESHOW, R.layout.album_slideshow_layer);
        super.setStatusBarColor(android.R.color.transparent);
    }

    @Override
    public void initContentView() {
        Intent intent = this.getIntent();
        WidgetData widgetData = (WidgetData) intent.getSerializableExtra(WidgetProvider.WIDGET_ITEM);
        if (widgetData != null) {
            this.isAlbum = widgetData.isAlbum();
            if (this.isAlbum) {
                AlbumData albumData = new AlbumData();
                albumData.setAlbumId(widgetData.getAlbumId());
                this.album = new AlbumView(this, albumData, this.layer);
                this.album.setPhotoViews(this.queryPhotos());
                this.photoIndex = this.album.getPhotoSize() > 0 ? this.photoIndex % this.album.getPhotoSize() : 0;
                this.photo = this.album.getPhotoView(this.photoIndex);
            } else {
                AlbumData albumData = new AlbumData();
                albumData.setAlbumId(widgetData.getAlbumId());
                this.album = new AlbumView(this, albumData, this.layer);
                this.photo = new PhotoView(this, widgetData.getPhoto(), this.layer);
            }
        } else {
            this.isAlbum = true;
            AlbumData albumData = (AlbumData) intent.getSerializableExtra(AlbumData.tableName);
            this.album = new AlbumView(this, albumData, this.layer);
            this.album.setPhotoViews(this.queryPhotos());
            this.photoIndex = this.album.getPhotoSize() > 0 ? this.photoIndex % this.album.getPhotoSize() : 0;
            this.photo = this.album.getPhotoView(this.photoIndex);
        }

        this.setPhotoView(this.getView(R.id.photo_view));

        this.getView(R.id.album_slideshow_view).setOnClickListener((v) -> this.onNextSlide());

        this.startTimer();
    }

    private void startTimer() {
        if (this.isAlbum) {
            this.settingData = this.querySetting();
            if (this.settingData.isSaved()) {
                this.slideSpeed = this.settingData.getSlideSpeed();
            }
            if (this.slideTimer != null) {
                this.slideTimer.cancel();
            }
            if (this.slideSpeed > 0) {
                long slidePeriod = slidePeriods[this.slideSpeed] * 1000L;
                this.slideTimer = new CountDownTimer(slidePeriod, slidePeriod) {
                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        onSlideshow();
                    }
                }.start();
            }
        }
    }

    private void onSlideshow() {
        if (this.album != null && this.album.getPhotoSize() > 0) {
            this.startTimer();
            this.photoIndex = (this.photoIndex + 1) % this.album.getPhotoSize();
            this.photo = this.album.getPhotoView(this.photoIndex);
            this.setPhotoView(this.getView(R.id.photo_view));
        }
    }

    private void onScaleChange() {
        if (this.photo != null) {
            this.photo.setPhotoImage(-1, -1, (this.photo.getScaleIndex() + 1) % 4);
            this.setPhotoView(this.getView(R.id.photo_view));
        }
    }

    private void onNextSlide() {
        if (this.isAlbum) {
            this.onSlideshow();
        } else {
            this.onScaleChange();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.setKeepScreenOn(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.setKeepScreenOn(false);
    }

    @Override
    protected void onDestroy() {
        if (this.slideTimer != null) {
            this.slideTimer.cancel();
        }
        super.onDestroy();
    }

    private void setKeepScreenOn(boolean keep) {
        this.getView(R.id.album_slideshow_view).setKeepScreenOn(keep);
    }

//    private void setKeepScreenOn(boolean keep) {
//        if (keep) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        } else {
//            getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        }
//    }
}