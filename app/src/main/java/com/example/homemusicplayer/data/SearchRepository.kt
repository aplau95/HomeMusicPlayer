package com.example.homemusicplayer.data

import com.example.homemusicplayer.api.ApiRequestFlow
import com.example.homemusicplayer.api.SearchService
import com.example.homemusicplayer.viewModel.BaseViewModel
import javax.inject.Inject


class SearchRepository @Inject constructor(
    private val service: SearchService,
) : BaseViewModel() {

    fun getCatalogResources(term: String) = ApiRequestFlow.apiRequestFlow {
        service.searchCatalogResources(term = term)
    }

    fun getSearchTermResources(term: String) = ApiRequestFlow.apiRequestFlow {
        service.searchTermSuggestions(term = term)
    }

//    fun getSearchResults(term: String) =
//        getCatalogResources(term).combine(getSearchTermResources(term)) { catalog, search ->
//
//
//        }.stateIn()


}