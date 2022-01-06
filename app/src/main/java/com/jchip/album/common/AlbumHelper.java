package com.jchip.album.common;

import android.Manifest;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

public class AlbumHelper {
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
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

    public static void alert(Context context, int titleId, int detailId, Runnable work) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(titleId);
        alert.setMessage(detailId);
        alert.setPositiveButton(android.R.string.yes, (dialog, which) -> {
            work.run();
            dialog.dismiss();
        }).show();
    }

    public static boolean isLetterIncluded(String text) {
        boolean letter = false;
        boolean special = false;
        if (text != null) {
            for (char ch : text.toCharArray()) {
                if (ch >= 'a' && ch <= 'z' && ch >= 'A' && ch <= 'Z') {
                    letter = true;
                }
                if (((int) ch) > 127) {
                    special = true;
                }
            }
        }
        return letter || !special;
    }

    public static void toast(Context context, int textId) {
        Toast toast = Toast.makeText(context, textId, Toast.LENGTH_LONG);
        // toast.setGravity(Gravity.CENTER, 0, 0);
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        messageTextView.setTextSize(14);
        toast.show();
    }

    public static void updateWidget(Context context, Class<?> widgetProvider) {
        try {
            Intent intent = new Intent(context, widgetProvider);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            ComponentName componentName = new ComponentName(context, widgetProvider);
            int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(componentName);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            context.sendBroadcast(intent);
        } catch (Exception ignore) {
        }
    }

    public static void setStatusBarColor(Activity activity, int colorId) {
        try {
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(colorId));
        } catch (Exception ignore) {
        }
    }

    public static void exitApp(Activity activity) {
        try {
            //activity.moveTaskToBack(true);
            activity.finishAndRemoveTask();
        } catch (Exception ignore) {
        }
    }
}
