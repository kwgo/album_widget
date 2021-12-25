package com.jchip.album.photo.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import com.jchip.album.photo.common.PhotoConfig;
import com.jchip.album.photo.model.FolderModel;
import com.jchip.album.photo.model.PhotoModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PhotoScanner
 */
public final class PhotoScanner {
    private static final String TAG = PhotoScanner.class.getSimpleName();

    private String[] PROJECTTION_IMAGES = new String[]{
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DESCRIPTION,
            MediaStore.Images.Media.IS_PRIVATE,
            MediaStore.Images.Media.LATITUDE,
            MediaStore.Images.Media.LONGITUDE,
            MediaStore.Images.Media.MINI_THUMB_MAGIC,
            MediaStore.Images.Media.ORIENTATION,
            MediaStore.Images.Media.PICASA_ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media._ID
    };
    private static PhotoScanner scanner;
    private int pickColor;
    private boolean showGif;

    private PhotoScanner(int pickColor, boolean showGif) {
        this.pickColor = pickColor;
        this.showGif = showGif;
    }

    public static PhotoScanner instances(int pickColor, boolean showGif) {
        if (scanner == null) {
            synchronized (PhotoScanner.class) {
                if (scanner == null) {
                    scanner = new PhotoScanner(pickColor, showGif);
                }
            }
        }
        return scanner;
    }

    public List<FolderModel> getPhotoAlbum(@NonNull Context context, String allFolderName) {
        // INTERNAL_CONTENT_URI ; EXTERNAL_CONTENT_URI
        Cursor mCursor = MediaStore.Images.Media.query(
                context.getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                PROJECTTION_IMAGES, null, MediaStore.Images.ImageColumns._ID);
        if (mCursor == null) return new ArrayList<>();

        Map<String, FolderModel> albumFolderMap = new HashMap<>();
        FolderModel allFolderModel = new FolderModel();
        allFolderModel.setFolderName(allFolderName);
        allFolderModel.setPickColor(pickColor);
        allFolderModel.setCheck(true);

        while (mCursor.moveToNext()) {
            String imgBucketDisplayName = mCursor.getString(0);
            int imgBucketId = mCursor.getInt(1);
            int imgDateToken = mCursor.getInt(2);
            String imgDescription = mCursor.getString(3) == null ? "" : mCursor.getString(3);
            int imgIsPrivate = mCursor.getInt(4);
            double imgLat = mCursor.getDouble(5);
            double imgLng = mCursor.getDouble(6);
            int imgMiniThumbMagic = mCursor.getInt(7);
            int imgOrientation = mCursor.getInt(8);
            String imgPicasaId = mCursor.getString(9);
            String imgData = mCursor.getString(10);
            long imgDateAdded = mCursor.getLong(11);
            long imgDateModified = mCursor.getLong(12);
            String imgDisplayName = mCursor.getString(13);
            int imgHeight = mCursor.getInt(14);
            String imgMimeType = mCursor.getString(15);
            int imgWidth = mCursor.getInt(16);
            long imgSize = mCursor.getLong(17);
            String imgTitle = mCursor.getString(18);
            int imgId = mCursor.getInt(19);
            /*
                imgBucketDisplayName --> Download
                imgBucketId          --> 540528482
                imgDateToken         --> 1482082104
                imgDescription       --> null
                imgIsPrivate         --> 0
                imgLat               --> 0.0
                imgLon               --> 0.0
                imgMiniThumbMagic    --> 0
                imgOrientation       --> 0
                imgPicasaId          --> null
                imgData              --> /storage/emulated/0/Download/mobile01_20171026_6.jpg
                imgDateAdded         --> 1530342373
                imgDateModified      --> 1509015603
                imgDisplayName       --> mobile01_20171026_6.jpg
                imgHeight            --> 480
                imgMimeType          --> image/jpeg
                imgWidth             --> 720
                imgSize              --> 236972
                imgTitle             --> mobile01_20171026_6
                imgId                --> 65
            */
            // photo info.
            PhotoModel photo = new PhotoModel(imgBucketDisplayName, imgId, imgDescription, imgLat, imgLng,
                    imgOrientation, imgDateAdded, imgDateModified, imgDisplayName,
                    imgWidth, imgHeight, imgSize, imgMimeType, imgIsPrivate == 1,
                    imgData, false, 0, pickColor);
            // add into all photos folder
            // if show .gif
            if (imgMimeType.equals(PhotoConfig.GIF) && !showGif) continue;
            allFolderModel.getFolderPhotos().add(photo);

            // get all photo folders on the device
            FolderModel folderModel = albumFolderMap.get(imgBucketDisplayName);
            if (folderModel == null) {
                folderModel = new FolderModel();
                folderModel.setFolderId(imgBucketId);
                folderModel.setFolderName(imgBucketDisplayName);
                folderModel.getFolderPhotos().add(photo);
                folderModel.setPickColor(pickColor);
                albumFolderMap.put(imgBucketDisplayName, folderModel);
            } else {
                folderModel.getFolderPhotos().add(photo);
            }
        }

        mCursor.close();
        List<FolderModel> list = new ArrayList<>();
        list.add(allFolderModel);

        // category photo folders
        for (Map.Entry<String, FolderModel> folderEntry : albumFolderMap.entrySet()) {
            FolderModel folderModel = folderEntry.getValue();
            list.add(folderModel);
        }
        return list;
    }

    public PhotoModel getSinglePhoto(@NonNull Context context, @NonNull Uri uri) {
        Cursor mCursor = MediaStore.Images.Media.query(
                context.getContentResolver(),
                uri, PROJECTTION_IMAGES, null, MediaStore.Images.ImageColumns._ID);
        if (mCursor == null) return null;

        PhotoModel photo = null;
        while (mCursor.moveToNext()) {
            String imgBucketDisplayName = mCursor.getString(0);
            int imgBucketId = mCursor.getInt(1);
            int imgDateToken = mCursor.getInt(2);
            String imgDescription = mCursor.getString(3) == null ? "" : mCursor.getString(3);
            int imgIsPrivate = mCursor.getInt(4);
            double imgLat = mCursor.getDouble(5);
            double imgLng = mCursor.getDouble(6);
            int imgMiniThumbMagic = mCursor.getInt(7);
            int imgOrientation = mCursor.getInt(8);
            String imgPicasaId = mCursor.getString(9);
            String imgData = mCursor.getString(10);
            long imgDateAdded = mCursor.getLong(11);
            long imgDateModified = mCursor.getLong(12);
            String imgDisplayName = mCursor.getString(13);
            int imgHeight = mCursor.getInt(14);
            String imgMimeType = mCursor.getString(15);
            int imgWidth = mCursor.getInt(16);
            long imgSize = mCursor.getLong(17);
            String imgTitle = mCursor.getString(18);
            int imgId = mCursor.getInt(19);

            photo = new PhotoModel(imgBucketDisplayName, imgId, imgDescription, imgLat, imgLng,
                    imgOrientation, imgDateAdded, imgDateModified, imgDisplayName,
                    imgWidth, imgHeight, imgSize, imgMimeType, imgIsPrivate == 1,
                    imgData, false, 0, pickColor);
        }
        mCursor.close();
        return photo;
    }
}
