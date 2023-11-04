package com.indivar.domain.repo

import com.indivar.common.api.NetworkApi
import com.indivar.core.data.Response
import com.indivar.domain.repo.match.details.MatchDetail
import com.indivar.domain.repo.match.details.MatchInning
import com.indivar.domain.repo.match.details.Officials
import com.indivar.domain.repo.match.details.Team
import com.indivar.domain.repo.series.fixtures.FixtureItem
import com.indivar.domain.repo.series.fixtures.FixtureScore
import com.indivar.domain.repo.series.fixtures.FixturesForSeries
import com.indivar.domain.repo.series.fixtures.InningsScore
import com.indivar.domain.repo.series.fixtures.MatchDetailsEntry
import com.indivar.domain.repo.series.fixtures.MatchDetailsMap
import com.indivar.domain.repo.series.fixtures.TeamScore
import com.indivar.domain.repo.series.fixtures.VenueInfo
import com.indivar.domain.repo.series.listings.NetworkSeries
import com.indivar.domain.repo.series.listings.NetworkSeriesGroup
import com.indivar.domain.repo.series.listings.NetworkSeriesListings
import com.indivar.domain.usecases.DetailedServerError
import com.indivar.models.Boundary
import com.indivar.models.BoundaryType
import com.indivar.models.Player
import com.indivar.models.match.BattingMatchStat
import com.indivar.models.match.BowlingMatchStat
import com.indivar.models.match.Inning
import com.indivar.models.match.Match
import com.indivar.models.match.MatchDates
import com.indivar.models.match.MatchOfficials
import com.indivar.models.match.Overs
import com.indivar.models.match.ScoreCard
import com.indivar.models.series.Fixture
import com.indivar.models.series.FixtureGroup
import com.indivar.models.series.MatchScores
import com.indivar.models.series.Scores
import com.indivar.models.series.Series
import com.indivar.models.series.SeriesFixtures
import com.indivar.models.series.SeriesGroup
import com.indivar.models.series.SeriesListings
import com.indivar.models.series.TeamInnings
import com.indivar.models.series.Venue
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import retrofit2.HttpException
import java.io.IOException
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject
import com.indivar.models.Team as ModelsTeam

class RepositoryImpl @Inject constructor(
    private val networkApi: NetworkApi,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : Repository {
    override suspend fun pullMatchDetails(matchId: Int): Response<Match> = makeRequest {
        requireNotNull(networkApi.getMatchDetails(matchId).match)
    }

    override suspend fun getSeriesListings(type: String): Response<SeriesListings> = makeRequest {
        val v = networkApi.getSeriesListings(type)
        v.seriesListings
    }

    private val NetworkSeriesListings.seriesListings: SeriesListings
        get() = SeriesListings(
            seoTitle = this.appIndex.seoTitle,
            webURL = this.appIndex.webURL,
            series = this.seriesMapProto.map { it.seriesGroup }
        )

    private val NetworkSeriesGroup.seriesGroup: SeriesGroup
        get() = SeriesGroup(
            dateTitle = this.date,
            list = this.series.map { it.seriesitem }
        )

    private val NetworkSeries.seriesitem: Series
        get() = Series(
            id = this.id,
            name = this.name,
            startDate = Instant.ofEpochMilli(this.startDt).atZone(ZoneId.systemDefault())
                .toLocalDate(),
            endDate = Instant.ofEpochMilli(this.endDt).atZone(ZoneId.systemDefault()).toLocalDate(),
        )

    private suspend fun <T> makeRequest(requestBlock: suspend () -> T): Response<T> {
        return withContext(defaultDispatcher) {
            withTimeoutOrNull(20000) {
                try {
                    Response.Success(requestBlock())
                } catch (ioException: IOException) {
                    Response.Error(
                        1000, ioException.message ?: "Unknown io error"
                    )
                } catch (httpException: HttpException) {
                    Response.Error(
                        httpException.code(),
                        convertErrorBody(httpException)?.message ?: "Unknown http exception"
                    )
                }

            } ?: Response.Error(
                1000,
                "Request Timed Out"
            )
        }
    }

    private fun convertErrorBody(throwable: HttpException): DetailedServerError? {
        return try {
            throwable.response()?.errorBody()?.source()?.let {
                val moshiAdapter = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
                    .adapter(DetailedServerError::class.java)
                moshiAdapter.fromJson(it)
            }
        } catch (exception: Exception) {
            null
        }
    }

    override suspend fun fetchSeriesFixtures(seriesId: Int): Response<SeriesFixtures> =
        makeRequest<SeriesFixtures> {
            val fixtures = networkApi.getSeriesFixtures(seriesId)
            fixtures.seriesFixtures
        }

}

val FixturesForSeries.seriesFixtures: SeriesFixtures
    get() = SeriesFixtures(
        fixtures = this.matchDetails.mapNotNull(MatchDetailsEntry::fixtures)
    )

val MatchDetailsEntry.fixtures: FixtureGroup?
    get() = matchDetailsMap?.fixtureGroup

val MatchDetailsMap.fixtureGroup: FixtureGroup
    get() = FixtureGroup(
        key = this.key,
        seriesId = this.seriesId,
        fixtures = this.match.map(FixtureItem::fixture)
    )


val FixtureItem.fixture: Fixture
    get() = Fixture(

        id = matchInfo.id,
        seriesId = matchInfo.seriesId,
        seriesName = matchInfo.seriesName,
        matchDesc = matchInfo.matchDesc,
        matchFormat = matchInfo.matchFormat,
        startDate = Instant.ofEpochMilli(matchInfo.startDate)
            .atZone(ZoneId.of(matchInfo.venue.timezone)).toLocalDate(),
        endDate = Instant.ofEpochMilli(matchInfo.endDate)
            .atZone(ZoneId.of(matchInfo.venue.timezone)).toLocalDate(),
        state = matchInfo.state,
        result = matchInfo.status,
        team1 = matchInfo.team1.team,
        team2 = matchInfo.team2.team,
        venue = matchInfo.venue.venue,
        currBatTeamId = matchInfo.currBatTeamId,
        seriesStartDate = Instant.ofEpochMilli(matchInfo.seriesStartDt)
            .atZone(ZoneId.of(matchInfo.venue.timezone)).toLocalDate(),
        seriesEndDate = Instant.ofEpochMilli(matchInfo.seriesEndDt)
            .atZone(ZoneId.of(matchInfo.venue.timezone)).toLocalDate(),
        isTimeAnnounced = matchInfo.isTimeAnnounced,
        matchScores = matchScore?.matchScores,
    )

val FixtureScore.matchScores: MatchScores
    get() = MatchScores(
        team1Scores = this.team1Score.score,
        team2Scores = this.team2Score.score,
    )
val TeamScore.score: Scores
    get() = Scores(
        innings1 = this.inngs1?.teamInnings,
        innings2 = this.inngs2?.teamInnings,
    )

val InningsScore.teamInnings: TeamInnings
    get() = TeamInnings(
        inningsId = inningsId,
        runs = runs,
        wickets = wickets,
        overs = Overs(
            completeOvers = overs.toInt(),
            ballsInCurrentOver = ((overs - overs.toInt()) * 10).toInt()
        )
    )
val VenueInfo.venue: Venue
    get() = Venue(
        id = id,
        ground = ground,
        city = city,
        timezone = timezone,
    )
val Team.team: ModelsTeam
    get() = ModelsTeam(
        id = this.teamId,
        name = this.teamName,
        code = this.teamSName,
        imageId = this.imageId,
    )
val MatchDetail.match: Match?
    get() = this.results?.let {
        Match(
            id = this.results.fixture.id,
            seriesId = this.results.fixture.series_id,
            matchDates = this.results.fixture.start_date?.let { startDate ->
                this.results.fixture.end_date?.let { endDate ->
                    MatchDates(startDate, endDate)
                }
            },
            title = this.results.fixture.match_title,
            homeTeam = this.results.fixture.home.team,
            awayTeams = listOf(this.results.fixture.away.team),
            result = this.results.liveDetails?.match_summary?.result == "Yes",
            tossResult = this.results.liveDetails?.match_summary?.toss,
            homeTeamScores = this.results.liveDetails?.match_summary?.home_scores,
            awayTeamScores = this.results.liveDetails?.match_summary?.away_scores,
            matchOfficials = this.results.liveDetails?.officials?.matchOfficials,
            scoreCard = this.results.liveDetails?.scorecard?.let { ScoreCard(it.map { it.inning }) },


            )
    }

val Officials.matchOfficials: MatchOfficials
    get() = MatchOfficials(
        firstUmpire = this.umpire_1,
        secondUmpire = this.umpire_2,
        thirdUmpire = this.umpire_tv,
        referee = this.referee,
        reserveUmpire = this.umpire_reserve,
    )

val String.overValue: Overs
    get() {
        this.toDoubleOrNull() ?: return Overs.None
        return if (this.contains('.')) {
            val parts = this.split('.')
            if (parts.size == 2) {
                Overs(completeOvers = parts[0].toInt(), ballsInCurrentOver = parts[1].toInt())
            } else {
                Overs.None
            }
        } else {
            Overs(completeOvers = this.toInt(), ballsInCurrentOver = 0)
        }
    }
val MatchInning.inning: Inning
    get() = Inning(
        title = this.title,
        runs = this.runs,
        overs = this.overs.overValue,
        wickets = this.wickets.toIntOrNull() ?: 0,
        extras = this.extras,
        extraTitle = this.extras_detail,
        fow = this.fow,
        batting = this.batting.map { stat ->
            val player = Player(id = stat.player_id, name = stat.player_name)
            val battingMatchStat = BattingMatchStat(
                battingOrder = stat.bat_order,
                outStyle = stat.how_out,
                minutesOfPlay = stat.minutes.toIntOrNull(),
                runs = stat.runs,
                balls = stat.balls,
                boundaries = buildList {
                    if (stat.fours > 0) {
                        this.add(Boundary(BoundaryType.Four, stat.fours))
                    }
                    if (stat.sixes > 0) {
                        this.add(Boundary(BoundaryType.Six, stat.sixes))
                    }
                },
                strikeRate = stat.strike_rate.toFloatOrNull(),
            )
            player to battingMatchStat
        },
        bowling = this.bowling.map { stat ->
            val player = Player(id = stat.player_id, name = stat.player_name)
            val battingMatchStat = BowlingMatchStat(

                overs = stat.overs.toIntOrNull() ?: 0,
                runs = stat.runs_conceded,
                maidens = stat.maidens,
                dotBalls = stat.dot_balls,
                boundaries = buildList {
                    if (stat.fours > 0) {
                        this.add(Boundary(BoundaryType.Four, stat.fours))
                    }
                    if (stat.sixes > 0) {
                        this.add(Boundary(BoundaryType.Six, stat.sixes))
                    }
                },
                economy = stat.economy.toFloatOrNull() ?: 0.0f,
                extras = stat.extras.toIntOrNull() ?: 0,
                wickets = stat.wickets,
            )
            player to battingMatchStat
        }
    )




