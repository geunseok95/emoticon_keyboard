package com.professionalandroid.apps.miniproject_platfarm

import android.app.Service
import android.content.Intent
import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.professionalandroid.apps.miniproject_platfarm.ApplicationClass.Companion.MODE
import com.professionalandroid.apps.miniproject_platfarm.ApplicationClass.Companion.sSharedPreferences
import com.professionalandroid.apps.miniproject_platfarm.emoji_custom_view.EmojiCustomView
import com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view.EmoticonCustomView
import com.professionalandroid.apps.miniproject_platfarm.english_custom_view.EnglishCustomView
import com.professionalandroid.apps.miniproject_platfarm.korean_custom_view.KoreanCustomView
import com.professionalandroid.apps.miniproject_platfarm.search_custom_view.SearchCustomView
import com.professionalandroid.apps.miniproject_platfarm.special_characters_custom_view.SpecialCharactersCustomView

class EmoticonKeyboardService: InputMethodService() {

    var keyboardView: LinearLayout? = null
    var keyboardContainer: FrameLayout? = null
    var emoticonKeyboard: EmoticonCustomView? = null
    var englishKeyboard: EnglishCustomView? = null
    var koreanKeyboard: KoreanCustomView? = null
    var specialCharactersCustomView: SpecialCharactersCustomView? = null
    var emojiCustomView: EmojiCustomView? = null
    var searchCustomView: SearchCustomView? = null
    var searchText = ""

    val keyboardInteractionListener = object : KeyboardInteractionListener{
        override fun modeChange(mode: Int) {
            currentInputConnection.finishComposingText()
            when(mode) {
                0 -> {
                    keyboardContainer?.removeAllViews()
                    emoticonKeyboard?.inputConnection = currentInputConnection
                    keyboardContainer?.addView(emoticonKeyboard?.getLayout())
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
                3 -> {
                    keyboardContainer?.removeAllViews()
                    specialCharactersCustomView?.inputConnection = currentInputConnection
                    keyboardContainer?.addView(specialCharactersCustomView?.getLayout())
                }
                4 -> {
                    keyboardContainer?.removeAllViews()
                    emojiCustomView?.inputConnection = currentInputConnection
                    keyboardContainer?.addView(emojiCustomView?.getLayout())
                }
                5 ->{
                    keyboardContainer?.removeAllViews()
                    searchCustomView?.inputConnection = currentInputConnection
                    keyboardContainer?.addView(searchCustomView?.getLayout())
                }
            }
            // 키보드 번호를 저장
            sSharedPreferences!!.edit().apply{
                putInt(MODE, mode)
                apply()
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

        specialCharactersCustomView = SpecialCharactersCustomView(applicationContext, layoutInflater, keyboardInteractionListener).apply {
            inputConnection = currentInputConnection
            init()
        }

        emojiCustomView = EmojiCustomView(applicationContext, layoutInflater, keyboardInteractionListener).apply {
            inputConnection = currentInputConnection
            init()
        }

        searchCustomView = SearchCustomView(applicationContext, keyboardInteractionListener, searchText).apply {
            inputConnection = currentInputConnection
        }
        return keyboardView!!
    }

    override fun updateInputViewShown() {
        super.updateInputViewShown()
        if (currentInputConnection != null) {
            currentInputConnection.finishComposingText()
            keyboardInteractionListener.modeChange(sSharedPreferences?.getInt(MODE, 2)!!)
        }
    }

    // Dialog 의 intent 받기
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        searchText = if(intent?.getStringExtra("search") != null) { intent.getStringExtra("search")!!}  else {"OMG"}
        if(searchText != ""){
            searchCustomView = SearchCustomView(applicationContext, keyboardInteractionListener, searchText).apply {
                inputConnection = currentInputConnection
            }
            keyboardInteractionListener.modeChange(5)
        }
        else{
            keyboardInteractionListener.modeChange(sSharedPreferences?.getInt(MODE, 2)!!)
        }
        return Service.START_STICKY
    }
}