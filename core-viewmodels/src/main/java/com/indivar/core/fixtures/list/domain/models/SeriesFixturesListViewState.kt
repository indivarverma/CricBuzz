package com.indivar.core.fixtures.list.domain.models

import com.indivar.models.series.Fixture
import com.indivar.models.series.FixtureGroup

data class SeriesFixturesListViewState(
    val fixtures: List<FixtureGroupViewData>,
    val isError: Boolean,
    val isLoading: Boolean,
    val onFixtureClicked: (FixtureCard) -> Unit,
)