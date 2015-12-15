package com.toam.android.hexcalculator;

/**
 * Created by luisfilipedias on 13-12-2015.
 *
 * Calculator class. Holds keys and operations
 *
 */
public class Calculator {

    double value, last_value, result;
    String display;
    char last_op;
    int base;
    final static String TAG = "Calculator";

    public Calculator() {   
        /* init with base 10 */
        this.base = 10;
    }

    public void addKey(int key){
        // must multiplicate for the base
        this.value = this.value * this.base + key;
        this.setDisplay(Double.toString(this.last_value) + this.last_op, Double.toString(this.value));
    }

    public void setOperation(char op){
        switch(op) {
            case '+':
            case '-':
            case '*':
            case '&':
                this.last_op = op;
                this.last_value = this.value;
                this.value = 0;
                this.setDisplay(Double.toString(this.last_value) + this.last_op,"");
                break;
            case '=':
                switch(this.last_op){
                    case '+':
                        this.result = this.last_value + this.value;
                        break;
                    case '-':
                        this.result = this.last_value - this.value;
                        break;
                    case '*':
                        this.result = this.last_value * this.value;
                        break;
                    case '&':
                        this.result = this.last_value / this.value;
                        break;
                }
                this.setDisplay(Double.toString(this.last_value) + this.last_op + Double.toString(this.value) + op, Double.toString(this.result));
                this.value = this.result;
                this.last_op = op;
                break;
        }
    }

    private void setDisplay(String line1, String line2){
        this.display = line1 + "\n" + line2;
    }

    public String getDisplay() {
        return this.display;
    }

    private void setBase(int base){
        this.base = base;
    }

    public int getBase() {
        return this.base;
    }

}

