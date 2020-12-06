package com.professionalandroid.apps.miniproject_platfarm

import android.inputmethodservice.InputMethodService
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view.EmoticonCustomView
import com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view.modles.EmoticonData

class EmoticonKeyboardService: InputMethodService() {

    var keyboardView: LinearLayout? = null
    var keyboardContainer: FrameLayout? = null
    var emoticonKeyboard: EmoticonCustomView? = null

    val itemList = listOf(
        EmoticonData(listOf(R.drawable.happy_zzooni, R.drawable.black_happy_zzooni), listOf(R.drawable.happy_zzooni,R.drawable.sad_zzani,R.drawable.sad_zzooni,R.drawable.happy_zzooni,R.drawable.sad_zzani,R.drawable.sad_zzooni,R.drawable.happy_zzooni,R.drawable.sad_zzani,R.drawable.sad_zzooni,R.drawable.happy_zzooni,R.drawable.sad_zzani,R.drawable.sad_zzooni,R.drawable.happy_zzooni,R.drawable.sad_zzani,R.drawable.sad_zzooni,R.drawable.happy_zzooni,R.drawable.sad_zzani,R.drawable.sad_zzooni)),
        EmoticonData(listOf(R.drawable.sad_zzani, R.drawable.black_sad_zzani), listOf(R.drawable.happy_zzooni,R.drawable.sad_zzani,R.drawable.sad_zzooni,R.drawable.happy_zzooni,R.drawable.sad_zzani,R.drawable.sad_zzooni)),
        EmoticonData(listOf(R.drawable.sad_zzooni, R.drawable.black_sad_zzooni), listOf(R.drawable.happy_zzooni,R.drawable.sad_zzani,R.drawable.sad_zzooni,R.drawable.happy_zzooni,R.drawable.sad_zzani,R.drawable.sad_zzooni)),
        EmoticonData(listOf(R.drawable.happy_zzooni, R.drawable.black_happy_zzooni), listOf(R.drawable.happy_zzooni,R.drawable.sad_zzani,R.drawable.sad_zzooni,R.drawable.happy_zzooni,R.drawable.sad_zzani,R.drawable.sad_zzooni)),
        EmoticonData(listOf(R.drawable.sad_zzani, R.drawable.black_sad_zzani), listOf(R.drawable.happy_zzooni,R.drawable.sad_zzani,R.drawable.sad_zzooni,R.drawable.happy_zzooni,R.drawable.sad_zzani,R.drawable.sad_zzooni)),
        EmoticonData(listOf(R.drawable.happy_zzooni, R.drawable.black_happy_zzooni), listOf(R.drawable.happy_zzooni,R.drawable.sad_zzani,R.drawable.sad_zzooni,R.drawable.happy_zzooni,R.drawable.sad_zzani,R.drawable.sad_zzooni)),
        EmoticonData(listOf(R.drawable.happy_zzooni, R.drawable.black_happy_zzooni), listOf(R.drawable.happy_zzooni,R.drawable.sad_zzani,R.drawable.sad_zzooni,R.drawable.happy_zzooni,R.drawable.sad_zzani,R.drawable.sad_zzooni)),
        EmoticonData(listOf(R.drawable.sad_zzani, R.drawable.black_sad_zzani), listOf(R.drawable.happy_zzooni,R.drawable.sad_zzani,R.drawable.sad_zzooni,R.drawable.happy_zzooni,R.drawable.sad_zzani,R.drawable.sad_zzooni)), EmoticonData(listOf(R.drawable.happy_zzooni, R.drawable.black_happy_zzooni), listOf(R.drawable.happy_zzooni,R.drawable.sad_zzani,R.drawable.sad_zzooni,R.drawable.happy_zzooni,R.drawable.sad_zzani,R.drawable.sad_zzooni))
    )

    val keyboardInteractionListener = object : KeyboardInteractionListener{
        override fun modeChange(mode: Int) {
            currentInputConnection.finishComposingText()

        }
    }

    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartInput(attribute: EditorInfo?, restarting: Boolean) {
        super.onStartInput(attribute, restarting)
        Log.d("test", "start input")
    }

    // view가 차지할 영역을 정의
    override fun onCreateInputView(): View {
        Log.d("test", "onCreateInputView")
        if (keyboardView != null){
            (keyboardView as ViewGroup).removeAllViews()
        }
        keyboardView = layoutInflater.inflate(R.layout.layout_emoticon_keyboard, null) as LinearLayout
        keyboardContainer = keyboardView?.findViewById(R.id.keyboard_container)
        return keyboardView!!
    }

    // view가 튀어 나올 때
    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)
        Log.d("test", "start input view")
        emoticonKeyboard = EmoticonCustomView(applicationContext)
        emoticonKeyboard?.inputConnection = currentInputConnection
        emoticonKeyboard?.setData(itemList)
        keyboardContainer?.addView(emoticonKeyboard?.getLayout())
    }

    override fun onFinishInputView(finishingInput: Boolean) {
        super.onFinishInputView(finishingInput)
        Log.d("test", "finish input view")
    }

    override fun onFinishInput() {
        super.onFinishInput()
        Log.d("test", "finish")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("test", "destroy")
    }
}