package com.example.spy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.shawnlin.numberpicker.NumberPicker;


public class StandardSpyModeFragment extends Fragment {

    private NumberPicker npSpiesCnt, npActivity;
    private final int spiesMin = SpyInitialValues.SPIES_FROM.getValue();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.standard_spy_mode, null);

        npSpiesCnt = view.findViewById(R.id.npSpiesCnt);
        npActivity = getActivity().findViewById(R.id.npPlayersCnt);

        npSpiesCnt.setMinValue(spiesMin);
        npSpiesCnt.setMaxValue(npActivity.getValue() - 1);
        npSpiesCnt.setValue(spiesMin);
        npSpiesCnt.setWrapSelectorWheel(false);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        npActivity.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                int value = npSpiesCnt.getValue();

                //npSpiesCnt.setMinValue(spiesMin);
                npSpiesCnt.setMaxValue(i1 - 1);
                npSpiesCnt.setValue(value);
                npSpiesCnt.setWrapSelectorWheel(false);
            }
        });
    }
}
