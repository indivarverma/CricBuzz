package com.indivar.core.series.detail.domain.usecase

import com.indivar.core.data.Response
import com.indivar.models.series.SeriesListings

interface GetSeriesListingsUseCase {
    suspend fun getSeriesListings(type: String): Response<SeriesListings>
}