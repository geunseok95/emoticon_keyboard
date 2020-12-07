package com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view.interfaces

import com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view.modles.GiphyResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EmoticonCustomViewRetrofitInterface {
    @GET("trending")
    fun getTrendingStickers(
        @Query("api_key") api_key: String
    ): Call<GiphyResponse>
}