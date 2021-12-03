package lib.kalu.tablayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

@SuppressLint("AppCompatCustomView")
class TabTextView extends TextView {

    private float mPadding = 0f;
    private float mMargin = 0f;

//    public TabTextView(@NonNull Context context) {
//        super(context);
//        init();
//    }

    public TabTextView(@NonNull Context context, @NonNull float padding, @NonNull float margin) {
        super(context);
        this.mPadding = padding;
        this.mMargin = margin;
        init();
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