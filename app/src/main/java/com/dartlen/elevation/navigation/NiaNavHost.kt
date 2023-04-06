package com.dartlen.elevation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.dartlen.current.navigation.forYouNavigationRoute
import com.dartlen.current.navigation.forYouScreen
import com.dartlen.history.navigation.bookmarksScreen
import com.dartlen.map.navigation.interestsGraph

/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
fun NiaNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = forYouNavigationRoute,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        // TODO: handle topic clicks from each top level destination
        forYouScreen(onTopicClick = {})
        bookmarksScreen(onTopicClick = {})
        interestsGraph(
            onTopicClick = { topicId ->
                //navController.navigateToTopic(topicId)
            },
            nestedGraphs = {
//                topicScreen(
//                    onBackClick = navController::popBackStack,
//                    onTopicClick = {},
//                )
            },
        )
    }
}
