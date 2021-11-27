package lib.kalu.tablayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;

import java.util.List;

/**
 * TabLayout for TV
 */
@SuppressLint("NewApi")
public class TabLayout extends HorizontalScrollView {

    private float mScale = 1f;

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
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            animClose();
            updateSelect(View.FOCUS_UP, true);
            TabUtil.logE("dispatchKeyEvent[up-leave] => select = " + getSelect());
        } else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
            animStart();
            updateSelect(View.FOCUS_UP, false);
            TabUtil.logE("dispatchKeyEvent[up-come] => select = " + getSelect());
            return true;
        } else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            animClose();
            updateSelect(View.FOCUS_DOWN, true);
            TabUtil.logE("dispatchKeyEvent[down-leave] => select = " + getSelect());
        } else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            animStart();
            updateSelect(View.FOCUS_DOWN, false);
            TabUtil.logE("dispatchKeyEvent[down-come] => select = " + getSelect());
            return true;
        } else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
            updateSelect(View.FOCUS_LEFT, false);
            TabUtil.logE("dispatchKeyEvent[left] => select = " + getSelect());
            return true;
        } else if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
            updateSelect(View.FOCUS_RIGHT, false);
            TabUtil.logE("dispatchKeyEvent[right] => select = " + getSelect());
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /************************************/

    private final void init(@Nullable AttributeSet attrs) {

        TypedArray attributes = null;
        try {
            attributes = getContext().obtainStyledAttributes(attrs, R.styleable.TabLayout);
            mScale = attributes.getFloat(R.styleable.TabLayout_tl_scale, 1f);
        } catch (Exception e) {
        }

        if (null != attributes) {
            attributes.recycle();
        }

        updateFocusability(true);
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
    }

    private final void animStart() {
        if (mScale <= 1f)
            return;
        ViewCompat.animate(this).scaleX(mScale).scaleY(mScale).start();
    }

    private final void animClose() {
        if (mScale <= 1f)
            return;
        ViewCompat.animate(this).scaleX(1f).scaleY(1f).start();
    }

    private final void updateFocusability(boolean force) {
        setDescendantFocusability(force ? FOCUS_BLOCK_DESCENDANTS : FOCUS_AFTER_DESCENDANTS);
    }

    private final int getSelect() {
        try {
            return (int) getTag(getId());
        } catch (Exception e) {
            return 0;
        }
    }

    private final void setSelect(int index, boolean leave) {
        if (index < 0)
            return;
        View container = getContainer();
        int count = ((LinearLayout) container).getChildCount();
        if (index >= count)
            return;

        setTag(getId(), index);
        updateFocus(leave);
    }

    private final void updateSelect(int direction, boolean leave) {
        int select = getSelect();
        if (direction == View.FOCUS_LEFT) {
            setSelect(select - 1, leave);
        } else if (direction == View.FOCUS_RIGHT) {
            setSelect(select + 1, leave);
        } else {
            setSelect(select, leave);
        }
    }

    private final void updateFocus(boolean leave) {
        View container = getContainer();
        int count = ((LinearLayout) container).getChildCount();
        int select = getSelect();
        for (int i = 0; i < count; i++) {
            View temp = ((LinearLayout) container).getChildAt(i);
            if (null == temp)
                continue;
            updateFocusability(false);
            temp.setActivated(i == select && leave);
            if (i == select) {
                TabUtil.logE("updateFocus[requestFocus] => select = " + select + ", temp = " + temp);
                temp.requestFocus();
            } else {
                temp.clearFocus();
            }
        }
    }

    private final View getContainer() {
        return getChildAt(0);
    }

    private final <T extends TabModel> void addContainerText(@NonNull TextView view, @NonNull T t) {
        View container = getContainer();
        if (null == container || !(container instanceof LinearLayout))
            return;

        int count = ((LinearLayout) container).getChildCount();
        ((LinearLayout) container).addView(view, count);

        // focus
        view.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean b) {
                TabUtil.logE("addContainerText => focus = " + b);
                updateFocusability(true);
                TabUtil.updateTextUI(view, t);
            }
        });
    }

    private final <T extends TabModel> void addContainerImage(@NonNull ImageView view, @NonNull T t) {
        View container = getContainer();
        if (null == container || !(container instanceof LinearLayout))
            return;

        int count = ((LinearLayout) container).getChildCount();
        ((LinearLayout) container).addView(view, count);

        // focus
        view.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean b) {
                TabUtil.logE("addContainerImage => focus = " + b);
                updateFocusability(true);
                TabUtil.updateImageUI(view, t);
            }
        });
    }

    private final <T extends TabModel> void addText(@NonNull T t) {

        String text = t.initText();
        if (null == text || text.length() == 0)
            return;

        View container = getContainer();
        if (null == container)
            return;

        TabTextView view = new TabTextView(getContext());
        view.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT));

        // ui
        TabUtil.updateTextUI(view, t);

        // addView
        addContainerText(view, t);
    }

    private final <T extends TabModel> void addImage(@NonNull T t) {

        if (null == t)
            return;

        TabImageView view = new TabImageView(getContext());
        view.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT));

        // ui
        TabUtil.updateImageUI(view, t);

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

    /**
     * 强制选中tab
     *
     * @param index 索引位置
     * @param anim  动画
     */
    @Keep
    public final void select(int index, boolean anim) {

        int select = getSelect();
        TabUtil.logE("select => select = " + select + ", index = " + index);

        if (select >= 0) {
            View container = getContainer();
            if (null != container && container instanceof LinearLayout) {
                View child = ((LinearLayout) container).getChildAt(select);
                if (null != child && child instanceof TabTextView) {
                    TabUtil.logE("select => clearFocus => TabTextView");
                    ((TabTextView) child).reset();
                } else if (null != child && child instanceof TabImageView) {
                    TabUtil.logE("select => clearFocus => TabImageView");
                    ((TabImageView) child).reset();
                }
            }
        }

        if (anim) {
            animStart();
        }
        setSelect(index, false);
    }
}