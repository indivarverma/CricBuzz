package com.indivar.domain.repo.series.listings

data class NetworkSeriesListings(
    val appIndex: AppIndex,
    val seriesMapProto: List<NetworkSeriesGroup>
)

data class AppIndex(
    val seoTitle: String,
    val webURL: String
)

data class NetworkSeriesGroup(
    val date: String,
    val series: List<NetworkSeries>
)


data class NetworkSeries(
    val id: Int,
    val name: String,
    val startDt: Long,
    val endDt: Long,
)