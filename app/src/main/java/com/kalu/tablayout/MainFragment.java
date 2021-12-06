package com.kalu.tablayout;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainFragment extends Fragment {

    private String str;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.activity_main_fragment, container, false);
        try {
            TextView textView = inflate.findViewById(R.id.content_text);
            textView.setText(str);
        } catch (Exception e) {
        }
        return inflate;
    }

    protected final void setText(@NonNull String message) {
        this.str = message;
    }
}
