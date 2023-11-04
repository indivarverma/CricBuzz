package com.indivar.models.series

import java.time.LocalDate

data class Series(
    val id: Int,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
)

data class SeriesGroup(
    val dateTitle: String,
    val list: List<Series>,
)