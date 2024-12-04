package com.tracker.hroneattandacncemark.entity


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GetTokenModel(
    @Json(name = "access_token")
    val accessToken: String?,
    @Json(name = "client_id")
    val clientId: String?,
    @Json(name = "disableRememberMe")
    val disableRememberMe: Boolean?,
    @Json(name = ".expires")
    val expires: String?,
    @Json(name = "expires_in")
    val expiresIn: Int?,
    @Json(name = "isMfaActive")
    val isMfaActive: String?,
    @Json(name = ".issued")
    val issued: String?,
    @Json(name = "mfa_auth_way")
    val mfaAuthWay: List<Any?>?,
    @Json(name = "refresh_token")
    val refreshToken: String?,
    @Json(name = "token_type")
    val tokenType: String?,
    @Json(name = "userName")
    val userName: String?,
    val loginData:LoginEntity?=null,
    val password:String?=null

)