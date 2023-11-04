package com.indivar.domain.usecases

import com.indivar.core.data.Response
import com.indivar.core.fixtures.list.domain.usecase.PullSeriesFixturesUseCase
import com.indivar.domain.repo.Repository
import com.indivar.models.series.SeriesFixtures
import javax.inject.Inject

class PullSeriesFixturesUseCaseImpl @Inject constructor(
    private val networkRepository: Repository,
) : PullSeriesFixturesUseCase {
    override suspend fun fetchSeriesFixtures(seriesId: Int): Response<SeriesFixtures> =

        when (val response = networkRepository.fetchSeriesFixtures(seriesId)) {
            is Response.Success -> response.copy(
                data = response.data
            )
            

            is Response.Error -> response
        }


}