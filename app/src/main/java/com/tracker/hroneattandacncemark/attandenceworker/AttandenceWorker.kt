package com.tracker.hroneattandacncemark.attandenceworker

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tracker.hroneattandacncemark.activity.MainActivity
import com.tracker.hroneattandacncemark.ext.appPreferences
import com.tracker.hroneattandacncemark.ext.toRequestBody
import com.tracker.hroneattandacncemark.network.NetworkClient
import com.tracker.hroneattandacncemark.utils.AppPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class AttandenceWorker(private val context: Context, param: WorkerParameters) :
    Worker(context, param) {
    private val appPreferences: AppPreferences by lazy { context.appPreferences() }
    private val apiRequest by lazy {
        NetworkClient(appPreferences).getNetworkApis()
    }

    companion object {
        private const val AttandenceMarkChannelID = "mark_attendance"
        fun getRequest() = OneTimeWorkRequestBuilder<AttandenceWorker>()
            .setConstraints(
                Constraints.Builder().setRequiresBatteryNotLow(false).build()
            ).addTag(AttandenceMarkChannelID)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30L, TimeUnit.MINUTES).build()
    }

    private fun markAttendance(): Result {
        return if (appPreferences.data == null || appPreferences.data?.loginData==null) {
            context.startActivity(Intent(context, MainActivity::class.java))
            Result.failure()
        }else runBlocking {
            withContext(Dispatchers.IO) {
                try {
                    val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        apiRequest.markAttendance(getRequest(appPreferences))
                    } else throw Exception("Date Parse Failed")
                    if (result) Result.success()
                    else Result.retry()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Result.retry()
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getRequest(appPreferences: AppPreferences)= JSONObject().apply {
        this.put("attendanceSource","W")
        this.put("attendanceType","Online")
        this.put("punchTime",LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")))
        this.put("requestType","A")
        this.put("employeeId",appPreferences.data?.loginData?.employeeId?:0)
    }.toRequestBody()


    override fun doWork(): Result {
        return markAttendance()
    }
}