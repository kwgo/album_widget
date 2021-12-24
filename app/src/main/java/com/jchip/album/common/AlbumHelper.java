package com.jchip.album.common;

import android.Manifest;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.jchip.album.R;
import com.jchip.album.photo.PhotoPicker;
//import com.rayzhang.android.rzalbum.RZAlbum;

public class AlbumHelper {
    public static final int ALBUM_REQUEST_CODE = 1;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public static void selectPhotos(Activity activity) {
        Log.d("", "selectPhotos ==============================");
        /**
         * @param ofAppName             : (required)
         * @param setLimitCount         : (choose)   (default:5)
         * @param setSpanCount          : (choose)   (default:3)
         * @param setStatusBarColor     : (choose)   (default:#ff673ab7)
         * @param setToolBarColor       : (choose)   (default:#ff673ab7)
         * @param setToolBarTitle       : (choose)   (default:RZAlbum)
         * @param setPickColor          : (choose)   (default:#ffffc107)
         * @param setPreviewOrientation : (choose)   (default:ORIENTATION_AUTO)
         * @param setAllFolderName      : (choose)   (default:All Photos)
         * @param setDialogIcon         : (choose)   (default:none)
         * @param showCamera            : (choose)   (default:true)
         * @param showGif               : (choose)   (default:true)
         * @param start                 : (required)
         */
        PhotoPicker.ofAppName("RZ - Album")
                .setLimitCount(12)
                .setSpanCount(3)
                .setStatusBarColor(Color.parseColor("#AD1457"))
                .setToolBarColor(Color.parseColor("#D81B60"))
                .setToolBarTitle("Album")
                .setPickerColor(0x000000)
                //.setPickerColor(18);
                //.setPickColor(Color.argb(255, 153, 51, 255))
                //.setDialogIcon(R.drawable.ic_bird_shape_30_3dp)
                .setDialogIcon(R.drawable.widget_icon)

                //    .setPreviewOrientation(RZAlbum.ORIENTATION_PORTRATI)
                .setAllFolderName("Photos")
                .showCamera(true)
                .showGif(false)
                .start(activity, ALBUM_REQUEST_CODE);
    }

    public static void alert(Context context, int titleId, int detailId, Runnable work) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
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

    public static String createFont() {
        String[] font = {
                "niconne_regular", "anton_regular", "macondo_egular",
                "frederickathe_great", "ole_regular", "wind_song_medium"
        };
        String[] align = {"start|center_vertical", "center", "end|center_vertical"};

        StringBuilder sb = new StringBuilder();
        for (int fontIndex = 0; fontIndex < font.length; fontIndex++) {
            sb.append(" <LinearLayout");
            sb.append(" android:id=\"@+id/label_font_").append(fontIndex).append("\"");
            sb.append(" android:layout_width=\"wrap_content\"");
            sb.append(" android:layout_height=\"wrap_content\"");
            sb.append(" android:orientation=\"vertical\">");
            for (int alignIndex = 0; alignIndex < align.length; alignIndex++) {
                sb.append(" <TextView");
                sb.append(" android:id=\"@+id/photo_label_").append(fontIndex * align.length + alignIndex).append("\"");
                sb.append(" android:gravity=\"").append(align[alignIndex]).append("\"");
                sb.append(" android:fontFamily=\"@font/").append(font[fontIndex]).append("\"");
                sb.append(" style=\"@style/widget_photo_label\"/>");
            }
            sb.append(" </LinearLayout>");
        }
        Log.d("", sb.toString());
        return sb.toString();
    }

    public static void updateWidget(Context context, Class widgetProvider) {
        try {
            Intent intent = new Intent(context, widgetProvider);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            ComponentName componentName = new ComponentName(context, widgetProvider);
            int appWidgetIds[] = AppWidgetManager.getInstance(context).getAppWidgetIds(componentName);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            context.sendBroadcast(intent);
        } catch (Exception ex) {
        }
    }

    public static void setStatusBarColor(Activity activity, int colorId) {
        try {
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(colorId));
        } catch (Exception ex) {
        }
    }
}
