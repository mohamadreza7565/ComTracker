package com.app.comtracker.broadcasts

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class RetryWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        /*val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app-db").build()
        val dao = db.pendingDataDao()
        val pendingList = dao.getAll()
        pendingList.forEach {
            val success = sendToApi(it.phoneNumber, it.message)
            if (success) {
                dao.delete(it)
            }
        }*/
        Log.i("TAG", "ComTrackerLogChecker RetryWorker -> retry")
        return Result.success()
    }
}
