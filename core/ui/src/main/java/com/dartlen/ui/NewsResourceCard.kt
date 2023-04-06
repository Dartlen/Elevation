package com.dartlen.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dartlen.designsystem.NiaIconToggleButton
import com.dartlen.designsystem.NiaTheme
import com.dartlen.designsystem.chart.chart
import com.dartlen.elevation.core.ui.R
import com.dartlen.model.Road
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

/**
 * [NewsResource] card used on the following screens: For You, Saved
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsResourceCardExpanded(
    road: Road,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onClick: () -> Unit,
    onTopicClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    //val clickActionLabel = stringResource(R.string.card_tap_action)
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        // Use custom label for accessibility services to communicate button's action to user.
        // Pass null for action to only override the label and not the actual action.
        modifier = modifier.semantics {
            onClick(label = "clickActionLabel", action = null)
        },
    ) {
        Row(
            modifier = Modifier
                .height(100.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .width(150.dp)
                    .padding(8.dp)
            ) {
                chart(road.path)
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = road.startTime.toFormatHistory())
//                Text(text = stringResource(id = R.string.finishing, road.endTime.toFormatHistory()))

                Text(
                    text = stringResource(
                        id = R.string.distance,
                        road.distance.roundToInt().toString() + "m"
                    )
                )
                Text(
                    text = stringResource(
                        id = R.string.max_elevation,
                        road.elevation.roundToInt().toString() + "m AMSL"
                    )
                )
            }
        }
//        Column {
//            if (!userNewsResource.headerImageUrl.isNullOrEmpty()) {
//                Row {
//                    NewsResourceHeaderImage(userNewsResource.headerImageUrl)
//                }
//            }
//            Box(
//                modifier = Modifier.padding(16.dp),
//            ) {
//                Column {
//                    Spacer(modifier = Modifier.height(12.dp))
//                    Row {
//                        NewsResourceTitle(
//                            userNewsResource.title,
//                            modifier = Modifier.fillMaxWidth((.8f)),
//                        )
//                        Spacer(modifier = Modifier.weight(1f))
//                        BookmarkButton(isBookmarked, onToggleBookmark)
//                    }
//                    Spacer(modifier = Modifier.height(12.dp))
//                    NewsResourceMetaData(userNewsResource.publishDate, userNewsResource.type)
//                    Spacer(modifier = Modifier.height(12.dp))
//                    NewsResourceShortDescription(userNewsResource.content)
//                    Spacer(modifier = Modifier.height(12.dp))
//                    NewsResourceTopics(
//                        topics = userNewsResource.followableTopics,
//                        onTopicClick = onTopicClick,
//                    )
//                }
//            }
//        }
    }
}

fun Date.toFormatHistory(): String = SimpleDateFormat("dd.MM.yyyy").format(this).toString()

//@Composable
//fun NewsResourceHeaderImage(
//    headerImageUrl: String?,
//) {
//    AsyncImage(
//        placeholder = if (LocalInspectionMode.current) {
//            painterResource(DesignsystemR.drawable.ic_placeholder_default)
//        } else {
//            // TODO b/228077205, show specific loading image visual
//            null
//        },
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(180.dp),
//        contentScale = ContentScale.Crop,
//        model = headerImageUrl,
//        // TODO b/226661685: Investigate using alt text of  image to populate content description
//        contentDescription = null, // decorative image
//    )
//}

@Composable
fun NewsResourceTitle(
    newsResourceTitle: String,
    modifier: Modifier = Modifier,
) {
    Text(newsResourceTitle, style = MaterialTheme.typography.headlineSmall, modifier = modifier)
}

@Composable
fun BookmarkButton(
    isBookmarked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NiaIconToggleButton(
        checked = isBookmarked,
        onCheckedChange = { onClick() },
        modifier = modifier,
        icon = {
//            Icon(
//                painter = painterResource(NiaIcons.BookmarkBorder),
//                contentDescription = stringResource(R.string.bookmark),
//            )
        },
        checkedIcon = {
//            Icon(
//                painter = painterResource(NiaIcons.Bookmark),
//                contentDescription = stringResource(R.string.unbookmark),
//            )
        },
    )
}

//@Composable
//fun dateFormatted(publishDate: Instant): String {
//    var zoneId by remember { mutableStateOf(ZoneId.systemDefault()) }
//
//    val context = LocalContext.current
//
//    DisposableEffect(context) {
//        val receiver = TimeZoneBroadcastReceiver(
//            onTimeZoneChanged = { zoneId = ZoneId.systemDefault() },
//        )
//        receiver.register(context)
//        onDispose {
//            receiver.unregister(context)
//        }
//    }
//
//    return DateTimeFormatter.ofPattern("MMM d, yyyy").withZone(zoneId).format(publishDate.toJavaInstant())
//}

//@Composable
//fun NewsResourceMetaData(
//    publishDate: Instant,
//    resourceType: NewsResourceType,
//) {
//    val formattedDate = dateFormatted(publishDate)
//    Text(
//        if (resourceType != NewsResourceType.Unknown) {
//            stringResource(R.string.card_meta_data_text, formattedDate, resourceType.displayText)
//        } else {
//            formattedDate
//        },
//        style = MaterialTheme.typography.labelSmall,
//    )
//}

//@Composable
//fun NewsResourceLink(
//    @Suppress("UNUSED_PARAMETER")
//    newsResource: NewsResource,
//) {
//    TODO()
//}

//@Composable
//fun NewsResourceShortDescription(
//    newsResourceShortDescription: String,
//) {
//    Text(newsResourceShortDescription, style = MaterialTheme.typography.bodyLarge)
//}
//
//@Composable
//fun NewsResourceTopics(
//    topics: List<FollowableTopic>,
//    onTopicClick: (String) -> Unit,
//    modifier: Modifier = Modifier,
//) {
//    Row(
//        modifier = modifier.horizontalScroll(rememberScrollState()), // causes narrow chips
//        horizontalArrangement = Arrangement.spacedBy(4.dp),
//    ) {
//        for (followableTopic in topics) {
//            NiaTopicTag(
//                followed = followableTopic.isFollowed,
//                onClick = { onTopicClick(followableTopic.topic.id) },
//                text = {
//                    val contentDescription = if (followableTopic.isFollowed) {
//                        stringResource(
//                            R.string.topic_chip_content_description_when_followed,
//                            followableTopic.topic.name,
//                        )
//                    } else {
//                        stringResource(
//                            R.string.topic_chip_content_description_when_not_followed,
//                            followableTopic.topic.name,
//                        )
//                    }
//                    Text(
//                        text = followableTopic.topic.name.uppercase(Locale.getDefault()),
//                        modifier = Modifier.semantics {
//                            this.contentDescription = contentDescription
//                        },
//                    )
//                },
//            )
//        }
//    }
//}

@Preview("Bookmark Button")
@Composable
private fun BookmarkButtonPreview() {
    NiaTheme {
        Surface {
            BookmarkButton(isBookmarked = false, onClick = { })
        }
    }
}

@Preview("Bookmark Button Bookmarked")
@Composable
private fun BookmarkButtonBookmarkedPreview() {
    NiaTheme {
        Surface {
            NewsResourceCardExpanded(
                road = Road(0, 0, 0.0, 0.0, 675.0, 1000f, 0.0f, Date(), Date(), mutableListOf()),
                isBookmarked = true, {}, {}, {})
        }
    }
}

//@Preview("NewsResourceCardExpanded")
//@Composable
//private fun ExpandedNewsResourcePreview(
//    @PreviewParameter(UserNewsResourcePreviewParameterProvider::class)
//    userNewsResources: List<UserNewsResource>,
//) {
//    NiaTheme {
//        Surface {
//            NewsResourceCardExpanded(
//                userNewsResource = userNewsResources[0],
//                isBookmarked = true,
//                onToggleBookmark = {},
//                onClick = {},
//                onTopicClick = {},
//            )
//        }
//    }
//}
