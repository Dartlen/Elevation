package com.dartlen.current

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dartlen.common.network.Dispatcher
import com.dartlen.common.network.NiaDispatchers
import com.dartlen.domain.GetCurrentElevationUseCase
import com.dartlen.model.Elevation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CurrentViewModel @Inject constructor(val getCurrentElevationUseCase: GetCurrentElevationUseCase,
                                           @Dispatcher(NiaDispatchers.IO) val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    val currentElevation: Flow<Elevation> = getCurrentElevationUseCase.getElevation()
        .flowOn(ioDispatcher)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            Elevation(0)
        )



}



