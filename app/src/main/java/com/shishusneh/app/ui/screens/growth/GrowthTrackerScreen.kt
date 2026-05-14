package com.shishusneh.app.ui.screens.growth

import android.view.ViewGroup
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.shishusneh.app.R
import com.shishusneh.app.data.db.entities.GrowthRecord
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.GrowthViewModel
import com.shishusneh.app.viewmodel.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrowthTrackerScreen(
    growthViewModel: GrowthViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel()
) {
    val profile by profileViewModel.activeProfile.collectAsStateWithLifecycle()
    val records by growthViewModel.records.collectAsStateWithLifecycle()
    val chartData by growthViewModel.chartData.collectAsStateWithLifecycle()
    val latestRecord by growthViewModel.latestRecord.collectAsStateWithLifecycle()

    var weightInput by remember { mutableStateOf("") }
    var heightInput by remember { mutableStateOf("") }
    var showAddForm by remember { mutableStateOf(false) }
    var inputError by remember { mutableStateOf("") }

    // ── FIX #19: key on profile?.id so switching profiles works correctly ──
    LaunchedEffect(profile?.id) {
        profile?.let { growthViewModel.setProfileId(it.id) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Growth Tracker",
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextBrown,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Track ${profile?.babyName ?: "your baby"}'s growth journey",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextGrey
                )
            }
            Text("📈", fontSize = 32.sp)
        }

        // Summary cards
        if (latestRecord != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GrowthSummaryCard(
                    label = "Latest Weight",
                    value = "${latestRecord!!.weightKg} kg",
                    emoji = "⚖️",
                    modifier = Modifier.weight(1f)
                )
                GrowthSummaryCard(
                    label = "Latest Height",
                    value = "${latestRecord!!.heightCm} cm",
                    emoji = "📏",
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // ── FIX #6: show chart from 1 record (with an info note when only 1 exists) ──
        if (chartData.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Growth Chart",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextBrown,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Monthly weight & height trend",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextGrey
                    )

                    if (chartData.size == 1) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SoftBlueContainer
                        ) {
                            Text(
                                text = "ℹ️ ${stringResource(R.string.add_more_records)}",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = TextBrown
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Chart legend
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        LegendItem(color = PeachPrimary, label = "Weight (kg)")
                        LegendItem(color = SoftGreen, label = "Height (÷10 cm)")
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // ── FIX #17: explicit note about height scaling ──
                    Text(
                        text = stringResource(R.string.chart_height_note),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    GrowthLineChart(records = chartData)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Add record form
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showAddForm = !showAddForm },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Add New Record",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextBrown,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = if (showAddForm) "Collapse form" else "Expand form",
                        tint = PeachPrimary
                    )
                }

                AnimatedVisibility(visible = showAddForm) {
                    Column(
                        modifier = Modifier.padding(top = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = weightInput,
                            onValueChange = { weightInput = it; inputError = "" },
                            label = { Text("Weight (kg)") },
                            placeholder = { Text("e.g., 5.4") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PeachPrimary,
                                focusedLabelColor = PeachPrimary
                            ),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = heightInput,
                            onValueChange = { heightInput = it; inputError = "" },
                            label = { Text("Height (cm)") },
                            placeholder = { Text("e.g., 58.5") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PeachPrimary,
                                focusedLabelColor = PeachPrimary
                            ),
                            singleLine = true
                        )
                        if (inputError.isNotEmpty()) {
                            Text(
                                text = inputError,
                                color = StatusDueSoon,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Button(
                            onClick = {
                                val weight = weightInput.toFloatOrNull()
                                val height = heightInput.toFloatOrNull()
                                if (weight == null || weight <= 0f || weight > 30f) {
                                    inputError = "Enter a valid weight (0.1 - 30 kg)"
                                    return@Button
                                }
                                if (height == null || height <= 0f || height > 120f) {
                                    inputError = "Enter a valid height (1 - 120 cm)"
                                    return@Button
                                }
                                profile?.let { p ->
                                    growthViewModel.addRecord(p.id, weight, height)
                                }
                                weightInput = ""
                                heightInput = ""
                                showAddForm = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PeachPrimary,
                                contentColor = Cream
                            )
                        ) {
                            Text("Save Record", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // History list
        if (records.isNotEmpty()) {
            Text(
                text = "HISTORY",
                style = MaterialTheme.typography.labelMedium,
                color = TextGrey,
                modifier = Modifier.padding(horizontal = 20.dp),
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            records.take(10).forEach { record ->
                GrowthHistoryItem(record = record)
                Spacer(modifier = Modifier.height(8.dp))
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📊", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No records yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGrey
                    )
                    Text(
                        text = "Add your baby's first weight measurement!",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun GrowthSummaryCard(
    label: String,
    value: String,
    emoji: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardPeach),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = TextBrown,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = TextGrey
            )
        }
    }
}

@Composable
private fun LegendItem(color: androidx.compose.ui.graphics.Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp, 3.dp)
                .background(color, RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = TextGrey)
    }
}

@Composable
private fun GrowthLineChart(records: List<GrowthRecord>) {
    val peachColor = PeachPrimary.toArgb()
    val greenColor = SoftGreen.toArgb()
    val textColor  = TextGrey.toArgb()
    val gridColor  = CreamDark.toArgb()

    val dateFormatter = remember { SimpleDateFormat("MMM d", Locale.getDefault()) }
    val labels = remember(records) {
        records.map { dateFormatter.format(Date(it.recordedAt)) }
    }

    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    600
                )
                setBackgroundColor(android.graphics.Color.TRANSPARENT)
                description.isEnabled = false
                legend.isEnabled = false
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(records.size > 1) // only allow zoom when meaningful
                setPinchZoom(records.size > 1)
                setDrawGridBackground(false)

                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    setDrawGridLines(false)
                    this.textColor = textColor
                    textSize = 10f
                    valueFormatter = IndexAxisValueFormatter(labels)
                }
                axisLeft.apply {
                    this.textColor = textColor
                    this.gridColor = gridColor
                    textSize = 10f
                    axisMinimum = 0f
                }
                axisRight.isEnabled = false
            }
        },
        update = { chart ->
            val weightEntries = records.mapIndexed { i, r -> Entry(i.toFloat(), r.weightKg) }
            // Height divided by 10 to share the Y-axis with weight; labelled in legend
            val heightEntries = records.mapIndexed { i, r -> Entry(i.toFloat(), r.heightCm / 10f) }

            val weightDataSet = LineDataSet(weightEntries, "Weight").apply {
                color = peachColor
                setCircleColor(peachColor)
                lineWidth = 2.5f
                circleRadius = 4f
                setDrawValues(true)
                valueTextSize = 9f
                valueTextColor = peachColor
                mode = LineDataSet.Mode.CUBIC_BEZIER
                setDrawFilled(true)
                fillColor = peachColor
                fillAlpha = 30
            }

            val heightDataSet = LineDataSet(heightEntries, "Height ÷10").apply {
                color = greenColor
                setCircleColor(greenColor)
                lineWidth = 2.5f
                circleRadius = 4f
                setDrawValues(true)
                valueTextSize = 9f
                valueTextColor = greenColor
                mode = LineDataSet.Mode.CUBIC_BEZIER
                setDrawFilled(true)
                fillColor = greenColor
                fillAlpha = 20
            }

            chart.data = LineData(weightDataSet, heightDataSet)
            chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            chart.notifyDataSetChanged()
            chart.invalidate()
            if (records.size > 1) chart.animateX(1000)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

@Composable
private fun GrowthHistoryItem(record: GrowthRecord) {
    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = dateFormatter.format(Date(record.recordedAt)),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextGrey
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "⚖️ ${record.weightKg} kg",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextBrown,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "📏 ${record.heightCm} cm",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextBrown,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
