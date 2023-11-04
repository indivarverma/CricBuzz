package com.indivar.domain.usecases

import com.indivar.core.data.Response
import com.indivar.core.series.detail.domain.usecase.GetSeriesListingsUseCase
import com.indivar.domain.repo.Repository
import com.indivar.models.series.SeriesListings
import javax.inject.Inject

class GetSeriesListingsUseCaseImpl @Inject constructor(
    private val networkRepository: Repository
) : GetSeriesListingsUseCase {
    override suspend fun getSeriesListings(type: String): Response<SeriesListings> =
        networkRepository.getSeriesListings(type)

}