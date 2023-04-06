package com.dartlen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dartlen.domain.GetRoadsUseCase
import com.dartlen.model.Road
import com.dartlen.ui.NewsFeedUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(getRoadsUseCase: GetRoadsUseCase,): ViewModel() {
    val feedUiState: StateFlow<NewsFeedUiState> = getRoadsUseCase()
        .map<List<Road>, NewsFeedUiState>(NewsFeedUiState::Success)
        .onStart { emit(NewsFeedUiState.Loading) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = NewsFeedUiState.Loading,
        )
}