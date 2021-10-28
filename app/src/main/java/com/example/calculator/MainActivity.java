package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.calculator.databinding.ActivityMainBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MaterialTextView display;

    private Double result = null;
    private String currentNumber = "";
    private Double storedNumber = null;
    private String operation = "+";

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
     * Initialize buttons
     */
    private void initButtons() {

        // Array of number buttons
        MaterialButton[] numberButtons = new MaterialButton[]{

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
        MaterialButton[] operatorButtons = new MaterialButton[]{

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

        // Set the displayed number to currentNumber
        display.setText(currentNumber);

    }

    /**
     * On click event for operator buttons
     * @param v The button that is being pressed
     */
    private void onClickOperator(View v) {

        // Get the text from the button being pressed
        String button = ((Button) v).getText().toString();

        // If mbtn_negate is pressed
        if (button.equals("+/-")) {

            // These are split to trap the execution of the method

            // If currentNumber is not empty
            if (!currentNumber.equals("")) {

                // Negate the currentNumber (positive to negate, negative to positive)
                currentNumber = String.valueOf(Integer.parseInt(currentNumber) * -1);

                // Set the displayed number to currentNumber
                display.setText(currentNumber);

            }

        // If mbtn_point is pressed
        } else if (button.equals(".")) {

            // These are split to trap the execution of the method

            // If currentNumber does not contain a decimal point
            if (!currentNumber.contains(".") && !currentNumber.equals("")) {

                // Add a decimal point to currentNumber
                currentNumber = currentNumber + ".";

                // Set the displayed number to currentNumber
                display.setText(currentNumber);

            }

        // If mbtn_delete is pressed
        } else if (button.equals("delete")) {

            // These are split to trap the execution of the method

            // If currentNumber is not empty
            if (!currentNumber.equals("")) {

                // Remove the last character from currentNumber
                currentNumber = currentNumber.substring(0, currentNumber.length() - 1);

                // Set the displayed number to currentNumber
                display.setText(currentNumber);

            }

        // If mbtn_clear is pressed
        } else if (button.equals("clear")) {

            // Reset all member variables
            result = null;
            currentNumber = "";
            storedNumber = null;
            operation = "+";

            // Set the displayed number to currentNumber
            display.setText(currentNumber);

        // If any operator is pressed (except mbtn_equals) and currentNumber is not empty
        } else if (!button.equals("=")) {

            // Set operator
            operation = button;

            // If currentNumber is empty and result is not null
            if (currentNumber.equals("") && result != null)
                storedNumber = result;

            // If currentNumber is not empty and storedNumber is null
            else if (!currentNumber.equals("") && storedNumber == null) {

                // Store currentNumber, clear current number
                storedNumber = Double.parseDouble(currentNumber);
                currentNumber = "";

                // Set the displayed number to currentNumber
                display.setText(currentNumber);

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

        // If result is null and is not equal to storedNumber
        if (Objects.equals(result, null) || !result.equals(storedNumber)) {

            // Addition
            if (operation.equals("+"))
                result = storedNumber + Double.parseDouble(currentNumber);

            // Subtraction
            if (operation.equals("-"))
                result = storedNumber - Double.parseDouble(currentNumber);

            // Multiplication
            if (operation.equals("*"))
                result = storedNumber * Double.parseDouble(currentNumber);

            // Division
            if (operation.equals(("/")))
                result = storedNumber / Double.parseDouble(currentNumber);

        } else {

            // Addition
            if (operation.equals("+"))
                result = result + Double.parseDouble(currentNumber);

            // Subtraction
            if (operation.equals("-"))
                result = result - Double.parseDouble(currentNumber);

            // Multiplication
            if (operation.equals("*"))
                result = result * Double.parseDouble(currentNumber);

            // Division
            if (operation.equals(("/")))
                result = result / Double.parseDouble(currentNumber);

        }

        // Set the displayed number to result
        display.setText(String.valueOf(result));

        // Clear currentNumber
        currentNumber = "";

        // Store result
        storedNumber = result;

    }

}