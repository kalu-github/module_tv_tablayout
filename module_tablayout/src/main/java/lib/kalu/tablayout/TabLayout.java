package lib.kalu.tablayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.IntRange;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;

import java.util.List;

import lib.kalu.tablayout.listener.OnTabChangeListener;
import lib.kalu.tablayout.model.TabModel;

/**
 * TabLayout for TV
 */
@SuppressLint("NewApi")
public class TabLayout extends HorizontalScrollView {

    private float mScale = 1f;
    private float mMargin = 0f;
    private float mPadding = 0f;
    private float mBackgroundColorsRadius = 0f;

    private boolean mTextUnderline = false;
    private int mTextUnderlineColor = Color.TRANSPARENT;
    private float mTextUnderlineWidth = 0;
    private float mTextUnderlineHeight = 0;
    private float mTextSize = 10f;

    private float mImageHeight = 0f;

    public TabLayout(Context context) {
        super(context);
        init(null);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        return super.onTouchEvent(ev);
        return false;
    }

    /**
     * scrollTo相对于view的初始位置移动，所以这里view无论点击多少次，都只会相对于view的初始位置移动一定距离。
     *
     * @param x
     * @param y
     */
    @Override
    public void scrollTo(int x, int y) {
        boolean enabled = isEnabled();
        if (enabled) {
            TabUtil.logE("scrollTo => x = " + x);
            super.scrollTo(x, y);
        }
    }

    /**
     * scrollBy相对于view的当前位置移动，所以此处view是每点击一次就向右下角移动一次的。
     *
     * @param x
     * @param y
     */
    @Override
    public void scrollBy(int x, int y) {
        boolean enabled = isEnabled();
        if (enabled) {
            TabUtil.logE("scrollBy => x = " + x);
            super.scrollBy(x, y);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            anim(true);
            scroll(View.FOCUS_UP, true, false);
            TabUtil.logE("dispatchKeyEvent[up-leave] =>");
        } else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            anim(false);
            scroll(View.FOCUS_UP, false, true);
            TabUtil.logE("dispatchKeyEvent[up-come] =>");
            return true;
        } else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            anim(true);
            scroll(View.FOCUS_DOWN, true, false);
            TabUtil.logE("dispatchKeyEvent[down-leave] =>");
        } else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            anim(false);
            scroll(View.FOCUS_DOWN, false, true);
            TabUtil.logE("dispatchKeyEvent[down-come] =>");
            return true;
        }
        // left repeat
        else if (event.getRepeatCount() > 0 && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            TabUtil.logE("dispatchKeyEvent[left-repeat] =>");
            return true;
        }
        // left outside
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            boolean outside = isOutside(true);
            if (outside) {
                TabUtil.logE("dispatchKeyEvent[left-outside] =>");
                return true;
            }
        }
        // left
        else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            scroll(View.FOCUS_LEFT, false, false);
            TabUtil.logE("dispatchKeyEvent[left] =>");
            return true;
        }
        // right repeat
        else if (event.getRepeatCount() > 0 && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            TabUtil.logE("dispatchKeyEvent[right-repeat] =>");
            return true;
        }
        // right outside
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            boolean outside = isOutside(false);
            if (outside) {
                TabUtil.logE("dispatchKeyEvent[right-outside] =>");
                return true;
            }
        }
        // right
        else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            scroll(View.FOCUS_RIGHT, false, false);
            TabUtil.logE("dispatchKeyEvent[right] =>");
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /************************************/

    private final void init(@Nullable AttributeSet attrs) {
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setSmoothScrollingEnabled(true);
        setNestedScrollingEnabled(true);
        setHorizontalScrollBarEnabled(false);
        setWillNotDraw(true);
        setLongClickable(false);
        setClickable(false);
        setFocusable(true);
        setFocusableInTouchMode(true);
        LinearLayout view = new LinearLayout(getContext());
        view.setClickable(true);
        view.setLongClickable(false);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setOrientation(LinearLayout.HORIZONTAL);
        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(view, 0);
        TypedArray attributes = null;
        try {
            attributes = getContext().obtainStyledAttributes(attrs, R.styleable.TabLayout);
            mScale = attributes.getFloat(R.styleable.TabLayout_tl_scale, 1f);
            mMargin = attributes.getDimension(R.styleable.TabLayout_tl_margin, 0f);
            mPadding = attributes.getDimension(R.styleable.TabLayout_tl_padding, 0f);
            mBackgroundColorsRadius = attributes.getDimension(R.styleable.TabLayout_tl_background_colors_radius, 0f);
            mTextUnderline = attributes.getBoolean(R.styleable.TabLayout_tl_text_underline, false);
            mTextUnderlineColor = attributes.getColor(R.styleable.TabLayout_tl_text_underline_color, Color.TRANSPARENT);
            mTextUnderlineWidth = attributes.getDimension(R.styleable.TabLayout_tl_text_underline_width, 0f);
            mTextUnderlineHeight = attributes.getDimension(R.styleable.TabLayout_tl_text_underline_height, 0f);
            mTextSize = attributes.getDimension(R.styleable.TabLayout_tl_text_size, 10f);
            mImageHeight = attributes.getDimension(R.styleable.TabLayout_tl_image_height, 0f);
        } catch (Exception e) {
        }

        if (null != attributes) {
            attributes.recycle();
        }
    }

    private final boolean isOutside(boolean left) {

        View container = getContainer();
        if (null == container || !(container instanceof LinearLayout))
            return false;

        int count = ((LinearLayout) container).getChildCount();
        int index = getIndex();
        TabUtil.logE("isOutside => count = " + count + ", index = " + index);

        // 左边界
        if (left && index <= 0) {
            return true;
        }
        // 右边界
        else if (!left && index + 1 >= count) {
            return true;
        } else {
            return false;
        }
    }

    private final void anim(boolean over) {
        if (mScale <= 1f)
            return;
        ViewCompat.animate(this).scaleX(over ? 1f : mScale).scaleY(over ? 1f : mScale).start();
    }

    /**
     * 强制选中
     *
     * @param before 旧索引位置
     * @param next   新索引位置
     * @param notify 通知
     * @param leave  离开
     * @param repeat 重复
     */
    private final void scroll(@IntRange(from = 0, to = Integer.MAX_VALUE) int before, @IntRange(from = 0, to = Integer.MAX_VALUE) int next, boolean notify, boolean leave, boolean repeat) {
        if (next < 0)
            return;
        View container = getContainer();
        int count = ((LinearLayout) container).getChildCount();
        if (next >= count)
            return;

        updateIndex(next);
        updateFocus(before, next, notify, leave, repeat);
    }

    private final void scroll(int direction, boolean leave, boolean repeat) {
        int before = getIndex();
        int next;
        if (direction == View.FOCUS_LEFT) {
            next = before - 1;
        } else if (direction == View.FOCUS_RIGHT) {
            next = before + 1;
        } else {
            next = before < 0 ? 0 : before;
        }
        TabUtil.logE("updateSelect => before = " + before + ", next = " + next);
        scroll(before, next, true, leave, repeat && before >= 0);
    }

    /**
     * 更新选中索引
     *
     * @param index
     */
    private final void updateIndex(@IntRange(from = 0, to = Integer.MAX_VALUE) int index) {
        if (index < 0)
            return;
        View container = getContainer();
        if (null == container || !(container instanceof LinearLayout))
            return;
        int count = ((LinearLayout) container).getChildCount();
        if (index + 1 > count)
            return;
        setTag(R.id.module_tablayout_id_index, index);
    }

    /**
     * 获取选中索引
     *
     * @return
     */
    private final int getIndex() {
        try {
            return (int) getTag(R.id.module_tablayout_id_index);
        } catch (Exception e) {
            return -1;
        }
    }

//    private final void setEnabled(@IntRange(from = 0, to = Integer.MAX_VALUE) int before, @IntRange(from = 0, to = Integer.MAX_VALUE) int next, boolean enabled) {
//        super.setEnabled(enabled);
//        TabUtil.logE("setEnabled => enabled = " + enabled + ", before = " + before + ", next = " + next);
//
//        if (null != view && next != before) {
//            int left = view.getLeft();
//            scrollTo(left, (int) getY());
//        }
//
//        //        if (enabled) {
////            View container = getContainer();
////            if (null == container || !(container instanceof LinearLayout))
////                return;
////            int count = ((LinearLayout) container).getChildCount();
////            if (count == 0)
////                return;
////            int index = getIndex();
////            if (index < 0) {
////                index = 0;
////            }
//////            ((LinearLayout) container).getChildAt(index).requestFocus();
////            View focus = ((LinearLayout) container).getChildAt(index);
////            int left = focus.getLeft();
////            scrollTo(left, (int) getY());
//////            requestChildFocus(focus, focus);
////        }
//    }

    /**
     * 更新焦点状态
     *
     * @param before 旧索引位置
     * @param next   新索引位置
     * @param notify 通知
     * @param leave  离开
     * @param repeat 重复
     */
    private final void updateFocus(@IntRange(from = 0, to = Integer.MAX_VALUE) int before, @IntRange(from = 0, to = Integer.MAX_VALUE) int next, boolean notify, boolean leave, boolean repeat) {
        TabUtil.logE("updateFocus => before = " + before + ", next = " + next + ", notify = " + notify + ", leave = " + leave + ", repeat = " + repeat);
        setEnabled(leave ? false : true);
        View container = getContainer();
        int count = ((LinearLayout) container).getChildCount();
        int num = 0;
        for (int i = 0; i < count; i++) {
            // over
            if (num >= 2) {
                TabUtil.logE("updateFocus[强制结束] => num = " + num);
                break;
            }
            // scan
            else {
                View view = ((LinearLayout) container).getChildAt(i);
                if (null == view)
                    continue;
                // 强制获焦
                if (i == next) {
                    TabUtil.logE("updateFocus[强制获焦] => index = " + next + ", view = " + view);
                    ++num;

                    // 焦点
                    if (isEnabled()) {
                        view.requestFocus();
                    }

                    if (view instanceof TabTextView) {
                        ((TabTextView) view).refresh(true, leave);
                    } else if (view instanceof TabImageView) {
                        ((TabImageView) view).refresh(true, leave);
                    }

                    if (null != mOnTabChangeListener) {
                        if (repeat) {
                            mOnTabChangeListener.onRepeat(i);
                        } else if (leave) {
                            mOnTabChangeListener.onLeave(i);
                        } else if (notify) {
                            mOnTabChangeListener.onSelect(i);
                        }
                    }
                }
                // 强制失焦
                else if (i == before) {
                    TabUtil.logE("updateFocus[强制失焦] => index = " + before + ", view = " + view);
                    ++num;
                    if (view instanceof TabTextView) {
                        ((TabTextView) view).refresh(false, false);
                    } else if (view instanceof TabImageView) {
                        ((TabImageView) view).refresh(false, false);
                    }
                }
            }
        }
    }

    private final View getContainer() {
        return getChildAt(0);
    }

    private final <T extends TabModel> void addContainerText(@NonNull final TextView view, @NonNull final T t) {
        View container = getContainer();
        if (null == container || !(container instanceof LinearLayout))
            return;

        int count = ((LinearLayout) container).getChildCount();
        ((LinearLayout) container).addView(view, count);
    }

    private final <T extends TabModel> void addContainerImage(@NonNull final ImageView view, @NonNull final T t) {
        View container = getContainer();
        if (null == container || !(container instanceof LinearLayout))
            return;

        int count = ((LinearLayout) container).getChildCount();
        ((LinearLayout) container).addView(view, count);
    }

    private final <T extends TabModel> void addText(@NonNull T t) {

        String text = t.initText();
        if (null == text || text.length() == 0)
            return;

        View container = getContainer();
        if (null == container)
            return;

        TabTextView view = new TabTextView(getContext()) {
            @Override
            protected void refresh(boolean focus, boolean stay) {
                TabUtil.updateTextUI(this, t, mBackgroundColorsRadius, focus, stay);
            }
        };
        view.setPadding(mPadding);
        view.setMargin(mMargin);
        view.setTextSize(mTextSize);
        view.setUnderline(mTextUnderline);
        view.setUnderlineColor(mTextUnderlineColor);
        view.setUnderlineWidth(mTextUnderlineWidth);
        view.setUnderlineHeight(mTextUnderlineHeight);
        view.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT));

        // ui
        TabUtil.updateTextUI(view, t, mBackgroundColorsRadius, false, false);

        // addView
        addContainerText(view, t);
    }

    private final <T extends TabModel> void addImage(@NonNull T t) {

        if (null == t)
            return;

        TabImageView view = new TabImageView(getContext()) {
            @Override
            protected void refresh(boolean focus, boolean stay) {
                super.refresh(focus, stay);
                TabUtil.updateImageUI(this, t, mBackgroundColorsRadius, focus, stay);
            }
        };
        view.setMargin(mMargin);
        view.setPadding(mPadding);
        view.setHeight(mImageHeight);
        view.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT));

        // ui
        TabUtil.updateImageUI(view, t, mBackgroundColorsRadius, false, false);

        // addView
        addContainerImage(view, t);
    }

    /************************************/

    /**
     * 更新数据
     *
     * @param list 数据源
     * @param <T>
     */
    @Keep
    public final <T extends TabModel> void update(@NonNull List<T> list) {

        View container = getContainer();
        if (null != container && container instanceof LinearLayout) {
            ((LinearLayout) container).removeAllViews();
        }

        for (int i = 0; i < list.size(); i++) {
            T t = list.get(i);
            if (null == t)
                continue;
            if (null != t.initImageSrcUrls() && t.initImageSrcUrls().length >= 3) {
                addImage(t);
            } else if (null != t.initTextColors() && t.initTextColors().length >= 3) {
                addText(t);
            }
        }
    }

    @Keep
    public final void select(@IntRange(from = 0, to = Integer.MAX_VALUE) int next) {
        select(next, true, false);
    }

    /**
     * 强制选中tab
     *
     * @param next   新索引位置
     * @param notify 开启通知
     * @param anim   开启动画
     */
    @Keep
    public final void select(@IntRange(from = 0, to = Integer.MAX_VALUE) int next, boolean notify, boolean anim) {

        int before = getIndex();
        TabUtil.logE("select => before = " + before + ", next = " + next);
        if (before == next)
            return;

        if (anim) {
            anim(false);
        }
        scroll(before, next, notify, false, before == next);
    }

    @Keep
    public final void left() {
        left(1, false);
    }

    @Keep
    public final void left(boolean anim) {
        left(1, anim);
    }

    @Keep
    public final void left(@IntRange(from = 0, to = Integer.MAX_VALUE) int num) {
        left(num, false);
    }

    @Keep
    public final void left(@IntRange(from = 0, to = Integer.MAX_VALUE) int num, boolean anim) {
        int select = getIndex();
        if (select <= 0)
            return;

        int index = select - num;
        if (index < 0) {
            index = 0;
        }
        select(index, true, anim);
    }

    @Keep
    public final void right() {
        right(1, false);
    }

    @Keep
    public final void right(boolean anim) {
        right(1, anim);
    }

    @Keep
    public final void right(@IntRange(from = 0, to = Integer.MAX_VALUE) int num) {
        right(num, false);
    }

    @Keep
    public final void right(@IntRange(from = 0, to = Integer.MAX_VALUE) int num, boolean anim) {
        View container = getContainer();
        if (null == container || !(container instanceof LinearLayout))
            return;

        int count = ((LinearLayout) container).getChildCount();
        int select = getIndex();
        if (select + 1 >= count)
            return;

        int index = select + num;
        if (index >= count) {
            index = count - 1;
        }
        select(index, true, anim);
    }

    @Keep
    public final boolean isSelect(@IntRange(from = 0, to = Integer.MAX_VALUE) int index) {
        int select = getIndex();
        return select == index;
    }

    @Keep
    public final int getSelect() {
        return getIndex();
    }

    /************************************/

    public OnTabChangeListener mOnTabChangeListener;

    public final void setOnTabChangeListener(@NonNull OnTabChangeListener listener) {
        this.mOnTabChangeListener = listener;
    }
}