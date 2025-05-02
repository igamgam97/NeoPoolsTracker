package org.neopool.project.chart

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianDrawingContext
import com.patrykandpatrick.vico.multiplatform.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.multiplatform.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.multiplatform.cartesian.marker.DefaultCartesianMarker.ValueFormatter
import com.patrykandpatrick.vico.multiplatform.cartesian.marker.LineCartesianLayerMarkerTarget
import kotlin.math.absoluteValue
import kotlin.math.floor
import kotlin.math.pow

internal class NeoPoolMarkerFormatter(
    private val decimalCount: Int,
    private val decimalSeparator: String,
    private val thousandsSeparator: String,
    private val prefix: String,
    private val suffix: String,
    private val colorCode: Boolean,
) : DefaultCartesianMarker.ValueFormatter {

    companion object {
        fun default(
            decimalCount: Int = 2,
            decimalSeparator: String = ".",
            thousandsSeparator: String = "",
            prefix: String = "",
            suffix: String = "",
            colorCode: Boolean = true,
        ): ValueFormatter = NeoPoolMarkerFormatter(
            decimalCount,
            decimalSeparator,
            thousandsSeparator,
            prefix,
            suffix,
            colorCode,
        )
    }

    private fun AnnotatedString.Builder.append(target: CartesianMarker.Target) {
        if (target is LineCartesianLayerMarkerTarget) {
            target.points.forEachIndexed { index, point ->
                append("Reward: ")
                append(point.entry.y)
                append("\n")
                append("Date: ")
                append(formatTimestamp(point.entry.x.toLong()))
                if (index != target.points.lastIndex) append(", ")
            }
        } else {
            error("Unexpected `CartesianMarker.Target` implementation.")
        }
    }

    private fun AnnotatedString.Builder.append(y: Double, color: Color? = null) {
        if (colorCode && color != null) {
            withStyle(SpanStyle(color = color)) {
                append(y.format(decimalCount, decimalSeparator, thousandsSeparator, prefix, suffix))
            }
        } else {
            append(y.format(decimalCount, decimalSeparator, thousandsSeparator, prefix, suffix))
        }
    }

    override fun format(
        context: CartesianDrawingContext,
        targets: List<CartesianMarker.Target>,
    ): CharSequence = buildAnnotatedString {
        targets.forEachIndexed { index, target ->
            append(target = target)
            if (index != targets.lastIndex) append(", ")
        }
    }

    override fun equals(other: Any?): Boolean =
        this === other ||
            other is NeoPoolMarkerFormatter &&
            decimalCount == other.decimalCount && colorCode == other.colorCode

    override fun hashCode(): Int = 31 * decimalCount.hashCode() + colorCode.hashCode()
}

internal fun Double.format(
    decimalCount: Int = 2,
    decimalSeparator: String = ".",
    thousandsSeparator: String = "",
    prefix: String = "",
    suffix: String = "",
): String {
    val isNegative = this < 0
    val factor = 10.0.pow(decimalCount)
    val truncated = floor(factor * absoluteValue) / factor
    val trimmed = truncated.toString().trimEnd('0').trimEnd('.').replace(".", decimalSeparator)
    val value = if (isNegative) "−$trimmed" else trimmed
    return buildString {
        append(prefix)
        append(value.addThousandsSeparator(decimalSeparator, thousandsSeparator))
        append(suffix)
    }
}

private fun String.addThousandsSeparator(
    decimalSeparator: String,
    thousandsSeparator: String,
): String {
    val parts = split(decimalSeparator)
    val integerPart = parts[0]
    val withCommas =
        integerPart.reversed().chunked(size = 3).joinToString(thousandsSeparator).reversed()
    return buildString {
        append(withCommas)
        if (parts.size > 1) {
            append(decimalSeparator)
            append(parts[1])
        }
    }
}