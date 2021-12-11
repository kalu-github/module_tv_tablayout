package lib.kalu.tv.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.IntRange;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import lib.kalu.tv.R;
import lib.kalu.tv.tab.listener.OnTabChangeListener;
import lib.kalu.tv.tab.model.TabModel;

/**
 * TabLayout for TV
 */
public class TabLayout extends HorizontalScrollView {

    private float mScale = 1f;
    private int mMargin = 0;
    private int mBackgroundColorsRadius = 0;

    private boolean mTextUnderline = false;
    private int mTextUnderlineColor = Color.TRANSPARENT;
    private int mTextUnderlineWidth = 0;
    private int mTextUnderlineHeight = 0;
    private int mTextSize = 10;
    private int mTextPadding = 0;

    private int mImageWidthMax = 0;
    private int mImageWidthMin = 0;
    private int mImageHeight = 0;
    private int mImagePadding = 0;

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
            setActivated(false);
            anim(true);
            scroll(View.FOCUS_UP, true, false);
            TabUtil.logE("dispatchKeyEvent[up-leave] =>");
        } else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            setActivated(true);
            anim(false);
            scroll(View.FOCUS_UP, false, true);
            TabUtil.logE("dispatchKeyEvent[up-come] =>");
            return true;
        } else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            setActivated(false);
            anim(true);
            scroll(View.FOCUS_DOWN, true, false);
            TabUtil.logE("dispatchKeyEvent[down-leave] =>");
        } else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            setActivated(true);
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
            int index = getSelectIndex();
            if (index <= 0) {
                TabUtil.logE("dispatchKeyEvent[left-outside] =>");
                return true;
            }
        }
        // left
        else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            TabUtil.logE("dispatchKeyEvent[left] => isActivated = " + isActivated());
            if (isActivated()) {
                scroll(View.FOCUS_LEFT, false, false);
                return true;
            } else {
                setActivated(true);
            }
        }
        // right repeat
        else if (event.getRepeatCount() > 0 && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            TabUtil.logE("dispatchKeyEvent[right-repeat] =>");
            return true;
        }
        // right outside
        else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            int index = getSelectIndex();
            int count = getCount();
            if (index + 1 >= count) {
                TabUtil.logE("dispatchKeyEvent[right-outside] =>");
                return true;
            }
        }
        // right
        else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            TabUtil.logE("dispatchKeyEvent[right] => isActivated = " + isActivated());
            if (isActivated()) {
                scroll(View.FOCUS_RIGHT, false, false);
                return true;
            } else {
                setActivated(true);
            }
        }
        return super.dispatchKeyEvent(event);
    }

    /************************************/

    private final void init(@Nullable AttributeSet attrs) {
        setPadding(0, 0, 0, 0);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setSmoothScrollingEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setNestedScrollingEnabled(true);
        }
        setHorizontalScrollBarEnabled(false);
        setWillNotDraw(true);
        setLongClickable(false);
        setClickable(false);
        setFocusable(true);
        setFocusableInTouchMode(true);
        LinearLayout root = new LinearLayout(getContext());
        root.setPadding(0, 0, 0, 0);
        root.setClickable(true);
        root.setLongClickable(false);
        root.setFocusable(true);
        root.setFocusableInTouchMode(true);
        root.setOrientation(LinearLayout.HORIZONTAL);
        root.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(root, 0);
        TypedArray attributes = null;
        try {
            attributes = getContext().obtainStyledAttributes(attrs, R.styleable.TabLayout);
            mScale = attributes.getFloat(R.styleable.TabLayout_tl_scale, 1);
            mMargin = attributes.getDimensionPixelOffset(R.styleable.TabLayout_tl_margin, 0);
            mBackgroundColorsRadius = attributes.getDimensionPixelOffset(R.styleable.TabLayout_tl_background_colors_radius, 0);
            mTextPadding = attributes.getDimensionPixelOffset(R.styleable.TabLayout_tl_text_padding, 0);
            mTextUnderline = attributes.getBoolean(R.styleable.TabLayout_tl_text_underline, false);
            mTextUnderlineColor = attributes.getColor(R.styleable.TabLayout_tl_text_underline_color, Color.TRANSPARENT);
            mTextUnderlineWidth = attributes.getDimensionPixelOffset(R.styleable.TabLayout_tl_text_underline_width, 0);
            mTextUnderlineHeight = attributes.getDimensionPixelOffset(R.styleable.TabLayout_tl_text_underline_height, 0);
            mTextSize = attributes.getDimensionPixelOffset(R.styleable.TabLayout_tl_text_size, 10);
            mImageWidthMin = attributes.getDimensionPixelOffset(R.styleable.TabLayout_tl_image_width_min, 0);
            mImageWidthMax = attributes.getDimensionPixelOffset(R.styleable.TabLayout_tl_image_width_max, 0);
            mImageHeight = attributes.getDimensionPixelOffset(R.styleable.TabLayout_tl_image_height, 0);
            mImagePadding = attributes.getDimensionPixelOffset(R.styleable.TabLayout_tl_image_padding, 0);
        } catch (Exception e) {
        }

        if (null != attributes) {
            attributes.recycle();
        }
    }

    private final void anim(boolean over) {
        if (mScale <= 1f)
            return;
        ViewCompat.animate(this).scaleX(over ? 1f : mScale).scaleY(over ? 1f : mScale).start();
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
     * 强制选中tab
     *
     * @param next   新索引位置
     * @param callback 开启通知
     * @param anim   开启动画
     */
    @Keep
    private final void select(@IntRange(from = 0, to = Integer.MAX_VALUE) int next, boolean callback, boolean anim, boolean ignore, boolean activated) {

        int before = getIndex();
        TabUtil.logE("select => before = " + before + ", next = " + next);
        if (!ignore && before == next)
            return;

        if (anim) {
            anim(false);
        }

        setActivated(activated);
        scroll(before, next, callback, false, before == next);
    }

    /**
     * 强制选中
     *
     * @param before 旧索引位置
     * @param next   新索引位置
     * @param callback 通知
     * @param leave  离开
     * @param repeat 重复
     */
    private final void scroll(@IntRange(from = 0, to = Integer.MAX_VALUE) int before, @IntRange(from = 0, to = Integer.MAX_VALUE) int next, boolean callback, boolean leave, boolean repeat) {
        if (next < 0)
            return;
        View container = getContainer();
        int count = ((LinearLayout) container).getChildCount();
        if (next >= count)
            return;

        updateIndex(next);
        updateFocus(before, next, callback, leave, repeat);
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

    /**
     * 更新焦点状态
     *
     * @param before 旧索引位置
     * @param next   新索引位置
     * @param callback 通知
     * @param leave  离开
     * @param repeat 重复
     */
    private final void updateFocus(@IntRange(from = 0, to = Integer.MAX_VALUE) int before, @IntRange(from = 0, to = Integer.MAX_VALUE) int next, boolean callback, boolean leave, boolean repeat) {
        TabUtil.logE("updateFocus => before = " + before + ", next = " + next + ", callback = " + callback + ", leave = " + leave + ", repeat = " + repeat);
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

                    if (callback && null != mOnTabChangeListener) {
                        if (repeat) {
                            mOnTabChangeListener.onRepeat(next);
                        } else if (leave) {
                            mOnTabChangeListener.onLeave(next);
                        } else {
                            mOnTabChangeListener.onSelect(next);
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
                    if (callback && null != mOnTabChangeListener) {
                        mOnTabChangeListener.onBefore(before);
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

    private final <T extends TabModel> void addText(@NonNull T t, int index, int count) {

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
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        view.setUnderline(mTextUnderline);
        view.setUnderlineColor(mTextUnderlineColor);
        view.setUnderlineWidth(mTextUnderlineWidth);
        view.setUnderlineHeight(mTextUnderlineHeight);
        view.setPadding(mTextPadding, 0, mTextPadding, 0);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, getHeight());
        if (index + 1 != count) {
            layoutParams.rightMargin = mMargin;
        }
        view.setLayoutParams(layoutParams);

        // ui
        TabUtil.updateTextUI(view, t, mBackgroundColorsRadius, false, false);

        // addView
        addContainerText(view, t);
    }

    private final <T extends TabModel> void addImage(@NonNull T t, int index, int count) {

        if (null == t)
            return;

        TabImageView view = new TabImageView(getContext()) {
            @Override
            protected void refresh(boolean focus, boolean stay) {
                super.refresh(focus, stay);
                TabUtil.updateImageUI(this, t, mBackgroundColorsRadius, focus, stay);
            }
        };
        view.setWidthMin(mImageWidthMin);
        view.setWidthMax(mImageWidthMax);
        view.setHeight(mImageHeight);
        view.setPadding(mImagePadding, 0, mImagePadding, 0);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, getHeight());
        if (index + 1 != count) {
            layoutParams.rightMargin = mMargin;
        }
        view.setLayoutParams(layoutParams);

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

        int size = list.size();
        for (int i = 0; i < size; i++) {
            T t = list.get(i);
            if (null == t)
                continue;
            if (null != t.initImageSrcUrls() && t.initImageSrcUrls().length >= 3) {
                addImage(t, i, size);
            } else if (null != t.initTextColors() && t.initTextColors().length >= 3) {
                addText(t, i, size);
            }
        }
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

        select(index, true, anim, false, false);
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

        select(index, true, anim, false, false);
    }

    @Keep
    public final boolean isSelect(@IntRange(from = 0, to = Integer.MAX_VALUE) int index) {
        int select = getIndex();
        return select == index;
    }

    @Keep
    public final int getCount() {
        try {
            View container = getContainer();
            return ((LinearLayout) container).getChildCount();
        } catch (Exception e) {
            return 0;
        }
    }

    @Keep
    public final int getSelectIndex() {
        return getIndex();
    }

    @Keep
    public final View getSelectChild() {
        try {
            View container = getContainer();
            int select = getSelectIndex();
            return ((LinearLayout) container).getChildAt(select);
        } catch (Exception e) {
            return null;
        }
    }

    @Keep
    public final void select(@IntRange(from = 0, to = Integer.MAX_VALUE) int next) {
        select(next, true, false, false, true);
    }

    @Keep
    public final void select(@IntRange(from = 0, to = Integer.MAX_VALUE) int next, boolean ignore) {
        select(next, true, false, ignore, true);
    }

    @Keep
    public final void select(@IntRange(from = 0, to = Integer.MAX_VALUE) int next, boolean callback,  boolean ignore) {
        select(next, callback, false, ignore, true);
    }

    /************************************/

    public OnTabChangeListener mOnTabChangeListener;

    public final void setOnTabChangeListener(@NonNull OnTabChangeListener listener) {
        this.mOnTabChangeListener = listener;
    }
}