package com.example.kotlincalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlincalculator.databinding.ActivityMainBinding
import com.example.kotlincalculator.layouts.Calculator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)

        Calculator(binding)

        setContentView(binding.root)

    }
}