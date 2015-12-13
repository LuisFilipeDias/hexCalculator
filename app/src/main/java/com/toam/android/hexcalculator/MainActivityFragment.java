package com.toam.android.hexcalculator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    /* separating between hex and operation buttons for ease of handle */
    Button btn_calc_hex[], btn_calc_op[];
    View frag_view;

    int btn_calc_hex_i[] = {R.id.calc_0, R.id.calc_1, R.id.calc_2, R.id.calc_3, R.id.calc_4, R.id.calc_5,
            R.id.calc_6, R.id.calc_7, R.id.calc_8, R.id.calc_9, R.id.calc_A, R.id.calc_B,
            R.id.calc_C, R.id.calc_D, R.id.calc_E, R.id.calc_F};
    int btn_calc_op_i[] = {R.id.calc_plus, R.id.calc_minus, R.id.calc_times, R.id.calc_div,
            R.id.calc_and, R.id.calc_or, R.id.calc_equal};

    final int HEX_COUNT = 16; // 0x0F
    final int OP_COUNT = 7;   // +, -, *, /, &, |, =

    char op_char[] = {'+', '-', '*', '/', '&', '|', '='};

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
        final Calculator calc = new Calculator();

        btn_calc_hex = new Button[HEX_COUNT];
        btn_calc_op  = new Button[OP_COUNT];

        /* set listener for hex's */
        for (int i = 0; i < HEX_COUNT; i++) {
            final int key = i;
            btn_calc_hex[i] = (Button) frag_view.findViewById(btn_calc_hex_i[i]);
            btn_calc_hex[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    calc.setCurrentKey(key);
                    calc.printInfo();
                }
            });
        }

        /* set listener for ops */
        for (int i = 0; i < OP_COUNT; i++) {
            final int key = i;
            btn_calc_op[i] = (Button) frag_view.findViewById(btn_calc_op_i[i]);
            btn_calc_op[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    calc.setOperation(op_char[key]);
                    calc.printInfo();
                }
            });
        }
    }


}
