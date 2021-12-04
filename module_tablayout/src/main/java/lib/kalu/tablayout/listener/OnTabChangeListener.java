package lib.kalu.tablayout.listener;

import androidx.annotation.IntRange;
import androidx.annotation.Keep;

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
