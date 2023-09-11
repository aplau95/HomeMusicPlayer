package com.example.homemusicplayer.viewModel

import com.example.homemusicplayer.data.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : BaseViewModel()