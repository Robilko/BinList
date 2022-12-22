package com.robivan.binlist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.robivan.binlist.R
import com.robivan.binlist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            startMainFragment()
        }
    }

    private fun startMainFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.container, MainFragment.newInstance())
            .commit()
    }
}