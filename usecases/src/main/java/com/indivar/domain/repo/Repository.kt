package com.indivar.domain.repo

import com.indivar.core.data.Response
import com.indivar.models.match.Match
import com.indivar.models.series.SeriesFixtures
import com.indivar.models.series.SeriesGroups
import com.indivar.models.series.SeriesListings

interface Repository {
    suspend fun pullMatchDetails(
        matchId: Int,
    ): Response<Match>


    suspend fun getSeriesListings(type: String): Response<SeriesListings>

    suspend fun fetchSeriesFixtures(
        seriesId: Int,
    ): Response<SeriesFixtures>
}

