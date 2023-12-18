package com.example.homemusicplayer.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homemusicplayer.persist.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Holds business logic for handling updating the tokens needed for Apple Music
 */
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
                    Log.e("TokenViewModel", "developer token: $it")
                    withContext(Dispatchers.Main) {
                        developerToken.value = it
                    }
                }
                getUserToken().collect {
                    Log.e("TokenViewModel", "userToken token: $it")
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