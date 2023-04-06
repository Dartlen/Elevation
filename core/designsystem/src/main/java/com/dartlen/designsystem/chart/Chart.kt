package com.dartlen.designsystem.chart

import android.graphics.Typeface
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dartlen.model.LatLng
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.compose.component.overlayingComponent
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.DefaultAlpha
import com.patrykandpatrick.vico.core.DefaultColors
import com.patrykandpatrick.vico.core.DefaultDimens
import com.patrykandpatrick.vico.core.chart.insets.Insets
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.chart.segment.SegmentProperties
import com.patrykandpatrick.vico.core.component.marker.MarkerComponent
import com.patrykandpatrick.vico.core.component.shape.DashedShape
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.cornered.Corner
import com.patrykandpatrick.vico.core.component.shape.cornered.MarkerCorneredShape
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.context.MeasureContext
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.extension.copyColor
import com.patrykandpatrick.vico.core.marker.Marker
import com.patrykandpatrick.vico.core.util.RandomEntriesGenerator

val LABEL_BACKGROUND_SHADOW_RADIUS = 4f
val LABEL_BACKGROUND_SHADOW_DY = 2f
val LABEL_LINE_COUNT = 1
val GUIDELINE_ALPHA = .2f
val INDICATOR_SIZE_DP = 36f
val INDICATOR_OUTER_COMPONENT_ALPHA = 32
val INDICATOR_CENTER_COMPONENT_SHADOW_RADIUS = 12f
val GUIDELINE_DASH_LENGTH_DP = 8f
val GUIDELINE_GAP_LENGTH_DP = 4f
val SHADOW_RADIUS_MULTIPLIER = 1.3f

val labelBackgroundShape = MarkerCorneredShape(Corner.FullyRounded)
val labelHorizontalPaddingValue = 8.dp
val labelVerticalPaddingValue = 4.dp
val labelPadding = dimensionsOf(labelHorizontalPaddingValue, labelVerticalPaddingValue)
val indicatorInnerAndCenterComponentPaddingValue = 5.dp
val indicatorCenterAndOuterComponentPaddingValue = 10.dp
val guidelineThickness = 2.dp
val guidelineShape =
    DashedShape(Shapes.pillShape, GUIDELINE_DASH_LENGTH_DP, GUIDELINE_GAP_LENGTH_DP)

@Composable
internal fun rememberMarker(): Marker {
    val labelBackgroundColor = MaterialTheme.colorScheme.surface
    val labelBackground = remember(labelBackgroundColor) {
        ShapeComponent(labelBackgroundShape, labelBackgroundColor.toArgb()).setShadow(
            radius = LABEL_BACKGROUND_SHADOW_RADIUS,
            dy = LABEL_BACKGROUND_SHADOW_DY,
            applyElevationOverlay = true,
        )
    }
    val label = textComponent(
        background = labelBackground,
        lineCount = LABEL_LINE_COUNT,
        padding = labelPadding,
        typeface = Typeface.MONOSPACE,
    )
    val indicatorInnerComponent =
        shapeComponent(Shapes.pillShape, MaterialTheme.colorScheme.surface)
    val indicatorCenterComponent = shapeComponent(Shapes.pillShape, Color.White)
    val indicatorOuterComponent = shapeComponent(Shapes.pillShape, Color.White)
    val indicator = overlayingComponent(
        outer = indicatorOuterComponent,
        inner = overlayingComponent(
            outer = indicatorCenterComponent,
            inner = indicatorInnerComponent,
            innerPaddingAll = indicatorInnerAndCenterComponentPaddingValue,
        ),
        innerPaddingAll = indicatorCenterAndOuterComponentPaddingValue,
    )
    val guideline = lineComponent(
        MaterialTheme.colorScheme.onSurface.copy(GUIDELINE_ALPHA),
        guidelineThickness,
        guidelineShape,
    )
    return remember(label, indicator, guideline) {
        object : MarkerComponent(label, indicator, guideline) {
            init {
                indicatorSizeDp = INDICATOR_SIZE_DP
                onApplyEntryColor = { entryColor ->
                    indicatorOuterComponent.color =
                        entryColor.copyColor(INDICATOR_OUTER_COMPONENT_ALPHA)
                    with(indicatorCenterComponent) {
                        color = entryColor
                        setShadow(
                            radius = INDICATOR_CENTER_COMPONENT_SHADOW_RADIUS,
                            color = entryColor
                        )
                    }
                }
            }

            override fun getInsets(
                context: MeasureContext,
                outInsets: Insets,
                segmentProperties: SegmentProperties
            ) =
                with(context) {
                    outInsets.top =
                        label.getHeight(context) + labelBackgroundShape.tickSizeDp.pixels +
                                LABEL_BACKGROUND_SHADOW_RADIUS.pixels * SHADOW_RADIUS_MULTIPLIER -
                                LABEL_BACKGROUND_SHADOW_DY.pixels
                }
        }
    }
}


@Composable
internal fun rememberChartStyle(
    columnChartColors: List<Color>,
    lineChartColors: List<Color>
): ChartStyle {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    return remember(columnChartColors, lineChartColors, isSystemInDarkTheme) {
        val defaultColors = if (isSystemInDarkTheme) DefaultColors.Dark else DefaultColors.Light
        ChartStyle(
            ChartStyle.Axis(
                axisLabelColor = Color(defaultColors.axisLabelColor),
                axisGuidelineColor = Color(defaultColors.axisGuidelineColor),
                axisLineColor = Color(defaultColors.axisLineColor),
            ),
            ChartStyle.ColumnChart(
                columnChartColors.map { columnChartColor ->
                    LineComponent(
                        columnChartColor.toArgb(),
                        DefaultDimens.COLUMN_WIDTH,
                        Shapes.roundedCornerShape(DefaultDimens.COLUMN_ROUNDNESS_PERCENT),
                    )
                },
            ),
            ChartStyle.LineChart(
                lineChartColors.map { lineChartColor ->
                    LineChart.LineSpec(
                        lineColor = lineChartColor.toArgb(),
                        lineBackgroundShader = DynamicShaders.fromBrush(
                            Brush.verticalGradient(
                                listOf(
                                    lineChartColor.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_START),
                                    lineChartColor.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_END),
                                ),
                            ),
                        ),
                    )
                },
            ),
            ChartStyle.Marker(),
            Color(defaultColors.elevationOverlayColor),
        )
    }
}

@Composable
internal fun rememberChartStyle(chartColors: List<Color>) =
    rememberChartStyle(columnChartColors = chartColors, lineChartColors = chartColors)


@Composable
fun previewInformation(modifier: Modifier, painter: Painter, title: String, value: String) {
    Card(modifier = modifier.padding(8.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(painter = painter, contentDescription = "", Modifier.size(50.dp))
            Text(modifier = Modifier.padding(top = 16.dp), text = title)
            Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
    }
}

private val customStepGenerator = RandomEntriesGenerator(
    xRange = IntProgression.fromClosedRange(rangeStart = 0, rangeEnd = 40, step = 2),
    yRange = 2..20,
)

@Composable
fun chart(path: MutableList<MutableList<LatLng>>) {
    val COLOR_1_CODE = 0xffffbb00
    val COLOR_2_CODE = 0xff9db591
    val color1: Color = Color(COLOR_1_CODE)
    val color2 = Color(COLOR_2_CODE)
    val chartColors = listOf(color1, color2)
    val chartEntryModelProducer: ChartEntryModelProducer = ChartEntryModelProducer()
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(220.dp)
            .fillMaxWidth(),
    ) {
        chartEntryModelProducer.setEntries(zipChart(path.firstOrNull()?.mapToChartEntry() ?: listOf()))
        val marker = rememberMarker()
        ProvideChartStyle(rememberChartStyle(chartColors)) {
            Chart(
                chart = lineChart(persistentMarkers = remember(marker) { mapOf(10f to marker) }),
                chartModelProducer = chartEntryModelProducer,
                marker = marker,
            )
        }
    }
}

private fun MutableList<LatLng>.mapToChartEntry(): List<FloatEntry> {
    return mapIndexed { index, latLng ->  FloatEntry(latLng.elevation.toFloat(), index.toFloat()) }
}

fun zipChart(entries: List<FloatEntry>): List<FloatEntry> {
    val zippedEntries = mutableListOf<FloatEntry>()
    val zipSize = entries.size / 10
    var count = 0
    var sumY = 0.0
    var number = 0
    //var sumY = 0.0
    for (i in entries.indices){
        if(count<zipSize){
            count++
            sumY += entries[i].x
            //sumY += entries[i].y
        }else{
            number++
            zippedEntries.add(FloatEntry(number.toFloat(), (sumY/zipSize).toFloat()))
            count = 0
            sumY = 0.0
            //sumY = 0.0
        }
    }
    return zippedEntries
}
