package com.indivar.models.series

data class SeriesListings(
    val seoTitle: String,
    val webURL: String,
    val series: List<SeriesGroup>
)