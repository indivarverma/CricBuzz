package com.indivar.core.fixtures.list.domain.models

data class FixtureCard(
    val id: Int,
    val title: String,
    val team1Code: String,
    val team2Code: String,
    val dateString: String,
    val result: String,
)
