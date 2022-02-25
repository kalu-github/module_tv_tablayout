package lib.kalu.tv.tab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.view.Gravity;
import android.widget.TextView;

import androidx.annotation.NonNull;

@SuppressLint("AppCompatCustomView")
class TabTextView extends TextView {

    private boolean mUnderline = false;
    private int mUnderlineColor = Color.TRANSPARENT;
    private int mUnderlineHeight = 0;
    private int mUnderlineWidth = 0;

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
                int measureTextHeight = (int) (fontMetrics.bottom - fontMetrics.top);
                int paintColor = textPaint.getColor();
                int strokeWidth = (int) textPaint.getStrokeWidth();
                int width = getWidth();
                int height = getHeight();
                int startX;
                int stopX;
                if (mUnderlineWidth <= 0) {
                    int measureTextWidth = (int) textPaint.measureText(String.valueOf(getText()));
                    startX = width / 2 - measureTextWidth / 2;
                    stopX = startX + measureTextWidth;
                } else {
                    startX = width / 2 - mUnderlineWidth / 2;
                    stopX = startX + mUnderlineWidth;
                }
                int startY = height / 2 + measureTextHeight / 2 + mUnderlineHeight / 2;
                if (startY >= height) {
                    startY = height - mUnderlineHeight / 2;
                }
                int stopY = startY;
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
        setMinEms(2);
        setGravity(Gravity.CENTER);
    }


    @Override
    public void setTextSize(int unit, float size) {
        super.setTextSize(unit, size);
    }

    protected final void setUnderline(boolean underline) {
        this.mUnderline = underline;
    }

    protected final void setUnderlineColor(int color) {
        this.mUnderlineColor = color;
    }

    protected final void setUnderlineWidth(int width) {
        this.mUnderlineWidth = width;
    }

    protected final void setUnderlineHeight(int height) {
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