package com.jchip.album.photo.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Zoom Image View
 */
public class ZoomImageView extends AppCompatImageView implements ViewTreeObserver.OnGlobalLayoutListener,
        ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {

    private boolean isFirst;     // is first load
    private Context context;
    private int density, statusBarHeight;
    /**
     * zoom range is between initScale ~ maxScale
     */
    private float initScale;   // initial scale
    private float midScale;    // scale on double tap
    private float maxScale;    // max zoom scale
    /**
     * by using matrix to scale an image
     */
    private Matrix scaleMatrix;

    /**
     * gesture detector
     */
    private ScaleGestureDetector scaleGestureDetector;

    // ------------free to move------------
    /**
     * finger pointer count on the last time
     */
    private int lastPointerCount;
    /**
     * the center position of the last time
     */
    private float lastCenterX;
    private float lastCenterY;

    private float touchSlop;
    private boolean isCanDrag;
    private boolean isCheckLeftAndRight;  // left and right edge
    private boolean isCheckTopAndBottom;  // top and bottom edge

    // ------------scale image on double tap------------
    private GestureDetector gestureDetector;

    private boolean isAutoScale;

    // ------------zoom out------------
    private float minScale;        // min scale
    private float maxOverScale;    // max over scale

    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metrics);
        density = (int) metrics.density;
        statusBarHeight = 0;
//        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
//        }

        scaleMatrix = new Matrix();
        // scale by matrix
        setScaleType(ScaleType.MATRIX);
        this.context = context;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (isAutoScale) {
                    return true;
                }

                // get center position to scale
                float x = e.getX();
                float y = e.getY();
                // if scale < midScale then midScale
                if (getScaleValue() < midScale) {
                    //mScaleMatrix.postScale(mMidScale / getScaleValue(), mMidScale / getScaleValue(), x, y);
                    //setImageMatrix(mScaleMatrix);
                    postDelayed(new AutoScaleRunnable(midScale, x, y), 16);
                } else {
                    //mScaleMatrix.postScale(mInitScale / getScaleValue(), mInitScale / getScaleValue(), x, y);
                    //setImageMatrix(mScaleMatrix);
                    postDelayed(new AutoScaleRunnable(initScale, x, y), 16);
                }
                isAutoScale = true;

                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) { // 長按時
                super.onLongPress(e);
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return super.onSingleTapUp(e);
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        if (!isFirst) {
            int width = getWidth();
            int height = getHeight();

            Drawable d = getDrawable();
            if (d == null) return;

            int dw = d.getIntrinsicWidth();
            int dh = d.getIntrinsicHeight();

            float scale = 1.0f;

            if (dw > width && dh < height) {
                scale = width * 1.0f / dw;
            }

            if (dw < width && dh > height) {
                scale = height * 1.0f / dh;
            }

            if (dw == width && dh > height) {
                scale = height * 1.0f / dh;
            }

            if (dw > width && dh == height) {
                scale = width * 1.0f / dw;
            }

            if ((dw > width && dh > height) || (dw < width && dh < height)) {
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            }

            initScale = scale;
            maxScale = initScale * 4;
            midScale = initScale * 2;
            minScale = initScale / 4;
            maxOverScale = initScale * 6.5f;

            int dx = getWidth() / 2 - dw / 2;
            int dy = getHeight() / 2 - dh / 2;

            scaleMatrix.postTranslate(dx, dy);
            scaleMatrix.postScale(initScale, initScale, width / 2, height / 2);
            setImageMatrix(scaleMatrix);

            isFirst = true;
            scaleGestureDetector = new ScaleGestureDetector(context, this);
            setOnTouchListener(this);
            touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        }
    }

    // sacle animation
    private class AutoScaleRunnable implements Runnable {
        private float mTargetScale;
        private float x;
        private float y;

        private final float BIGGER = 1.07f;
        private final float SMALL = 0.93f;

        private float tmpScale;

        private AutoScaleRunnable(float mTargetScale, float x, float y) {
            this.mTargetScale = mTargetScale;
            this.x = x;
            this.y = y;

            if (getScaleValue() < mTargetScale) {
                tmpScale = BIGGER;
            }
            if (getScaleValue() > mTargetScale) {
                tmpScale = SMALL;
            }
        }

        @Override
        public void run() {
            // do scale
            scaleMatrix.postScale(tmpScale, tmpScale, x, y);
            checkBorderAndCenterWhenScale();
            setImageMatrix(scaleMatrix);

            float currentScale = getScaleValue();

            if ((tmpScale > 1.0f && currentScale < mTargetScale) || (tmpScale < 1.0f && currentScale > mTargetScale)) {
                // every 16ms
                postDelayed(this, 16);
            } else {
                float scale = mTargetScale / currentScale;
                scaleMatrix.postScale(scale, scale, x, y);
                checkBorderAndCenterWhenScale();
                setImageMatrix(scaleMatrix);

                isAutoScale = false;
            }
        }
    }

    /**
     * get image current scale
     */
    private float getScaleValue() {
        float[] values = new float[9];
        scaleMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) { // 縮放進行的時候

        float scale = getScaleValue();
        float scaleFactor = detector.getScaleFactor();

        if (getDrawable() == null) {
            return true;
        }

        // (scale < mMaxScale && scaleFactor > 1.0f) || (scale > mInitScale && scaleFactor < 1.0f)
        if ((scale < maxOverScale && scaleFactor > 1.0f) || (scale > minScale && scaleFactor < 1.0f)) {

            //            if (scale * scaleFactor < mInitScale) {
//                // calculation methods, i.e. 5 * y = 20 -> y = 20 / 5
//                scaleFactor = mInitScale / scale;
//            }
            if (scale * scaleFactor < minScale) {  // zoom out back
                scaleFactor = minScale / scale;
            }

            // border check, not allow image scale
//            if (scale * scaleFactor > mMaxScale) {
//                scaleFactor = mMaxScale / scale;
//            }
            if (scale * scaleFactor > maxOverScale) { // zoom in back
                scaleFactor = maxOverScale / scale;
            }
            // by view center
            //mScaleMatrix.postScale(scaleFactor, scaleFactor, getWidth() / 2, getHeight() / 2);
            // by touch point center
            scaleMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            checkBorderAndCenterWhenScale();
            setImageMatrix(scaleMatrix);
        }

        return true;
    }

    // get scaled image rect
    private RectF getMatrixRectF() {
        Matrix matrix = scaleMatrix;
        RectF rectF = new RectF();

        Drawable d = getDrawable();
        if (d != null) {
            rectF.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }

        return rectF;
    }

    private void checkBorderAndCenterWhenScale() {
        if (getDrawable() == null) {
            return;
        }

        // movement value
        float delTaX = 0;
        float delTaY = 0;

        // get view size
        int width = getWidth();
        int height = getHeight();

        // get current image location
        RectF rectF = getMatrixRectF();

        // horizontal
        if (rectF.width() >= width) {
            if (rectF.left > 0) {
                delTaX = -rectF.left;
            }
            if (rectF.right < width) {
                delTaX = width - rectF.right;
            }
        }

        // vertical
        if (rectF.height() >= height) {
            if (rectF.top > 0) {
                delTaY = -rectF.top;
            }

            if (rectF.bottom < height) {
                delTaY = height - rectF.bottom;
            }
        }
        if (rectF.width() < width) {
            delTaX = width / 2 - rectF.right + rectF.width() / 2;
        }
        // center image once the image size < view size
        if (rectF.height() < height) {
            delTaY = height / 2 - rectF.bottom + rectF.height() / 2;
        }

        scaleMatrix.postTranslate(delTaX, delTaY);
    }

    private void checkBorderWhenTranslate() {


        RectF rectF = getMatrixRectF();
        float delTaX = 0.0f;
        float delTaY = 0.0f;

        int width = getWidth();
        int height = getHeight();

        if (rectF.top > 0 && isCheckTopAndBottom) {
            delTaY = -rectF.top;
        }

        if (rectF.bottom < height && isCheckTopAndBottom) {
            delTaY = height - rectF.bottom;
        }

        if (rectF.left > 0 && isCheckLeftAndRight) {
            delTaX = -rectF.left;
        }

        if (rectF.right < width && isCheckLeftAndRight) {
            delTaX = width - rectF.right;
        }
        scaleMatrix.postTranslate(delTaX, delTaY);

    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) { // 縮放開始的時候 要設為 true
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        scaleGestureDetector.onTouchEvent(event);

        float centerX = 0.0f;
        float cenetrY = 0.0f;
        int pointCount = event.getPointerCount();

        for (int i = 0; i < pointCount; i++) {
            centerX += event.getX(i);
            cenetrY += event.getY(i);
        }
        centerX /= pointCount;
        cenetrY /= pointCount;
        if (lastPointerCount != pointCount) {
            lastCenterX = centerX;
            lastCenterY = cenetrY;
            isCanDrag = false;
        }
        lastPointerCount = pointCount;

        RectF rectF = getMatrixRectF();
        float div = 0.01f;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (rectF.width() > getWidth() + div || rectF.height() > getHeight() + div) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (rectF.width() > getWidth() + div || rectF.height() > getHeight() + div) {
                    //if (getParent() instanceof ViewPager) {
                    //getParent().requestDisallowInterceptTouchEvent(true);
                    //}
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                float dx = centerX - lastCenterX;
                float dy = cenetrY - lastCenterY;

                if (!isCanDrag) {
                    isCanDrag = isMoveAction(dx, dy);
                }

                if (isCanDrag) {
                    if (getDrawable() != null) {
                        isCheckLeftAndRight = isCheckTopAndBottom = true;

                        if (rectF.width() < getWidth()) {
                            dx = 0;
                            isCheckLeftAndRight = false;
                        }

                        if (rectF.height() < getHeight()) {
                            dy = 0;
                            isCheckTopAndBottom = false;
                        }

                        scaleMatrix.postTranslate(dx, dy);
                        checkBorderWhenTranslate();
                        setImageMatrix(scaleMatrix);
                    }
                }
                lastCenterX = centerX;
                lastCenterY = cenetrY;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                lastPointerCount = 0;

                if (getScaleValue() < initScale) {
                    post(new AutoScaleRunnable(initScale, getWidth() / 2, getHeight() / 2));
                }
                if (getScaleValue() > maxScale) {
                    post(new AutoScaleRunnable(maxScale, getWidth() / 2, getHeight() / 2));
                }

                break;
        }

        return true;
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Drawable d = getDrawable();
        if (d == null) return;
        // newConfig.screenHeightDp : Subtract statusBar height
        int width = newConfig.screenWidthDp * density;
        int height = newConfig.screenHeightDp * density + statusBarHeight;
        int dw = d.getIntrinsicWidth();
        int dh = d.getIntrinsicHeight();

        float scale = 1.0f;
        if (dw > width && dh < height) {
            scale = width * 1.0f / dw;
        }
        if (dw < width && dh > height) {
            scale = height * 1.0f / dh;
        }
        if (dw == width && dh > height) {
            scale = height * 1.0f / dh;
        }
        if (dw > width && dh == height) {
            scale = width * 1.0f / dw;
        }
        if ((dw > width && dh > height) || (dw < width && dh < height)) {
            scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
        }

        initScale = scale;
        maxScale = initScale * 4;
        midScale = initScale * 2;
        minScale = initScale / 4;
        maxOverScale = initScale * 6.5f;
        // reset
        scaleMatrix.reset();
        int dx = (width - dw) / 2;
        int dy = (height - dh) / 2;
        // translate
        scaleMatrix.postTranslate(dx, dy);
        // scale
        scaleMatrix.postScale(initScale, initScale, width / 2, height / 2);
        setImageMatrix(scaleMatrix);
    }

    /**
     * is on moving?
     *
     * @param dx dx
     * @param dy dy
     * @return isMoveAction
     */
    private boolean isMoveAction(float dx, float dy) {
        return Math.sqrt(dx * dx + dy * dy) > touchSlop;
    }
}
