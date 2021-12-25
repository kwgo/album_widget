package com.jchip.album.photo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * PhotoView
 */
public class PhotoBorderView extends View {
    private Paint paint;
    private Path borderPath;
    // draw border
    private boolean isDraw = false;
    // border stroke width : 5dp
    private float strokeWidth = dp2px(2.5f);
    // border color
    private int borderColor = Color.argb(200, 255, 153, 0);

    public PhotoBorderView(Context context) {
        this(context, null);
    }

    public PhotoBorderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoBorderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PhotoBorderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        paint = new Paint();
        borderPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = getWidth();
        float height = getHeight();

        if (isDraw) {
            // draw background
            paintReset();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.argb(140, 0, 0, 0));
            canvas.drawRect(0, 0, width, height, paint);
            // draw border use path
            paintReset();
            paint.setColor(borderColor);
            // top
            borderPath.addRect(0, 0, width, strokeWidth, Path.Direction.CCW);
            // bottom
            borderPath.addRect(0, height - strokeWidth, width, height, Path.Direction.CCW);
            // left
            borderPath.addRect(0, strokeWidth, strokeWidth, height - strokeWidth, Path.Direction.CCW);
            // right
            borderPath.addRect(width - strokeWidth, strokeWidth, width, height - strokeWidth, Path.Direction.CCW);
            borderPath.addRect(0, strokeWidth, strokeWidth, height, Path.Direction.CCW);
            canvas.drawPath(borderPath, paint);
        } else {
            paint.setAlpha(0);
            canvas.drawRect(0, 0, width, height, paint);
        }
    }

    private void paintReset() {
        paint.reset();
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setDraw(boolean isDraw, int borderColor) {
        this.borderColor = borderColor;
        this.isDraw = isDraw;
        invalidate();
    }
}
