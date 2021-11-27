package lib.kalu.tablayout;

import androidx.annotation.Keep;

@Keep
public abstract class TabModelImage implements TabModel {
    @Override
    public String initText() {
        return null;
    }

    @Override
    public int[] initTextColors() {
        return null;
    }

    @Override
    public int initTextSize() {
        return 0;
    }

    @Override
    public int initTextGravity() {
        return 0;
    }

    @Override
    public String[] initTextBackgroundUrls() {
        return null;
    }

    @Override
    public int[] initTextBackgroundDefaults() {
        return null;
    }

    @Override
    public int[] initTextBackgroundResources() {
        return null;
    }

    /*********************/

    @Override
    public int initImagePlaceholder() {
        return R.drawable.module_tablayout_ic_shape_background_normal;
    }

    @Override
    public String[] initImageBackgroundUrls() {
        return null;
    }

    @Override
    public int[] initImageBackgroundResources() {
        return new int[]{R.drawable.module_tablayout_ic_shape_background_normal, R.drawable.module_tablayout_ic_shape_background_focus, R.drawable.module_tablayout_ic_shape_background_select};
    }

    @Override
    public int[] initImageBackgroundDefaults() {
        return new int[]{R.drawable.module_tablayout_ic_shape_background_normal, R.drawable.module_tablayout_ic_shape_background_focus, R.drawable.module_tablayout_ic_shape_background_select};
    }
}
