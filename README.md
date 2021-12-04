#
#### 预览
![image](https://github.com/153437803/module_tv_tablayout/blob/master/preview.gif )

#
#### 更新
```
******************
2021-12-03
1.添加.9图支持
```

#
#### 支持
```
1.支持.9图[必须是编译过后的.9, 否则会出现.9显示异常]
2.支持网络图片、本地图片、 Assets内置图片
3.支持动画, 默认关闭
4.支持文字宽度自适应
```

#
#### 功能
```
# 更新数据
@Keep
public final <T extends TabModel> void update(@NonNull List<T> list)

# 强制选中
@Keep
public final void select(int index, boolean anim)

# 获取选中位置
@Keep
public final int getSelect()

# 指定位置是否选中
@Keep
public final boolean isSelect(@NonNull int index)

# 右移
@Keep
public final void right(@IntRange(from = 0, to = Integer.MAX_VALUE) int num, boolean anim)

# 左移
@Keep
public final void left(@IntRange(from = 0, to = Integer.MAX_VALUE) int num, boolean anim)
```

#
#### 数据
```
@Keep
public interface TabModel {

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
     * 0: 默认文字背景颜色, 支持渐变背景色
     * 1：焦点文字背景颜色, 支持渐变背景色
     * 2：选中文字背景颜色, 支持渐变背景色
     *
     * @return
     */
    int[][] initTextBackgroundColors();

    /**
     * 0: 默认文字背景网络图片 => 首先图片下载本地, 之后本地缓存拿
     * 1：焦点文字背景网络图片 => 首先图片下载本地, 之后本地缓存拿
     * 2：选中文字背景网络图片 => 首先图片下载本地, 之后本地缓存拿
     *
     * @return
     */
    String[] initTextBackgroundUrls();

    /**
     * 0: 默认图片背景Assets图片
     * 1：焦点图片背景Assets图片
     * 2：选中图片背景Assets图片
     *
     * @return
     */
    String[] initTextBackgroundAssets();

    /**
     * 0: 默认图片背景本地图片
     * 1：焦点图片背景本地图片
     * 2：选中图片背景本地图片
     *
     * @return
     */
    String[] initTextBackgroundFiles();

    /**
     * 0: 默认文字背景本地图片
     * 1：焦点文字背景本地图片
     * 2：选中文字背景本地图片
     *
     * @return
     */
    int[] initTextBackgroundResources();

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
     * 0: 默认图片背景颜色, 支持渐变背景色
     * 1：焦点图片背景颜色, 支持渐变背景色
     * 2：选中图片背景颜色, 支持渐变背景色
     *
     * @return
     */
    int[][] initImageBackgroundColors();

    /**
     * 0: 默认图片背景网络图片 => 首先图片下载本地, 之后本地缓存拿
     * 1：焦点图片背景网络图片 => 首先图片下载本地, 之后本地缓存拿
     * 2：选中图片背景网络图片 => 首先图片下载本地, 之后本地缓存拿
     *
     * @return
     */
    String[] initImageBackgroundUrls();

    /**
     * 0: 默认图片背景Assets图片
     * 1：焦点图片背景Assets图片
     * 2：选中图片背景Assets图片
     *
     * @return
     */
    String[] initImageBackgroundAssets();

    /**
     * 0: 默认图片背景本地图片
     * 1：焦点图片背景本地图片
     * 2：选中图片背景本地图片
     *
     * @return
     */
    int[] initImageBackgroundResources();

    /**
     * 占位图
     *
     * @return
     */
    @DrawableRes
    int initImagePlaceholder();
}
```