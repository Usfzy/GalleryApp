package com.usfzy.photogallery.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.usfzy.photogallery.repository.PhotoRepository
import com.usfzy.photogallery.repository.PreferenceRepository
import kotlinx.coroutines.flow.first

private const val TAG = "PollWorker"

class PollWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        Log.d(TAG, "doWork: ")

        val preferenceRepository = PreferenceRepository.get()
        val photoRepository = PhotoRepository()

        val query = preferenceRepository.storedQuery.first()
        val lastId = preferenceRepository.lastResultId.first()

        if (query.isEmpty()) {
            Log.d(TAG, "doWork: ")
            return Result.success()
        }

        return try {
            val items = photoRepository.searchPhotos(query)

            if (items.isNotEmpty()) {
                val newResId = items.first().id
                if (newResId == lastId) {
                    Log.d(TAG, "doWork: Still have the same result")
                } else {
                    Log.d(TAG, "doWork: Got a new Result")
                    preferenceRepository.setLastResultId(newResId)
                }
            }

            Result.success()
        } catch (ex: Exception) {
            Log.d(TAG, "doWork: ${ex.message}")
            Result.success()
        }
    }
}