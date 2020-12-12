package com.professionalandroid.apps.miniproject_platfarm.search_custom_view

import android.util.Log
import com.professionalandroid.apps.miniproject_platfarm.ApplicationClass
import com.professionalandroid.apps.miniproject_platfarm.ApplicationClass.Companion.api_key
import com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view.modles.GiphyResponse
import com.professionalandroid.apps.miniproject_platfarm.search_custom_view.interfaces.SearchCustomViewRetrofitInterface
import com.professionalandroid.apps.miniproject_platfarm.search_custom_view.interfaces.SearchCustomViewView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchCustomViewPresenter(val mSearchCustomViewView: SearchCustomViewView) {
    val mSearchCustomViewRetrofitInterface: SearchCustomViewRetrofitInterface =
        ApplicationClass.retrofitService()!!.create(SearchCustomViewRetrofitInterface::class.java)

    fun getSearchSticker(searchText: String){
        mSearchCustomViewRetrofitInterface.getSearchSticker(api_key, searchText, 15).enqueue(object : Callback<GiphyResponse>{
            override fun onFailure(call: Call<GiphyResponse>, t: Throwable) {
                Log.d("test", "검색 스티커 불러오기 실패")
            }

            override fun onResponse(call: Call<GiphyResponse>, response: Response<GiphyResponse>) {
                val body = response.body()
                if(body != null){
                    mSearchCustomViewView.getSearchSticker(body)
                }
            }

        })
    }
}