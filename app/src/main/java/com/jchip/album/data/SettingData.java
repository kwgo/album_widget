package com.jchip.album.data;

public class SettingData extends AbstractData {
    public static final String tableName = "setting";
    public static final String fieldSettingId = "id";
    public static final String fieldSlideSpeed = "slideSpeed";
    public static final String fieldBackgroundColor = "backgroundColor";
    public static final String fieldNameBackground = "nameBackground";
    public static final String fieldFrameIndex = "frameIndex";
    public static final String fieldAutoRefresh = "autoRefresh";

    private int settingId = -1;
    private int slideSpeed = 5;
    private int backgroundColor = 0x1F2229;
    private int nameBackground = 0x434449;
    private int frameIndex = 0;
    private int autoRefresh = 1;

    public SettingData() {
    }

    public boolean isSaved() {
        return settingId >= 0;
    }

    public int getSettingId() {
        return settingId;
    }

    public void setSettingId(int albumId) {
        this.settingId = albumId;
    }

    public int getSlideSpeed() {
        return slideSpeed;
    }

    public void setSlideSpeed(int slideSpeed) {
        this.slideSpeed = slideSpeed;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getNameBackground() {
        return nameBackground;
    }

    public void setNameBackground(int nameBackground) {
        this.nameBackground = nameBackground;
    }

    public int getFrameIndex() {
        return frameIndex;
    }

    public void setFrameIndex(int frameIndex) {
        this.frameIndex = frameIndex;
    }

    public int getAutoRefresh() {
        return autoRefresh;
    }

    public void setAutoRefresh(int autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

}
