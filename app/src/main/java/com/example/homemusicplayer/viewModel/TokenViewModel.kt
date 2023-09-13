package com.example.homemusicplayer.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homemusicplayer.persist.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TokenViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {

    val developerToken = MutableLiveData("")
    val userToken = MutableLiveData("")

    init {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.run {
                getJWTToken().collect {
                    withContext(Dispatchers.Main) {
                        developerToken.value = it
                    }
                }
                getUserToken().collect {
                    withContext(Dispatchers.Main) {
                        userToken.value = it
                    }
                }
            }
        }
    }

    fun saveJWTToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.saveJWTToken(token)
        }
    }

    fun deleteJWTToken() {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.deleteJWTToken()
        }
    }

    fun saveUserToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            tokenManager.saveUserToken(token)
        }
    }
}