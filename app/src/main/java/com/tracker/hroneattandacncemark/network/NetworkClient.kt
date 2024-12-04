package com.tracker.hroneattandacncemark.network

import com.tracker.hroneattandacncemark.ext.toRequestBody
import com.tracker.hroneattandacncemark.utils.AppPreferences
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.atomic.AtomicBoolean

private const val ERROR_CODE = 401
private const val REQUEST_RETRY = 3

class NetworkClient(private val preferences: AppPreferences) {
    private val updateTokenHolder = AtomicBoolean(false)
    private var retruCount = 0
    private val interceptor = OkHttpClient.Builder().addInterceptor(object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            return chain.request().newBuilder().run {
                this.header("Authorization", "Bearer ${preferences.data?.accessToken}")
                val response = chain.proceed(this@run.build())
                synchronized(this) {
                    if (response.code() == ERROR_CODE && retruCount < REQUEST_RETRY && updateTokenHolder.get()
                            .not()
                    ) {
                        updateTokenApi(chain)
                        return intercept(chain)
                    }
                    if (response.code() == ERROR_CODE && updateTokenHolder.get().not()) {
                        Thread.sleep(1000)
                        return intercept(chain)
                    }
                }
                retruCount = 0
                response
            }

        }
    }).build()

    private fun updateTokenApi(chain: Interceptor.Chain) {
        updateTokenHolder.set(true)
        chain.proceed(chain.newTokenRequest(preferences)).let { result ->
            if (result.isSuccessful) {
                updateTokenHolder.set(false)
                JSONObject(result.body().toString()).apply {
                    preferences.data = preferences.data?.copy(
                        accessToken = this.getString("access_token"),
                        refreshToken = this.getString("access_token")
                    )
                }
                // updateToken
            } else {
                updateTokenHolder.set(false)
                // Show Some Error
            }
        }
        retruCount += 1
    }

    private fun Interceptor.Chain.newTokenRequest(preferences: AppPreferences) =
        request().newBuilder()
            .url("https://gateway.hrone.cloud" + "/oauth2/token").post(
                JSONObject().apply {
                    this.put("username", preferences.data?.loginData?.officialEmail.orEmpty())
                    this.put("password", preferences.data?.password)
                    this.put("grant_type", "password")
                    this.put("companyDomainCode", "sunstone")
                    this.put("isUpdate", 1)
                    this.put("validSource", "y")
                    this.put("deviceName", "Chrome-mac-os-x-15")
                    this.put("loginType", 1)
                    this.put("refresh_token", preferences.data?.refreshToken)
                }.toRequestBody()
            ).build()


    fun getNetworkApis(): Api =
        Retrofit.Builder().baseUrl("https://api.hrone.cloud/").client(interceptor)
            .addConverterFactory(MoshiConverterFactory.create()).build().create(Api::class.java)

}