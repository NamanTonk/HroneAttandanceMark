package com.tracker.hroneattandacncemark.ext

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.tracker.hroneattandacncemark.utils.AppPreferences
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

fun Context.getSharedPreferences() : SharedPreferences = this.getSharedPreferences(packageName,Context.MODE_PRIVATE)

fun Context.appPreferences() :AppPreferences = AppPreferences.initalize(this.getSharedPreferences(), moshi = Moshi.Builder().build())


fun JSONObject.toRequestBody(): RequestBody {
    return RequestBody.create(MediaType.parse("application/json; charset=utf-8"),this.toString())
}