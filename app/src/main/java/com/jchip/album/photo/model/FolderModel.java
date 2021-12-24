package com.jchip.album.photo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.jchip.album.R;
import com.jchip.album.photo.adapter.factory.IItemType;

import java.util.ArrayList;

/**
 * FolderModel
 */

public class FolderModel implements Parcelable, IItemType {
    public static final int FOLDER_ITEM = R.layout.photo_folder_item;

    private int folderId;
    private String folderName;
    private ArrayList<PhotoModel> folderPhotos = new ArrayList<>();
    private boolean isCheck;
    private int pickColor;

    public FolderModel() {
    }

    protected FolderModel(Parcel in) {
        folderId = in.readInt();
        folderName = in.readString();
        folderPhotos = in.createTypedArrayList(PhotoModel.CREATOR);
        isCheck = in.readByte() != 0;
        pickColor = in.readInt();
    }

    public static final Creator<FolderModel> CREATOR = new Creator<FolderModel>() {
        @Override
        public FolderModel createFromParcel(Parcel in) {
            return new FolderModel(in);
        }

        @Override
        public FolderModel[] newArray(int size) {
            return new FolderModel[size];
        }
    };

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public ArrayList<PhotoModel> getFolderPhotos() {
        return folderPhotos;
    }

    public void setFolderPhotos(ArrayList<PhotoModel> folderPhotos) {
        this.folderPhotos = folderPhotos;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getPickColor() {
        return pickColor;
    }

    public void setPickColor(int pickColor) {
        this.pickColor = pickColor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(folderId);
        dest.writeString(folderName);
        dest.writeTypedList(folderPhotos);
        dest.writeByte((byte) (isCheck ? 1 : 0));
        dest.writeInt(pickColor);
    }

    @Override
    public String toString() {
        return "AlbumFolder{" +
                "folderId=" + folderId +
                ", folderName='" + folderName + '\'' +
                ", folderPhotos=" + folderPhotos +
                ", isCheck=" + isCheck +
                ", pickColor=" + pickColor +
                '}';
    }

    @Override
    public int itemLayout() {
        return FOLDER_ITEM;
    }
}
