package org.neopool.project.poolreward.presentation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.neopool.project.chart.PoolRewardChart
import org.neopool.project.core.state.StateType
import org.neopool.project.core.state.map
import org.neopool.project.core.util.formatTimestamp
import org.neopool.project.core.viewmodel.collectInLaunchedEffectWithLifecycle
import org.neopool.project.poolreward.translations.Translations
import org.neopool.project.poolreward.translations.Translations.CHART_DESCRIPTION

private fun PoolViewModel.init() {
    dispatch(PoolAction.Init)
}

private fun PoolViewModel.updateBtnClick() {
    dispatch(PoolAction.Update)
}

@Composable
fun PoolRoute(
    modifier: Modifier = Modifier,
    viewModel: PoolViewModel = koinInject(),
) {

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.init()
    }

    viewModel.singleEvent.collectInLaunchedEffectWithLifecycle {
        launch {
            when (it) {
                is PoolSingleEvent.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = it.message,
                    )
                }
            }
        }
    }

    val uiState by viewModel.uiState.collectAsState()

    PoolScreen(
        state = uiState,
        modifier = modifier,
        updateBtnClick = viewModel::updateBtnClick,
        snackbarHostState = snackbarHostState,
    )
}

@Composable
private fun PoolScreen(
    state: PoolUiState,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    updateBtnClick: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Data(
            modifier = Modifier
                .padding(padding)
                .navigationBarsPadding()
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            uiState = state,
            updateBtnClick = updateBtnClick,
        )
    }
}

@Composable
private fun Data(
    uiState: PoolUiState,
    modifier: Modifier = Modifier,
    updateBtnClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            when {
                uiState.stateType.isLoading() -> {
                    CircularProgressIndicator()
                }

                uiState.stateType.isError() -> {
                    Text(
                        text = uiState.stateType.map { it } ?: "Unknown error",
                        style = MaterialTheme.typography.h6,
                    )
                }

                else -> {
                    NewPoolRewardInfo(uiState)
                }
            }
        }

        Button(
            onClick = updateBtnClick,
            enabled = !uiState.stateType.isLoading(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = Translations.RETRY)
        }
    }
}

@Composable
private fun NewPoolRewardInfo(
    uiState: PoolUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "${Translations.HASHRATE}: ${uiState.hashrate1d}",
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Start,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "${Translations.FEE_TYPE}: ${uiState.feeType}",
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.body1,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "${Translations.LAST_UPDATED}: ${formatTimestamp(uiState.updated)}",
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Start,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = CHART_DESCRIPTION,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Start,
        )
        Spacer(modifier = Modifier.height(12.dp))
        PoolRewardChart()
    }
}

@Preview
@Composable
fun PoolScreenPreview() {
    PoolScreen(
        state = previewPoolUiState,
    )
}

@Preview
@Composable
fun LoadingPoolScreenPreview() {
    PoolScreen(
        state = PoolUiState.Default.copy(
            stateType = StateType.loading(),
        ),
    )
}