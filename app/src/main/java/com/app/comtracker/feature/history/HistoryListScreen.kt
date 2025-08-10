package com.app.comtracker.feature.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    content = {

                        items(viewState.histories) { history ->
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
                                                Text(
                                                    color = if (history.isUploaded) Color.Red else Color.Green,
                                                    text = if (history.isUploaded) "Uploaded" else "Pending"
                                                )
                                                Spacer(modifier = Modifier.width(16.dp))
                                            }
                                            Text(
                                                text = history.type.name
                                            )
                                        }
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "Phone number : ${history.phoneNumber}"
                                    )


                                    if (history.message.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = history.message
                                        )
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