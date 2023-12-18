package com.example.homemusicplayer.data.apiResponse.search.searchSuggestionsResponse.suggestion

import com.google.gson.annotations.SerializedName

// Destructuring JSON response
data class TermSuggestion(
    @field:SerializedName("displayTerm") val displayTerm: String,
    @field:SerializedName("kind") val kind: String,
    @field:SerializedName("searchTerm") val searchTerm: String,
)
