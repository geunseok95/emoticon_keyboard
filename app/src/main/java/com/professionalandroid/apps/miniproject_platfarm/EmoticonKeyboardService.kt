package com.professionalandroid.apps.miniproject_platfarm

import android.inputmethodservice.InputMethodService
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view.EmoticonCustomView
import com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view.interfaces.EmoticonCustomViewRetrofitInterface
import com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view.modles.EmoticonData
import com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view.modles.GiphyResponse
import com.professionalandroid.apps.miniproject_platfarm.english_custom_view.EnglishCustomView
import com.professionalandroid.apps.miniproject_platfarm.korean_custom_view.KoreanCustomView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class EmoticonKeyboardService: InputMethodService() {

    var keyboardView: LinearLayout? = null
    var keyboardContainer: FrameLayout? = null
    var emoticonKeyboard: EmoticonCustomView? = null
    var englishKeyboard: EnglishCustomView? = null
    var koreanKeyboard: KoreanCustomView? = null

    val itemList = mutableListOf<EmoticonData>()

    val keyboardInteractionListener = object : KeyboardInteractionListener{
        override fun modeChange(mode: Int) {
            currentInputConnection.finishComposingText()
            when(mode) {
                0 -> {
                    keyboardContainer?.removeAllViews()
                    emoticonKeyboard?.inputConnection = currentInputConnection
                    keyboardContainer?.addView(emoticonKeyboard?.getLayout())
                }
                1 -> {
                    keyboardContainer?.removeAllViews()
                    englishKeyboard?.inputConnection = currentInputConnection
                    keyboardContainer?.addView(englishKeyboard?.getLayout())
                }
                2 -> {
                    keyboardContainer?.removeAllViews()
                    koreanKeyboard?.inputConnection = currentInputConnection
                    keyboardContainer?.addView(koreanKeyboard?.getLayout())
                }
            }
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

        emoticonKeyboard = EmoticonCustomView(applicationContext, keyboardInteractionListener).apply {
            inputConnection = currentInputConnection
        }

        englishKeyboard = EnglishCustomView(applicationContext, layoutInflater, keyboardInteractionListener).apply {
            inputConnection = currentInputConnection
            init()
        }

        koreanKeyboard = KoreanCustomView(applicationContext, layoutInflater, keyboardInteractionListener).apply {
            inputConnection = currentInputConnection
            init()
        }

        return keyboardView!!
    }

    // view가 튀어 나올 때
    override fun onStartInputView(info: EditorInfo?, restarting: Boolean) {
        super.onStartInputView(info, restarting)

        keyboardContainer?.addView(englishKeyboard?.getLayout())
    }

}