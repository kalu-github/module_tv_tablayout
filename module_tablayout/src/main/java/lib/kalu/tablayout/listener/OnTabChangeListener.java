package lib.kalu.tablayout.listener;

import androidx.annotation.IntRange;
import androidx.annotation.Keep;

@Keep
public interface OnTabChangeListener {

    void onSelect(@IntRange(from = 0, to = Integer.MAX_VALUE) int index);
}
