package com.indivar.models.series

data class SeriesGroups(
    val title: String,
    val description: String,
    val seriesListings: List<SeriesListings>,
)

