package lib.kalu.tv.tab.model;

import androidx.annotation.Keep;

import lib.kalu.tv.R;

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
    public int[][] initTextBackgroundColors() {
        return null;
    }

    @Override
    public String[] initTextBackgroundUrls() {
        return null;
    }

    @Override
    public int[] initTextBackgroundResources() {
        return null;
    }

    @Override
    public String[] initTextBackgroundFiles() {
        return null;
    }

    @Override
    public String[] initTextBackgroundAssets() {
        return null;
    }

    /*********************/

    @Override
    public int initImagePlaceholder() {
        return R.drawable.module_tablayout_ic_shape_background_normal;
    }

    @Override
    public int[][] initImageBackgroundColors() {
        return null;
    }

    @Override
    public String[] initImageBackgroundUrls() {
        return null;
    }

    @Override
    public String[] initImageBackgroundFiles() {
        return null;
    }

    @Override
    public String[] initImageBackgroundAssets() {
        return null;
    }

    @Override
    public int[] initImageBackgroundResources() {
        return new int[]{R.drawable.module_tablayout_ic_shape_background_normal, R.drawable.module_tablayout_ic_shape_background_focus, R.drawable.module_tablayout_ic_shape_background_select};
    }
}
