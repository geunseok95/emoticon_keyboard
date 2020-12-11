package com.professionalandroid.apps.miniproject_platfarm

import android.app.Application
import android.content.SharedPreferences
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApplicationClass: Application() {

    override fun onCreate() {
        super.onCreate()
        if (sSharedPreferences == null) {
            sSharedPreferences = applicationContext.getSharedPreferences(TAG, MODE_PRIVATE)
        }
    }

    companion object{
        val BASE_URL = "https://api.giphy.com/v1/stickers/"

        val api_key = "g5jEYnDfhdwpO7SL3CfBBNNzkRN7HKIi"

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

        var TAG = "KEYBOARD_APP"

        var sSharedPreferences: SharedPreferences? = null

        var MODE = "MODE-NUMBER"
    }


}