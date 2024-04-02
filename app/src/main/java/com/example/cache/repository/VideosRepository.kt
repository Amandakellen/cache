package com.example.cache.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.cache.database.VideosDatabase
import com.example.cache.database.asDomainModel
import com.example.cache.domain.Video
import com.example.cache.network.Network
import com.example.cache.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class VideosRepository(private val database: VideosDatabase) {
    val videos: LiveData<List<Video>> = database.videoDao.getVideos().map { databaseVideos ->
        databaseVideos.asDomainModel()
    }

    suspend fun refreshVideos() {
        withContext(Dispatchers.IO) {
            val playlist = Network.devbytes.getPlaylist().await()
            database.videoDao.insertAll(*playlist.asDatabaseModel())
        }
    }
}