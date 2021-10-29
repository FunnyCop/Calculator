package com.example.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.calculator.databinding.ActivityMainBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MaterialTextView display;

    private String currentNumber = "";
    private Double storedNumber = null;
    private Double result = null;
    private String operation;

    private final String DIVIDE_BY_ZERO_ERROR = "Error, cannot divide by zero";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Initialization
        initBinding();
        initDisplay();
        initButtons();

    }

    /**
     * Initialize Binding
     */
    private void initBinding() {

        // Set binding to activity_main
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

    }

    /**
     * Initialize Display
     */
    private void initDisplay() {

        // Set display to tv_main
        display = binding.tvMain;

        // Set the displayed number to currentNumber
        display.setText(currentNumber);

    }

    /**
     * Set displayed string
     */
    private void setDisplay() {

        // If result is Infinity, display error
        if (String.valueOf(result).equals("Infinity")) {

            display.setText(DIVIDE_BY_ZERO_ERROR);

            // Set currentNumber to storedNumber
            currentNumber = String.valueOf(storedNumber);

            // Clear operation
            operation = "";

            // Clear storedNumber
            storedNumber = null;

            // Clear result
            result = null;

            // If storedNumber is not null
        } else if (storedNumber != null)

            // Set the displayed number to currentNumber
            display.setText(String.format("%s %s %s", storedNumber, operation, currentNumber));

        // Set the displayed number to currentNumber
        else display.setText(currentNumber);

    }

    /**
     * Initialize buttons
     */
    private void initButtons() {

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

        // Array of operator buttons
        final MaterialButton[] operatorButtons = new MaterialButton[] {

                binding.mbtnPlus,
                binding.mbtnMinus,
                binding.mbtnMultiplication,
                binding.mbtnDivide,
                binding.mbtnNegate,
                binding.mbtnEquals,
                binding.mbtnPoint,
                binding.mbtnDelete,
                binding.mbtnClear

        };

        // For each number button, attach onClickNumber to the onClickListener
        for (MaterialButton button : numberButtons)
            button.setOnClickListener(this::onClickNumber);

        // For each operator button, attach onClickOperator to the onClickListener
        for (MaterialButton button : operatorButtons)
            button.setOnClickListener(this::onClickOperator);

    }

    /**
     * On click event for number buttons
     * @param v The Button that is being pressed
     */
    private void onClickNumber(View v) {

        // Add the clicked number to current number
        currentNumber = currentNumber + ((Button) v).getText().toString();

        setDisplay();

    }

    /**
     * On click event for operator buttons
     * @param v The button that is being pressed
     */
    private void onClickOperator(View v) {

        // Get the text from the button being pressed
        final String button = ((Button) v).getText().toString();

        // Might change this huge if/elseif with a switch, not sure if possible

        switch(button) {

            // Negate
            case "+/-": negateCurrentNumber(); break;

            // Point
            case ".": addDecimalPoint(); break;

            // Delete
            case "delete": deleteLastCharacter(); break;

            // Clear
            case "clear": clearValues(); break;

            default: determineEvaluation(button);

        }

        // If button is not mbtn_negate, set operator
        if (!button.equals("+/-") && !button.equals("=")) operation = button;

        // If button is not mbtn_equals, set display
        if (!button.equals("=")) setDisplay();

    }

    /**
     * Negate currentNumber
     */
    private void negateCurrentNumber() {

        // If currentNumber is not empty
        if (!currentNumber.equals("")) {

            // Negate currentNumber (positive to negate, negative to positive)
            currentNumber = String.valueOf(Integer.parseInt(currentNumber) * -1);

            setDisplay();

        }

    }

    /**
     * Add a decimal point to currentNumber
     */
    private void addDecimalPoint() {

        // If currentNumber does not contain a decimal point
        if (!currentNumber.contains(".") && !currentNumber.equals("")) {

            // Add a decimal point to currentNumber
            currentNumber = currentNumber + ".";

            setDisplay();

        }

    }

    /**
     * Delete the last character in currentNumber
     */
    private void deleteLastCharacter() {

        // If currentNumber is not empty
        if (!currentNumber.equals("")) {

            // Remove the last character from currentNumber
            currentNumber = currentNumber.substring(0, currentNumber.length() - 1);

            setDisplay();

        }

    }

    /**
     * Clear all values
     */
    private void clearValues() {

        // Reset all member variables
        result = null;
        currentNumber = "";
        storedNumber = null;
        operation = "";

        // If error is not displayed
        if (!display.getText().toString().equals("Error"))
            setDisplay();

    }

    /**
     * Determine how to evaluate the pressed operator (mathematical)
     * @param button Button that is being pressed
     */
    private void determineEvaluation(@NonNull String button) {

        // If any operator is pressed (except mbtn_equals)
        if (!button.equals("=")) {

            // If currentNumber is empty and result is not null
            if (currentNumber.equals("") && result != null)
                storedNumber = result;

                // If currentNumber is not empty and storedNumber is null
            else if (!currentNumber.equals("") && storedNumber == null) {

                // Store currentNumber, clear current number
                storedNumber = Double.parseDouble(currentNumber);
                currentNumber = "";

                setDisplay();

                // If currentNumber is not empty
            } else if (!currentNumber.equals(""))
                evaluateOperation();

            // If mbtn_equals is pressed and currentNumber is not empty
        } else if (!currentNumber.equals(""))
            evaluateOperation();

    }

    /**
     * Evaluate operation against current and stored numbers
     */
    private void evaluateOperation() {

        logValues("BEFORE EVALUATE");

        final String equation = String.format("%s %s %s", storedNumber, operation, currentNumber);
        Double target;

        // If result is null and is not equal to storedNumber
        if (Objects.equals(result, null) || !result.equals(storedNumber))
            target = storedNumber;

        else target = result;

        switch (operation) {

            // Addition
            case "+":
                result = target + Double.parseDouble(currentNumber);
                break;

            // Subtraction
            case "-":
                result = target - Double.parseDouble(currentNumber);
                break;

            // Multiplication
            case "*":
                result = target * Double.parseDouble(currentNumber);
                break;

            // Division
            case "/":
                result = target / Double.parseDouble(currentNumber);

        }

        // If result is Infinity, display error
        if (String.valueOf(result).equals("Infinity")) {

            display.setText(DIVIDE_BY_ZERO_ERROR);

            // Set currentNumber to storedNumber
            currentNumber = String.valueOf(storedNumber);

            // Clear operation
            operation = "";

            // Clear storedNumber
            storedNumber = null;

            // Clear result
            result = null;

        } else {

            // Set the displayed number to result
            display.setText(String.format("%s = %s", equation, result));

            // Store result
            storedNumber = result;

            // Clear currentNumber
            currentNumber = "";

        }

        logValues("AFTER EVALUATE");

    }

    /**
     * Log dynamic values
     * @param description Description of action
     */
    private void logValues(@NonNull String description) {

        final String DEBUG_TAG = "DEBUG_INFO";

        Log.d(DEBUG_TAG, description);
        Log.d(DEBUG_TAG, "Current Number: " + currentNumber);
        Log.d(DEBUG_TAG, "Stored Number: " + storedNumber);
        Log.d(DEBUG_TAG, "Result: " + result);
        Log.d(DEBUG_TAG, "Operation: " + operation);
        Log.d(DEBUG_TAG, "Display: " + display.getText().toString());

    }

}