package com.shishusneh.app.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shishusneh.app.R
import com.shishusneh.app.domain.VaccineStatus
import com.shishusneh.app.ui.navigation.Screen
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.HomeViewModel
import com.shishusneh.app.viewmodel.MilestoneViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel(),
    milestoneViewModel: MilestoneViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Refreshes every minute so the greeting updates when the clock crosses noon/5pm
    var currentHour by remember { mutableIntStateOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) }
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(60_000L)
            currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        }
    }
    val timeGreeting = when {
        currentHour < 12 -> stringResource(R.string.good_morning)
        currentHour < 17 -> stringResource(R.string.good_afternoon)
        else             -> stringResource(R.string.good_evening)
    }

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

    val profile = uiState.profile
    val babyName  = profile?.babyName ?: stringResource(R.string.nav_home)
    val motherName = profile?.motherName ?: ""
    val ageWeeks  = uiState.babyAgeWeeks
    val ageMonths = ageWeeks / 4
    val extraDays = uiState.babyAgeDays % 30

    // Set milestone context whenever profile is available
    LaunchedEffect(profile?.id, ageWeeks) {
        profile?.let { milestoneViewModel.setProfile(it.id, ageWeeks) }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            // ─── Top bar ─────────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "$timeGreeting, $motherName",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGrey
                    )
                    Text(
                        text = stringResource(R.string.is_weeks_old, babyName, ageWeeks),
                        style = MaterialTheme.typography.titleLarge,
                        color = TextBrown,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.navigate(Screen.BabyProfiles.route) }) {
                        Icon(
                            Icons.Default.SwitchAccount,
                            contentDescription = "Switch Profile",
                            tint = TextBrown
                        )
                    }
                    IconButton(onClick = { navController.navigate(Screen.LanguageSettings.route) }) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Language Settings",
                            tint = TextBrown
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(PeachContainer)
                            .semantics { contentDescription = "Baby profile icon" },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👶", fontSize = 18.sp)
                    }
                }
            }
    
            // ─── Hero card — Baby Age ─────────────────────────────────────────────
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(PeachPrimary, PeachGolden)
                            ),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.babys_age),
                                style = MaterialTheme.typography.bodySmall,
                                color = Cream.copy(alpha = 0.8f)
                            )
                            Text(
                                text = stringResource(R.string.weeks_short, ageWeeks),
                                style = MaterialTheme.typography.displaySmall,
                                color = Cream,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (ageMonths > 0)
                                    stringResource(R.string.months_days, ageMonths, extraDays)
                                else
                                    stringResource(R.string.days_old, uiState.babyAgeDays),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Cream.copy(alpha = 0.9f)
                            )
                        }
                        Text(
                            "🌱",
                            fontSize = 52.sp,
                            modifier = Modifier.semantics { contentDescription = "Seedling plant" }
                        )
                    }
                }
            }
    
            Spacer(modifier = Modifier.height(16.dp))
    
            // ─── Weight + Vaccine mini-cards ──────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Weight card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { navController.navigate(Screen.Growth.route) },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("⚖️", fontSize = 24.sp, modifier = Modifier.semantics { contentDescription = "Scale" })
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.last_weight),
                            style = MaterialTheme.typography.bodySmall,
                            color = TextGrey
                        )
                        Text(
                            text = uiState.latestWeight?.let { "${it.weightKg} kg" } ?: "—",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextBrown,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (uiState.latestWeight != null)
                                stringResource(R.string.healthy_range)
                            else
                                stringResource(R.string.no_data_yet),
                            style = MaterialTheme.typography.bodySmall,
                            color = SoftGreen
                        )
                    }
                }
    
                // Next vaccine card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { navController.navigate(Screen.Vaccination.route) },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("💉", fontSize = 24.sp, modifier = Modifier.semantics { contentDescription = "Syringe" })
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.next_vaccine),
                            style = MaterialTheme.typography.bodySmall,
                            color = TextGrey
                        )
                        // ── FIX #4: removed .take(8); use maxLines + ellipsis ──
                        Text(
                            text = uiState.nextVaccine?.vaccine?.name
                                ?: stringResource(R.string.up_to_date),
                            style = MaterialTheme.typography.titleMedium,
                            color = TextBrown,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        val daysText = uiState.nextVaccine?.let { item ->
                            when {
                                item.daysDiff <= 0 -> stringResource(R.string.due_today_home)
                                item.daysDiff == 1L -> stringResource(R.string.tomorrow)
                                else -> stringResource(R.string.in_days, item.daysDiff)
                            }
                        } ?: stringResource(R.string.all_done)
                        Text(
                            text = daysText,
                            style = MaterialTheme.typography.bodySmall,
                            color = when {
                                uiState.nextVaccine?.status == VaccineStatus.DUE_SOON -> StatusDueSoon
                                else -> TextGrey
                            }
                        )
                    }
                }
            }
    
            Spacer(modifier = Modifier.height(20.dp))
    
            // ─── Vaccination Schedule preview ─────────────────────────────────────
            Text(
                text = stringResource(R.string.vaccination_schedule),
                style = MaterialTheme.typography.labelMedium,
                color = TextGrey,
                modifier = Modifier.padding(horizontal = 20.dp),
                letterSpacing = 1.5.sp
            )
    
            Spacer(modifier = Modifier.height(8.dp))
    
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clickable { navController.navigate(Screen.Vaccination.route) },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.upcoming_vaccines),
                            style = MaterialTheme.typography.titleSmall,
                            color = TextBrown,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (uiState.nextVaccine?.status == VaccineStatus.DUE_SOON) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = StatusDueSoon.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = stringResource(R.string.due_soon_count, 1),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = StatusDueSoon
                                )
                            }
                        }
                    }
    
                    Spacer(modifier = Modifier.height(12.dp))
    
                    if (profile != null) {
                        val schedulePreview = com.shishusneh.app.domain.VaccineSchedule
                            .getSchedule(profile.babyDob).take(4)
                        schedulePreview.forEach { item ->
                            VaccinePreviewRow(item = item)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
    
            Spacer(modifier = Modifier.height(20.dp))
    
            // ─── Today's Milestone ────────────────────────────────────────────────
            Text(
                text = stringResource(R.string.todays_milestone),
                style = MaterialTheme.typography.labelMedium,
                color = TextGrey,
                modifier = Modifier.padding(horizontal = 20.dp),
                letterSpacing = 1.5.sp
            )
    
            Spacer(modifier = Modifier.height(8.dp))
    
            val todaysMilestone = remember(ageWeeks) {
                com.shishusneh.app.domain.MilestoneData.getTodaysMilestone(ageWeeks)
            }
    
            if (todaysMilestone != null) {
                // ── FIX #16: milestoneAnswer persisted to Room via MilestoneViewModel ──
                val milestoneDbRecords by milestoneViewModel.dbRecords.collectAsStateWithLifecycle()
                val savedAnswer = milestoneDbRecords
                    .find { it.milestoneId == todaysMilestone.id }?.isAchieved
    
                var milestoneAnswer by remember(todaysMilestone.id) {
                    mutableStateOf(savedAnswer)
                }
    
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SoftGreenContainer),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("⭐", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.week_check, ageWeeks),
                                style = MaterialTheme.typography.titleSmall,
                                color = TextBrown,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = todaysMilestone.question,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextBrownLight
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedButton(
                                onClick = {
                                    milestoneAnswer = true
                                    milestoneViewModel.updateMilestone(todaysMilestone.id, true)
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (milestoneAnswer == true) SoftGreen else Color.Transparent,
                                    contentColor = if (milestoneAnswer == true) CardWhite else TextBrown
                                ),
                                border = BorderStroke(1.5.dp, SoftGreen)
                            ) {
                                Text(stringResource(R.string.yes_check))
                            }
                            OutlinedButton(
                                onClick = {
                                    milestoneAnswer = false
                                    milestoneViewModel.updateMilestone(todaysMilestone.id, false)
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (milestoneAnswer == false) PeachLight else Color.Transparent,
                                    contentColor = TextBrown
                                ),
                                border = BorderStroke(1.5.dp, PeachLight)
                            ) {
                                Text(stringResource(R.string.not_yet))
                            }
                        }
                    }
                }
            }
    
            Spacer(modifier = Modifier.height(20.dp))
    
            // ─── Add weight CTA ───────────────────────────────────────────────────
            Button(
                onClick = { navController.navigate(Screen.Growth.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PeachPrimary,
                    contentColor = Cream
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.add_weight),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
    
            Spacer(modifier = Modifier.height(100.dp)) // Extra padding for FAB
        }
    }
}

@Composable
private fun VaccinePreviewRow(
    item: com.shishusneh.app.domain.VaccineScheduleItem
) {
    val dateFormatter = remember { SimpleDateFormat("d MMM yyyy", Locale.getDefault()) }
    val dotColor = when (item.status) {
        VaccineStatus.DONE     -> StatusDone
        VaccineStatus.DUE_SOON -> StatusDueSoon
        VaccineStatus.UPCOMING -> StatusUpcoming
        VaccineStatus.MISSED   -> StatusMissed
    }
    val statusText = when (item.status) {
        VaccineStatus.DONE     -> "Done ✓"
        VaccineStatus.DUE_SOON -> if (item.daysDiff <= 0) "Due today!" else "In ${item.daysDiff} days"
        VaccineStatus.UPCOMING -> dateFormatter.format(Date(item.dueDate))
        VaccineStatus.MISSED   -> "Missed"
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(dotColor)
                    .semantics { contentDescription = "Status: ${item.status.name}" }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = item.vaccine.name,
                style = MaterialTheme.typography.bodySmall,
                color = TextBrown
            )
        }
        Text(
            text = statusText,
            style = MaterialTheme.typography.bodySmall,
            color = dotColor,
            fontWeight = if (item.status == VaccineStatus.DUE_SOON) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}
