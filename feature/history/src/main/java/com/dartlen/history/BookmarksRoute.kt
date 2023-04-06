package com.dartlen.history

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dartlen.designsystem.LocalTintTheme
import com.dartlen.designsystem.NiaTheme
import com.dartlen.designsystem.component.NiaLoadingWheel
import com.dartlen.elevation.feature.history.R
import com.dartlen.ui.NewsFeedUiState
import com.dartlen.ui.TrackScrollJank
import com.dartlen.ui.newsFeed

@Composable
internal fun HistoryRoute(
    onTopicClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    val feedState by viewModel.feedUiState.collectAsStateWithLifecycle()
    HistoryScreen(
        feedState = feedState,
        removeFromBookmarks = {},//viewModel::removeFromSavedResources,
        onTopicClick = onTopicClick,
        modifier = modifier,
    )
}

/**
 * Displays the user's bookmarked articles. Includes support for loading and empty states.
 */
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun HistoryScreen(
    feedState: NewsFeedUiState,
    removeFromBookmarks: (String) -> Unit,
    onTopicClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (feedState) {
        NewsFeedUiState.Loading -> LoadingState(modifier)
        is NewsFeedUiState.Success -> if (feedState.roads.isNotEmpty()) {
            BookmarksGrid(feedState, removeFromBookmarks, onTopicClick, modifier)
        } else {
            EmptyState(modifier)
        }
    }
    //TrackScreenViewEvent(screenName = "Saved")
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    NiaLoadingWheel(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize()
            .testTag("forYou:loading"),
        contentDesc = "stringResource(id = R.string.saved_loading)",
    )
}

@Composable
private fun BookmarksGrid(
    feedState: NewsFeedUiState,
    removeFromBookmarks: (String) -> Unit,
    onTopicClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollableState = rememberLazyGridState()
    TrackScrollJank(scrollableState = scrollableState, stateName = "bookmarks:grid")
    LazyVerticalGrid(
        columns = GridCells.Adaptive(300.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        state = scrollableState,
        modifier = modifier
            .fillMaxSize()
            .testTag("bookmarks:feed"),
    ) {
        newsFeed(
            feedState = feedState,
            onNewsResourcesCheckedChanged = { id, _ -> removeFromBookmarks(id) },
            onTopicClick = onTopicClick,
        )
        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
            .testTag("bookmarks:empty"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val iconTint = LocalTintTheme.current.iconTint
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(id = com.dartlen.elevation.core.designsystem.R.drawable.empty_history),
            colorFilter = if (iconTint != null) ColorFilter.tint(iconTint) else null,
            contentDescription = null,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.history_empty_error),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.history_empty_description),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview
@Composable
private fun LoadingStatePreview() {
    NiaTheme {
        LoadingState()
    }
}

//@Preview
//@Composable
//private fun BookmarksGridPreview(
//    @PreviewParameter(UserNewsResourcePreviewParameterProvider::class)
//    userNewsResources: List<UserNewsResource>,
//) {
//    NiaTheme {
//        BookmarksGrid(
//            feedState = Success(userNewsResources),
//            removeFromBookmarks = {},
//            onTopicClick = {},
//        )
//    }
//}

@Preview
@Composable
private fun EmptyStatePreview() {
    NiaTheme {
        EmptyState()
    }
}