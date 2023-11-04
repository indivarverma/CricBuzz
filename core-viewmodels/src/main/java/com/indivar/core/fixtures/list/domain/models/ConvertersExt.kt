package com.indivar.core.fixtures.list.domain.models

import com.indivar.models.series.Fixture
import com.indivar.models.series.FixtureGroup
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val Fixture.card: FixtureCard
    get() = FixtureCard(
        id = id,
        title = matchDesc,
        team1Code = team1.code.take(3),
        team2Code = team2.code.take(3),
        result = result,
        dateString = if (startDate == endDate) {
            startDate.stringValue
        } else {
            "${startDate.stringValue} - ${endDate.stringValue}"
        },
    )


val LocalDate.stringValue: String
    get() = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
        .format(this)

val FixtureGroup.viewdata: FixtureGroupViewData
    get() = FixtureGroupViewData(
        key = key,
        seriesId = seriesId,
        fixtures = fixtures.map(Fixture::card)
    )

val List<FixtureGroup>.viewdata: List<FixtureGroupViewData>
    get() = map(FixtureGroup::viewdata)