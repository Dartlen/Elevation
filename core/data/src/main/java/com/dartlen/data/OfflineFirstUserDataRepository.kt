package com.dartlen.data


import androidx.annotation.VisibleForTesting
import com.dartlen.datastore.NiaPreferencesDataSource
import com.dartlen.model.DarkThemeConfig
import com.dartlen.model.ThemeBrand
import com.dartlen.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineFirstUserDataRepository @Inject constructor(
    private val niaPreferencesDataSource: NiaPreferencesDataSource,
    //private val analyticsHelper: AnalyticsHelper,
) : UserDataRepository {

    override val userData: Flow<UserData> = niaPreferencesDataSource.userData

    @VisibleForTesting
    override suspend fun setFollowedTopicIds(followedTopicIds: Set<String>) =
        niaPreferencesDataSource.setFollowedTopicIds(followedTopicIds)

    override suspend fun toggleFollowedTopicId(followedTopicId: String, followed: Boolean) {
        niaPreferencesDataSource.toggleFollowedTopicId(followedTopicId, followed)
        //analyticsHelper.logTopicFollowToggled(followedTopicId, followed)
    }

    override suspend fun updateNewsResourceBookmark(newsResourceId: String, bookmarked: Boolean) {
        niaPreferencesDataSource.toggleNewsResourceBookmark(newsResourceId, bookmarked)
        //analyticsHelper.logNewsResourceBookmarkToggled(newsResourceId = newsResourceId, isBookmarked = bookmarked,)
    }

    override suspend fun setThemeBrand(themeBrand: ThemeBrand) {
        niaPreferencesDataSource.setThemeBrand(themeBrand)
        //analyticsHelper.logThemeChanged(themeBrand.name)
    }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        niaPreferencesDataSource.setDarkThemeConfig(darkThemeConfig)
        //analyticsHelper.logDarkThemeConfigChanged(darkThemeConfig.name)
    }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        niaPreferencesDataSource.setDynamicColorPreference(useDynamicColor)
        //analyticsHelper.logDynamicColorPreferenceChanged(useDynamicColor)
    }

    override suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        niaPreferencesDataSource.setShouldHideOnboarding(shouldHideOnboarding)
        //analyticsHelper.logOnboardingStateChanged(shouldHideOnboarding)
    }
}
