package com.example.translator.view.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.translator.R
import com.example.translator.view.main.adapter.MainAdapter
import kotlinx.android.synthetic.main.fragment_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment())
                .commitNow()
        }
    }
}