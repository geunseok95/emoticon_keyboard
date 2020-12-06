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
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.professionalandroid.apps.miniproject_platfarm.R
import com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view.modles.EmoticonData

open class EmoticonCustomView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attributeSet, defStyleAttr), EmoticonObjectRecyclerViewAdapter.ItemSelected {

    var emoticonCustomView: LinearLayout? = null
    var inputConnection: InputConnection? = null

    private var itemList: List<EmoticonData>? = null

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
    }

    // setData
    fun setData(itemList: List<EmoticonData>){
        this.itemList = itemList
        mEmoticonTabLayout = emoticonCustomView!!.findViewById<TabLayout>(R.id.emoticon_tab_layout)

        val config = context.resources.configuration
        val height = 800

        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE){ // 가로화면
            mEmotionCustomViewPagerAdapter = EmoticonCustomViewPagerAdapter(itemList, this, 6)
            mEmoticonViewPager2 = emoticonCustomView!!.findViewById<ViewPager2>(R.id.emoticon_view_pager).apply {
                adapter = mEmotionCustomViewPagerAdapter
            }
            mEmoticonTabLayoutContainer?.layoutParams = LayoutParams(0, height / 5, 11f)
            mEmoticonSubContainer?.layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f).apply {
                gravity = Gravity.CENTER
            }
            mEmoticonTabLayout?.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, height / 5)
            mEmoticonViewPager2?.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, height / 2)
            mEmoticonSetting?.layoutParams = LayoutParams(height / 10, height / 10)
            mEmoticonShop?.layoutParams = LayoutParams(height / 10, height / 10)
        }
        else{   // 세로화면
            mEmotionCustomViewPagerAdapter = EmoticonCustomViewPagerAdapter(itemList, this, 3)
            mEmoticonViewPager2 = emoticonCustomView!!.findViewById<ViewPager2>(R.id.emoticon_view_pager).apply {
                adapter = mEmotionCustomViewPagerAdapter
            }
            mEmoticonTabLayoutContainer?.layoutParams = LayoutParams(0, height / 5, 10f)
            mEmoticonSubContainer?.layoutParams = LayoutParams(0, LayoutParams.WRAP_CONTENT, 2f).apply {
                    gravity = Gravity.CENTER
            }
            mEmoticonTabLayout?.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, height / 5)
            mEmoticonViewPager2?.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, height)
            mEmoticonSetting?.layoutParams = LayoutParams(height / 10, height / 10)
            mEmoticonShop?.layoutParams = LayoutParams(height / 10, height / 10)
        }

        // Attaching TabLayout and ViewPager
        TabLayoutMediator(mEmoticonTabLayout!!, mEmoticonViewPager2!!) { tab, position ->
            if(position == 0) {
                tab.setIcon(itemList[0].tabImage[0])
            }
            else {
                tab.setIcon(itemList[position].tabImage[1])
            }
        }.attach()

        // Tab Listener
        mEmoticonTabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.setIcon(itemList[tab.position].tabImage[1])
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                // changing tab icon
                tab?.setIcon(itemList[tab.position].tabImage[0])
            }
        })

    }

    // return view
    fun getLayout(): LinearLayout{
        return emoticonCustomView!!
    }

    // itemSelectedListener
    override fun itemSelected(parent_position: Int, position: Int) {

    }
}

// ViewPager Adapter
class EmoticonCustomViewPagerAdapter(val itemList: List<EmoticonData>, val listener: EmoticonObjectRecyclerViewAdapter.ItemSelected, val spanCount: Int): RecyclerView.Adapter<EmoticonCustomViewPagerAdapter.ViewHolder>(){

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
        val mAdapter = EmoticonObjectRecyclerViewAdapter(itemList[position].emoticon, position, listener)

        holder.mRecyclerView?.apply {
            layoutManager = GridLayoutManager(context, spanCount)
            adapter = mAdapter
        }
    }
}


// RecyclerView Adapter in ViewPager
class EmoticonObjectRecyclerViewAdapter(val imageList: List<Int>, val parent_position: Int, val listener: ItemSelected): RecyclerView.Adapter<EmoticonObjectRecyclerViewAdapter.ViewHolder>() {

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
        holder.emoticonImage?.apply {
            setImageResource(imageList[position])
            setOnClickListener {
                listener.itemSelected(parent_position, position)
            }
        }
    }
}
