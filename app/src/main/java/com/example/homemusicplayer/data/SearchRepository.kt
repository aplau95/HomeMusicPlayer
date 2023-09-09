package com.example.homemusicplayer.data

import com.example.homemusicplayer.api.SearchService
import javax.inject.Inject


class SearchRepository @Inject constructor(private val service: SearchService) {

//    fun getSearchTermSuggestionStream(term: String): Flow<SearchSuggestionResponse> {
////        return flow {
////            val searchSuggestionResponse =
//                return service.searchTermSuggestions(listOf("terms"), term = term)
////            emit(searchSuggestionResponse)
//        }
//
////        return Pager(
////            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
////            pagingSourceFactory = { SearchPagingSource(service, term = term) }
////        ).flow
//    }
//
//    companion object {
//
//        private const val NETWORK_PAGE_SIZE = 5
//    }
}