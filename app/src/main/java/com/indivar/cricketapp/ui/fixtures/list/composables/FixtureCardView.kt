package com.indivar.cricketapp.ui.fixtures.list.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.indivar.core.fixtures.list.domain.models.FixtureCard

@Composable
fun FixtureCardView(
    fixture: FixtureCard,
    onItemClick: FixtureCard.() -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 8.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                fixture.onItemClick()
            }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()

                    .padding(horizontal = 24.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = fixture.title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = fixture.team1Code,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = " vs ",
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Text(
                        text = fixture.team2Code,
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = fixture.dateString,
                    style = MaterialTheme.typography.bodySmall,
                )
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = fixture.result,
                    style = MaterialTheme.typography.bodySmall,
                )

            }
        }
    }
}