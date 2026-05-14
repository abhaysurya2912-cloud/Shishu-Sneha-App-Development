package com.shishusneh.app.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shishusneh.app.R
import com.shishusneh.app.data.db.entities.MotherProfile
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.ProfileViewModel
import java.util.concurrent.TimeUnit
import androidx.compose.material3.MaterialTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BabyProfileListScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAddProfile: () -> Unit,
    onProfileSelected: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val profiles by viewModel.allProfiles.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.baby_profiles)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Cream,
                    titleContentColor = TextBrown
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddProfile,
                containerColor = PeachPrimary,
                contentColor = Cream,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_baby))
            }
        },
        containerColor = Cream
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(
                text = stringResource(R.string.manage_profiles),
                style = MaterialTheme.typography.bodyMedium,
                color = TextGrey,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(profiles) { profile ->
                    ProfileCard(
                        profile = profile,
                        onSwitch = {
                            viewModel.setActiveProfile(profile.id)
                            onProfileSelected()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileCard(
    profile: MotherProfile,
    onSwitch: () -> Unit
) {
    val ageDays = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - profile.babyDob).toInt()
    val ageMonths = ageDays / 30

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (profile.isActive) SoftGreenContainer else CardWhite
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(if (profile.babyGender == "Boy") SoftBlueContainer else PeachContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (profile.babyGender == "Boy") "👦" else "👧",
                    fontSize = 28.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = profile.babyName,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextBrown,
                        fontWeight = FontWeight.Bold
                    )
                    if (profile.isActive) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = StatusDone,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Text(
                    // FIX #11: use dedicated format string instead of locale-unsafe .split(" ")[1]
                    text = if (ageMonths > 0)
                        stringResource(R.string.months_old_short, ageMonths)
                    else
                        stringResource(R.string.newborn),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextGrey
                )
                Text(
                    text = "Mom: ${profile.motherName}",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }

            if (!profile.isActive) {
                Button(
                    onClick = onSwitch,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PeachPrimary,
                        contentColor = Cream
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(stringResource(R.string.switch_baby), fontSize = 12.sp)
                }
            } else {
                Text(
                    text = stringResource(R.string.active),
                    style = MaterialTheme.typography.labelMedium,
                    color = StatusDone,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
