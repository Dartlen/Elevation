package com.dartlen.ui

import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items

/**
 * An extension on [LazyListScope] defining a feed with news resources.
 * Depending on the [feedState], this might emit no items.
 */
fun LazyGridScope.newsFeed(
    feedState: NewsFeedUiState,
    onNewsResourcesCheckedChanged: (String, Boolean) -> Unit,
    onTopicClick: (String) -> Unit,
) {
    when (feedState) {
        NewsFeedUiState.Loading -> Unit
        is NewsFeedUiState.Success -> {
            items(feedState.roads, key = { it.id }) { userNewsResource ->
//                val resourceUrl by remember {
//                    mutableStateOf(Uri.parse(userNewsResource.url))
//                }
//                val context = LocalContext.current
//                val analyticsHelper = LocalAnalyticsHelper.current
//                val backgroundColor = MaterialTheme.colorScheme.background.toArgb()

                NewsResourceCardExpanded(
                    road = userNewsResource,
                    isBookmarked = false,
                    onClick = {
//                        analyticsHelper.logNewsResourceOpened(
//                            newsResourceId = userNewsResource.id,
//                            newsResourceTitle = userNewsResource.title,
//                        )
                        //launchCustomChromeTab(context, resourceUrl, backgroundColor)
                    },
                    onToggleBookmark = {
//                        onNewsResourcesCheckedChanged(
//                            userNewsResource.id,
//                            !userNewsResource.isSaved,
//                        )
                    },
                    onTopicClick = onTopicClick,
                )
            }
        }
    }
}