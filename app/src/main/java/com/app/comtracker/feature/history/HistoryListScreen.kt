package com.app.comtracker.feature.history

import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.comtracker.utilities.onSuccess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryListScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistoryListViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                actions = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            contentDescription = null,
                            imageVector = Icons.Default.ArrowForward
                        )
                    }
                },
                title = { Text(text = "Histories") },
            )
        },
        modifier = modifier.fillMaxSize(),
        content = { innerPadding ->
            state.onSuccess { viewState ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding),
                    content = {

                        LazyRow(
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            content = {
                                items(
                                    count = viewState.filters.size,
                                    key = { it }
                                ) { index ->

                                    val isSelected = index == viewState.selectedFilterIndex
                                    val containerColor = when (isSelected) {
                                        true -> MaterialTheme.colorScheme.primary
                                        false -> MaterialTheme.colorScheme.secondaryContainer
                                    }

                                    val textColor = when (isSelected) {
                                        true -> MaterialTheme.colorScheme.onPrimary
                                        false -> MaterialTheme.colorScheme.onSecondaryContainer
                                    }

                                    Card(
                                        shape = CircleShape,
                                        colors = CardDefaults.cardColors(
                                            containerColor = containerColor,
                                            contentColor = textColor
                                        ),
                                        onClick = {
                                            viewModel.updateFilter(index)
                                        },
                                        content = {
                                            Text(
                                                modifier = Modifier.padding(
                                                    horizontal = 16.dp,
                                                    vertical = 8.dp
                                                ),
                                                text = viewState.filters[index]
                                            )
                                        }
                                    )
                                }
                            }
                        )

                        if (viewState.histories.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                                content = {
                                    Text(text = "Not tracked any thing")
                                }
                            )
                        } else {

                            val listState = rememberLazyListState()

                            val lastVisibleItemIndex by remember {
                                derivedStateOf {
                                    listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                                }
                            }
                            val totalItemsCount by remember {
                                derivedStateOf {
                                    listState.layoutInfo.totalItemsCount
                                }
                            }

                            LaunchedEffect(lastVisibleItemIndex >= totalItemsCount - 1) {
                                snapshotFlow {
                                    lastVisibleItemIndex >= viewState.histories.lastIndex &&
                                            !viewState.isLastPage &&
                                            viewState.histories.isNotEmpty()
                                }
                                    .collect { isLastItem ->
                                        if (isLastItem) {
                                            viewModel.getAllHistories(loadMore = true)
                                        }
                                    }
                            }


                            LazyColumn(
                                state = listState,
                                modifier = Modifier
                                    .fillMaxSize(),
                                content = {

                                    items(count = viewState.histories.size, key = { it }) { index ->
                                        val history = viewState.histories[index]
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp),
                                            verticalArrangement = Arrangement.Center,
                                            content = {
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    modifier = Modifier.fillMaxSize(),
                                                    content = {
                                                        Row {
                                                            val status =
                                                                if (history.isUploaded) "Success" else "Pending"
                                                            val containerColor =
                                                                if (history.isUploaded) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.errorContainer

                                                            val textColor =
                                                                if (history.isUploaded) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onErrorContainer

                                                            Box(
                                                                modifier = Modifier
                                                                    .clip(
                                                                        RoundedCornerShape(4.dp)
                                                                    )
                                                                    .background(containerColor),
                                                                contentAlignment = Alignment.Center,
                                                                content = {
                                                                    Text(
                                                                        modifier = Modifier.padding(
                                                                            horizontal = 8.dp,
                                                                            vertical = 4.dp
                                                                        ),
                                                                        color = textColor,
                                                                        text = "$status (${history.retryCount})"
                                                                    )
                                                                })
                                                            Spacer(modifier = Modifier.width(8.dp))
                                                            Text(
                                                                modifier = Modifier.padding(
                                                                    horizontal = 8.dp,
                                                                    vertical = 4.dp
                                                                ),
                                                                color = textColor,
                                                                text = history.createdAt
                                                            )
                                                        }
                                                        Box(
                                                            modifier = Modifier
                                                                .clip(
                                                                    RoundedCornerShape(4.dp)
                                                                )
                                                                .background(MaterialTheme.colorScheme.inversePrimary),
                                                            contentAlignment = Alignment.Center,
                                                            content = {
                                                                Text(
                                                                    modifier = Modifier.padding(
                                                                        horizontal = 8.dp,
                                                                        vertical = 4.dp
                                                                    ),
                                                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                                                    text = history.type.name
                                                                )
                                                            }
                                                        )
                                                    }
                                                )

                                                Spacer(modifier = Modifier.height(8.dp))

                                                Text(
                                                    text = "Phone number : ${history.phoneNumber}"
                                                )

                                                if (history.message.isNotEmpty()) {
                                                    Spacer(modifier = Modifier.height(8.dp))

                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .clip(
                                                                RoundedCornerShape(8.dp)
                                                            )
                                                            .background(MaterialTheme.colorScheme.secondaryContainer),
                                                        content = {
                                                            Text(
                                                                style = TextStyle(
                                                                    textDirection = TextDirection.ContentOrRtl
                                                                ),
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .padding(16.dp),
                                                                text = history.message
                                                            )
                                                        }
                                                    )
                                                }

                                                Spacer(modifier = Modifier.height(8.dp))

                                                if (!history.isUploaded) {
                                                    Button(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        shape = RoundedCornerShape(8.dp),
                                                        onClick = {
                                                            viewModel.retry(
                                                                index = index,
                                                                history = history
                                                            )
                                                        }
                                                    ) {
                                                        Text(
                                                            text = "Retry"
                                                        )
                                                    }
                                                }
                                                Spacer(modifier = Modifier.height(8.dp))
                                                HorizontalDivider()
                                                Spacer(modifier = Modifier.height(8.dp))

                                            }
                                        )
                                    }

                                }
                            )
                        }
                    }
                )
            }
        }

    )

}