package com.dartlen.history.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.dartlen.history.HistoryRoute

const val bookmarksRoute = "bookmarks_route"

fun NavController.navigateToBookmarks(navOptions: NavOptions? = null) {
    this.navigate(bookmarksRoute, navOptions)
}

fun NavGraphBuilder.bookmarksScreen(onTopicClick: (String) -> Unit) {
    composable(route = bookmarksRoute) {
        HistoryRoute(onTopicClick)
    }
}
