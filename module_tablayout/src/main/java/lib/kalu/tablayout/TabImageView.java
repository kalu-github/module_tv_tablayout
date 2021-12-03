package lib.kalu.tablayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

@SuppressLint("AppCompatCustomView")
class TabImageView extends ImageView {

    private float mPadding = 0f;
    private float mMargin = 0f;

    //    public TabImageView(@NonNull Context context) {
//        super(context);
//        init();
//    }

    public TabImageView(@NonNull Context context, @NonNull float padding, @NonNull float margin) {
        super(context);
        this.mPadding = padding;
        this.mMargin = margin;
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height = MeasureSpec.getSize(heightMeasureSpec);
        try {
            Drawable drawable = getDrawable();
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
//            LogUtils.e("AutoImageView", "onMeasure => intrinsicWidth = " + intrinsicWidth + ", intrinsicHeight = " + intrinsicHeight);
            width = height * intrinsicWidth / intrinsicHeight;
        } catch (Exception e) {
            width = 0;
        }

//        LogUtils.e("AutoImageView", "onMeasure => width = " + width + ", height = " + height);
        setMeasuredDimension(width, height);

        // padding
        setPadding((int) mPadding, 0, (int) mPadding, 0);

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
        setScaleType(ScaleType.CENTER);
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