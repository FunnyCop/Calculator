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
    private double storedNumber = 0.0;
    private String operation = "+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        initBinding();
        initDisplay();
        initButtons();

    }

    /**
     * Initialize Binding
     */
    private void initBinding() {

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

    }

    /**
     * Initialize Display
     */
    private void initDisplay() {

        display = binding.tvMain;
        display.setText(currentNumber);

    }

    /**
     * Initialize buttons
     */
    private void initButtons() {

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

        for (MaterialButton button : numberButtons)
            button.setOnClickListener(this::onClickNumber);

        for (MaterialButton button : operatorButtons)
            button.setOnClickListener(this::onClickOperator);

    }

    /**
     * On click event for number buttons
     * @param v The Button that is being pressed
     */
    private void onClickNumber(View v) {

        String button = ((Button) v).getText().toString();
        currentNumber = currentNumber + button;

        display.setText(currentNumber);

    }

    /**
     * On click event for operator buttons
     * @param v The button that is being pressed
     */
    private void onClickOperator(View v) {

        String button = ((Button) v).getText().toString();

        if (button.equals("+/-")) {

            currentNumber = String.valueOf(Integer.parseInt(currentNumber) * -1);

            display.setText(currentNumber);

        } else if (button.equals(".")) {

            if (!currentNumber.contains(".")) {

                currentNumber = currentNumber + ".";

                display.setText(currentNumber);

            }

        } else if (button.equals("delete")) {

            currentNumber = currentNumber.substring(0, currentNumber.length() - 1);

            display.setText(currentNumber);

        } else if (button.equals("clear")) {

            result = null;
            currentNumber = "";
            storedNumber = 0.0;
            operation = "+";

            display.setText(currentNumber);

        } else if (!button.equals("=") && !currentNumber.equals("")) {

            storedNumber = Integer.parseInt(currentNumber);
            currentNumber = "";
            operation = button;

            display.setText(currentNumber);

        } else if (!currentNumber.equals(""))
            evaluateOperation();

    }

    /**
     * Evaluate operation against current and stored numbers
     */
    private void evaluateOperation() {

        if (Objects.equals(result, null) || result != storedNumber) {

            if (operation.equals("+"))
                result = storedNumber + (double) Integer.parseInt(currentNumber);

            if (operation.equals("-"))
                result = storedNumber - (double) Integer.parseInt(currentNumber);

            if (operation.equals("*"))
                result = storedNumber * (double) Integer.parseInt(currentNumber);

            if (operation.equals(("/")))
                result = storedNumber / (double) Integer.parseInt(currentNumber);

        } else {

            if (operation.equals("+"))
                result = result + (double) Integer.parseInt(currentNumber);

            if (operation.equals("-"))
                result = result - (double) Integer.parseInt(currentNumber);

            if (operation.equals("*"))
                result = result * (double) Integer.parseInt(currentNumber);

            if (operation.equals(("/")))
                result = result / (double) Integer.parseInt(currentNumber);

        }

        display.setText(String.valueOf(result));

        currentNumber = "";
        storedNumber = result;

    }

}