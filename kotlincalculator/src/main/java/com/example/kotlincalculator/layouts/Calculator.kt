package com.example.kotlincalculator.layouts

import android.util.Log
import android.view.View
import android.widget.Button
import com.example.kotlincalculator.databinding.ActivityMainBinding
import com.google.android.material.textview.MaterialTextView
import org.mozilla.javascript.Context
import java.lang.Exception


class Calculator(binding: ActivityMainBinding) {

    private val display: MaterialTextView = binding.tvMain
    private val context = Context.enter()

    private var currentNumber = ""
    private var displayText = ""
    private var equation = ""

    init {

        context.optimizationLevel = -1

        initButtons(binding)

    }

    /**
     * Initialize on click listeners for buttons
     * @param binding The main activity's binding
     */
    private fun initButtons(binding: ActivityMainBinding) {

        // Array of number buttons
        val numberButtons = arrayOf(

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

        )

        // Array of mathematical operators (basic)
        val operatorButtons = arrayOf(

            binding.mbtnPlus,
            binding.mbtnMinus,
            binding.mbtnMultiplication,
            binding.mbtnDivide,
            binding.mbtnModulus

        )

        // For each number button, set on click listener
        for (button in numberButtons)
            button.setOnClickListener(this::onClickNumber)

        // For each operator button (basic), set on click listener
        for (button in operatorButtons)
            button.setOnClickListener(this::onClickOperator)

        // Negate
        binding.mbtnNegate.setOnClickListener(this::onClickNegate)

        // Decimal Point
        binding.mbtnPoint.setOnClickListener(this::onClinkPoint)

        // Delete
        binding.mbtnDelete.setOnClickListener(this::onClickDelete)

        // Clear
        binding.mbtnClear.setOnClickListener(this::onClickClear)

        // Start Parenthesis
        binding.mbtnStartParen.setOnClickListener(this::onClickParenthesis)

        // End Parenthesis
        binding.mbtnEndParen.setOnClickListener(this::onClickParenthesis)

        // Exponent
        binding.mbtnExponent.setOnClickListener(this::onClickExponent)

        // Equals
        binding.mbtnEquals.setOnClickListener(this::onClickEquals)

        // Square Root
        binding.mbtnSquareRoot.setOnClickListener(this::onClickSquareRoot)

    }

    /**
     * Evaluates a JavaScript script
     * @param script The script to execute
     * @return String or null
     */
    private fun evaluateScript(script: String): String? {

        try {

            // Create scope that the script wil run in
            val scope = context.initStandardObjects()

            // Run the script
            val result = context.evaluateString(scope, script, "script", 0, null)

            Log.d("DEBUG_INFO", result.toString())

            return result.toString()

        } catch(e: Exception) {

            Log.d("DEBUG_INFO", e.toString())

            return null

        }

    }

    /**
     * Update the displayed text
     */
    private fun updateDisplay() {

        display.text = String.format("%s %s", displayText, currentNumber)

    }

    /**
     * Logic for the number buttons
     * @param v The number button
     */
    private fun onClickNumber(v: View) {

        val button = (v as Button).text.toString()

        currentNumber = String.format("%s%s", currentNumber, button)

        updateDisplay()

    }


    /**
     * Logic for operator buttons (basic)
     * @param v The operator button
     */
    private fun onClickOperator(v: View) {

        val button = (v as Button).text.toString()

        if (currentNumber != "") {

            when {

                displayText == "" -> displayText = String.format("%s %s", currentNumber, button)
                equation == "" -> displayText = String.format("%s %s %s", displayText, currentNumber, button)

                else -> {

                    displayText = String.format("%s %s %s", displayText, currentNumber, button)
                    equation = String.format("%s %s %s", equation, currentNumber, button)

                }

            }

            currentNumber = ""

        } else if (displayText != "")
            displayText = String.format("%s %s", displayText, button)

        updateDisplay()

    }

    /**
     * Logic for the negate button
     * @param v The negate button
     */
    private fun onClickNegate(v: View) {

        if (currentNumber != "") {

            val number = currentNumber.toDouble() * -1

            currentNumber = if (number % 1.0 != 0.0) number.toString() else number.toInt().toString()

            updateDisplay()

        }

    }

    /**
     * Logic for the decimal point button
     * @param v The decimal point number
     */
    private fun onClinkPoint(v: View) {

        if (currentNumber != "" && !currentNumber.contains(".")) {

            currentNumber = String.format("%s.", currentNumber)

            updateDisplay()

        }

    }

    /**
     * Logic for the delete button
     * @param v The delete button
     */
    private fun onClickDelete(v: View) {

        if (currentNumber != "") {

            currentNumber = currentNumber.substring(0, currentNumber.lastIndex)

            updateDisplay()

        }

    }

    /**
     * Logic for the clear button
     * @param v The clear button
     */
    private fun onClickClear(v: View) {

        currentNumber = ""
        displayText = ""
        equation = ""

        updateDisplay()

    }

    /**
     * Logic for the parenthesis buttons
     * @param v The parenthesis button
     */
    private fun onClickParenthesis(v: View) {

        val button = (v as Button).text.toString()

        if (currentNumber == "") {

            displayText = String.format("%s %s", displayText, button)

            if (equation != "")
                equation = String.format("%s %s", equation, button)

        } else {

            displayText = String.format("%s %s %s", displayText, currentNumber, button)

            if (equation != "")
                equation = String.format("%s %s %s", equation, currentNumber, button)

            currentNumber = ""

        }

        updateDisplay()

    }

    /**
     * Logic for the exponent button
     * @param v The exponent button
     */
    private fun onClickExponent(v: View) {

        if (currentNumber != "") {

            equation = if (equation == "")
                String.format("%s %s", displayText, String.format("Math.pow( %s,", currentNumber))
                else String.format("%s %s", equation, String.format("Math.pow( %s,", currentNumber))

            displayText = String.format("%s %s", displayText, String.format("%s^(", currentNumber))
            currentNumber = ""

            updateDisplay()

        }

    }

    /**
     * Logic for the square root button
     * @param v The square root button
     */
    private fun onClickSquareRoot(v: View) {

        if (currentNumber == "") {

            equation = if (equation == "")
                String.format("%s Math.sqrt( ", displayText)
                else String.format("%s Math.sqrt( ", equation)

            displayText = String.format("%s √(", displayText)

            updateDisplay()

        }

    }

    /**
     * Logic for the equals button
     * @param v The equals button
     */
    private fun onClickEquals(v: View) {

        Log.d("DEBUG_INFO", "Current Number: $currentNumber")
        Log.d("DEBUG_INFO", "Display Text: $displayText")
        Log.d("DEBUG_INFO", "Equation: $equation")

        if (currentNumber != "" || displayText != "") {

            var result = if (equation == "")
                evaluateScript(String.format("%s %s", displayText, currentNumber))
                else evaluateScript(String.format("%s %s", equation, currentNumber))

            if (result != null) {

                if (result.toDouble() % 1.0 == 0.0)
                    result = result.toDouble().toInt().toString()

                display.text = String.format("%s %s = %s", displayText, currentNumber, result)

                displayText = ""
                equation = ""
                currentNumber = result

                if (result == "Infinity") {

                    onClickClear(v)

                    display.text = "Cannot divide by zero"

                }

            } else {

                onClickClear(v)

                display.text = "Invalid equation"

            }

        }

    }

}