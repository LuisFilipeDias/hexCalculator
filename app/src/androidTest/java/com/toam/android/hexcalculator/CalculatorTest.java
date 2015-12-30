package com.toam.android.hexcalculator;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;

import java.util.LinkedList;
import java.util.Random;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * Created by luisfilipedias on 28-12-2015.
 */
public class CalculatorTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mMainActivity;

    Calculator calc;

    public CalculatorTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mMainActivity = getActivity();

        View v = mMainActivity.findViewById(android.R.id.content).getRootView();

        calc = new Calculator(v);
    }


    @SmallTest
    public void testCalculator_computation() throws EvalError {

        final int COMPUTATION_TEST_COUNT = 10;
        double result, expected_result;
        Object expected;
        String input;

        Interpreter interpreter = new Interpreter();
        LinkedList<Utils.Parcel> list;

        for(int i = 0; i < COMPUTATION_TEST_COUNT; i++) {

            /* generate an input */
            input = generateInput();

            /* pass the input into the interpreter to check expected value */
            interpreter.eval("result = " + input);
            expected = interpreter.get("result");
            expected_result = Double.valueOf(expected.toString());

            /* compile the list from the input string */
            list = compileList(input);

            /* calculate the result via the calculator */
            result = calc.computeResult(list);

            /* assert computation went fine */
            assertEquals(result, expected_result);

        }

        // 1) activate different modes then check for the activated buttons.
        // 2) click disabled buttons and observe no behaviour

    }

    private String generateInput() {
    /* random related variables */
        Random rdm = new Random();
        int r, r_m, r_M, c = 0, c_max;
        String input = "";
        char ops[] = {'+', '-', '*', '/'};

        //r_m = 1;
        //r_M = 100;
        //c_max = rdm.nextInt(r_M - r_m + 1) + r_m;
        c_max = 4;

        if(c_max%2 == 0)
            c_max += 1;

        while(c < c_max){
            if(c%2 == 0){
                r_m = 0;
                //r_M = 1000;
                r_M = 9;
                r = rdm.nextInt(r_M - r_m + 1) + r_m;
                input += Integer.toString(r);
            } else {
                /* 4 operators [0-3] */
                r_m = 0;
                r_M = 3;
                /* get a random operation */
                r = rdm.nextInt(r_M - r_m + 1) + r_m;
                input += ops[r];
            }
            c++;
        }
        return input;
    }

    public LinkedList<Utils.Parcel> compileList(String input) {
        LinkedList<Utils.Parcel> l_list = new LinkedList<>();
        Utils.Parcel l_prc = new Utils.Parcel();

        for (char ch : input.toCharArray()) {
            switch (ch) {
                case '+':
                    l_prc.op = '+';
                    l_prc.value = 0;
                    break;
                case '-':
                    l_prc.op = '-';
                    l_prc.value = 0;
                    break;
                case '*':
                    l_prc.op = '*';
                    l_prc.value = 0;
                    break;
                case '/':
                    l_prc.op = '/';
                    l_prc.value = 0;
                    break;
                case '&':
                    l_prc.op = '&';
                    l_prc.value = 0;
                    break;
                case '|':
                    l_prc.op = '|';
                    l_prc.value = 0;
                    break;
                default:
                    l_prc.op = 'v';
                    if (l_prc.value != 0)
                        l_list.pop();
                    l_prc.value = l_prc.value * 10 + Integer.parseInt(String.valueOf(ch));
                    break;

            }
            l_list.push(l_prc);
        }
        return l_list;
    }
}