package com.indivar.domain.repo.series.fixtures

import com.indivar.domain.repo.match.details.Team
import com.indivar.domain.repo.series.listings.AppIndex
import com.squareup.moshi.Json

data class FixturesForSeries(
    @Json(name = "matchDetails")
    val matchDetails: List<MatchDetailsEntry>,
    @Json(name = "landingPosition")
    val landingPosition: Int,
    @Json(name = "appIndex")
    val appIndex: AppIndex,
)


data class MatchDetailsEntry(
    @Json(name = "matchDetailsMap")
    val matchDetailsMap: MatchDetailsMap?,
    @Json(name = "adDetail")
    val addDetail: AdDetail?,
)

data class MatchDetailsMap(
    val key: String,
    val seriesId: Int,
    val match: List<FixtureItem>,
)

data class AdDetail(
    val name: String,
    val layout: String,
    val position: Int,
)

data class FixtureItem(
    val matchInfo: FixtureInfo,
    val matchScore: FixtureScore?,
)

data class FixtureInfo(
    @Json(name = "matchId")
    val id: Int,
    @Json(name = "seriesId")
    val seriesId: Int,
    @Json(name = "seriesName")
    val seriesName: String,
    @Json(name = "matchDesc")
    val matchDesc: String,
    @Json(name = "matchFormat")
    val matchFormat: String,
    @Json(name = "startDate")
    val startDate: Long,
    @Json(name = "endDate")
    val endDate: Long,

    @Json(name = "state")
    val state: String,
    @Json(name = "status")
    val status: String,
    @Json(name = "team1")
    val team1: Team,
    @Json(name = "team2")
    val team2: Team,
    @Json(name = "venueInfo")
    val venue: VenueInfo,
    @Json(name = "currBatTeamId")
    val currBatTeamId: Int?,
    @Json(name = "seriesStartDt")
    val seriesStartDt: Long,
    @Json(name = "seriesEndDt")
    val seriesEndDt: Long,
    @Json(name = "isTimeAnnounced")
    val isTimeAnnounced: Boolean?,
)

data class FixtureScore(
    val team1Score: TeamScore,
    val team2Score: TeamScore,
)

data class TeamScore(
    val inngs1: InningsScore?,
    val inngs2: InningsScore?,
)

data class InningsScore(
    val inningsId: Int,
    val runs: Int,
    val wickets: Int,
    val overs: Float,
)

data class VenueInfo(
    val id: Int,
    val ground: String,
    val city: String,
    val timezone: String,
)