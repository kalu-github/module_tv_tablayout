package lib.kalu.tablayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

@SuppressLint("AppCompatCustomView")
class TabTextView extends TextView {

    private float mPadding = 0f;
    private float mMargin = 0f;
    private boolean mUnderline = false;
    private int mUnderlineColor = Color.TRANSPARENT;
    private float mUnderlineHeight = 0;
    private float mUnderlineWidth = 0;
    private float mSize = 10f;

//    public TabTextView(@NonNull Context context) {
//        super(context);
//        init();
//    }

    public TabTextView(@NonNull Context context, @NonNull float padding, @NonNull float margin, boolean underline, int underlineColor, float underlineWidth, float underlineHeight, float size) {
        super(context);
        this.mPadding = padding;
        this.mMargin = margin;
        this.mUnderline = underline;
        this.mUnderlineColor = underlineColor;
        this.mUnderlineWidth = underlineWidth;
        this.mUnderlineHeight = underlineHeight;
        this.mSize = size;
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        getPaint().setFakeBoldText(false);
        super.onDraw(canvas);

        // 驻留文字下划线
        if (mUnderline && mUnderlineHeight > 0f && !hasFocus() && isActivated()) {
            try {
                TextPaint textPaint = getPaint();
                Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
                float measureTextHeight = fontMetrics.bottom - fontMetrics.top;
                int paintColor = textPaint.getColor();
                float strokeWidth = textPaint.getStrokeWidth();
                float width = getWidth();
                float height = getHeight();
                float startX;
                float stopX;
                if (mUnderlineWidth <= 0) {
                    float measureTextWidth = textPaint.measureText(String.valueOf(getText()));
                    startX = width / 2 - measureTextWidth / 2;
                    stopX = startX + measureTextWidth;
                } else {
                    startX = width / 2 - mUnderlineWidth / 2;
                    stopX = startX + mUnderlineWidth;
                }
                float startY = height / 2 + measureTextHeight / 2 + mUnderlineHeight;
                float stopY = startY;
                textPaint.setStrokeJoin(Paint.Join.ROUND);
                textPaint.setStrokeCap(Paint.Cap.ROUND);
                textPaint.setAntiAlias(true);
                textPaint.setStrokeWidth(mUnderlineHeight);
                if (mUnderlineColor == Color.TRANSPARENT) {
                    mUnderlineColor = paintColor;
                }
                textPaint.setColor(mUnderlineColor);
                canvas.drawLine(startX, startY, stopX, stopY, textPaint);
                textPaint.setColor(paintColor);
                textPaint.setStrokeWidth(strokeWidth);
            } catch (Exception e) {
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        CharSequence text = getText();

        int measureText = (int) getPaint().measureText(String.valueOf(text));
        if (mPadding == 0) {
            mPadding = measureText / text.length();
        }
        int width = (int) (measureText + mPadding * 1.5);
        setMeasuredDimension(width, height);

        // margin
        try {
            if (mMargin == 0f) {
                mMargin = getResources().getDimension(R.dimen.module_tablayout_d4);
            }
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
            params.rightMargin = (int) mMargin;
            params.leftMargin = (int) mMargin;
        } catch (Exception e) {
        }
    }

    private final void init() {
        setActivated(false);
        setClickable(true);
        setLongClickable(false);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setMaxLines(1);
        setLines(1);
        try {
            setGravity(Gravity.CENTER);
            setTextSize(mSize);
        } catch (Exception e) {
        }
//        setSingleLine(true);
//        setMaxEms(10);
//        setMinEms(2);
    }

    /*************************/

    private OnFocusChangeListener mOnFocusChangeListener;

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        this.mOnFocusChangeListener = l;
        super.setOnFocusChangeListener(l);
    }

    public final void reset() {
        setActivated(false);
        if (null != mOnFocusChangeListener) {
            mOnFocusChangeListener.onFocusChange(this, false);
        }
    }
}