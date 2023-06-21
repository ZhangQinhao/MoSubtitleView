package com.monke.mosubtitleviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class MoSubtitleView extends ViewGroup {
    public MoSubtitleView(Context context) {
        this(context, null);
    }

    public MoSubtitleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoSubtitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MoSubtitleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private MoSubtitleItemView subBottom;
    private MoSubtitleItemView subLeft;
    private MoSubtitleItemView subLeftTop;
    private MoSubtitleItemView subRight;
    private MoSubtitleItemView subRightTop;

    private LayoutParams bottomParams;
    private LayoutParams leftParams;
    private LayoutParams leftTopParams;
    private LayoutParams rightParams;
    private LayoutParams rightTopParams;

    private int itemWidth = 600;
    private int childCount = 3;
    private int textColor = Color.BLACK;

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MoSubtitleView);
            itemWidth = typedArray.getDimensionPixelOffset(R.styleable.MoSubtitleView_mosubtitle_item_width, itemWidth);
            childCount = typedArray.getInteger(R.styleable.MoSubtitleView_mosubtitle_childCount, childCount);
            textColor = typedArray.getColor(R.styleable.MoSubtitleView_android_textColor, textColor);
            typedArray.recycle();
        }

        LayoutInflater.from(getContext()).inflate(R.layout.layout_subtitle_view, this, true);

        subBottom = findViewById(R.id.sub_bottom);
        subBottom.setChildCount(childCount);
        subBottom.setTextColor(textColor);

        subLeft = findViewById(R.id.sub_left);
        subLeft.setChildCount(childCount);
        subLeft.setTextColor(textColor);

        subLeftTop = findViewById(R.id.sub_left_top);
        subLeftTop.setChildCount(childCount);
        subLeftTop.setTextColor(textColor);

        subRight = findViewById(R.id.sub_right);
        subRight.setChildCount(childCount);
        subRight.setTextColor(textColor);

        subRightTop = findViewById(R.id.sub_right_top);
        subRightTop.setChildCount(childCount);
        subRightTop.setTextColor(textColor);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        bottomParams = subBottom.getLayoutParams();
        bottomParams.width = itemWidth;

        leftParams = subLeft.getLayoutParams();
        leftParams.width = itemWidth;

        leftTopParams = subLeftTop.getLayoutParams();
        leftTopParams.width = itemWidth;

        rightParams = subRight.getLayoutParams();
        rightParams.width = itemWidth;

        rightTopParams = subRightTop.getLayoutParams();
        rightTopParams.width = itemWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY && MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int centerX = getMeasuredWidth() / 2;
        int subBottom1Height = subBottom.getChildViewHeight(0);
        int subBottom234Height = subBottom.getMeasuredHeight() - subBottom1Height;

        subBottom.layout(centerX - subBottom.getMeasuredWidth() / 2,
                getMeasuredHeight() - subBottom.getMeasuredHeight(),
                centerX + subBottom.getMeasuredWidth() / 2,
                getMeasuredHeight());

        subLeft.layout(centerX - subLeft.getMeasuredWidth() / 2,
                getMeasuredHeight() - subBottom234Height - subLeft.getMeasuredHeight(),
                centerX + subLeft.getMeasuredWidth() / 2,
                getMeasuredHeight() - subBottom234Height);
        subLeft.setPivotY(subLeft.getMeasuredHeight());

        subRight.layout(centerX - subRight.getMeasuredWidth() / 2,
                getMeasuredHeight() - subBottom234Height - subRight.getMeasuredHeight(),
                centerX + subRight.getMeasuredWidth() / 2,
                getMeasuredHeight() - subBottom234Height);
        subRight.setPivotX(subRight.getMeasuredWidth());
        subRight.setPivotY(subRight.getMeasuredHeight());

        subLeftTop.layout(centerX - subBottom.getMeasuredWidth() / 2 - subLeft.getMeasuredHeight() - subLeftTop.getMeasuredWidth() + subLeft.getChildViewHeight(0),
                getMeasuredHeight() - subBottom234Height - subLeft.getMeasuredWidth() - subLeftTop.getMeasuredHeight(),
                centerX - subBottom.getMeasuredWidth() / 2 - subLeft.getMeasuredHeight() + subLeft.getChildViewHeight(0),
                getMeasuredHeight() - subBottom234Height - subLeft.getMeasuredWidth());
        subRightTop.layout(centerX + subBottom.getMeasuredWidth() / 2 + subRight.getMeasuredHeight() - subRight.getChildViewHeight(0),
                getMeasuredHeight() - subBottom234Height - subRight.getMeasuredWidth() - subRightTop.getMeasuredHeight(),
                centerX + subBottom.getMeasuredWidth() / 2 + subRight.getMeasuredHeight() - subRight.getChildViewHeight(0) + subRightTop.getMeasuredWidth(),
                getMeasuredHeight() - subBottom234Height - subRight.getMeasuredWidth());
    }

    private SubtitleBean subtitleData;
    private int curSubtitleIndex = -1;

    private boolean nextSideLeft(int index) {
        if (index < 0) {
            return true;
        } else {
            if ((index / childCount) % 2 == 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void setSubtitleData(SubtitleBean subtitleData) {
        release();
        this.subtitleData = subtitleData;
    }


    public void updateTime(long time) {
        if (subtitleData != null && subtitleData.getSubtitles() != null && subtitleData.getSubtitles().size() > 0) {
            int tempIndex = -1;
            for (int i = subtitleData.getSubtitles().size() - 1; i >= 0; i--) {
                if (subtitleData.getSubtitles().get(i).getStartTime() <= time) {
                    tempIndex = i;
                    break;
                }
            }
            if (tempIndex == curSubtitleIndex) {
                //相同状态不需要处理
                return;
            } else {
                if (tempIndex < 0) {
                    //无效
                    release();
                } else {
                    if (tempIndex < curSubtitleIndex) {
                        //进度向前
                        release();
                        addText(tempIndex, false);
                    } else {
                        //进度向后
                        if (tempIndex - curSubtitleIndex == 1) {
                            //添加一个
                            addText(tempIndex, true);
                        } else {
                            release();
                            addText(tempIndex, false);
                        }
                    }
                }
            }
        }
    }

    private void addText(int index, boolean needSideAnim) {
        clearAnim();
        curSubtitleIndex = index;
        boolean leftSide = nextSideLeft(curSubtitleIndex - 1);
        if (subBottom.isFull()) {
            int t = 1;
            String[] leftStr = new String[subLeft.getChildCount()];
            for (int i = leftStr.length - 1; i >= 0; i--) {
                leftStr[i] = getIndexText(curSubtitleIndex - t);
                t++;
            }
            String[] rightStr = new String[subRight.getChildCount()];
            for (int i = rightStr.length - 1; i >= 0; i--) {
                rightStr[i] = getIndexText(curSubtitleIndex - t);
                t++;
            }
            if (leftSide) {
                subLeft.setTexts(leftStr);
                subLeftTop.setTexts(rightStr);
                subRight.clearData();
                subRightTop.clearData();
                if (needSideAnim) {
                    //播放左转动画
                    subLeft.startAnimation(getAnimLeft());
                    subLeftTop.startAnimation(getAnimLeftTop());
                }
            } else {
                subRight.setTexts(leftStr);
                subRightTop.setTexts(rightStr);
                subLeft.clearData();
                subLeftTop.clearData();
                if (needSideAnim) {
                    //播放右转动画
                    subRight.startAnimation(getAnimRight());
                    subRightTop.startAnimation(getAnimRightTop());
                }
            }
            subBottom.clearAll();
        }
        if (!subBottom.childIsUsed(0)) {
            MoAutoSizeTextView moAutoSizeTextView = subBottom.addText(subtitleData.getSubtitles().get(curSubtitleIndex).getContent());
            //播放添加动画
            if (moAutoSizeTextView != null) {
                if (leftSide) {
                    moAutoSizeTextView.startAnimation(getAnimBottom1FromRight());
                } else {
                    moAutoSizeTextView.startAnimation(getAnimBottom1FromLeft());
                }
            }
        } else {
            MoAutoSizeTextView moAutoSizeTextView = subBottom.addText(subtitleData.getSubtitles().get(curSubtitleIndex).getContent());
            //播放添加动画
            if (moAutoSizeTextView != null) {
                moAutoSizeTextView.startAnimation(getAnimBottom2());
            }
        }
    }

    private String getIndexText(int i) {
        if (subtitleData == null || subtitleData.getSubtitles() == null) {
            return "";
        } else {
            if (i < 0 || i >= subtitleData.getSubtitles().size()) {
                return "";
            } else {
                return subtitleData.getSubtitles().get(i).getContent();
            }
        }
    }

    public void release() {
        curSubtitleIndex = -1;
        subBottom.clearAll();
        subLeft.clearAll();
        subLeftTop.clearAll();
        subRight.clearAll();
        subRightTop.clearAll();
    }

    public void clearAnim() {
        subBottom.clearAnim();
        subLeft.clearAnim();
        subLeftTop.clearAnim();
        subRight.clearAnim();
        subRightTop.clearAnim();
    }

    private Animation animBottom1FromLeft;

    private Animation getAnimBottom1FromLeft() {
        if (animBottom1FromLeft == null) {
            animBottom1FromLeft = AnimationUtils.loadAnimation(getContext(), R.anim.mo_sub_bottom_1_from_left);
        }
        return animBottom1FromLeft;
    }

    private Animation animBottom1FromRight;

    private Animation getAnimBottom1FromRight() {
        if (animBottom1FromRight == null) {
            animBottom1FromRight = AnimationUtils.loadAnimation(getContext(), R.anim.mo_sub_bottom_1_from_right);
        }
        return animBottom1FromRight;
    }

    private Animation animBottom2;

    private Animation getAnimBottom2() {
        if (animBottom2 == null) {
            animBottom2 = AnimationUtils.loadAnimation(getContext(), R.anim.mo_sub_bottom_2);
        }
        return animBottom2;
    }

    private Animation animLeft;

    private Animation getAnimLeft() {
        if (animLeft == null) {
            animLeft = AnimationUtils.loadAnimation(getContext(), R.anim.mo_sub_left);
        }
        return animLeft;
    }

    private Animation animRight;

    private Animation getAnimRight() {
        if (animRight == null) {
            animRight = AnimationUtils.loadAnimation(getContext(), R.anim.mo_sub_right);
        }
        return animRight;
    }

    private Animation animLeftTop;

    private Animation getAnimLeftTop() {
        animLeftTop = new RotateAnimation(90, 0,
                subLeftTop.getMeasuredWidth() + subLeft.getMeasuredHeight() - subLeft.getChildViewHeight(0), subLeftTop.getMeasuredHeight() + subLeft.getMeasuredWidth());
        animLeftTop.setInterpolator(new LinearInterpolator());
        animLeftTop.setDuration(200);
        animLeftTop.setFillAfter(false);
        return animLeftTop;
    }

    private Animation animRightTop;

    private Animation getAnimRightTop() {
        if (animRightTop == null) {
            animRightTop = new RotateAnimation(-90, 0,
                    -subRight.getMeasuredHeight() + subRight.getChildViewHeight(0), subRightTop.getMeasuredHeight() + subRight.getMeasuredWidth());
            animRightTop.setInterpolator(new LinearInterpolator());
            animRightTop.setDuration(200);
            animRightTop.setFillAfter(false);
        }
        return animRightTop;
    }
}
