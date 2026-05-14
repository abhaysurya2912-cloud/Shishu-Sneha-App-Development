package com.shishusneh.app.ui.screens.milestones

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shishusneh.app.domain.MilestoneCategory
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.MilestoneUiItem
import com.shishusneh.app.viewmodel.MilestoneViewModel
import com.shishusneh.app.viewmodel.ProfileViewModel
import java.util.concurrent.TimeUnit

@Composable
fun MilestoneScreen(
    milestoneViewModel: MilestoneViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel()
) {
    val profile by profileViewModel.activeProfile.collectAsStateWithLifecycle()
    val milestoneGroups by milestoneViewModel.milestoneUiItems.collectAsStateWithLifecycle()
    val achievedCount by milestoneViewModel.achievedCount.collectAsStateWithLifecycle()

    val babyAgeWeeks = profile?.let {
        TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - it.babyDob).toInt() / 7
    } ?: 0

    LaunchedEffect(profile) {
        profile?.let { milestoneViewModel.setProfile(it.id, babyAgeWeeks) }
    }

    val totalMilestones = milestoneGroups.values.sumOf { it.size }

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
                    text = "Milestones",
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextBrown,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${profile?.babyName ?: "Baby"}'s developmental journey",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextGrey
                )
            }
            Text(
                "⭐",
                fontSize = 32.sp,
                modifier = Modifier.semantics { contentDescription = "Star milestone icon" }
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
            Row(
                modifier = Modifier.padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Milestones Achieved",
                        style = MaterialTheme.typography.titleSmall,
                        color = TextBrown,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "$achievedCount of $totalMilestones checked",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextGrey
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = {
                            if (totalMilestones > 0) achievedCount.toFloat() / totalMilestones else 0f
                        },
                        modifier = Modifier
                            .width(200.dp)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = StatusDone,
                        trackColor = CreamDark
                    )
                }
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(SoftGreenContainer)
                        .semantics { contentDescription = "Trophy icon" },
                    contentAlignment = Alignment.Center
                ) {
                    Text("🏆", fontSize = 28.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── FIX #5: LazyRow of FilterChips instead of misused ScrollableTabRow ──
        val categories = MilestoneCategory.entries.toTypedArray()
        var selectedCategory by remember { mutableStateOf<MilestoneCategory?>(null) }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = { selectedCategory = null },
                    label = { Text("All") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PeachContainer,
                        selectedLabelColor = TextBrown
                    )
                )
            }
            items(categories) { cat ->
                FilterChip(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = if (selectedCategory == cat) null else cat },
                    label = { Text("${cat.emoji} ${cat.displayName}") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PeachContainer,
                        selectedLabelColor = TextBrown
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Milestone groups
        milestoneGroups.entries.sortedBy { it.key }.forEach { (weekGroup, items) ->
            val filteredItems = if (selectedCategory != null) {
                items.filter { it.milestone.category == selectedCategory }
            } else items

            if (filteredItems.isEmpty()) return@forEach

            MilestoneGroupSection(
                weekGroup = weekGroup,
                items = filteredItems,
                onToggle = { milestoneId, achieved ->
                    milestoneViewModel.updateMilestone(milestoneId, achieved)
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (milestoneGroups.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("👶", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Milestones will appear as your baby grows!",
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
private fun MilestoneGroupSection(
    weekGroup: Int,
    items: List<MilestoneUiItem>,
    onToggle: (String, Boolean) -> Unit
) {
    val monthLabel = when {
        weekGroup == 0  -> "Birth"
        weekGroup <= 4  -> "1 Month"
        weekGroup <= 8  -> "2 Months"
        weekGroup <= 12 -> "3 Months"
        weekGroup <= 16 -> "4 Months"
        weekGroup <= 24 -> "6 Months"
        weekGroup <= 36 -> "9 Months"
        weekGroup <= 52 -> "12 Months"
        else            -> "$weekGroup Weeks"
    }

    val achievedInGroup = items.count { it.record?.isAchieved == true }

    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(PeachContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$weekGroup",
                        style = MaterialTheme.typography.labelSmall,
                        color = PeachPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = monthLabel,
                    style = MaterialTheme.typography.titleSmall,
                    color = TextBrown,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Text(
                text = "$achievedInGroup/${items.size}",
                style = MaterialTheme.typography.bodySmall,
                color = if (achievedInGroup == items.size) StatusDone else TextGrey,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        items.forEach { uiItem ->
            MilestoneItemCard(
                uiItem = uiItem,
                onToggle = { achieved -> onToggle(uiItem.milestone.id, achieved) }
            )
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
private fun MilestoneItemCard(
    uiItem: MilestoneUiItem,
    onToggle: (Boolean) -> Unit
) {
    val isAchieved = uiItem.record?.isAchieved ?: false
    val bgColor = if (isAchieved) SoftGreenContainer else MaterialTheme.colorScheme.surface

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = uiItem.milestone.category.emoji,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(top = 2.dp)
                    .semantics { contentDescription = uiItem.milestone.category.displayName }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = uiItem.milestone.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = TextBrown,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = uiItem.milestone.question,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextBrownLight
                )
                if (uiItem.milestone.tip.isNotEmpty() && !isAchieved) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SoftYellowContainer
                    ) {
                        Text(
                            text = "💡 ${uiItem.milestone.tip}",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = TextBrown
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // ── FIX A2: use proper Button for adequate touch target ──
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                SmallButton(
                    text = "Yes ✓",
                    isSelected = isAchieved,
                    selectedColor = StatusDone,
                    onClick = { onToggle(true) }
                )
                SmallButton(
                    text = "Not yet",
                    isSelected = !isAchieved && uiItem.record != null,
                    selectedColor = PeachLight,
                    onClick = { onToggle(false) }
                )
            }
        }
    }
}

@Composable
private fun SmallButton(
    text: String,
    isSelected: Boolean,
    selectedColor: Color,
    onClick: () -> Unit
) {
    // Using OutlinedButton for proper 48dp touch-target and accessibility role
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .width(88.dp)
            .height(36.dp),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) selectedColor.copy(alpha = 0.15f) else Color.Transparent,
            contentColor = if (isSelected) selectedColor else TextGrey
        ),
        border = BorderStroke(
            1.dp,
            if (isSelected) selectedColor else CreamDark
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            maxLines = 1
        )
    }
}
