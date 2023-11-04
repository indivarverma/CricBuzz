package com.indivar.cricketapp.ui.series.listings

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.indivar.core.series.detail.domain.viewmodel.SeriesListingsEffect
import com.indivar.core.series.detail.domain.viewmodel.SeriesListingsViewModel
import com.indivar.cricketapp.collectAsEffect
import com.indivar.models.series.Series
import com.indivar.models.series.SeriesGroup
import kotlinx.coroutines.Dispatchers
import java.time.format.DateTimeFormatter

@Composable
fun SeriesListingsViewScreen(
    viewModel: SeriesListingsViewModel
) {
    val coroutineScope = rememberCoroutineScope()

    fun SeriesListingsEffect.consume() = Unit

    val state = viewModel.state.collectAsState(viewModel.initialViewState).value
    viewModel.effects.collectAsEffect(
        coroutineScope = coroutineScope,
        context = Dispatchers.Main,
        block = SeriesListingsEffect::consume
    )

    SeriesListingsView(
        title = state.title,
        series = state.list,
        onSeriesSelected = state.onSeriesSelected,
    )

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SeriesListingsView(
    title: String,
    series: List<SeriesGroup>,
    onSeriesSelected: Series.() -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                )
            })
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            series.onEach { group ->
                stickyHeader {
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
                            text = group.dateTitle,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
                items(group.list) { series ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 8.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxWidth().wrapContentHeight().clickable {
                            series.onSeriesSelected()
                        }) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()

                                    .padding(horizontal = 24.dp, vertical = 12.dp),
                                verticalArrangement = Arrangement.SpaceEvenly,
                                horizontalAlignment = Alignment.Start,
                            ) {
                                Text(
                                    text = series.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                ) {
                                    Text(
                                        text = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
                                            .format(series.startDate),
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                    Text(
                                        modifier = Modifier.padding(16.dp),
                                        text = "-",
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                    Text(
                                        text = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
                                            .format(series.endDate),
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                }

                            }
                        }
                    }
                }

            }

        }
    }
}