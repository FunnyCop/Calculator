package com.example.calculator.layouts;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.calculator.databinding.ActivityMainBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Calculator {

    private static MaterialTextView display;
    private static ScriptEngine engine;

    private static String currentNumber = "";
    private static String displaytText = "";

    /**
     * Initialize the Calculator
     * @param binding ActivityMainBinding
     */
    public static void init(@NonNull ActivityMainBinding binding) {

        display = binding.tvMain;
        engine = new ScriptEngineManager().getEngineByName("rhino");

        initButtons(binding);

    }

    /**
     * Initialize button on click listeners
     * @param binding ActivityMainBinding
     */
    private static void initButtons(@NonNull ActivityMainBinding binding) {

        final MaterialButton[] numberButtons = new MaterialButton[] {

                binding.mbtnZero,
                binding.mbtnOne,
                binding.mbtnTwo,
                binding.mbtnThree,
                binding.mbtnFour,
                binding.mbtnFive,
                binding.mbtnSix,
                binding.mbtnSeven,
                binding.mbtnEight,
                binding.mbtnNine

        };

        for (MaterialButton button : numberButtons)
            button.setOnClickListener(Calculator::onClickNumber);

        binding.mbtnNegate.setOnClickListener(Calculator::onClickNegate);
        binding.mbtnPoint.setOnClickListener(Calculator::onClickPoint);
        binding.mbtnDelete.setOnClickListener(Calculator::onClickDelete);
        binding.mbtnClear.setOnClickListener(Calculator::onClickClear);
        binding.mbtnEquals.setOnClickListener(Calculator::onClickEquals);
        binding.mbtnStartParen.setOnClickListener(Calculator::onClickParenthesis);
        binding.mbtnEndParen.setOnClickListener(Calculator::onClickParenthesis);

        final MaterialButton[] operatorButtons = new MaterialButton[] {

                binding.mbtnPlus,
                binding.mbtnMinus,
                binding.mbtnMultiplication,
                binding.mbtnDivide

        };

        for (MaterialButton button : operatorButtons)
            button.setOnClickListener(Calculator::onClickOperator);

        display.setText(displaytText);

    }

    /**
     * Update the displayed text
     */
    private static void updateDisplay() {

        display.setText(String.format("%s %s", displaytText, currentNumber));

    }

    /**
     * Logic for number buttons
     * @param v Button that is clicked
     */
    private static void onClickNumber(View v) {

        currentNumber = currentNumber + ((Button) v).getText().toString();

        updateDisplay();

    }

    /**
     * Logic for the negate button
     * @param v The negate button
     */
    private static void onClickNegate(View v) {

        if (!currentNumber.equals("")) {

            Double number = Double.parseDouble(currentNumber);

            number = number * -1;

            if (number % 1 != 0)
                currentNumber = String.valueOf(number);

            else currentNumber = String.valueOf(number.intValue());

            updateDisplay();

        }

    }

    /**
     * Logic for the decimal point button
     * @param v The decimal point button
     */
    private static void onClickPoint(View v) {

        if (!currentNumber.contains(".")) {

            currentNumber = currentNumber + ".";

            updateDisplay();

        }

    }

    /**
     * Logic for the delete button
     * @param v The delete button
     */
    private static void onClickDelete(View v) {

        if (!currentNumber.equals("")) {

            currentNumber = currentNumber.substring(0, currentNumber.length() - 1);

            updateDisplay();

        }

    }

    /**
     * Logic for the clear button
     * @param v The clear button
     */
    private static void onClickClear(View v) {

        currentNumber = "";
        displaytText = "";

        updateDisplay();

    }

    /**
     * Logic for basic operator buttons
     * @param v The operator button
     */
    private static  void onClickOperator(View v) {

        if (!currentNumber.equals("")) {

            if (displaytText.equals(""))
                displaytText = String.format("%s %s", currentNumber, ((Button) v).getText().toString());

            else
                displaytText = String.format("%s %s %s", displaytText, currentNumber, ((Button) v).getText().toString());

            currentNumber = "";

            updateDisplay();

        } else if (!displaytText.equals("")) {

            displaytText = String.format("%s %s", displaytText, ((Button) v).getText().toString());

            updateDisplay();

        }

    }

    /**
     * Logic for the equals button
     * @param v The equals button
     */
    private static void onClickEquals(View v) {

        if (!currentNumber.equals("") || !displaytText.equals(""))
            try {

                String result = String.valueOf(engine.eval(displaytText + currentNumber));

                if ((Double.parseDouble(result) % 1 == 0))
                    result = String.valueOf(((Double) Double.parseDouble(result)).intValue());

                display.setText(String.format("%s %s = %s", displaytText, currentNumber, result));

                displaytText = "";
                currentNumber = result;

                if (result.equals("Infinity")) {

                    onClickClear(v);

                    display.setText("Cannot divide by zero");

                }

            } catch (ScriptException e) {

                Log.wtf("onClickEquals", e);

            }

    }

    /**
     * Logic for the parenthesis buttons
     * @param v The parenthesis button
     */
    private static void onClickParenthesis(View v) {

        String button = ((Button) v).getText().toString();

        if (currentNumber.equals(""))
            displaytText = String.format("%s %s", displaytText, "(");

        else {

            displaytText = String.format("%s %s %s", displaytText, currentNumber, button);
            currentNumber = "";

        }

        updateDisplay();

    }

}
