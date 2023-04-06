package com.dartlen.elevation.navigation


import com.dartlen.designsystem.icon.Icon
import com.dartlen.designsystem.icon.NiaIcons
import com.dartlen.designsystem.icon.Icon.*
import com.dartlen.elevation.R
import com.dartlen.elevation.feature.history.R as historyR

/**
 * Type for the top level destinations in the application. Each of these destinations
 * can contain one or more screens (based on the window size). Navigation from one screen to the
 * next within a single destination will be handled directly in composables.
 */
enum class TopLevelDestination(
    val selectedIcon: Icon,
    val unselectedIcon: Icon,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    FOR_YOU(
        selectedIcon = DrawableResourceIcon(NiaIcons.Landscape),
        unselectedIcon = DrawableResourceIcon(NiaIcons.LandscapeBorder),
        iconTextId = R.string.for_you,
        titleTextId = R.string.app_name,
    ),
    INTERESTS(
        selectedIcon = DrawableResourceIcon(NiaIcons.map),
        unselectedIcon = DrawableResourceIcon(NiaIcons.map),
        iconTextId = historyR.string.map,
        titleTextId = historyR.string.map,
    ),
    BOOKMARKS(
        selectedIcon = DrawableResourceIcon(NiaIcons.History),
        unselectedIcon = DrawableResourceIcon(NiaIcons.HistoryBorder),
        iconTextId = R.string.interests,
        titleTextId = R.string.interests,
    ),
}
