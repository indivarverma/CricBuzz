package com.indivar.cricketapp.ui.fixtures.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.indivar.core.fixtures.list.domain.viewmodel.SeriesFixtureListViewModel
import com.indivar.core.fixtures.list.domain.viewmodel.SeriesFixturesListEffect
import com.indivar.cricketapp.collectAsEffect
import com.indivar.cricketapp.ui.common.LoadingScreen
import com.indivar.models.series.Fixture
import com.indivar.models.series.FixtureGroup
import kotlinx.coroutines.Dispatchers
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeriesFixturesListView(
    viewModel: SeriesFixtureListViewModel
) {

    val coroutineScope = rememberCoroutineScope()
    fun SeriesFixturesListEffect.consume() = Unit

    val state = viewModel.state.collectAsState(viewModel.initialViewState).value
    viewModel.effects.collectAsEffect(
        coroutineScope = coroutineScope,
        context = Dispatchers.Main,
        block = SeriesFixturesListEffect::consume
    )

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Fixtures",
                    style = MaterialTheme.typography.headlineMedium
                )
            })
        },
    ) { paddingValues ->
        if (state.isLoading) {
            LoadingScreen(Modifier.fillMaxSize())
        }
        if (state.isError) {
            Text("Encountered Error")
        }
        FixturesListView(
            data = state.fixtures,
            onItemClick = state.onFixtureClicked,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun FixturesListView(
    data: List<FixtureGroup>,
    onItemClick: Fixture.() -> Unit,
    modifier: Modifier = Modifier
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        data.onEach { group ->

            header {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer
                        )
                        .border(width = 2.dp, color = MaterialTheme.colorScheme.outline)
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = group.key,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            this.items(group.fixtures) { fixture ->
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
                                text = fixture.matchDesc,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text(
                                    text = fixture.team1.code.take(3),
                                    style = MaterialTheme.typography.titleLarge,
                                )
                                Text(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    text = " vs ",
                                    style = MaterialTheme.typography.bodySmall,
                                )
                                Text(
                                    text = fixture.team2.code.take(3),
                                    style = MaterialTheme.typography.titleLarge,
                                )
                            }
                            Text(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                text = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
                                    .format(fixture.startDate),
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

        }

    }

}

fun LazyGridScope.header(
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(span = { GridItemSpan(this.maxLineSpan) }, content = content)
}