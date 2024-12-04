package com.tracker.hroneattandacncemark.utils

import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.tracker.hroneattandacncemark.entity.GetTokenModel

object AppPreferences {
    private var sharedPreferences:SharedPreferences?=null
    private var moshi:Moshi?=null
    private const val AUTH_TOKEN = "auth_token"

    fun initalize(sharedPreferences : SharedPreferences,moshi: Moshi):AppPreferences{
        this.sharedPreferences = sharedPreferences
        this.moshi = moshi
        return this@AppPreferences
    }

    var data: GetTokenModel?
        get() {
            return try {
                 moshi?.adapter(GetTokenModel::class.java)?.fromJson(sharedPreferences?.getString(AUTH_TOKEN,"{}")?:"{}")
            }catch (e:Exception){
               throw e
            }
        }
        set(value) {
            try{
                sharedPreferences?.edit()?.putString(AUTH_TOKEN, moshi?.adapter(GetTokenModel::class.java)?.toJson(value))?.apply()
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
}