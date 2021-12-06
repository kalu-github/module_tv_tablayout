#
#### 预览
![image](https://github.com/153437803/module_tv_tablayout/blob/master/preview.gif )

#
#### 更新
```
******************
2021-12-03
添加.9图支持
```

#
#### 示例
```
# 1.加工数据
ArrayList<TabModel> list = new ArrayList<>();
for (int i = 0; i < 20; i++) {
  TabModel temp = new TabModel()
  list.add(temp);
}

# 2.刷新组件
TabLayout tabLayout = findViewById(R.id.tab_plus);
tabLayout.update(list);

# 3.添加监听
TabLayout tabLayout = findViewById(R.id.tab_plus);
tabLayout.setOnTabChangeListener(new OnTabChangeListener() {
    @Override
    public void onSelect(int index) {
    }

    @Override
    public void onBefore(int index) {
    }

    @Override
    public void onRepeat(int index) {
    }

    @Override
    public void onLeave(int index) {
    }
});
```

#
#### 支持
```
1.支持文字颜色默认、 选中、 驻留三种状态
2.支持文字背景默认、 选中、 驻留三种状态
3.支持图片前景默认、 选中、 驻留三种状态
4.支持图片背景默认、 选中、 驻留三种状态
5.支持.9图[必须是编译过后的.9, 否则会出现.9显示异常]
6.支持网络图片、本地图片、 Assets内置图片
7.支持动画, 默认关闭
8.支持文字宽度自适应
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

# 监听
public final void setOnTabChangeListener(@NonNull OnTabChangeListener listener)
```

#### 监听器
```
@Keep
public interface OnTabChangeListener {

    /**
     * 选中
     *
     * @param index 索引位置
     */
    void onSelect(@IntRange(from = 0, to = Integer.MAX_VALUE) int index);

    /**
     * 之前选中
     *
     * @param index 索引位置
     */
    void onBefore(@IntRange(from = 0, to = Integer.MAX_VALUE) int index);

    /**
     * 复位
     *
     * @param index 索引位置
     */
    void onRepeat(@IntRange(from = 0, to = Integer.MAX_VALUE) int index);

    /**
     * 离开
     *
     * @param index 索引位置
     */
    void onLeave(@IntRange(from = 0, to = Integer.MAX_VALUE) int index);
}
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

#
#### 开源项目
```
https://github.com/Anatolii/NinePatchChunk
```