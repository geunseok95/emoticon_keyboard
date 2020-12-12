package com.professionalandroid.apps.miniproject_platfarm.search_custom_view.interfaces

import com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view.modles.GiphyResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchCustomViewRetrofitInterface {
    // 특정 Sticker 불러오기
    @GET("search")
    fun getSearchSticker(
        @Query("api_key") api_key: String,
        @Query("q") q: String,
        @Query("limit") limit: Int
    ): Call<GiphyResponse>
}