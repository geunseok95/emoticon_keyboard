package com.professionalandroid.apps.miniproject_platfarm

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApplicationClass: Application() {

    companion object{
        var BASE_URL = "http://api.giphy.com/v1/stickers/"

        var retrofit: Retrofit? = null

        fun retrofitService(): Retrofit? {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
    }


}