package com.professionalandroid.apps.miniproject_platfarm

import android.inputmethodservice.InputMethodService
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

    // view가 차지할 영역을 정의
    override fun onCreateInputView(): View {
        // view 중복 참조 제거 (화면 회전 시 등)
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
        emoticonKeyboard = EmoticonCustomView(applicationContext).apply {
            inputConnection = currentInputConnection
            setData(itemList)   // customView에 데이터 추가
        }
        keyboardContainer?.addView(emoticonKeyboard?.getLayout())
    }

}