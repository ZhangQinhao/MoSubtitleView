package com.monke.mosubtitleviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class MoSubtitleItemView extends LinearLayout {
    public MoSubtitleItemView(Context context) {
        this(context, null);
    }

    public MoSubtitleItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoSubtitleItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MoSubtitleItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private int childCount = 0;

    private void init(AttributeSet attr) {
        int tempChildCount = 0;
        if (attr != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attr, R.styleable.MoSubtitleItemView);
            tempChildCount = typedArray.getInt(R.styleable.MoSubtitleItemView_mosubtitle_childCount, 0);
            typedArray.recycle();
        }
        setOrientation(LinearLayout.VERTICAL);
        setChildCount(tempChildCount);
    }

    public void setChildCount(int childCount) {
        if (childCount < 0) {
            return;
        }
        if (this.childCount != childCount) {
            removeAllViews();
            this.childCount = childCount;
            if (childCount > 0) {
                for (int i = 0; i < childCount; i++) {
                    MoAutoSizeTextView moAutoSizeTextView = new MoAutoSizeTextView(getContext(), getResources().getDimension(R.dimen.mosubtitle_max_textsize), getResources().getDimension(R.dimen.mosubtitle_min_textsize), getResources().getDimension(R.dimen.mosubtitle_step_textsize));
                    moAutoSizeTextView.setTextColor(Color.BLACK);
                    moAutoSizeTextView.setVisibility(View.GONE);
                    LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    addView(moAutoSizeTextView, layoutParams);
                }
            }
        }
    }

    private int color = Color.BLACK;

    public void setTextColor(int color) {
        if (this.color != color) {
            if (getChildCount() > 0) {
                for (int i = 0; i < getChildCount(); i++) {
                    MoAutoSizeTextView v = getChildView(i);
                    if (v != null) {
                        v.setTextColor(color);
                    }
                }
            }
        }
    }

    /**
     * 设置文字用该方法
     *
     * @param v
     * @param t
     */
    private void setVText(MoAutoSizeTextView v, String t) {
        if (TextUtils.isEmpty(t)) {
            setVT(v, "");
            setVis(v, View.GONE);
        } else {
            setVT(v, t);
            setVis(v, View.VISIBLE);
        }
    }

    public void setTexts(String... t) {
        for (int i = 0; i < Math.min(getChildCount(), t.length); i++) {
            setVText((MoAutoSizeTextView) getChildAt(i), t[i]);
        }
    }

    public MoAutoSizeTextView addText(String t) {
        if (!TextUtils.isEmpty(t)) {
            for (int i = 0; i < getChildCount(); i++) {
                if (getChildAt(i).getVisibility() == View.GONE) {
                    MoAutoSizeTextView moAutoSizeTextView = (MoAutoSizeTextView) getChildAt(i);
                    setVText(moAutoSizeTextView, t);
                    return moAutoSizeTextView;
                }
            }
        }
        return null;
    }

    public boolean isFull() {
        if (getChildCount() <= 0) {
            return true;
        }
        if (getChildAt(getChildCount() - 1).getVisibility() == View.VISIBLE) {
            return true;
        } else {
            return false;
        }
    }

    public MoAutoSizeTextView getChildView(int index) {
        if (index < getChildCount()) {
            View v = getChildAt(index);
            if (v == null || !(v instanceof MoAutoSizeTextView)) {
                return null;
            }
            return (MoAutoSizeTextView) v;
        }
        return null;
    }

    public int getChildViewHeight(int index) {
        if (index < getChildCount()) {
            return getChildAt(index).getMeasuredHeight();
        }
        return 0;
    }

    public boolean childIsUsed(int index) {
        if (index < getChildCount()) {
            return getChildAt(index).getVisibility() == View.VISIBLE;
        }
        return true;
    }

    public void clearData() {
        for (int i = 0; i < getChildCount(); i++) {
            MoAutoSizeTextView moAutoSizeTextView = (MoAutoSizeTextView) getChildAt(i);
            setVText(moAutoSizeTextView, "");
        }
    }

    public void clearAll() {
        for (int i = 0; i < getChildCount(); i++) {
            MoAutoSizeTextView moAutoSizeTextView = (MoAutoSizeTextView) getChildAt(i);
            setVText(moAutoSizeTextView, "");
            moAutoSizeTextView.clearAnimation();
        }
        clearAnimation();
    }

    public void clearAnim() {
        for (int i = 0; i < getChildCount(); i++) {
            MoAutoSizeTextView moAutoSizeTextView = (MoAutoSizeTextView) getChildAt(i);
            moAutoSizeTextView.clearAnimation();
        }
        clearAnimation();
    }

    private void setVis(View v, int s) {
        if (v.getVisibility() != s) {
            v.setVisibility(s);
        }
    }

    private void setVT(MoAutoSizeTextView v, String t) {
        if (!TextUtils.equals(v.getText(), t)) {
            v.setText(t);
        }
    }
}
