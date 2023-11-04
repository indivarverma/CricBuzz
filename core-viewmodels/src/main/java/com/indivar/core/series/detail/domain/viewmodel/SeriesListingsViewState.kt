package com.indivar.core.series.detail.domain.viewmodel

import com.indivar.models.series.Series
import com.indivar.models.series.SeriesGroup

data class SeriesListingsViewState (
    val title: String,
    val list: List<SeriesGroup>,
    val onSeriesSelected: Series.() -> Unit
)