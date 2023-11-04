package com.indivar.core.series.detail.domain.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.indivar.core.Navigator
import com.indivar.core.common.domain.viewmodel.MviViewModel
import com.indivar.core.data.ErrorState
import com.indivar.core.data.Response
import com.indivar.core.series.detail.domain.usecase.GetSeriesListingsUseCase

import com.indivar.models.series.Series
import com.indivar.models.series.SeriesGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SeriesListingsViewModel @Inject constructor(
    private val navigator: Navigator,
    private val stateHandle: SavedStateHandle,
    private val useCase: GetSeriesListingsUseCase,
) : MviViewModel<SeriesListingsViewState, SeriesListingsViewModel.DataState, SeriesListingsEffect>() {

    private val type  = stateHandle.get<String>("series_type")

    init {
        viewModelScope.launch(
            CoroutineExceptionHandler { coroutineContext, throwable ->
                enqueue {
                    copy(
                        seriesListings = emptyList(),
                        error = ErrorState(1000, throwable.message ?: "Unknown error"),
                    )
                }
            }
        ) {
            val dSeriesGroup = async(Dispatchers.IO) {
                type?.let {
                    useCase.getSeriesListings(it)
                }

            }

            val seriesGroup = dSeriesGroup.await()
            enqueue {
                when (seriesGroup) {
                    is Response.Success -> copy(
                        seriesListings = seriesGroup.data.series,
                        title = seriesGroup.data.seoTitle,
                        webUrl = seriesGroup.data.webURL,
                    )

                    is Response.Error -> copy(
                        seriesListings = emptyList(),
                        error = ErrorState(
                            seriesGroup.errorCode,
                            seriesGroup.errorMessage
                        )
                    )
                    null -> this
                }
            }

        }
    }

    private fun onSeriesSelected(series: Series) {
        viewModelScope.launch {
            navigator.navigateToSeriesFixtures(series)
        }
    }

    override val initialState: DataState
        get() = DataState(emptyList(), title = "", webUrl = null, error = null)

    override fun mapState(dataState: DataState): SeriesListingsViewState = SeriesListingsViewState(
        title = dataState.title,
        list = dataState.seriesListings,
        onSeriesSelected = ::onSeriesSelected,
    )


    data class DataState(
        val seriesListings: List<SeriesGroup>,
        val title: String,
        val webUrl: String?,
        val error: ErrorState? = null,
    )
}