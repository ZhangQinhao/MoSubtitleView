package com.monke.mosubtitleviewlib;

import android.os.Parcel;
import android.os.Parcelable;

public class SubtitleItemBean implements Parcelable {
    private long startTime;  //相对音乐的开始时间
    private String content;

    public SubtitleItemBean() {
    }

    public SubtitleItemBean(long startTime, String content) {
        this.startTime = startTime;
        this.content = content;
    }

    protected SubtitleItemBean(Parcel in) {
        startTime = in.readLong();
        content = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(startTime);
        dest.writeString(content);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubtitleItemBean> CREATOR = new Creator<SubtitleItemBean>() {
        @Override
        public SubtitleItemBean createFromParcel(Parcel in) {
            return new SubtitleItemBean(in);
        }

        @Override
        public SubtitleItemBean[] newArray(int size) {
            return new SubtitleItemBean[size];
        }
    };

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
