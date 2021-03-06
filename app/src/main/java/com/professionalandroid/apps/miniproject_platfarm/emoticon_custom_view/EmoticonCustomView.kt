package com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view

import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Environment
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputConnection
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.professionalandroid.apps.miniproject_platfarm.ApplicationClass.Companion.ConvertDPtoPX
import com.professionalandroid.apps.miniproject_platfarm.KeyboardInteractionListener
import com.professionalandroid.apps.miniproject_platfarm.R
import com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view.interfaces.EmoticonCustomViewView
import com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view.modles.EmoticonData
import com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view.modles.GiphyResponse
import com.professionalandroid.apps.miniproject_platfarm.search_custom_view.SearchActivity
import java.io.*


open class EmoticonCustomView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attributeSet, defStyleAttr), EmoticonObjectRecyclerViewAdapter.ItemSelected , EmoticonCustomViewView{

    val templist = mutableListOf("christmas", "love", "sad", "OMG", "coffee", "thanks")
    var size = templist.size

    var mEmoticonCustomViewPresenter: EmoticonCustomViewPresenter
    lateinit var keyboardInteractionListener: KeyboardInteractionListener

    constructor(context: Context, keyboardInteractionListener: KeyboardInteractionListener): this(context){
        this.keyboardInteractionListener = keyboardInteractionListener
    }

    var emoticonCustomView: LinearLayout? = null
    var inputConnection: InputConnection? = null

    private var itemList = mutableListOf<EmoticonData>()

    var mEmoticonBar: LinearLayout? = null
    var mEmoticonTabLayout: TabLayout? = null
    var mEmoticonTabLayoutContainer: LinearLayout? = null
    var mEmoticonSubContainer: LinearLayout? = null
    var mEmoticonViewPager2: ViewPager2? = null
    var mEmoticonSetting: ImageView? = null
    var mEmoticonSearch: ImageView? = null
    var mKeyboardChange: ImageView? = null
    var mEmotionCustomViewPagerAdapter: EmoticonCustomViewPagerAdapter? = null

    init {
        mEmoticonCustomViewPresenter = EmoticonCustomViewPresenter(this)
        emoticonCustomView = inflate(context, R.layout.layout_emoticon_custom_view, this) as LinearLayout

        mEmoticonBar = findViewById(R.id.emoticon_bar)
        mEmoticonTabLayoutContainer = findViewById(R.id.emoticon_tab_layout_container)
        mEmoticonSubContainer = findViewById(R.id.emoticon_sub_container)
        mEmoticonSetting = findViewById(R.id.setting)
        mEmoticonSearch = findViewById(R.id.search)
        mKeyboardChange = findViewById(R.id.changeKeyBoard)

        // getSticker
        if (itemList.size == 0) {
            // Trending Sticker
            mEmoticonCustomViewPresenter.getTrendingStickerFromGiphy(0, 15)

            // Certain Sticker
            for (i in templist.indices) {
                mEmoticonCustomViewPresenter.getCertainStickerFromGiphy(templist[i], i + 1, 15)
            }
        }
        else{
            setData()
        }
    }

    // setData
    fun setData(){
        mEmoticonTabLayout = emoticonCustomView!!.findViewById<TabLayout>(R.id.emoticon_tab_layout)

        val config = context.resources.configuration

        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE){ // ????????????
            mEmotionCustomViewPagerAdapter = EmoticonCustomViewPagerAdapter(context, itemList, this, 6)  // ???????????? ???????????? 6??????
            mEmoticonViewPager2 = emoticonCustomView!!.findViewById<ViewPager2>(R.id.emoticon_view_pager).apply {
                adapter = mEmotionCustomViewPagerAdapter
            }
            mEmoticonTabLayoutContainer?.layoutParams = LayoutParams(0, ConvertDPtoPX(context, 40), 1f)
            mEmoticonSubContainer?.layoutParams = LayoutParams(ConvertDPtoPX(context, 80), LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.CENTER
            }
            mEmoticonTabLayout?.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ConvertDPtoPX(context, 40))
            mEmoticonViewPager2?.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ConvertDPtoPX(context, 150))
            mEmoticonSetting?.layoutParams = LayoutParams(ConvertDPtoPX(context, 24), ConvertDPtoPX(context, 24))
            mEmoticonSearch?.layoutParams = LayoutParams(ConvertDPtoPX(context, 24), ConvertDPtoPX(context, 24))
            mKeyboardChange?.layoutParams = LayoutParams(ConvertDPtoPX(context, 24), ConvertDPtoPX(context, 24))

        }
        else{   // ????????????
            mEmotionCustomViewPagerAdapter = EmoticonCustomViewPagerAdapter(context, itemList, this, 3)  // ???????????? ???????????? 3??????
            mEmoticonViewPager2 = emoticonCustomView!!.findViewById<ViewPager2>(R.id.emoticon_view_pager).apply {
                adapter = mEmotionCustomViewPagerAdapter
            }

            mEmoticonTabLayoutContainer?.layoutParams = LayoutParams(0, ConvertDPtoPX(context, 40), 1f)
            mEmoticonSubContainer?.layoutParams = LayoutParams(ConvertDPtoPX(context, 80), LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.CENTER
            }
            mEmoticonTabLayout?.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ConvertDPtoPX(context, 40))
            mEmoticonViewPager2?.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ConvertDPtoPX(context, 210))
            mEmoticonSetting?.layoutParams = LayoutParams(ConvertDPtoPX(context, 24), ConvertDPtoPX(context, 24))
            mEmoticonSearch?.layoutParams = LayoutParams(ConvertDPtoPX(context, 24), ConvertDPtoPX(context, 24))
            mKeyboardChange?.layoutParams = LayoutParams(ConvertDPtoPX(context, 24), ConvertDPtoPX(context, 24))
        }

        mKeyboardChange?.setOnClickListener {
            keyboardInteractionListener.modeChange(2)
            Log.d("test", "change")
        }

        mEmoticonSearch?.setOnClickListener {
            val popupIntent = Intent(context, SearchActivity()::class.java)
            val pi = PendingIntent.getActivity(context, 0, popupIntent, PendingIntent.FLAG_ONE_SHOT)
            try {
                pi.send()
            } catch (e: Exception) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
            }
            keyboardInteractionListener.modeChange(1)
        }

        // TabLayout??? ViewPager ??????
        TabLayoutMediator(mEmoticonTabLayout!!, mEmoticonViewPager2!!) { tab, position ->
            if(position == 0) {
                val imageView =  ImageView(context)
                Glide.with(context)
                    .load(Integer.parseInt(itemList[0].tabImage[0]))
                    .into(imageView)
                tab.customView = imageView
            }
            else {
                val imageView =  ImageView(context)
                Glide.with(context)
                    .load(itemList[position].tabImage[1])
                    .into(imageView)
                tab.customView = imageView
            }
        }.attach()

        // Tab Listener
        mEmoticonTabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                if(tab?.position == 0) {    // Trending
                    Glide.with(context)
                        .load(Integer.parseInt(itemList[tab.position].tabImage[1]))
                        .into(tab.customView as ImageView)
                }
                else{   // Certain
                    Glide.with(context)
                        .load(itemList[tab!!.position].tabImage[1])
                        .into(tab.customView as ImageView)
                }
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab?.position == 0) {    // Trending
                    Glide.with(context)
                        .load(Integer.parseInt(itemList[tab.position].tabImage[0]))
                        .into(tab.customView as ImageView)
                }
                else{   // Certain
                    Glide.with(context)
                        .load(itemList[tab!!.position].tabImage[0])
                        .into(tab.customView as ImageView)
                }
            }
        })
    }

    // return view
    fun getLayout(): LinearLayout{
        return emoticonCustomView!!
    }

    // ???????????? click listener
    override fun itemSelected(parent_position: Int, position: Int) {    // ????????? ??????
        // download file -> copy file to temporary file -> share sheet

        Glide.with(this)
                .download(itemList[parent_position].emoticon[position])
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

    override fun addStickerToList(body: GiphyResponse, position: Int, kinds: Int) {
        if(kinds == 0) {    // Trending
            val temp = EmoticonData(
                mutableListOf(
                    (R.drawable.star_down).toString(),
                    (R.drawable.star).toString()
                ), mutableListOf()
            )
            for (i in body.data) {
                temp.emoticon.add(i.images.original.url)
            }
            itemList.add(0, temp)
        }
        else {  // Certain
            val temp = EmoticonData(
                mutableListOf(
                    body.data[0].images.the480WStill.url,
                    body.data[0].images.the480WStill.url
                ), mutableListOf()
            )
            for (i in body.data) {
                temp.emoticon.add(i.images.original.url)
            }
            itemList.add(temp)
        }
        if (itemList.size == size + 1){
            setData()
        }
    }

}

// ViewPager Adapter
class EmoticonCustomViewPagerAdapter(val context: Context, val itemList: MutableList<EmoticonData>, val listener: EmoticonObjectRecyclerViewAdapter.ItemSelected, val spanCount: Int): RecyclerView.Adapter<EmoticonCustomViewPagerAdapter.ViewHolder>(){

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var mRecyclerView: RecyclerView? = null
        init {
            mRecyclerView = view.findViewById(R.id.emoticon_recyclerView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflateView = LayoutInflater.from(parent.context).inflate(R.layout.layout_emoticon_view_pager_item, parent, false)
        return ViewHolder(inflateView)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mAdapter = EmoticonObjectRecyclerViewAdapter(context, itemList[position].emoticon, position, spanCount, listener)

        holder.mRecyclerView?.apply {
            layoutManager = GridLayoutManager(context, spanCount)
            adapter = mAdapter
        }
    }
}


// RecyclerView Adapter in ViewPager
class EmoticonObjectRecyclerViewAdapter(val context: Context, val imageList: MutableList<String>, val parent_position: Int, val spanCount: Int, val listener: ItemSelected): RecyclerView.Adapter<EmoticonObjectRecyclerViewAdapter.ViewHolder>() {

    var selectedItemPosition = -1

    // ???????????? click interface
    interface ItemSelected{
        fun itemSelected(parent_position: Int, position: Int)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var emoticonImage: ImageView? = null
        init {
            emoticonImage = view.findViewById(R.id.emoticon_image)
            emoticonImage?.setOnClickListener {
                //toggleItemSelected(adapterPosition)
                listener.itemSelected(parent_position, adapterPosition)
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
        return imageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(imageList[position])
            .into(holder.emoticonImage!!)

//        if (selectedItemPosition == position){
//            holder.emoticonImage?.setBackgroundColor(Color.GRAY)
//        }
//        else{
//            holder.emoticonImage?.setBackgroundColor(Color.TRANSPARENT)
//        }
    }

    fun toggleItemSelected(position:Int){
        val previous = selectedItemPosition
        if(position != selectedItemPosition){
            selectedItemPosition = position
        }
        notifyItemChanged(position)
        notifyItemChanged(previous)
    }
}

