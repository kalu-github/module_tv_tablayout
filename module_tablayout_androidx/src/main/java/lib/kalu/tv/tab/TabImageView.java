package lib.kalu.tv.tab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
class TabImageView extends ImageView {

    private float mHeight = 0f;

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
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {

        try {
            int imgWidth = drawable.getIntrinsicWidth();
            int imgHeight = drawable.getIntrinsicHeight();

            int tabHeight = 0;
            if (mHeight > 0f) {
                tabHeight = (int) mHeight;
            }
            if (tabHeight == 0) {
                tabHeight = getHeight();
            }
            if (tabHeight == 0) {
                tabHeight = imgHeight;
            }
            int tabWidth = imgWidth * tabHeight / imgHeight;

            int canvasWidth;
            int canvasHeight;
            int paddingLeft = getPaddingLeft();
            int paddingRight = getPaddingRight();
            if (paddingLeft > 0f && paddingRight>0f) {
                canvasWidth = (int) (tabWidth + paddingLeft+paddingRight);
                canvasHeight = (int) (tabHeight + paddingLeft+paddingRight);
            } else {
                canvasWidth = (int) (tabWidth * 1.1f);
                canvasHeight = (int) (tabWidth * 1.1f);
            }

            Bitmap bitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            TabUtil.logE("setImageDrawable => imgWidth = " + imgWidth + ", imgHeight =" + imgHeight);
            TabUtil.logE("setImageDrawable => tabWidth = " + tabWidth + ", tabHeight =" + tabHeight);
            TabUtil.logE("setImageDrawable => canvasWidth = " + canvasWidth + ", canvasHeight =" + canvasHeight);
            // 裁剪
            Rect src = new Rect(0, 0, imgWidth, imgHeight);
            // 坐标
            float left = canvasWidth / 5;
            float right = left * 4;
            float top = canvasHeight / 5;
            float bottom = top * 4;
            RectF dst = new RectF(left, top, right, bottom);
            Bitmap temp = ((BitmapDrawable) drawable).getBitmap();
            canvas.drawBitmap(temp, src, dst, null);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
            if (null != temp) {
                temp.recycle();
            }
            super.setImageDrawable(bitmapDrawable);
        } catch (Exception e) {
        }
    }

    private final void init() {
        setClickable(true);
        setLongClickable(false);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setScaleType(ScaleType.CENTER);
    }

    protected final void setHeight(float height) {
        this.mHeight = height;
    }

    /*************************/

    protected void refresh(boolean focus, boolean stay){
    }
}