package org.neopool.project.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.ColumnScope
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.wrapContentWidth
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import org.neopool.project.core.state.DataType
import org.neopool.project.core.state.ErrorType
import org.neopool.project.core.state.LoadingType
import org.neopool.project.core.state.StateType
import org.neopool.project.core.state.StateTypeWithError
import org.neopool.project.core.util.formatTimestamp
import org.neopool.project.poolreward.translations.Translations

private val defaultTextColor = ColorProvider(Color.White)

class NeoPoolRewardWidget : GlanceAppWidget() {

    private val viewModel = NewPoolRewardWidgetViewModel().apply {
        init()
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                val state by viewModel.state.collectAsState(PoolWidgetUiState.Default)

                WidgetBody(
                    state = state,
                    onRetryClick = {
                        viewModel.retry()
                        // TODO(GlebShcherbakov) move later to viewmodel by koin
                        NeoPoolRewardTrackerWorker.requestUpdate(context)
                    },
                )
            }
        }
    }

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        viewModel.clean()
        super.onDelete(context, glanceId)
    }
}

@Composable
private fun WidgetBody(
    state: PoolWidgetUiState,
    modifier: GlanceModifier = GlanceModifier,
    onRetryClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (state.stateType) {
            is LoadingType -> {
                Loading()
            }

            is ErrorType -> {
                Error(
                    onRetryClick = onRetryClick,
                )
            }

            is DataType -> {
                Data(state)
            }
        }
    }
}

@Composable
private fun ColumnScope.Loading(
    modifier: GlanceModifier = GlanceModifier,
) {
    Text(
        modifier = modifier.wrapContentWidth(),
        text = "Loading...",
        style = TextStyle(color = defaultTextColor),
    )
}

@Composable
private fun ColumnScope.Error(
    modifier: GlanceModifier = GlanceModifier,
    onRetryClick: () -> Unit = {},
) {
    Text(
        modifier = modifier.wrapContentWidth(),
        text = "Error",
        style = TextStyle(color = defaultTextColor),
    )

    Spacer(modifier = GlanceModifier.height(12.dp))

    Button(
        text = Translations.RETRY,
        onClick = onRetryClick,
    )
}

@Composable
private fun Data(
    uiState: PoolWidgetUiState,
    modifier: GlanceModifier = GlanceModifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = GlanceModifier.fillMaxWidth(),
            text = "${Translations.HASHRATE}: ${uiState.hashrate1d}",
            style = TextStyle(color = defaultTextColor),
        )
        Spacer(modifier = GlanceModifier.height(12.dp))
        Text(
            modifier = GlanceModifier.fillMaxWidth(),
            text = "${Translations.FEE_TYPE}: ${uiState.feeType}",
            style = TextStyle(color = defaultTextColor),
        )
        Spacer(modifier = GlanceModifier.height(12.dp))
        Text(
            modifier = GlanceModifier.fillMaxWidth(),
            text = "${Translations.LAST_UPDATED}: ${formatTimestamp(uiState.updated ?: 0)}",
            style = TextStyle(color = defaultTextColor),
        )
    }
}

data class PoolWidgetUiState(
    val stateType: StateTypeWithError<String>,
    val hashrate1d: Long?,
    val feeType: String?,
    val updated: Long?,
) {
    companion object {
        val Default = PoolWidgetUiState(
            stateType = StateType.loading(),
            hashrate1d = null,
            feeType = null,
            updated = null,
        )
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 300, heightDp = 300)
@Composable
private fun PreviewWidget() {
    GlanceTheme {
        WidgetBody(
            state = PoolWidgetUiState(
                stateType = StateType.data(),
                hashrate1d = 123_456_789L,
                feeType = "Standard",
                updated = System.currentTimeMillis(),
            ),
        )
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 300, heightDp = 300)
@Composable
private fun PreviewLoadingWidget() {
    GlanceTheme {
        WidgetBody(
            state = PoolWidgetUiState(
                stateType = StateType.loading(),
                hashrate1d = 0L,
                feeType = "Standard",
                updated = System.currentTimeMillis(),
            ),
        )
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 300, heightDp = 300)
@Composable
private fun PreviewErrorWidget() {
    GlanceTheme {
        WidgetBody(
            state = PoolWidgetUiState.Default.copy(
                stateType = StateType.error("Error message"),
            ),
        )
    }
}