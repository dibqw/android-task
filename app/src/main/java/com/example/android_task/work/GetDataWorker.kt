package com.example.android_task.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.android_task.data.repos.TasksRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class GetDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val tasksRepo: TasksRepo
): CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        Log.e("Worker", "Worker started")
        tasksRepo.refreshTasks()
        return Result.success()
    }
}