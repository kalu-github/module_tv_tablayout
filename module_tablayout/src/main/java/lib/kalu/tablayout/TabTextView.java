package lib.kalu.tablayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

@SuppressLint("AppCompatCustomView")
class TabTextView extends TextView {

    public TabTextView(Context context) {
        super(context);
        init();
    }

    public TabTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TabTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TabTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        CharSequence text = getText();
        int measureText = (int) getPaint().measureText(String.valueOf(text));
        int padding = measureText / text.length();
        int width = measureText + padding * 2;
        setMeasuredDimension(width, height);
        try {
            int margin = (int) getResources().getDimension(R.dimen.module_tablayout_d4);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
            params.rightMargin = margin;
            params.leftMargin = margin;
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