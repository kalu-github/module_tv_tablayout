package lib.kalu.tablayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.Keep;

@Keep
public interface TabModel {

    /**
     * 文字剧中显示
     *
     * @return
     */
    int initTextGravity();

    /**
     * 文字大小
     *
     * @return
     */
    int initTextSize();

    /**
     * 文字内容
     *
     * @return
     */
    String initText();

    /**
     * 0: 默认文字颜色
     * 1：焦点文字颜色
     * 2：选中文字颜色
     *
     * @return
     */
    int[] initTextColors();

    /**
     * 0: 默认文字背景网络图片 => 首先图片下载本地, 之后本地缓存拿
     * 1：焦点文字背景网络图片 => 首先图片下载本地, 之后本地缓存拿
     * 2：选中文字背景网络图片 => 首先图片下载本地, 之后本地缓存拿
     *
     * @return
     */
    String[] initTextBackgroundUrls();

    /**
     * 0: 默认文字背景本地图片
     * 1：焦点文字背景本地图片
     * 2：选中文字背景本地图片
     *
     * @return
     */
    int[] initTextBackgroundResources();

    /**
     * 0: 默认文字背景本地图片
     * 1：焦点文字背景本地图片
     * 2：选中文字背景本地图片
     *
     * @return
     */
    int[] initTextBackgroundDefaults();

    /****************************/

    /**
     * 0: 默认网络图片 => 首先图片下载本地, 之后本地缓存拿
     * 1：焦点网络图片 => 首先图片下载本地, 之后本地缓存拿
     * 2：选中网络图片 => 首先图片下载本地, 之后本地缓存拿
     *
     * @return
     */
    String[] initImageSrcUrls();

    /**
     * 0: 默认图片背景网络图片 => 首先图片下载本地, 之后本地缓存拿
     * 1：焦点图片背景网络图片 => 首先图片下载本地, 之后本地缓存拿
     * 2：选中图片背景网络图片 => 首先图片下载本地, 之后本地缓存拿
     *
     * @return
     */
    String[] initImageBackgroundUrls();

    /**
     * 0: 默认图片背景本地图片
     * 1：焦点图片背景本地图片
     * 2：选中图片背景本地图片
     *
     * @return
     */
    int[] initImageBackgroundResources();

    /**
     * 0: 默认图片背景本地图片
     * 1：焦点图片背景本地图片
     * 2：选中图片背景本地图片
     *
     * @return
     */
    int[] initImageBackgroundDefaults();

    /**
     * 占位图
     *
     * @return
     */
    @DrawableRes
    int initImagePlaceholder();
}
