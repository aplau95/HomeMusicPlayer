package com.example.homemusicplayer.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


open class BaseViewModel : ViewModel() {

    private var mJob: Job? = null

    protected fun <T> baseRequest(
        stateFlow: MutableStateFlow<T>,
        errorHandler: CoroutinesErrorHandler,
        request: () -> Flow<T>
    ) {
        mJob = viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, error ->
            viewModelScope.launch(Dispatchers.Main) {
                errorHandler.onError(error.localizedMessage ?: "Error please try again")
            }
        }) {
            request().collect {
                withContext(Dispatchers.Main) {
                    stateFlow.value = it
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mJob?.apply {
            if (isActive) cancel()
        }
    }

    val coroutinesErrorHandler = object : CoroutinesErrorHandler {
        override fun onError(message: String) {
            Log.e("Error", message)
        }
    }
}

interface CoroutinesErrorHandler {

    fun onError(message: String)
}