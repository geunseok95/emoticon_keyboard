package com.professionalandroid.apps.miniproject_platfarm.search_custom_view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import com.professionalandroid.apps.miniproject_platfarm.EmoticonKeyboardService
import com.professionalandroid.apps.miniproject_platfarm.R
import java.util.*

class SearchActivity: Activity() {

    var edit: EditText? = null
    var searchBtn: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.layout_search_keyboard)
        window.setGravity(Gravity.BOTTOM)
        val disp = applicationContext.resources.displayMetrics

        val params: ViewGroup.LayoutParams? = window?.attributes
        val deviceWidth = disp.widthPixels
        params?.width = deviceWidth
        window?.attributes = params as WindowManager.LayoutParams

        edit = findViewById<EditText>(R.id.search_bar)
        searchBtn = findViewById(R.id.search_button)
    }

    override fun onResume() {
        super.onResume()

        // 키보드가 자동으로 내려가므로 delay후 다시 올리기
        val myTask: TimerTask = object : TimerTask() {
            override fun run() {
                showKeyboard()
            }
        }
        val timer = Timer()
        timer.schedule(myTask, 300)

        searchBtn?.setOnClickListener {
            val intent = Intent(applicationContext, EmoticonKeyboardService()::class.java).apply {
                putExtra(
                    "search",
                    edit?.text.toString()
                )
            }
            startService(intent)
            finish()
        }
    }

    fun showKeyboard() {
        val inputMethodManager =
            applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }

}