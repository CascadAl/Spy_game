package com.example.spy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shawnlin.numberpicker.NumberPicker;

public class RandSpyModeFragment extends Fragment {

    private NumberPicker npSpiesCntFrom, npSpiesCntTo, npActivity;
    private final int spiesMin = SpyInitialValues.SPIES_FROM.getValue();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rand_spy_mode, null);

        npSpiesCntFrom = view.findViewById(R.id.npSpiesCntFrom);
        npSpiesCntTo = view.findViewById(R.id.npSpiesCntTo);
        npActivity = getActivity().findViewById(R.id.npPlayersCnt);

        npSpiesCntFrom.setMinValue(spiesMin);
        npSpiesCntFrom.setMaxValue(spiesMin);
        npSpiesCntFrom.setValue(spiesMin);
        npSpiesCntFrom.setWrapSelectorWheel(false);

        npSpiesCntTo.setMinValue(spiesMin + 1);
        npSpiesCntTo.setMaxValue(npActivity.getValue());
        npSpiesCntTo.setValue(spiesMin + 1);
        npSpiesCntTo.setWrapSelectorWheel(false);

        npSpiesCntFrom.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                npSpiesCntTo.setMinValue(newVal + 1);
                npSpiesCntTo.setWrapSelectorWheel(false);
            }
        });

        npSpiesCntTo.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                npSpiesCntFrom.setMaxValue(newVal - 1);
                npSpiesCntFrom.setWrapSelectorWheel(false);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        npActivity.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                //int valueFrom = npSpiesCntFrom.getValue();
                //int valueTo = npSpiesCntTo.getValue();

                npSpiesCntFrom.setMaxValue(npSpiesCntTo.getValue() - 1);
                npSpiesCntFrom.setWrapSelectorWheel(false);

                npSpiesCntTo.setMaxValue(i1);
                npSpiesCntTo.setWrapSelectorWheel(false);
            }
        });
    }
}
