package com.example.calculator.layouts;

import android.annotation.SuppressLint;
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

/**
 * TODO
 * Create exponent
 * Comment more
 * Test Parenthesis
 * Fix equals onClick
 */

public class Calculator {

    private static MaterialTextView display;
    private static ScriptEngine engine;

    private static String currentNumber = "";
    private static String displayText = "";
    private static String equation = "";

    /**
     * Initialize the Calculator
     * @param binding ActivityMainBinding
     */
    public static void init(@NonNull ActivityMainBinding binding) {

        // Store binding from activity
        display = binding.tvMain;

        // Get rhino JavaScript engine
        engine = new ScriptEngineManager().getEngineByName("rhino");

        // Initialize on click listeners for buttons
        initButtons(binding);

    }

    /**
     * Initialize button on click listeners
     * @param binding ActivityMainBinding
     */
    private static void initButtons(@NonNull ActivityMainBinding binding) {

        // Array of number buttons
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

        // For each number button, set on click listener
        for (MaterialButton button : numberButtons)
            button.setOnClickListener(Calculator::onClickNumber);

        // Array of mathematical operators (basic)
        final MaterialButton[] operatorButtons = new MaterialButton[] {

                binding.mbtnPlus,
                binding.mbtnMinus,
                binding.mbtnMultiplication,
                binding.mbtnDivide

        };

        // For each mathematical operator, set on click listener
        for (MaterialButton button : operatorButtons)
            button.setOnClickListener(Calculator::onClickOperator);

        // Negate
        binding.mbtnNegate.setOnClickListener(Calculator::onClickNegate);

        // Decimal Point
        binding.mbtnPoint.setOnClickListener(Calculator::onClickPoint);

        // Delete
        binding.mbtnDelete.setOnClickListener(Calculator::onClickDelete);

        // Clear
        binding.mbtnClear.setOnClickListener(Calculator::onClickClear);

        // Equals
        binding.mbtnEquals.setOnClickListener(Calculator::onClickEquals);

        // Start Parenthesis
        binding.mbtnStartParen.setOnClickListener(Calculator::onClickParenthesis);

        // End Parenthesis
        binding.mbtnEndParen.setOnClickListener(Calculator::onClickParenthesis);

        // Exponent
        binding.mbtnExponent.setOnClickListener(Calculator::onClickExponent);

    }

    /**
     * Update the displayed text
     */
    private static void updateDisplay() {

        display.setText(String.format("%s %s", displayText, currentNumber));

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
        displayText = "";
        equation = "";

        updateDisplay();

    }

    /**
     * Logic for basic operator buttons
     * @param v The operator button
     */
    private static  void onClickOperator(View v) {

        if (!currentNumber.equals("")) {

            if (displayText.equals(""))
                displayText = String.format("%s %s", currentNumber, ((Button) v).getText().toString());

            else if (equation.equals(""))
                displayText = String.format("%s %s %s", displayText, currentNumber, ((Button) v).getText().toString());

            else {

                displayText = String.format("%s %s %s", displayText, currentNumber, ((Button) v).getText().toString());
                equation = String.format("%s %s %s", equation, currentNumber, ((Button) v).getText().toString());

            }

            currentNumber = "";

            updateDisplay();

        } else if (!displayText.equals("")) {

            displayText = String.format("%s %s", displayText, ((Button) v).getText().toString());

            updateDisplay();

        }

    }

    /**
     * Logic for the equals button
     * @param v The equals button
     */
    @SuppressLint("SetTextI18n")
    private static void onClickEquals(View v) {

        Log.d("DEBUG_INFO", "Current Number: " + currentNumber);
        Log.d("DEBUG_INFO", "Display Text: " + displayText);
        Log.d("DEBUG_INFO", "Equation: " + equation);

        if (!currentNumber.equals("") || !displayText.equals(""))
            try {

                String result;

                if (equation.equals(""))
                    result = String.valueOf(engine.eval(displayText + currentNumber));

                else result = String.valueOf(engine.eval(equation + currentNumber));

                if ((Double.parseDouble(result) % 1 == 0))
                    result = String.valueOf(((Double) Double.parseDouble(result)).intValue());

                display.setText(String.format("%s %s = %s", displayText, currentNumber, result));

                displayText = "";
                currentNumber = result;

                if (result.equals("Infinity")) {

                    onClickClear(v);

                    display.setText("Cannot divide by zero");

                }

                Log.d("DEBUG_INFO", result);

            } catch (ScriptException e) {

                Log.wtf("onClickEquals", e);

                onClickClear(v);

                display.setText("Error");

            }

    }

    /**
     * Logic for the parenthesis buttons
     * @param v The parenthesis button
     */
    private static void onClickParenthesis(View v) {

        String button = ((Button) v).getText().toString();

        if (currentNumber.equals("")) {

            displayText = String.format("%s %s", displayText, button);

            if (!equation.equals(""))
                equation = String.format("%s %s", equation, button);

        } else {

            displayText = String.format("%s %s %s", displayText, currentNumber, button);

            if (!equation.equals(""))
                equation = String.format("%s %s %s", equation, currentNumber, button);

            currentNumber = "";

        }

        updateDisplay();

    }

    /**
     * Logic for the exponent button
     * @param v The exponent button
     */
    private static void onClickExponent(View v) {

        if (!currentNumber.equals("")) {

            if (equation.equals(""))
                equation = String.format("%s %s", displayText, String.format("Math.pow(%s,", currentNumber));

            else
                equation = String.format("%s %s", equation, String.format("Math.pow(%s,", currentNumber));

            displayText = String.format("%s %s", displayText, String.format("%s^(", currentNumber));
            currentNumber = "";

            updateDisplay();

        }

    }

}
