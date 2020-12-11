package com.professionalandroid.apps.miniproject_platfarm.emoji_custom_view

import android.view.*
import android.widget.TextView
import com.professionalandroid.apps.miniproject_platfarm.R
import android.content.Context
import android.os.*
import android.view.inputmethod.InputConnection
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.core.view.setPadding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.professionalandroid.apps.miniproject_platfarm.ApplicationClass
import com.professionalandroid.apps.miniproject_platfarm.ApplicationClass.Companion.ConvertDPtoPX
import com.professionalandroid.apps.miniproject_platfarm.KeyboardInteractionListener

class EmojiCustomView constructor(var context:Context, var layoutInflater: LayoutInflater, var keyboardInteractionListener: KeyboardInteractionListener){
    lateinit var emojiLayout: LinearLayout
    var inputConnection:InputConnection? = null
        set(inputConnection){
            field = inputConnection
        }

    lateinit var vibrator: Vibrator

    lateinit var emojiRecyclerViewAdapter: EmojiRecyclerViewAdapter
    val fourthLineText = listOf<String>("한/영",getEmojiByUnicode(0x1F600), getEmojiByUnicode(0x1F466), getEmojiByUnicode(0x1F91A), getEmojiByUnicode(0x1F423),getEmojiByUnicode(0x1F331), getEmojiByUnicode(0x1F682),"DEL")
    var vibrate = 100
    var sound = 32

    fun init() {
        emojiLayout = layoutInflater.inflate(R.layout.layout_emoji_keyboard, null) as LinearLayout
        vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        val fourthLine = emojiLayout.findViewById<LinearLayout>(
                R.id.fourth_line
        )
        val children = fourthLine.children.toList()
        for(item in children.indices){
            val actionButton = children[item].findViewById<Button>(R.id.key_button)
            val specialKey = children[item].findViewById<ImageView>(R.id.special_key)
            if(fourthLineText[item] == "DEL"){
                val myOnClickListener = getDeleteAction()
                specialKey.apply {
                    setImageResource(R.drawable.del)
                    setOnClickListener(myOnClickListener)
                    specialKey.visibility = View.VISIBLE
                }
                actionButton.visibility = View.GONE
            }
            else{
                actionButton.text = fourthLineText[item]
                actionButton.setOnClickListener {
                    when((it as Button).text){
                        "한/영" -> {
                            keyboardInteractionListener.modeChange(1)
                        }
                        getEmojiByUnicode(0x1F600) -> {
                            setLayoutComponents(0x1F600, 79)
                        }
                        getEmojiByUnicode(0x1F466) -> {
                            setLayoutComponents(0x1F466, 88)
                        }
                        getEmojiByUnicode(0x1F91A) -> {
                            setLayoutComponents(0x1F91A, 88)
                        }
                        getEmojiByUnicode(0x1F423) -> {
                            setLayoutComponents(0x1F423, 35)
                        }
                        getEmojiByUnicode(0x1F331) -> {
                            setLayoutComponents(0x1F331, 88)
                        }
                        getEmojiByUnicode(0x1F682) -> {
                            setLayoutComponents(0x1F682, 64)
                        }
                    }
                }
            }
        }
        setLayoutComponents(0x1F600, 79)
    }

    fun getLayout():LinearLayout{
        return emojiLayout
    }

    private fun playVibrate(){
        if(vibrate > 0){
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
                vibrator.vibrate(VibrationEffect.createOneShot(70, vibrate))
            }
            else{
                vibrator.vibrate(70)
            }
        }
    }

    private fun setLayoutComponents(unicode: Int, count:Int) {
        val recyclerView = emojiLayout.findViewById<RecyclerView>(R.id.emoji_recyclerview)
        val emojiList = ArrayList<String>()
        val config = context.resources.configuration
        val sharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE)

//            unicode = 0x1F600
//            val unicode = 0x1F48B
        for(i in 0..count){
            emojiList.add(getEmojiByUnicode(unicode + i))
//                emojiList.add(i.toString())
        }

        emojiRecyclerViewAdapter = EmojiRecyclerViewAdapter(context, emojiList, inputConnection!!)
        recyclerView.adapter = emojiRecyclerViewAdapter
        val gm = GridLayoutManager(context,8)
        gm.isItemPrefetchEnabled = true
        recyclerView.layoutManager = gm
        recyclerView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ConvertDPtoPX(context, 50) * 5)
    }

    fun getEmojiByUnicode(unicode: Int): String {
        return String(Character.toChars(unicode))
    }

    fun getDeleteAction():View.OnClickListener{
        return View.OnClickListener{
            playVibrate()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                inputConnection?.deleteSurroundingTextInCodePoints(1, 0)
            }else{
                inputConnection?.deleteSurroundingText(1,0)
            }
        }
    }
}


class EmojiRecyclerViewAdapter(val context:Context, val emojiList:ArrayList<String>, val inputConnection: InputConnection) :RecyclerView.Adapter<EmojiRecyclerViewAdapter.Holder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_emoji_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return emojiList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(emojiList[position], context)
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val textView = itemView?.findViewById<TextView>(R.id.emoji_text)

        fun bind(emoji: String, context: Context) {
            textView?.text = emoji
            textView?.setOnClickListener {
                inputConnection.commitText((it as TextView).text.toString(), 1)
            }
        }
    }
}