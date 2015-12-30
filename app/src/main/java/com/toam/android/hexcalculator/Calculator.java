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

    /* the view */
    private View v;

    /* xml related variables */
    private TextView tv_display, tv_sub_display;
    private Button[] btn_calc_hex;
    private Button[] btn_calc_op;

    /* calculation and display variables */
    private int base, mode;
    private double result, curr_val;
    private String display, sub_display;

    /* value/operands queue */
    private LinkedList<Utils.Parcel> q;

    //private final static String TAG = "Calculator";

    public Calculator(View v) {
        this.v = v;

        /* start with mode 0 (bin) */
        this.mode = 1;
        /* init with base 10 */
        this.base = 10;
        this.q = new LinkedList<>();

        btn_calc_hex = new Button[Utils.HEX_COUNT];
        btn_calc_op = new Button[Utils.OP_COUNT];
        Button[] btn_calc_mode = new Button[Utils.MODE_COUNT];

        tv_display = (TextView) v.findViewById(R.id.main_display);
        tv_sub_display = (TextView) v.findViewById(R.id.sub_display);

        /* set listener for hex's */
        for (int i = 0; i < Utils.HEX_COUNT; i++) {
            final int key = i;
            btn_calc_hex[i] = (Button) v.findViewById(Utils.btn_calc_hex_i[i]);
            btn_calc_hex[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    addKey(key);
                }
            });
        }

        /* set listener for ops */
        for (int i = 0; i < Utils.OP_COUNT; i++) {
            final int key = i;
            btn_calc_op[i] = (Button) v.findViewById(Utils.btn_calc_op_i[i]);
            btn_calc_op[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    setOperation(Utils.OP_CHAR[key]);
                }
            });
        }

        /* set listener for calc modes */
        for (int i = 0; i < Utils.MODE_COUNT; i++) {
            final int mode = i;
            btn_calc_mode[i] = (Button) v.findViewById(Utils.btn_calc_mode_i[i]);
            btn_calc_mode[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    setMode(mode);
                }
            });
        }

        enableButtons();
    }

    /* set the latest operation */
    private void setOperation(char op) {
        Utils.Parcel prc = new Utils.Parcel();

        // if list has 3 positions and mode 0 or 2 -> must flush and use new value
        /* do just for hex or binary calculation */
        if( this.mode == 0 || this.mode == 2){
            if(this.q.size() == 3) {
                flush();
            }
        }

        /* if queue isn't empty */
        if (!this.q.isEmpty()) {
        /* pop older, not updated value, then push new value */
            if (this.q.peek().op != 'v')
                this.q.pop();
        }

        /* insert operation to queue */
        prc.op = op;
        prc.value = 0;
        this.q.push(prc);

        /* case = reset everything */
        if (op == '=') {
            flush();
        }

        /* value has been pushed, reset it */
        this.curr_val = 0;

        refreshDisplay();
    }

    /* flush the value into the result string */
    private void flush() {
        Utils.Parcel prc = new Utils.Parcel();

        this.q.clear();
        prc.op = 'v';
        prc.value = this.result;
        this.q.push(prc);

        this.curr_val = 0;
        this.sub_display = "";
    }

    /* add pressed key into the current value */
    private void addKey(int key) {
        Utils.Parcel prc = new Utils.Parcel();

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

        this.result = computeResult(this.q);
        refreshDisplay();
    }

    /* compute the result by parsing the queue values */
    public double computeResult(LinkedList<Utils.Parcel> t_q) {
        /* s as in start */
        char prev_op = 's';

        /* values below are used for multiplication or division operations */
        boolean precedence = false;
        char pre_precedence_op = 's';
        double old_value = 0;
        double precedence_result = 0;
        double pre_precedence_result = 0;

        for (int i = 0; i < t_q.size(); i++) {

            /* take values in the contrary order */
            int idx = t_q.size() - i - 1;

            char l_op = t_q.get(idx).op;
            double l_val = t_q.get(idx).value;

            if (l_op != 'v') {
                prev_op = l_op;
                continue;
            } else if (prev_op == 's') {
                /* in this case it's easy, no history, result is the value */
                result = l_val;
            }

            switch (prev_op) {
                case '+':
                    precedence = false;
                    precedence_result = 0;
                    pre_precedence_op = '+';
                    pre_precedence_result = result;
                    result = result + l_val;
                    break;
                case '-':
                    precedence = false;
                    precedence_result = 0;
                    pre_precedence_op = '-';
                    pre_precedence_result = result;
                    result = result - l_val;
                    break;
                case '&':
                    result = (int)result & (int)l_val;
                    break;
                case '|':
                    result = (int)result | (int)l_val;
                    break;
                case '*':
                    if (!precedence) {
                        precedence_result = old_value;
                        precedence = true;
                    }
                    precedence_result *= l_val;
                    switch (pre_precedence_op) {
                        case 's':
                            result = precedence_result;
                            break;
                        case '+':
                            result = pre_precedence_result + precedence_result;
                            break;
                        case '-':
                            result = pre_precedence_result - precedence_result;
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
                            result = precedence_result;
                            break;
                        case '+':
                            result = pre_precedence_result + precedence_result;
                            break;
                        case '-':
                            result = pre_precedence_result - precedence_result;
                            break;
                    }
                    break;
                default:
                    break;
            }
            old_value = l_val;

        }
        switch (mode) {
            case 0:
                this.sub_display = Integer.toString((int) result, 2);
                break;
            case 1:
                this.sub_display = Double.toString(result);
                break;
            case 2:
                this.sub_display = "0x" + Integer.toString((int) result, 16);
                break;
        }
        return result;
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
                switch (mode) {
                    case 0:
                        this.display += Integer.toString((int) l_val, 2);
                        break;
                    case 1:
                        this.display += Double.toString(l_val);
                        break;
                    case 2:
                        this.display += "0x" + Integer.toString((int) l_val, 16);
                        break;
                }
            else
                this.display += l_op;

            this.display += " ";
        }

        int len = display.length();
        if (len < 20)
            tv_display.setTextSize(40);
        else if (len >= 20 && len < 25)
            tv_display.setTextSize(35);
        else if (len >= 25 && len < 30)
            tv_display.setTextSize(30);
        else if (len >= 30)
            tv_display.setTextSize(25);

        tv_display.setText(display);
        /* we should only print the result, if there is a calculation made */
        if(this.q.size() > 2)
            tv_sub_display.setText(sub_display);
        else
            tv_sub_display.setText("");
    }

    private void setMode(int mode) {
        if (mode != this.mode) {
            /* changing mode, must reset value */
            this.curr_val = 0;

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
            flush();
            refreshDisplay();
            enableButtons();
        }
    }

    private void enableButtons(){
        enableHexButtons();
        enableOpButtons();
    }

    /* enable the calculator buttons according to current mode/base */
    private void enableHexButtons() {
        for (int i = 0; i < Utils.HEX_COUNT; i++) {
            if (i < this.base) {
                btn_calc_hex[i].setClickable(true);
                btn_calc_hex[i].setTextColor(v.getResources().getColor(R.color.white));
            } else {
                btn_calc_hex[i].setClickable(false);
                btn_calc_hex[i].setTextColor(v.getResources().getColor(R.color.grey));
            }
        }
    }

    /* enable the calculator buttons according to current mode/base */
    private void enableOpButtons() {

        // final static char OP_CHAR[] = {'+', '-', '*', '/', '&', '|', '='};
        switch (this.mode) {
            case 0:
            case 2:
                for (int i = 2; i < 4; i++) {
                    btn_calc_op[i].setClickable(false);
                    btn_calc_op[i].setTextColor(v.getResources().getColor(R.color.grey));
                }
                for (int i = 4; i < 6; i++) {
                    btn_calc_op[i].setClickable(true);
                    btn_calc_op[i].setTextColor(v.getResources().getColor(R.color.white));
                }
                break;
            case 1:
                for (int i = 4; i < 6; i++) {
                    btn_calc_op[i].setClickable(false);
                    btn_calc_op[i].setTextColor(v.getResources().getColor(R.color.grey));
                }
                for (int i = 2; i < 4; i++) {
                    btn_calc_op[i].setClickable(true);
                    btn_calc_op[i].setTextColor(v.getResources().getColor(R.color.white));
                }
                break;

        }
    }
}
