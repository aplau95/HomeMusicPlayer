//package com.example.homemusicplayer.data
//
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.example.homemusicplayer.api.SearchService
//import com.example.homemusicplayer.data.apiResponse.search.searchSuggestionsResponse.suggestion.TermSuggestion
//
//private const val UNSPLASH_STARTING_PAGE_INDEX = 1
//
//class SearchPagingSource(
//    private val service: SearchService,
//    private val query: List<String> = listOf("terms"),
//    private val term: String
//) : PagingSource<Int, TermSuggestion>() {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TermSuggestion> {
////        val page = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
////        return try {
////            val response = service.searchTermSuggestions(query, term = term)
////            val photos = response.results
////            Log.e("Paging Result", "response results ${response.results}")
////            LoadResult.Page(
////                data = photos,
////                prevKey = if (page == UNSPLASH_STARTING_PAGE_INDEX) null else page - 1,
////                nextKey = if (page == response.results.size) null else page + 1
////            )
////        } catch (exception: Exception) {
////            LoadResult.Error(exception)
////        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, TermSuggestion>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
//            // multiple pages, the initial load will still load items centered around
//            // anchorPosition. This also prevents needing to immediately launch prepend due to
//            // prefetchDistance.
//            state.closestPageToPosition(anchorPosition)?.prevKey
//        }
//    }
//}