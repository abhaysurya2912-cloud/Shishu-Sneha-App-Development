package com.shishusneh.app.ui.screens.feeding

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shishusneh.app.R
import com.shishusneh.app.domain.FeedingData
import com.shishusneh.app.domain.FeedingTip
import com.shishusneh.app.domain.TipCategory
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.ProfileViewModel
import java.util.concurrent.TimeUnit

/** Maps the domain enum to a Compose Color — keeps Color out of the domain layer. */
@Composable
private fun TipCategory.toBackgroundColor(): Color = when (this) {
    TipCategory.PEACH      -> PeachContainer
    TipCategory.GREEN      -> SoftGreenContainer
    TipCategory.PURPLE     -> SoftPurpleContainer
    TipCategory.YELLOW     -> SoftYellowContainer
    TipCategory.BLUE       -> SoftBlueContainer
    TipCategory.WARNING    -> Color(0xFFFFEBEE)
    TipCategory.CARD_PEACH -> CardPeach
}

@Composable
fun FeedingGuideScreen(
    profileViewModel: ProfileViewModel = viewModel()
) {
    val profile by profileViewModel.activeProfile.collectAsStateWithLifecycle()
    val babyAgeDays = profile?.let {
        TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - it.babyDob).toInt()
    } ?: 0

    val relevantTips = remember(babyAgeDays) {
        FeedingData.getTipsForAge(babyAgeDays)
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
                    text = stringResource(R.string.feeding_guide),
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextBrown,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.nutrition_tips),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextGrey
                )
            }
            Text("🍼", fontSize = 32.sp)
        }

        // Age badge
        Card(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .wrapContentWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = PeachContainer),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("📅", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(6.dp))
                val months = babyAgeDays / 30
                val ageText = if (months > 0) stringResource(R.string.months_old, months)
                              else stringResource(R.string.newborn)
                Text(
                    text = stringResource(R.string.showing_tips, ageText),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextBrown,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Age-relevant tips
        relevantTips.forEach { tip ->
            FeedingTipCard(tip = tip)
            Spacer(modifier = Modifier.height(12.dp))
        }

        // General tips always shown
        Text(
            text = stringResource(R.string.general_guidance),
            style = MaterialTheme.typography.labelMedium,
            color = TextGrey,
            modifier = Modifier.padding(horizontal = 20.dp),
            letterSpacing = 1.5.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                listOf(
                    R.string.gen_tip_doctor,
                    R.string.gen_tip_force_feed,
                    R.string.gen_tip_wash_hands,
                    R.string.gen_tip_diary,
                    R.string.gen_tip_instincts
                ).forEach { tipRes ->
                    Text(
                        text = stringResource(tipRes),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextBrownLight,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun FeedingTipCard(tip: FeedingTip) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = tip.category.toBackgroundColor()),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(tip.emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = stringResource(tip.titleRes),
                    style = MaterialTheme.typography.titleSmall,
                    color = TextBrown,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(tip.descriptionRes),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextBrownLight,
                    lineHeight = 20.sp
                )
            }
        }
    }
}
