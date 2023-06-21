package com.monke.mosubtitleviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 目前仅支持以宽度为准确值且单行来自适应
 */
public class MoAutoSizeTextView extends View {
    public MoAutoSizeTextView(@NonNull Context context, float autoMaxSize, float autoMinSize, float autoSizeStep) {
        this(context, null);
        this.autoMaxSize = autoMaxSize;
        this.autoMinSize = autoMinSize;
        this.autoSizeStep = autoSizeStep;
    }

    public MoAutoSizeTextView(@NonNull Context context) {
        this(context, null);
    }

    public MoAutoSizeTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoAutoSizeTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private TextPaint textPaint;

    private float autoMaxSize;
    private float autoMinSize;
    private float autoSizeStep;

    private void init(AttributeSet attrs) {
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MoAutoSizeTextView);
            autoMaxSize = typedArray.getDimension(R.styleable.MoAutoSizeTextView_mosubtitle_autoSizeMax, textPaint.getTextSize());
            autoMinSize = typedArray.getDimension(R.styleable.MoAutoSizeTextView_mosubtitle_autoSizeMin, textPaint.getTextSize());
            autoSizeStep = typedArray.getDimension(R.styleable.MoAutoSizeTextView_mosubtitle_autoSizeStep, 1);
            textPaint.setColor(typedArray.getColor(R.styleable.MoAutoSizeTextView_android_textColor, textPaint.getColor()));
            typedArray.recycle();
        } else {
            autoMaxSize = textPaint.getTextSize();
            autoMinSize = textPaint.getTextSize();
            autoSizeStep = 1;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (autoMinSize <= 0 || autoMaxSize <= 0 || autoSizeStep <= 0 || autoMaxSize == autoMinSize || curTextSize != 0 || TextUtils.isEmpty(text) || textPaint == null || curTextSize == textPaint.getTextSize()) {
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            if (heightMode != MeasureSpec.EXACTLY) {
                if (autoMinSize <= 0 || autoMaxSize <= 0 || autoSizeStep <= 0 || TextUtils.isEmpty(text) || textPaint == null) {
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(getPaddingTop() + getPaddingBottom(), MeasureSpec.AT_MOST);
                } else {
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) Math.ceil(getTextHeight() + getPaddingTop() + getPaddingBottom()), MeasureSpec.AT_MOST);
                }
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            if (widthMode == MeasureSpec.EXACTLY) {
                int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
                if (width > 0) {
                    curTextSizeReturnHeight(text, width);
                    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
                    if (heightMode != MeasureSpec.EXACTLY) {
                        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) Math.ceil(getTextHeight() + getPaddingTop() + getPaddingBottom()), MeasureSpec.AT_MOST);
                    }
                }
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!TextUtils.isEmpty(text) && textPaint != null) {
            canvas.drawText(text, getPaddingLeft(), getPaddingTop() - textPaint.getFontMetrics().ascent, textPaint);
        }
    }

    private float getTextHeight() {
        if (TextUtils.isEmpty(text)) {
            return 0;
        }
        return (textPaint.getFontMetrics().descent - textPaint.getFontMetrics().ascent);
    }

    /**
     * 根据真实宽度 设置符合的文字大小，返回真实高度
     */
    private float wucha = 1f;

    private void curTextSizeReturnHeight(String text, int width) {
        if (textPaint.getTextSize() > autoMaxSize || textPaint.getTextSize() < autoMinSize) {
            textPaint.setTextSize((autoMinSize + autoMaxSize) / 2);
        }
        float textWidth = textPaint.measureText(text);
        if (textWidth <= width && width - textWidth <= wucha) {
            curTextSize = textPaint.getTextSize();
            return;
        } else {
            curTextSize = textPaint.getTextSize() * (width * 1.0f / textWidth);
            if (curTextSize <= autoMinSize || curTextSize >= autoMaxSize) {
                if (curTextSize <= autoMinSize) {
                    curTextSize = autoMinSize;
                } else {
                    curTextSize = autoMaxSize;
                }
                textPaint.setTextSize(curTextSize);
                return;
            } else {
                textPaint.setTextSize(curTextSize);
                textWidth = textPaint.measureText(text);
                if (textWidth <= width && width - textWidth <= wucha) {
                    return;
                } else {
                    if (textWidth > width) {
                        while (curTextSize - autoSizeStep >= autoMinSize) {
                            curTextSize = curTextSize - autoSizeStep;
                            textPaint.setTextSize(curTextSize);
                            textWidth = textPaint.measureText(text);
                            if (textWidth <= width) {
                                return;
                            }

                            if (curTextSize != autoMinSize && curTextSize - autoSizeStep < autoMinSize) {
                                curTextSize = autoMinSize;
                                textPaint.setTextSize(curTextSize);
                                return;
                            }
                        }
                        return;
                    } else {
                        while (curTextSize + autoSizeStep <= autoMaxSize) {
                            float f = curTextSize + autoSizeStep;
                            textPaint.setTextSize(f);
                            textWidth = textPaint.measureText(text);
                            if (textWidth > width) {
                                textPaint.setTextSize(curTextSize);
                                return;
                            } else if (textWidth <= width && width - textWidth <= wucha) {
                                curTextSize = f;
                                return;
                            }
                            if (curTextSize != autoMaxSize && curTextSize + autoSizeStep > autoMaxSize) {
                                curTextSize = autoMaxSize;
                                textPaint.setTextSize(curTextSize);
                                return;
                            }
                        }
                        return;
                    }
                }
            }
        }
    }

    private float curTextSize;

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        curTextSize = 0;
        this.text = text;
        if (text != null && text.length() > 0) {
            int realWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingBottom();
            if (textPaint != null && realWidth > 0 && getLayoutParams() != null && (getLayoutParams().width > 0 || getLayoutParams().width == -1)) {
                curTextSizeReturnHeight(text, realWidth);
            }
        }
        invalidate();
    }

    public void setTextColor(int white) {
        if (textPaint != null) {
            textPaint.setColor(white);
        }
    }
}
