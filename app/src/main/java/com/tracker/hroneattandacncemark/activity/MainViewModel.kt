package com.tracker.hroneattandacncemark.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tracker.hroneattandacncemark.network.Api
import com.tracker.hroneattandacncemark.utils.AppPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val preference: AppPreferences,
    private val networkClient: Api
) : ViewModel() {

    private val _loginState = MutableStateFlow<Pair<String?, Boolean>>(null to false)
    val loginState = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            kotlin.runCatching {
                networkClient.getToken(email, password)
            }.onSuccess { success ->
                if (success.accessToken?.isNotEmpty() == true) {
                    preference.data = success
                    loginUser(password)
                }else  updateState("Login Failed." to false)
            }.onFailure {
                updateState("Token Fetch Failed." to false)
            }
        }
    }

     fun loginUser(password: String){
        viewModelScope.launch {
            kotlin.runCatching {
                networkClient.loginUser()
            }.onSuccess {success->
                preference.data =  preference.data?.copy(loginData = success, password = password)
                updateState("Login SuccessFully." to true)
            }.onFailure {
                it.printStackTrace()
                updateState("Login Failed." to false)
            }
        }

    }

    private fun updateState(data: Pair<String?, Boolean>) {
        viewModelScope.launch {
            _loginState.update { data }
        }
    }

}