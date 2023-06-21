package com.monke.mosubtitleviewlib;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class SubtitleBean implements Parcelable {
    private List<SubtitleItemBean> subtitles;

    public SubtitleBean() {
    }

    protected SubtitleBean(Parcel in) {
        subtitles = in.createTypedArrayList(SubtitleItemBean.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(subtitles);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubtitleBean> CREATOR = new Creator<SubtitleBean>() {
        @Override
        public SubtitleBean createFromParcel(Parcel in) {
            return new SubtitleBean(in);
        }

        @Override
        public SubtitleBean[] newArray(int size) {
            return new SubtitleBean[size];
        }
    };

    public List<SubtitleItemBean> getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(List<SubtitleItemBean> subtitles) {
        this.subtitles = subtitles;
    }
}
