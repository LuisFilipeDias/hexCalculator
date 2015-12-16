package com.toam.android.hexcalculator;

/**
 * Created by luisfilipedias on 13-12-2015.
 *
 * Calculator class. Holds keys and operations
 *
 */
public class Calculator {

    double value[], result, curr_val;
    private String display, sub_display;
    private char op;
    private int base, position;
    private boolean update_values;
    private final static String TAG = "Calculator";
    private String display_copy;
    private char old_op;

    /*
    these values are used to compute the result, when a * or / operation appears
        this is necessary because of precedence
        we must save the last result before the operation, and then sum everything after
     */
    private double old_result;
    private double mlt_result;
    private double old_val;
    private boolean multy;

    public Calculator() {   
        /* init with base 10 */
        this.base = 10;
        this.display = "";
        this.update_values = false;
        this.value = new double[Utils.MAX_DIGITS];
        this.position = 0;
        /* s is for start -> means result is the value itself */
        this.op = 's';
        this.old_op = 's';
        this.multy = false;
    }

    public void addKey(int key){
        /* if update_values, push value to last_value before computing new value */
        /*if(update_values) {
            this.update_values = false;
            this.display += this.op;
        }*/

        //computeResult();

        // must multiplicate for the base
        this.curr_val = this.curr_val * this.base + key;
        this.display += key;
        this.display_copy = this.display;
        computeResult();
//        this.setDisplay(this. + this.op, Double.toString(this.result));
    }

    public void setOperation(char op){

        insertToList();
        /* create a copy of op, to use when a * or / is called */
        if(this.op == '+' || this.op == '=')
            this.old_op = this.op;

        switch(op) {
            case '+':
            case '-':
            case '*':
            case '/':
                this.display = this.display_copy + op;
                this.op = op;
                this.update_values = true;
          //      this.setDisplay(Double.toString(this.value) + this.op,Double.toString(this.result));
                break;
            case '=':
     //           computeResult();
       //         this.setDisplay(Double.toString(this.result), "");
            //    this.value = this.result;
                this.op = op;
                break;
        }
    }

    private void insertToList(){
        this.old_val = this.curr_val;
        this.value[this.position] = this.curr_val;
        this.curr_val = 0;
        this.position += 1;
    }

    private void computeResult(){
        switch(this.op){
            /* case s we don't want to print the result, go back */
            case 's':
                this.result = this.curr_val;
                this.mlt_result = 1;
                return;
            case '+':
                this.multy = false;
                this.old_result = this.result;
                this.result = this.result + this.curr_val;
                this.mlt_result = 1;
                break;
            case '-':
                this.multy = false;
                this.old_result = this.result;
                this.result = this.result - this.curr_val;
                this.mlt_result = 1;
                break;
            case '*':
                /* save last operator */
                /* save last value */
                if( (this.old_op == '+' || this.old_op == '-' || this.old_op == 's') && !this.multy) {
                    this.mlt_result = this.old_val;
                    this.multy = true;
                }

                this.mlt_result = this.mlt_result * this.curr_val;
                this.result = this.mlt_result;

                switch (this.old_op){
                    case '+':
                        this.result = this.old_result + this.mlt_result;
                        break;
                    case '-':
                        this.result = this.old_result - this.mlt_result;
                        break;
                }
                break;
            case '/':
                /* save last operator */
                /* save last value */
                if( (this.old_op == '+' || this.old_op == '-' || this.old_op == 's') && !this.multy) {
                    this.mlt_result = this.old_val;
                    this.multy = true;
                }

                this.mlt_result = this.mlt_result / this.curr_val;
                this.result = this.mlt_result;

                switch (this.old_op){
                    case '+':
                        this.result = this.old_result + this.mlt_result;
                        break;
                    case '-':
                        this.result = this.old_result - this.mlt_result;
                        break;
                }
                break;
        }
        this.sub_display = Double.toString(this.result);
    }

    public String getDisplay() {
        return this.display;
    }

    public String getSubDisplay() {
        return this.sub_display;
    }

    private void setBase(int base){
        this.base = base;
    }

    public int getBase() {
        return this.base;
    }

}

