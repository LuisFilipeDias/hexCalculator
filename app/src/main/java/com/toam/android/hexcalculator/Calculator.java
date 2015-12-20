package com.toam.android.hexcalculator;

import java.util.LinkedList;

/**
 * Created by luisfilipedias on 13-12-2015.
 *
 * Calculator class. Holds keys and operations
 *
 */
public class Calculator {

    double value[], result, curr_val;
    private String display, sub_display;
    private int base;
    private final static String TAG = "Calculator";

    class Parcel {
        /* op = 'v' stands for value, otherwise they are actual operations */
        char op;
        /* in case op != 'v' value should be null */
        double value;
    }

    private LinkedList <Parcel> q;

    /*
    these values are used to compute the result, when a * or / operation appears
        this is necessary because of precedence
        we must save the last result before the operation, and then sum everything after
     */
    private double old_result;
    private double mlt_result;
    private double old_val;
    private boolean showResult;

    public Calculator() {   
        /* init with base 10 */
        this.base = 10;
        /* show result only after first operation */
        this.showResult = false;
        this.q = new LinkedList<Parcel>();
    }

    public void addKey(int key){
        Parcel prc = new Parcel();

        this.curr_val = this.curr_val * this.base + key;

        /* insert value to queue */
        prc.op = 'v';
        prc.value = this.curr_val;

        /* if queue isn't empty */
        if(!this.q.isEmpty())
        /* pop older, not updated value, then push new value */
            if(this.q.peek().op == 'v')
                this.q.pop();

        this.q.push(prc);

        computeResult();
        refreshDisplay();
    }

    private void computeResult(){

        /* e as in empty */
        char prev_op = 'e';

        /* values below are used for multiplication or division operations */
        boolean precedence = false;
        char pre_precedence_op = 's';
        double old_value = 0;
        double precedence_result = 0;
        double pre_precedence_result = 0;

        for(int i = 0; i < this.q.size(); i++){

            /* take values in the contrary order */
            int idx = this.q.size()- i - 1;

            char   l_op  = this.q.get(idx).op;
            double l_val = this.q.get(idx).value;


            if(l_op != 'v') {
                prev_op = l_op;
                this.showResult = true;
                continue;
            } else if(prev_op == 'e') {
                /* in this case it's easy, no history, result is the value */
                this.result = l_val;
                continue;
            }

            switch(prev_op){
                case '+':
                    this.result = this.result + l_val;
                    pre_precedence_op = '+';
                    pre_precedence_result = this.result;
                    break;
                case '-':
                    this.result = this.result - l_val;
                    pre_precedence_op = '-';
                    pre_precedence_result = this.result;
                    break;
                case '*':
                    if(precedence == false){
                        precedence_result = old_value;
                        precedence = true;
                    } else {
                        precedence_result *= precedence_result;
                    }

                    switch (pre_precedence_op){
                        case '+':
                            this.result = pre_precedence_result + precedence_result;
                            break;
                        case '-':
                            this.result = pre_precedence_result - precedence_result;
                            break;
                    }

                    break;
            }
            old_value = l_val;

        }
        this.sub_display = Double.toString(this.result);

        /*
        switch(this.op){
             case s we don't want to print the result, go back
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
                 save last operator
                 save last value
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
        this.sub_display = Double.toString(this.result);*/
    }

    public void setOperation(char op){
        Parcel prc = new Parcel();

        /* insert operation to queue */
        prc.op = op;
        prc.value = 0;
        this.q.push(prc);

        /* value has been pushed, reset it */
        this.curr_val = 0;

        refreshDisplay();
    }


    public void refreshDisplay() {
        this.display = "";

        /* if is empty, there is nothing to display */
        if(this.q.isEmpty())
            return;

        for(int i = 0; i < this.q.size(); i++){
            /* take values in the contrary order */
            int idx = this.q.size()- i - 1;

            char   l_op  = this.q.get(idx).op;
            double l_val = this.q.get(idx).value;

            if(l_op == 'v')
                this.display += Double.toString(l_val);
            else
                this.display += l_op;
        }
    }

    public String getDisplay() {
        return this.display;
    }

    public String getSubDisplay() {
        if(showResult)
            return this.sub_display;
        else
            return "";
    }

    private void setBase(int base){
        this.base = base;
    }

    public int getBase() {
        return this.base;
    }

}

