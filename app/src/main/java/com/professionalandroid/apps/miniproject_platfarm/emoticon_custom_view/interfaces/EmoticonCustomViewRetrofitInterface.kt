package com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view.interfaces

import com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view.modles.GiphyResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EmoticonCustomViewRetrofitInterface {

    // 인기있는 Sticker 불러오기
    @GET("trending")
    fun getTrendingStickers(
        @Query("api_key") api_key: String
    ): Call<GiphyResponse>

    // 특정 Sticker 불러오기
    @GET("search")
    fun getCertainSticker(
        @Query("api_key") api_key: String,
        @Query("q") q: String
    ): Call<GiphyResponse>
}