package com.toam.android.hexcalculator;

import android.util.Log;

/**
 * Created by luisfilipedias on 13-12-2015.
 *
 * Calculator class. Holds keys and operations
 *
 */
public class Calculator {

    int last_key, current_key;
    char op;
    final static String TAG = "Calculator";

    public Calculator() {

    }

    public void setCurrentKey(int key){
        this.current_key = key;
        this.last_key = this.current_key;
    }

    public void setOperation(char op){
        this.op = op;
    }

    public void printInfo(){
        Log.d(TAG, "Current Key: " + this.current_key);
        Log.d(TAG, "Last Key: " + this.last_key);
        Log.d(TAG, "Operation: " + this.op);
    }
}
