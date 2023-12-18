package com.example.homemusicplayer.data

import com.example.homemusicplayer.api.ApiRequestFlow
import com.example.homemusicplayer.api.SearchService
import com.example.homemusicplayer.viewModel.BaseViewModel
import javax.inject.Inject

/**
 *
 */
class SearchRepository @Inject constructor(
    private val service: SearchService,
) : BaseViewModel() {

    /**
     * @param term, the search term that is inputted into the search bar
     *
     * Gets the catalog of albums, artists and songs related to the term
     */
    fun getCatalogResources(term: String) = ApiRequestFlow.apiRequestFlow {
        service.searchCatalogResources(term = term)
    }

    /**
     * @param term, the search term that is inputted into the search bar
     *
     * Gets the term suggestions for autocompletion in the search bar
     */
    fun getSearchTermResources(term: String) = ApiRequestFlow.apiRequestFlow {
        service.searchTermSuggestions(term = term)
    }
}