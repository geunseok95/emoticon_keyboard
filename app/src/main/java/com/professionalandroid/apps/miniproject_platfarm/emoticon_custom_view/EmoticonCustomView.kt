package com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputConnection
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.professionalandroid.apps.miniproject_platfarm.ApplicationClass
import com.professionalandroid.apps.miniproject_platfarm.R
import com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view.interfaces.EmoticonCustomViewRetrofitInterface
import com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view.modles.EmoticonData
import com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view.modles.GiphyResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class EmoticonCustomView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attributeSet, defStyleAttr), EmoticonObjectRecyclerViewAdapter.ItemSelected {

    var emoticonCustomView: LinearLayout? = null
    var inputConnection: InputConnection? = null

    private var itemList = mutableListOf<EmoticonData>()

    var mEmoticonTabLayout: TabLayout? = null
    var mEmoticonTabLayoutContainer: LinearLayout? = null
    var mEmoticonSubContainer: LinearLayout? = null
    var mEmoticonViewPager2: ViewPager2? = null
    var mEmoticonSetting: ImageView? = null
    var mEmoticonShop: ImageView? = null
    var mEmotionCustomViewPagerAdapter: EmoticonCustomViewPagerAdapter? = null

    init {
        emoticonCustomView = inflate(context, R.layout.layout_emoticon_custom_view, this) as LinearLayout
        mEmoticonTabLayoutContainer = findViewById(R.id.emoticon_tab_layout_container)
        mEmoticonSubContainer = findViewById(R.id.emoticon_sub_container)
        mEmoticonSetting = findViewById(R.id.setting)
        mEmoticonShop = findViewById(R.id.shop)

        val mEmoticonCustomViewRetrofitInterface = ApplicationClass.retrofitService()!!.create(
            EmoticonCustomViewRetrofitInterface::class.java)

        mEmoticonCustomViewRetrofitInterface.getTrendingStickers("g5jEYnDfhdwpO7SL3CfBBNNzkRN7HKIi").enqueue(object :
            Callback<GiphyResponse> {
            override fun onFailure(call: Call<GiphyResponse>, t: Throwable) {
                Log.d("test", "sticker 불러오기 실패")
            }

            override fun onResponse(call: Call<GiphyResponse>, response: Response<GiphyResponse>) {
                val body = response.body()
                Log.d("test",body.toString())
                if (body != null) {
                    val temp = EmoticonData(mutableListOf(body.data[0].images.original.url, body.data[1].images.original.url), mutableListOf())
                    for (i in body.data){
                        temp.emoticon.add(i.images.downsized_medium.url)
                    }
                    Log.d("test", temp.toString())
                    itemList.add(temp)
                    setData()
                }
            }
        })

    }

    // setData
    fun setData(){

        mEmoticonTabLayout = emoticonCustomView!!.findViewById<TabLayout>(R.id.emoticon_tab_layout)

        val config = context.resources.configuration
        val height = 1200

        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE){ // 가로화면
            mEmotionCustomViewPagerAdapter = EmoticonCustomViewPagerAdapter(context, itemList, this, 6)  // 가로에는 이모티콘 6개씩
            mEmoticonViewPager2 = emoticonCustomView!!.findViewById<ViewPager2>(R.id.emoticon_view_pager).apply {
                adapter = mEmotionCustomViewPagerAdapter
            }
            mEmoticonTabLayoutContainer?.layoutParams = LayoutParams(0, height / 8, 11f)
            mEmoticonSubContainer?.layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f).apply {
                gravity = Gravity.CENTER
            }
            mEmoticonTabLayout?.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, height / 8)
            mEmoticonViewPager2?.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, height / 2)
            mEmoticonSetting?.layoutParams = LayoutParams(height / 15, height / 15)
            mEmoticonShop?.layoutParams = LayoutParams(height / 15, height / 15)
        }
        else{   // 세로화면
            mEmotionCustomViewPagerAdapter = EmoticonCustomViewPagerAdapter(context, itemList, this, 3)  // 세로에는 이모티콘 3개씩
            mEmoticonViewPager2 = emoticonCustomView!!.findViewById<ViewPager2>(R.id.emoticon_view_pager).apply {
                adapter = mEmotionCustomViewPagerAdapter
            }
            mEmoticonTabLayoutContainer?.layoutParams = LayoutParams(0, height / 8, 10f)
            mEmoticonSubContainer?.layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT, 2f).apply {
                    gravity = Gravity.CENTER
            }
            mEmoticonTabLayout?.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, height / 8)
            mEmoticonViewPager2?.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, height)
            mEmoticonSetting?.layoutParams = LayoutParams(height / 15, height / 15)
            mEmoticonShop?.layoutParams = LayoutParams(height / 15, height / 15)
        }

        // TabLayout과 ViewPager 연결
        TabLayoutMediator(mEmoticonTabLayout!!, mEmoticonViewPager2!!) { tab, position ->
            if(position == 0) {

                val imageView =  ImageView(context)
                Glide.with(context)
                    .load(itemList[0].tabImage[0])
                    .into(imageView)
                tab.customView = imageView
            }
            else {
                val imageView =  ImageView(context)
                Glide.with(context)
                    .load(itemList[0].tabImage[1])
                    .into(imageView)
                tab.customView = imageView
            }
        }.attach()

        // Tab Listener
        mEmoticonTabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val imageView =  ImageView(context)
                Glide.with(context)
                    .load(itemList[0].tabImage[0])
                    .into(imageView)
                tab?.customView = imageView
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                // changing tab icon
                val imageView =  ImageView(context)
                Glide.with(context)
                    .load(itemList[0].tabImage[1])
                    .into(imageView)
                tab?.customView = imageView
            }
        })
    }

    // return view
    fun getLayout(): LinearLayout{
        return emoticonCustomView!!
    }

    // 이모티콘 click listener
    override fun itemSelected(parent_position: Int, position: Int) {

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
        val mAdapter = EmoticonObjectRecyclerViewAdapter(context, itemList[position].emoticon, position, listener)

        holder.mRecyclerView?.apply {
            layoutManager = GridLayoutManager(context, spanCount)
            adapter = mAdapter
        }
    }
}


// RecyclerView Adapter in ViewPager
class EmoticonObjectRecyclerViewAdapter(val context: Context, val imageList: MutableList<String>, val parent_position: Int, val listener: ItemSelected): RecyclerView.Adapter<EmoticonObjectRecyclerViewAdapter.ViewHolder>() {

    // 이모티콘 click interface
    interface ItemSelected{
        fun itemSelected(parent_position: Int, position: Int)
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var emoticonImage: ImageView? = null
        init {
            emoticonImage = view.findViewById(R.id.emoticon_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflateView = LayoutInflater.from(parent.context).inflate(R.layout.layout_emoticon_recyclerview_item, parent,false)
        return ViewHolder(inflateView)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(imageList[position])
            .into(holder.emoticonImage!!)

        holder.emoticonImage?.setOnClickListener {
            listener.itemSelected(parent_position, position)
        }

    }
}
