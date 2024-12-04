package com.tracker.hroneattandacncemark.activity

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.tracker.hroneattandacncemark.activity.utils.ViewModelFactory
import com.tracker.hroneattandacncemark.ext.appPreferences
import com.tracker.hroneattandacncemark.schdulejob.AlarmScheduler
import com.tracker.hroneattandacncemark.network.NetworkClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
private const val HOURS = 12
private  const val MIN = 0
class MainActivity : ComponentActivity(){
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted: Boolean ->
        if(granted){
            finish()
        }
    }
 private val viewModel : MainViewModel by viewModels<MainViewModel> {
        ViewModelFactory(
            appPreferences = this.appPreferences(),
            networkClient = NetworkClient(this.appPreferences()).getNetworkApis())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        if(this.appPreferences().data?.accessToken == null)
            viewModel.login(email = "naman.tonk@sunstone.in",password="vdjR6px3\$#g9mUu")
        else if(this.appPreferences().data?.loginData == null )
            viewModel.loginUser("vdjR6px3\$#g9mUu")
        lifecycleScope.launch {
                viewModel.loginState.onEach { value->
                    if(value.second)
                        AlarmScheduler(this@MainActivity, HOURS,MIN).scheduleWeekdayAlarm()
                    if(value.first?.isNotEmpty()==true)
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@MainActivity,value.first,Toast.LENGTH_SHORT).show()
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                        }
                }.launchIn(lifecycleScope)
            }

    }
}