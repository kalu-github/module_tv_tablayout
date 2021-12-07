package lib.kalu.tab.model;

import android.graphics.Color;

import androidx.annotation.Keep;

import lib.kalu.tab.R;

@Keep
public abstract class TabModelText implements TabModel {

    @Override
    public int initImagePlaceholder() {
        return 0;
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
    public String[] initImageSrcUrls() {
        return null;
    }

    @Override
    public String[] initImageBackgroundAssets() {
        return null;
    }

    @Override
    public int[] initImageBackgroundResources() {
        return null;
    }

    /**********************/

    @Override
    public int[] initTextColors() {
        return new int[]{Color.BLACK, Color.RED, Color.BLUE};
    }

    @Override
    public int[][] initTextBackgroundColors() {
        return null;
    }

    @Override
    public String[] initTextBackgroundUrls() {
        return null;
    }

    public String[] initTextBackgroundAssets() {
        return null;
    }

    @Override
    public String[] initTextBackgroundFiles() {
        return null;
    }

    @Override
    public int[] initTextBackgroundResources() {
        return new int[]{R.drawable.module_tablayout_ic_shape_background_normal, R.drawable.module_tablayout_ic_shape_background_focus, R.drawable.module_tablayout_ic_shape_background_select};
    }
}
