package com.toam.android.hexcalculator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    View frag_view;

    public MainActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        frag_view = inflater.inflate(R.layout.fragment_main, container, false);

        /* listeners and stuff must be done after inflation */
        createFragment();

        return frag_view;
    }

    private void createFragment() {
        /* creating new object calc */
        new Calculator(frag_view);
    }


}
