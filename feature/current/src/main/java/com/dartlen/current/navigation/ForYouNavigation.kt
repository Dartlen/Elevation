package com.dartlen.current.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.dartlen.current.currentRoute

const val forYouNavigationRoute = "for_you_route"

fun NavController.navigateToForYou(navOptions: NavOptions? = null) {
    this.navigate(forYouNavigationRoute, navOptions)
}

fun NavGraphBuilder.forYouScreen(onTopicClick: (String) -> Unit) {
    composable(route = forYouNavigationRoute) {
        currentRoute()
    }
}
