package com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view

import android.util.Log
import com.professionalandroid.apps.miniproject_platfarm.ApplicationClass
import com.professionalandroid.apps.miniproject_platfarm.ApplicationClass.Companion.api_key
import com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view.interfaces.EmoticonCustomViewRetrofitInterface
import com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view.interfaces.EmoticonCustomViewView
import com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view.modles.GiphyResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmoticonCustomViewPresenter(val mEmoticonCustomViewView: EmoticonCustomViewView) {
    val mEmoticonCustomViewRetrofitInterface = ApplicationClass.retrofitService()!!.create(
        EmoticonCustomViewRetrofitInterface::class.java)

    fun getTrendingStickerFromGiphy(position: Int, limit: Int) {
        mEmoticonCustomViewRetrofitInterface.getTrendingStickers(api_key, limit)
            .enqueue(object :
                Callback<GiphyResponse> {
                override fun onFailure(call: Call<GiphyResponse>, t: Throwable) {
                    Log.d("test", "Trending sticker 불러오기 실패")
                }

                override fun onResponse(
                    call: Call<GiphyResponse>,
                    response: Response<GiphyResponse>
                ) {
                    val body = response.body()
                    if (body != null) {
                        mEmoticonCustomViewView.addStickerToList(body, position, 0)
                    }
                    response.errorBody()?.string()
                }
            })
    }

    fun getCertainStickerFromGiphy(q: String, position: Int, limit: Int){
        mEmoticonCustomViewRetrofitInterface.getCertainSticker(api_key, q, limit).enqueue(object : Callback<GiphyResponse>{
            override fun onFailure(call: Call<GiphyResponse>, t: Throwable) {
                Log.d("test", "Certain sticker 불러오기 실패")
            }

            override fun onResponse(call: Call<GiphyResponse>, response: Response<GiphyResponse>) {
                val body = response.body()
                //Log.d("test", body.toString())
                if (body != null) {
                    mEmoticonCustomViewView.addStickerToList(body, position, 1)
                }
                response.errorBody()?.string()
            }
        })
    }

}