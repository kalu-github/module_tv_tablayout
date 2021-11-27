package lib.kalu.tablayout;

import android.graphics.Color;
import android.view.Gravity;

import androidx.annotation.Keep;

@Keep
public abstract class TabModelText implements TabModel {

    @Override
    public int initImagePlaceholder() {
        return 0;
    }

    @Override
    public int[] initImageBackgroundDefaults() {
        return null;
    }

    @Override
    public String[] initImageBackgroundUrls() {
        return null;
    }

    @Override
    public String[] initImageSrcUrls() {
        return null;
    }

    @Override
    public int[] initImageBackgroundResources() {
        return null;
    }

    /**********************/

    @Override
    public int initTextGravity() {
        return Gravity.CENTER;
    }

    @Override
    public int initTextSize() {
        return 20;
    }

    @Override
    public int[] initTextColors() {
        return new int[]{Color.BLACK, Color.RED, Color.BLUE};
    }

    @Override
    public String[] initTextBackgroundUrls() {
        return new String[0];
    }

    @Override
    public int[] initTextBackgroundResources() {
        return new int[]{R.drawable.module_tablayout_ic_shape_background_normal, R.drawable.module_tablayout_ic_shape_background_focus, R.drawable.module_tablayout_ic_shape_background_select};
    }

    @Override
    public int[] initTextBackgroundDefaults() {
        return new int[]{R.drawable.module_tablayout_ic_shape_background_normal, R.drawable.module_tablayout_ic_shape_background_focus, R.drawable.module_tablayout_ic_shape_background_select};
    }
}
