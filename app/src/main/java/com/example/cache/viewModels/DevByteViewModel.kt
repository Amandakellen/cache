package com.example.cache.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cache.database.getDatabase
import com.example.cache.domain.Video
import com.example.cache.network.Network
import com.example.cache.network.asDomainModel
import com.example.cache.repository.VideosRepository
import kotlinx.coroutines.launch
import java.io.IOException

class DevByteViewModel(application: Application) : AndroidViewModel(application) {

    private val database =  getDatabase(application)
    private val videosRepository = VideosRepository(database)

     var playlist : LiveData<List<Video>>

    init {
        viewModelScope.launch {
            videosRepository.refreshVideos()
        }

         playlist = videosRepository.videos
    }

    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DevByteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DevByteViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
