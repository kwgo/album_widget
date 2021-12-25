package com.jchip.album.photo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * PhotoView Number
 */
public class PhotoNumberView extends View {
    private Paint paint;
    private boolean isDrawIndex = false;
    // Circle radius : 12dp
    private float radius = dp2px(12);
    // Circle margin : 9dp
    private float margin = dp2px(9);
    // Circle StrokeWidth : 1.5dp
    private float strokeWidth = dp2px(1.5f);
    // Circle pick color
    private int pickColor;
    // Number
    private int number = 0;
    // TextSize : 13sp
    private float textSize = sp2px(13f);

    public PhotoNumberView(Context context) {
        this(context, null);
    }

    public PhotoNumberView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoNumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PhotoNumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.EXACTLY) {
            widthSize = (int) ((radius + margin) * 2);
        } else if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.AT_MOST) {
            heightSize = (int) ((radius + margin) * 2);
        } else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            widthSize = (int) ((radius + margin) * 2);
            heightSize = (int) ((radius + margin) * 2);
        } else {
            // don't do anything
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        // Circle center
        float x = width / 2;
        float y = height / 2;

        if (isDrawIndex) {
            // draw has number
            paintReset();
            paint.setColor(pickColor);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawCircle(x, y, radius, paint);
            paintReset();
            paint.setColor(Color.WHITE);
            paint.setTextSize(textSize);
            String numberStr = String.valueOf(number);
            Paint.FontMetricsInt fm = paint.getFontMetricsInt();
            int ascent = fm.ascent;
            int descent = fm.descent;
            // calculate text height
            float textHeight = descent - ascent;
            // calculate text width
            float textWidth = paint.measureText(numberStr);
            // calculate x position
            x = (width - textWidth) / 2;
            // calculate y position
            y = height - margin - textHeight / 2;
            canvas.drawText(numberStr, x, y, paint);
        } else {
            // draw no number
            paint.setColor(Color.argb(40, 0, 0, 0));
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawCircle(x, y, radius, paint);
            paintReset();
            paint.setColor(Color.argb(200, 255, 255, 255));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(strokeWidth);
            canvas.drawCircle(x, y, radius, paint);
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

    private int sp2px(float spValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }

    public void setNumber(int number) {
        if (number <= 0) {
            isDrawIndex = false;
            return;
        }
        this.number = number;
        isDrawIndex = true;
        invalidate();
    }

    public void setPickColor(int pickColor) {
        this.pickColor = pickColor;
        invalidate();
    }
}
