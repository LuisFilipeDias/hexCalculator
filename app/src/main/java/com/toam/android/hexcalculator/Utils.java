package com.toam.android.hexcalculator;

/**
 * Created by luisfilipedias on 1-12-2015.
 */
public class Utils {

    /* class used for the values/operands queue */
    final static class Parcel {
        /* op = 'v' stands for value, otherwise they are actual operations */
        char op;
        /* in case op != 'v' value should be null */
        double value;
    }

    /* bin, dec, hex */
    final static int  MODE_COUNT = 3;

    /* 0 to 0xF */
    final static int  HEX_COUNT = 16;

    /* +, -, *, /, &, |, = */
    final static int  OP_COUNT = 7;
    final static char OP_CHAR[] = {'+', '-', '*', '/', '&', '|', '='};

    /* ids of the number buttons */
    final static int[] btn_calc_hex_i = {R.id.calc_0, R.id.calc_1, R.id.calc_2, R.id.calc_3, R.id.calc_4, R.id.calc_5,
            R.id.calc_6, R.id.calc_7, R.id.calc_8, R.id.calc_9, R.id.calc_A, R.id.calc_B,
            R.id.calc_C, R.id.calc_D, R.id.calc_E, R.id.calc_F};

    /* ids of the operation buttons */
    final static int btn_calc_op_i[] = {R.id.calc_plus, R.id.calc_minus, R.id.calc_times, R.id.calc_div,
            R.id.calc_and, R.id.calc_or, R.id.calc_equal};

    /* ids of the mode radios */
    final static int btn_calc_mode_i[] = {R.id.radBin, R.id.radDec, R.id.radHex};

}
