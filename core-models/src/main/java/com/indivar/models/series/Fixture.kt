package com.indivar.models.series

import com.indivar.models.Team
import com.indivar.models.match.Overs
import java.time.LocalDate


data class FixtureGroup(
    val key: String,
    val seriesId: Int,
    val fixtures: List<Fixture>,
)

data class Fixture(
    val seriesId: Int,
    val id: Int,
    val seriesName: String,
    val matchDesc: String,
    val matchFormat: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val state: String,
    val result: String,
    val team1: Team,
    val team2: Team,
    val venue: Venue,
    val currBatTeamId: Int?,
    val seriesStartDate: LocalDate,
    val seriesEndDate: LocalDate,
    val isTimeAnnounced: Boolean?,
    val matchScores: MatchScores?,
)

data class Venue(
    val id: Int,
    val ground: String,
    val city: String,
    val timezone: String,
)

data class MatchScores(
    val team1Scores: Scores?,
    val team2Scores: Scores?,
)
data class Scores(
    val innings1: TeamInnings?,
    val innings2: TeamInnings?,
)

data class TeamInnings(
    val inningsId: Int,
    val runs: Int,
    val wickets: Int,
    val overs: Overs,
)