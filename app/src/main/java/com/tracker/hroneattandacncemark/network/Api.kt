package com.tracker.hroneattandacncemark.network

import com.tracker.hroneattandacncemark.entity.GetTokenModel
import com.tracker.hroneattandacncemark.entity.LoginEntity
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Api {
    @FormUrlEncoded
    @POST("https://gateway.hrone.cloud/oauth2/token")
    suspend fun getToken(
        @Field("username") name: String,
        @Field("password") password: String,
        @Field("grant_type") grantType: String = "password",
        @Field("companyDomainCode") companyDomainCode: String = "sunstone",
        @Field("isUpdate") isUpdate: Int = 1,
        @Field("loginType") loginType: Int = 1,
        @Field("validSource") validSource: String = "Y",
        @Field("deviceName") deviceName: String = "Chrome-mac-os-x-15"
    ): GetTokenModel

    @GET("https://api.hrone.cloud/api/LogOnUser/LogOnUserDetail")
    suspend fun loginUser(): LoginEntity

    @POST("api/timeoffice/mobile/checkin/Attendance/Request")
    suspend fun markAttendance(
       @Body body :  RequestBody
    ): Boolean
}
