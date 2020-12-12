package com.professionalandroid.apps.miniproject_platfarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        Log.d("test", "onResume")
    }

    override fun onStart() {
        super.onStart()
        Log.d("test", "onStart")
    }

    override fun onPause() {
        super.onPause()
        Log.d("test", "onPause")

    }

    override fun onStop() {
        super.onStop()
        Log.d("test", "onStop")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("test", "onDestroy")

    }

}