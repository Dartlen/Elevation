package com.dartlen.ui

import com.dartlen.model.Road

sealed interface NewsFeedUiState {
    /**
     * The roads is still loading.
     */
    object Loading : NewsFeedUiState

    /**
     * The roads is loaded.
     */
    data class Success(
        /**
         * The list of roads.
         */
        val roads: List<Road>,
    ) : NewsFeedUiState
}
