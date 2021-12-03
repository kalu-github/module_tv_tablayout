package lib.kalu.tablayout.listener;

import androidx.annotation.IntRange;
import androidx.annotation.Keep;

@Keep
public interface OnTabChangeListener {

    /**
     * @param index
     */
    void onSelect(@IntRange(from = 0, to = Integer.MAX_VALUE) int index);

    /**
     * @param index
     */
    void onRepeat(@IntRange(from = 0, to = Integer.MAX_VALUE) int index);

    /**
     * @param index
     */
    void onLeave(@IntRange(from = 0, to = Integer.MAX_VALUE) int index);
}
