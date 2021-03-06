package com.professionalandroid.apps.miniproject_platfarm.search_custom_view

import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Environment
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputConnection
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.view.setMargins
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.professionalandroid.apps.miniproject_platfarm.ApplicationClass.Companion.ConvertDPtoPX
import com.professionalandroid.apps.miniproject_platfarm.KeyboardInteractionListener
import com.professionalandroid.apps.miniproject_platfarm.R
import com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view.modles.GiphyResponse
import com.professionalandroid.apps.miniproject_platfarm.search_custom_view.interfaces.SearchCustomViewView
import java.io.*

class SearchCustomView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attributeSet, defStyleAttr), SearchCustomViewView, SearchCustomViewRecyclerViewAdapter.ItemSelected{

    lateinit var keyboardInteractionListener: KeyboardInteractionListener
    var mSearchCustomViewRecyclerViewAdapter: SearchCustomViewRecyclerViewAdapter
    var mSearchCustomViewPresenter: SearchCustomViewPresenter
    val stickerList = mutableListOf<String>()

    var searchCustomView: LinearLayout? = null
    var mSearchEditText: EditText? = null
    var mSearchRecyclerView: RecyclerView? = null
    var inputConnection: InputConnection? = null
    var mSearchChangeKeyboard: ImageView? = null

    constructor(context: Context, keyboardInteractionListener: KeyboardInteractionListener, searchText: String): this(context){
        this.keyboardInteractionListener = keyboardInteractionListener
        mSearchEditText?.setText(searchText)
        mSearchCustomViewPresenter.getSearchSticker(searchText)
    }

    init {
        stickerList.clear()
        searchCustomView = View.inflate(context, R.layout.layout_search_custom_view, this) as LinearLayout
        mSearchEditText = findViewById(R.id.search_text)
        mSearchChangeKeyboard = findViewById(R.id.search_changeKeyBoard)
        mSearchCustomViewPresenter = SearchCustomViewPresenter(this)

        val config = context.resources.configuration

        if(config.orientation == Configuration.ORIENTATION_LANDSCAPE){
            mSearchCustomViewRecyclerViewAdapter = SearchCustomViewRecyclerViewAdapter(context, stickerList, 6, this)
            mSearchRecyclerView = findViewById<RecyclerView>(R.id.search_recyclerview).apply {
                layoutManager = GridLayoutManager(context, 3)
                adapter = mSearchCustomViewRecyclerViewAdapter
            }

            mSearchEditText?.layoutParams = LayoutParams(0, ConvertDPtoPX(context, 35), 1f).apply {
                setMargins(ConvertDPtoPX(context, 5))
            }

            mSearchChangeKeyboard?.layoutParams = LayoutParams(ConvertDPtoPX(context, 30), ConvertDPtoPX(context, 30)).apply {
                setMargins(ConvertDPtoPX(context, 5))
            }
            mSearchRecyclerView?.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ConvertDPtoPX(context, 150))
        }
        else{
            mSearchCustomViewRecyclerViewAdapter = SearchCustomViewRecyclerViewAdapter(context, stickerList, 3, this)
            mSearchRecyclerView = findViewById<RecyclerView>(R.id.search_recyclerview).apply {
                layoutManager = GridLayoutManager(context, 3)
                adapter = mSearchCustomViewRecyclerViewAdapter
            }

            mSearchEditText?.layoutParams = LayoutParams(0, ConvertDPtoPX(context, 35), 1f).apply {
                    setMargins(ConvertDPtoPX(context, 5))
                }

            mSearchChangeKeyboard?.layoutParams = LayoutParams(ConvertDPtoPX(context, 30), ConvertDPtoPX(context, 30)).apply {
                setMargins(ConvertDPtoPX(context, 5))
            }
            mSearchRecyclerView?.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ConvertDPtoPX(context, 215))

        }

        mSearchChangeKeyboard?.setOnClickListener {
            keyboardInteractionListener.modeChange(2)
        }

        mSearchEditText?.setOnClickListener {
            val popupIntent = Intent(context, SearchActivity()::class.java)
            val pi = PendingIntent.getActivity(context, 0, popupIntent, PendingIntent.FLAG_ONE_SHOT)
            try {
                pi.send()
            } catch (e: Exception) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
            keyboardInteractionListener.modeChange(1)
        }

    }

    fun getLayout(): LinearLayout{
        return searchCustomView!!
    }

    override fun getSearchSticker(body: GiphyResponse) {
        for(i in body.data){
            stickerList.add(i.images.original.url)
        }
        mSearchCustomViewRecyclerViewAdapter.notifyDataSetChanged()
    }

    override fun itemSelected(position: Int) {  // ????????? ??????
        // download file -> copy file to temporary file -> share sheet

        Glide.with(this)
                .download(stickerList[position])
                .listener(object : RequestListener<File?> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<File?>?, isFirstResource: Boolean): Boolean {
                        Log.d("test","test")
                        return false
                    }
                    override fun onResourceReady(resource: File?, model: Any?, target: Target<File?>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                        val storageDir : File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                        val contentResolver: ContentResolver = context.contentResolver!!
                        val file = File.createTempFile( "${System.currentTimeMillis()}","gif",storageDir)

                        // copy file to temporary file
                        try {
                            val inputStream: InputStream = contentResolver.openInputStream(Uri.fromFile(resource))!!
                            val outputStream: OutputStream = FileOutputStream(file)
                            val buf = ByteArray(1024)
                            var len: Int
                            while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
                            outputStream.close()
                            inputStream.close()
                        }
                        catch (ignore: IOException){
                        }

                        // Sharesheet
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context, "com.professionalandroid.apps.miniproject_platfarm.fileprovider", file))
                            type = "image/gif"
                            setPackage("com.kakao.talk")
                        }

                        val pi = PendingIntent.getActivity(context, 0, shareIntent, PendingIntent.FLAG_ONE_SHOT)

                        try {
                            pi.send()
                        } catch (e: Exception) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
                        }
                        return true
                    }
                }).submit()
     }
}

class SearchCustomViewRecyclerViewAdapter(val context: Context, val stickerList: MutableList<String>, val spanCount: Int, val listener: ItemSelected): RecyclerView.Adapter<SearchCustomViewRecyclerViewAdapter.ViewHolder>(){

    var selectedItemPosition = -1

    interface ItemSelected{
        fun itemSelected(position: Int)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var emoticonImage: ImageView? = null
        init {
            emoticonImage = view.findViewById(R.id.emoticon_image)
            emoticonImage?.setOnClickListener {
              //  toggleItemSelected(adapterPosition)
                listener.itemSelected(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflateView = LayoutInflater.from(parent.context).inflate(R.layout.layout_emoticon_recyclerview_item, parent,false)
        val gl: GridLayoutManager.LayoutParams = inflateView.layoutParams as GridLayoutManager.LayoutParams
        gl.apply {
            width = parent.measuredWidth / spanCount
            height = parent.measuredWidth / spanCount
        }
        inflateView.layoutParams = gl
        return ViewHolder(inflateView)
    }

    override fun getItemCount(): Int {
        return stickerList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(stickerList[position])
            .into(holder.emoticonImage!!)

//        if (selectedItemPosition == position){
//            holder.emoticonImage?.setBackgroundColor(Color.GRAY)
//        }
//        else{
//            holder.emoticonImage?.setBackgroundColor(Color.TRANSPARENT)
//        }
    }

    // ????????? ?????????
    fun toggleItemSelected(position:Int){
        val previous = selectedItemPosition
        if(position != selectedItemPosition){
            selectedItemPosition = position
        }
        notifyItemChanged(position)
        notifyItemChanged(previous)
    }
}