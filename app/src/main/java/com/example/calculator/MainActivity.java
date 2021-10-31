package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.calculator.databinding.ActivityMainBinding;
import com.example.calculator.layouts.Calculator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        Calculator.init(binding);
        setContentView(binding.getRoot());

    }

}