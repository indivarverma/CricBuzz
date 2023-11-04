package com.indivar.core.fixtures.list.domain.models

import com.indivar.models.series.Fixture
import com.indivar.models.series.FixtureGroup

data class FixtureGroupViewData(
    val key: String,
    val seriesId: Int,
    val fixtures: List<FixtureCard>,
)


