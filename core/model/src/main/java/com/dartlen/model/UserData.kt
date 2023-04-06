package com.dartlen.model

/**
 * Class summarizing user interest data
 */
data class UserData(
    val bookmarkedNewsResources: Set<String>,
    val followedTopics: Set<String>,
    val themeBrand: ThemeBrand,
    val darkThemeConfig: DarkThemeConfig,
    val useDynamicColor: Boolean,
    val shouldHideOnboarding: Boolean,
)
