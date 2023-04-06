package com.dartlen.map.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dartlen.map.MapWrapper

private const val interestsGraphRoutePattern = "interests_graph"
const val interestsRoute = "interests_route"

fun NavController.navigateToInterestsGraph(navOptions: NavOptions? = null) {
    this.navigate(interestsGraphRoutePattern, navOptions)
}

fun NavGraphBuilder.interestsGraph(
    onTopicClick: (String) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    navigation(
        route = interestsGraphRoutePattern,
        startDestination = interestsRoute,
    ) {
        composable(route = interestsRoute) {
            //InterestsRoute(onTopicClick)

            MapWrapper()
        }
        nestedGraphs()
    }
}
