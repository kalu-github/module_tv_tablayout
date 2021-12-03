package lib.kalu.tablayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
class TabImageView extends ImageView {

    private float mMargin = 0f;
    private float mPadding = 0f;

    //    public TabImageView(@NonNull Context context) {
//        super(context);
//        init();
//    }

    public TabImageView(@NonNull Context context) {
        super(context);
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

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {

        try {
            int canvasWidth;
            int canvasHeight;
            int imgWidth = drawable.getIntrinsicWidth();
            int imgHeight = drawable.getIntrinsicHeight();
            if (mPadding > 0f) {
                canvasWidth = (int) (imgWidth + mPadding * 2);
                canvasHeight = (int) (imgHeight + mPadding * 2);
            } else {
                canvasWidth = (int) (imgWidth * 1.1f);
                canvasHeight = (int) (imgHeight * 1.1f);
            }
            Bitmap temp1 = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(temp1);
            TabUtil.logE("setImageDrawable => imgWidth = " + imgWidth + ", imgHeight =" + imgHeight);
            TabUtil.logE("setImageDrawable => canvasWidth = " + canvasWidth + ", canvasHeight =" + canvasHeight);
            // 裁剪
            Rect src = new Rect(0, 0, imgWidth, imgHeight);
            // 坐标
            float left = canvasWidth / 5;
            float right = left * 4;
            float top = canvasHeight / 5;
            float bottom = top * 4;
            RectF dst = new RectF(left, top, right, bottom);
            Bitmap temp2 = ((BitmapDrawable) drawable).getBitmap();
            canvas.drawBitmap(temp2, src, dst, null);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), temp1);
            if (null != temp2) {
                temp2.recycle();
            }
            super.setImageDrawable(bitmapDrawable);
            if (null != temp1) {
                temp1.recycle();
            }
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

    protected final void setMargin(float margin) {
        this.mMargin = margin;
    }

    protected final void setPadding(float padding) {
        this.mPadding = padding;
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