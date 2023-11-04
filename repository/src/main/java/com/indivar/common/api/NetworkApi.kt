package com.indivar.common.api

import com.indivar.domain.repo.match.details.MatchDetail
import com.indivar.domain.repo.series.fixtures.FixturesForSeries
import com.indivar.domain.repo.series.listings.NetworkSeriesListings
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkApi {
    @GET("/match/{matchId}")
    suspend fun getMatchDetails(
        @Path("matchId") matchId: Int,
    ): MatchDetail

    @GET("/fixtures-by-series/{seriesId}")
    suspend fun getSeriesFixtures(
        @Path("seriesId") seriesId: Int,
    ): FixturesForSeries


    @GET("/series/v1/{type}")
    suspend fun getSeriesListings(
        @Path("type") type: String
    ): NetworkSeriesListings
}