package com.toam.android.hexcalculator;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.LinkedList;

/**
 * Created by luisfilipedias on 13-12-2015.
 * <p/>
 * Calculator class. Holds keys and operations
 */
public class Calculator {

    /* class used for the values/operands queue */
    public class Parcel {
        /* op = 'v' stands for value, otherwise they are actual operations */
        char op;
        /* in case op != 'v' value should be null */
        double value;
    }

    /* xml related variables */
    private TextView tv_display, tv_sub_display;
    /* ids of the number buttons */
    int btn_calc_hex_i[] = {R.id.calc_0, R.id.calc_1, R.id.calc_2, R.id.calc_3, R.id.calc_4, R.id.calc_5,
            R.id.calc_6, R.id.calc_7, R.id.calc_8, R.id.calc_9, R.id.calc_A, R.id.calc_B,
            R.id.calc_C, R.id.calc_D, R.id.calc_E, R.id.calc_F};
    /* ids of the operation buttons */
    int btn_calc_op_i[] = {R.id.calc_plus, R.id.calc_minus, R.id.calc_times, R.id.calc_div,
            R.id.calc_and, R.id.calc_or, R.id.calc_equal};
    /* ids of the mode radios */
    int btn_calc_mode_i[] = {R.id.radBin, R.id.radDec, R.id.radHex};

    /* calculation and display variables */
    private boolean showResult;
    private double result, curr_val;
    private String display, sub_display;
    private int base, mode;

    /* value/operands queue */
    private LinkedList<Parcel> q;

    //private final static String TAG = "Calculator";

    public Calculator(View v) {
        /* start with mode 0 (bin) */
        this.mode = 0;
        /* init with base 10 */
        this.base = 10;
        /* show result only after first operation */
        this.showResult = false;
        this.q = new LinkedList<>();

        Button[] btn_calc_hex = new Button[Utils.HEX_COUNT];
        Button[] btn_calc_op = new Button[Utils.OP_COUNT];
        Button[] btn_calc_mode = new Button[Utils.MODE_COUNT];

        tv_display = (TextView) v.findViewById(R.id.main_display);
        tv_sub_display = (TextView) v.findViewById(R.id.sub_display);

        /* set listener for hex's */
        for (int i = 0; i < Utils.HEX_COUNT; i++) {
            final int key = i;
            btn_calc_hex[i] = (Button) v.findViewById(btn_calc_hex_i[i]);
            btn_calc_hex[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    addKey(key);
                    updateDisplay(getDisplay(), getSubDisplay());
                }
            });
        }

        /* set listener for ops */
        for (int i = 0; i < Utils.OP_COUNT; i++) {
            final int key = i;
            btn_calc_op[i] = (Button) v.findViewById(btn_calc_op_i[i]);
            btn_calc_op[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    setOperation(Utils.OP_CHAR[key]);
                    updateDisplay(getDisplay(), getSubDisplay());
                }
            });
        }

        /* set listener for calc modes */
        for (int i = 0; i < Utils.MODE_COUNT; i++) {
            final int mode = i;
            btn_calc_mode[i] = (Button) v.findViewById(btn_calc_mode_i[i]);
            btn_calc_mode[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    setMode(mode);
                }
            });
        }
    }

    private void setOperation(char op) {
        Parcel prc = new Parcel();

        /* if queue isn't empty */
        if (!this.q.isEmpty())
        /* pop older, not updated value, then push new value */
            if (this.q.peek().op != 'v')
                this.q.pop();

        /* insert operation to queue */
        prc.op = op;
        prc.value = 0;
        this.q.push(prc);

        /* case = reset everything */
        if (op == '=') {
            this.q.clear();
            prc.op = 'v';
            prc.value = this.result;
            this.q.push(prc);
            this.sub_display = "";
        }

        /* value has been pushed, reset it */
        this.curr_val = 0;

        refreshDisplay();
    }

    private void addKey(int key) {
        Parcel prc = new Parcel();

        this.curr_val = this.curr_val * this.base + key;

        /* insert value to queue */
        prc.op = 'v';
        prc.value = this.curr_val;

        /* if queue isn't empty */
        if (!this.q.isEmpty())
        /* pop older, not updated value, then push new value */
            if (this.q.peek().op == 'v')
                this.q.pop();

        this.q.push(prc);

        computeResult();
        refreshDisplay();
    }

    private void computeResult() {

        /* s as in start */
        char prev_op = 's';

        /* values below are used for multiplication or division operations */
        boolean precedence = false;
        char pre_precedence_op = 's';
        double old_value = 0;
        double precedence_result = 0;
        double pre_precedence_result = 0;

        for (int i = 0; i < this.q.size(); i++) {

            /* take values in the contrary order */
            int idx = this.q.size() - i - 1;

            char l_op = this.q.get(idx).op;
            double l_val = this.q.get(idx).value;

            if (l_op != 'v') {
                prev_op = l_op;
                this.showResult = true;
                continue;
            } else if (prev_op == 's') {
                /* in this case it's easy, no history, result is the value */
                this.result = l_val;
            }

            switch (prev_op) {
                case '+':
                    precedence = false;
                    precedence_result = 0;
                    pre_precedence_op = '+';
                    pre_precedence_result = this.result;
                    this.result = this.result + l_val;
                    break;
                case '-':
                    precedence = false;
                    precedence_result = 0;
                    pre_precedence_op = '-';
                    pre_precedence_result = this.result;
                    this.result = this.result - l_val;
                    break;
                case '*':
                    if (!precedence) {
                        precedence_result = old_value;
                        precedence = true;
                    }
                    precedence_result *= l_val;
                    switch (pre_precedence_op) {
                        case 's':
                            this.result = precedence_result;
                            break;
                        case '+':
                            this.result = pre_precedence_result + precedence_result;
                            break;
                        case '-':
                            this.result = pre_precedence_result - precedence_result;
                            break;
                    }
                    break;
                case '/':
                    if (!precedence) {
                        precedence_result = old_value;
                        precedence = true;
                    }
                    precedence_result /= l_val;
                    switch (pre_precedence_op) {
                        case 's':
                            this.result = precedence_result;
                            break;
                        case '+':
                            this.result = pre_precedence_result + precedence_result;
                            break;
                        case '-':
                            this.result = pre_precedence_result - precedence_result;
                            break;
                    }
                    break;
                default:
                    break;
            }
            old_value = l_val;

        }
        this.sub_display = Double.toString(this.result);
    }

    private void refreshDisplay() {
        this.display = "";

        /* if is empty, there is nothing to display */
        if (this.q.isEmpty())
            return;

        for (int i = 0; i < this.q.size(); i++) {
            /* take values in the contrary order */
            int idx = this.q.size() - i - 1;

            char l_op = this.q.get(idx).op;
            double l_val = this.q.get(idx).value;

            if (l_op == 'v')
                this.display += Double.toString(l_val);
            else
                this.display += l_op;
        }
    }

    private void updateDisplay(String display, String sub_display) {
        tv_display.setText(display);
        tv_sub_display.setText(sub_display);
    }

    private String getDisplay() {
        return this.display;
    }

    private String getSubDisplay() {
        if (showResult)
            return this.sub_display;
        else
            return "";
    }

    private void setMode(int mode) {
        this.mode = mode;
        switch (this.mode) {
            case 0:
                this.base = 2;
                break;
            case 1:
                this.base = 10;
                break;
            case 2:
                this.base = 16;
                break;
        }
        refreshButtons();
    }

    private void refreshButtons() {

    }

}

