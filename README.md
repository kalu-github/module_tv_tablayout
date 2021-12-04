#
#### 预览
![image](https://github.com/153437803/module_tv_tablayout/blob/master/preview.gif )

#
#### 功能
```
/**
 * 更新数据
 *
 * @param list 数据源
 * @param <T>
 */
@Keep
public final <T extends TabModel> void update(@NonNull List<T> list)

/**
 * 强制选中tab
 *
 * @param index 索引位置
 * @param anim  动画
 */
@Keep
public final void select(int index, boolean anim)
```

#
#### data
```
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
```

#
#### 支持
```
1. 服务器下载的.9图必须是编译apk解压取出的.9图[编译过后没有黑线]
```

#
#### 更新
```
******************
2021-12-03
1.添加.9图支持
```