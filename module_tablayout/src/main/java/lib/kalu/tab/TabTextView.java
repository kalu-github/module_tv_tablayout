package lib.kalu.tab;

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

    private boolean mUnderline = false;
    private int mUnderlineColor = Color.TRANSPARENT;
    private float mUnderlineHeight = 0;
    private float mUnderlineWidth = 0;

//    public TabTextView(@NonNull Context context) {
//        super(context);
//        init();
//    }

    public TabTextView(@NonNull Context context) {
        super(context);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        getPaint().setFakeBoldText(false);
        super.onDraw(canvas);

        // 驻留文字下划线
        if (mUnderline && mUnderlineHeight > 0f && String.valueOf(1).equals(getHint())) {
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

        int left = getPaddingLeft();
        int right = getPaddingRight();
        int measureText = (int) getPaint().measureText(String.valueOf(text));
        int width = measureText;

        if (left == 0 || right == 0) {
            int size = measureText / text.length();
            width += size * 1.5;
        } else {
            width += left;
            width += right;
        }
        setMeasuredDimension(width, height);
    }

    private final void init() {
        setClickable(true);
        setLongClickable(false);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setMaxLines(1);
        setLines(1);
        setGravity(Gravity.CENTER);
    }

    protected final void setUnderline(boolean underline) {
        this.mUnderline = underline;
    }

    protected final void setUnderlineColor(int color) {
        this.mUnderlineColor = color;
    }

    protected final void setUnderlineWidth(float width) {
        this.mUnderlineWidth = width;
    }

    protected final void setUnderlineHeight(float height) {
        this.mUnderlineHeight = height;
    }

    /*************************/

    protected final void updateTextColor(int color, boolean stay) {
        setHint(stay ? String.valueOf(1) : null);
        super.setTextColor(color);
    }

    protected void refresh(boolean focus, boolean stay) {
    }
}