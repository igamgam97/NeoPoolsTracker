package org.neopool.project.chart

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.Axis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.multiplatform.cartesian.data.lineSeries
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.multiplatform.common.Fill
import com.patrykandpatrick.vico.multiplatform.common.Insets
import com.patrykandpatrick.vico.multiplatform.common.LegendItem
import com.patrykandpatrick.vico.multiplatform.common.component.ShapeComponent
import com.patrykandpatrick.vico.multiplatform.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.multiplatform.common.component.rememberTextComponent
import com.patrykandpatrick.vico.multiplatform.common.data.ExtraStore
import com.patrykandpatrick.vico.multiplatform.common.rememberVerticalLegend
import com.patrykandpatrick.vico.multiplatform.common.shape.CorneredShape
import com.patrykandpatrick.vico.multiplatform.common.vicoTheme
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.neopool.project.poolreward.translations.Translations

private val LegendLabelKey = ExtraStore.Key<Set<String>>()

private val data = mapOf<String, Map<Int, Number>>(
    Translations.CHART_TITLE to mapOf(
        LocalDate(2025, 4, 29).toSeconds() to 3.13,
        LocalDate(2025, 4, 30).toSeconds() to 3.14,
        LocalDate(2025, 5, 1).toSeconds() to 3.15,
        LocalDate(2025, 5, 2).toSeconds() to 3.16,
    ),
)

private fun LocalDate.toSeconds(): Int {
    val instant = this.atStartOfDayIn(TimeZone.UTC)
    return instant.epochSeconds.toInt()
}

@Composable
fun PoolRewardChart(modifier: Modifier = Modifier) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            // Learn more: https://patrykandpatrick.com/z5ah6v.
            lineSeries { data.forEach { (_, map) -> series(map.keys, map.values) } }
            extras { extraStore -> extraStore[LegendLabelKey] = data.keys }
        }
    }
    val lineColors = listOf(Color(0xff916cda), Color(0xffd877d8), Color(0xfff094bb))
    val legendItemLabelComponent = rememberTextComponent(TextStyle(vicoTheme.textColor, 12.sp))
    CartesianChartHost(
        rememberCartesianChart(
            rememberLineCartesianLayer(
                LineCartesianLayer.LineProvider.series(
                    lineColors.map { color ->
                        LineCartesianLayer.rememberLine(
                            fill = LineCartesianLayer.LineFill.single(Fill(color)),
                            areaFill = null,
                            pointProvider = LineCartesianLayer.PointProvider.single(
                                LineCartesianLayer.Point(
                                    rememberShapeComponent(
                                        Fill(color),
                                        CorneredShape.Pill,
                                    ),
                                ),
                            ),
                        )
                    },
                ),
            ),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(
                guideline = null,
                valueFormatter = NeoPoolXValueFormatter(),
            ),
            legend = rememberVerticalLegend(
                items = { extraStore ->
                    extraStore[LegendLabelKey].forEachIndexed { index, label ->
                        add(
                            LegendItem(
                                ShapeComponent(Fill(lineColors[index]), CorneredShape.Pill),
                                legendItemLabelComponent,
                                label,
                            ),
                        )
                    }
                },
                padding = Insets(top = 16.dp),
            ),
            marker = rememberMarker(valueFormatter = NeoPoolMarkerFormatter.default()),
        ),
        modelProducer,
        modifier.height(294.dp),
        rememberVicoScrollState(scrollEnabled = false),
    )
}

class NeoPoolXValueFormatter : CartesianValueFormatter {
    override fun format(
        context: CartesianMeasuringContext,
        value: Double,
        verticalAxisPosition: Axis.Position.Vertical?,
    ): CharSequence {
        return formatTimestamp(value.toLong())
    }
}

fun formatTimestamp(timestamp: Long): String {
    val instant = Instant.fromEpochSeconds(timestamp)
    val localDateTime = instant.toLocalDateTime(TimeZone.UTC)
    return "${localDateTime.dayOfMonth} ${localDateTime.month}"
}