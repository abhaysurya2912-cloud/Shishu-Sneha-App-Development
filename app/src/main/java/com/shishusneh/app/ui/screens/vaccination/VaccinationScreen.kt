package com.shishusneh.app.ui.screens.vaccination

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shishusneh.app.R
import com.shishusneh.app.domain.VaccineScheduleItem
import com.shishusneh.app.domain.VaccineStatus
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.VaccinationViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun VaccinationScreen(
    viewModel: VaccinationViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PeachPrimary)
        }
        return
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
                    text = "Vaccine Schedule",
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextBrown,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${uiState.profile?.babyName ?: "Baby"}'s immunization plan",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextGrey
                )
            }
            Text(
                "💉",
                fontSize = 32.sp,
                modifier = Modifier.semantics { contentDescription = "Vaccination syringe" }
            )
        }

        // Progress card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Vaccination Progress",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextBrown,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${uiState.doneCount} of ${uiState.totalCount} completed",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextGrey
                        )
                        if (uiState.dueSoonCount > 0) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = StatusDueSoon.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = "⚠️ ${uiState.dueSoonCount} vaccine${if (uiState.dueSoonCount > 1) "s" else ""} due soon",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = StatusDueSoon,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                    Text(
                        text = "${if (uiState.totalCount > 0) (uiState.doneCount * 100 / uiState.totalCount) else 0}%",
                        style = MaterialTheme.typography.titleLarge,
                        color = PeachPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = {
                        if (uiState.totalCount > 0)
                            uiState.doneCount.toFloat() / uiState.totalCount
                        else 0f
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = StatusDone,
                    trackColor = CreamDark
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Legend
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            listOf(
                Triple(StatusDone, "Done", "Done status"),
                Triple(StatusDueSoon, "Due Soon", "Due soon status"),
                Triple(StatusUpcoming, "Upcoming", "Upcoming status"),
                Triple(StatusMissed, "Missed", "Missed status")
            ).forEach { (color, label, description) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(color)
                            .semantics { contentDescription = description }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextGrey
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Vaccine list
        uiState.schedule.forEach { item ->
            VaccineCard(
                item = item,
                onToggleDone = { vaccineId, currentlyDone ->
                    uiState.profile?.id?.let { profileId ->
                        viewModel.toggleDone(vaccineId, profileId, currentlyDone)
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (uiState.schedule.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("💉", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Set up your baby's profile first",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGrey
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun VaccineCard(
    item: VaccineScheduleItem,
    onToggleDone: (String, Boolean) -> Unit
) {
    val dateFormatter = remember { SimpleDateFormat("d MMM yyyy", Locale.getDefault()) }
    val isDone = item.status == VaccineStatus.DONE
    val dotColor = when (item.status) {
        VaccineStatus.DONE     -> StatusDone
        VaccineStatus.DUE_SOON -> StatusDueSoon
        VaccineStatus.UPCOMING -> StatusUpcoming
        VaccineStatus.MISSED   -> StatusMissed
    }
    val bgColor = when (item.status) {
        VaccineStatus.DONE     -> SoftGreenContainer
        VaccineStatus.DUE_SOON -> Color(0xFFFFEBEE)
        VaccineStatus.UPCOMING -> MaterialTheme.colorScheme.surface
        VaccineStatus.MISSED   -> Color(0xFFF5F5F5)
    }
    val statusText = when (item.status) {
        VaccineStatus.DONE     -> "Completed ✓"
        VaccineStatus.DUE_SOON -> if (item.daysDiff <= 0) "Due TODAY!" else "Due in ${item.daysDiff} days"
        VaccineStatus.UPCOMING -> dateFormatter.format(Date(item.dueDate))
        VaccineStatus.MISSED   -> "Overdue — see doctor"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(dotColor)
                        .semantics { contentDescription = "Status: ${item.status.name}" }
                )
                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.vaccine.name,
                        style = MaterialTheme.typography.titleSmall,
                        color = TextBrown,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "🛡️ Prevents: ${item.vaccine.diseasePrevented}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextBrownLight
                    )
                    if (item.vaccine.notes.isNotEmpty()) {
                        Text(
                            text = item.vaccine.notes,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextGrey
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = dotColor.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = statusText,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = dotColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── FIX #8: Distinct button for MISSED status ──
            when (item.status) {
                VaccineStatus.MISSED -> {
                    OutlinedButton(
                        onClick = { onToggleDone(item.vaccine.id, false) },
                        modifier = Modifier.align(Alignment.End),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = StatusMissed
                        ),
                        border = BorderStroke(1.dp, StatusMissed)
                    ) {
                        Text(
                            text = stringResource(R.string.mark_administered_late),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                VaccineStatus.DUE_SOON, VaccineStatus.DONE -> {
                    OutlinedButton(
                        onClick = { onToggleDone(item.vaccine.id, isDone) },
                        modifier = Modifier.align(Alignment.End),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (isDone) TextGrey else StatusDone
                        ),
                        border = BorderStroke(1.dp, if (isDone) CreamDark else StatusDone)
                    ) {
                        Text(
                            text = if (isDone)
                                stringResource(R.string.mark_undone)
                            else
                                stringResource(R.string.mark_done),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                VaccineStatus.UPCOMING -> { /* no action button for future vaccines */ }
            }
        }
    }
}
