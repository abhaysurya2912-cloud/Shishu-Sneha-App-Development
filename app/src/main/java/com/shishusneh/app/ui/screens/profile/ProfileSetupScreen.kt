package com.shishusneh.app.ui.screens.profile

import android.app.DatePickerDialog
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shishusneh.app.R
import com.shishusneh.app.ui.theme.*
import com.shishusneh.app.viewmodel.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(
    onProfileSaved: () -> Unit,
    viewModel: ProfileViewModel
) {
    var motherName by remember { mutableStateOf("") }
    var babyName by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("Girl") }
    var babyDob by remember { mutableStateOf<Long?>(null) }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    val defaultDobText = stringResource(R.string.tap_to_select)
    var dobDisplayText by remember { mutableStateOf(defaultDobText) }
    var isSubmitting by remember { mutableStateOf(false) }

    // ── FIX #10: per-field error strings ──
    var motherNameError by remember { mutableStateOf("") }
    var babyNameError by remember { mutableStateOf("") }
    var dobError by remember { mutableStateOf("") }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            calendar.set(year, month, day, 0, 0, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            babyDob = calendar.timeInMillis
            dobDisplayText = dateFormatter.format(calendar.time)
            dobError = "" // clear error when user picks a date
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).apply {
        datePicker.maxDate = System.currentTimeMillis()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Header icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(PeachContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ChildCare,
                    contentDescription = null,
                    tint = PeachPrimary,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.welcome_to_shishu_sneh),
                style = MaterialTheme.typography.headlineSmall,
                color = TextBrown,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.setup_profile_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = TextGrey
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Form card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = stringResource(R.string.mothers_info),
                        style = MaterialTheme.typography.titleMedium,
                        color = PeachPrimary,
                        fontWeight = FontWeight.SemiBold
                    )

                    // Mother name field with inline error
                    OutlinedTextField(
                        value = motherName,
                        onValueChange = {
                            motherName = it
                            if (it.isNotBlank()) motherNameError = ""
                        },
                        label = { Text(stringResource(R.string.your_name)) },
                        placeholder = { Text("e.g., Priya Sharma") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        isError = motherNameError.isNotEmpty(),
                        supportingText = {
                            if (motherNameError.isNotEmpty()) {
                                Text(motherNameError, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PeachPrimary,
                            focusedLabelColor = PeachPrimary,
                            unfocusedBorderColor = PeachLight
                        ),
                        singleLine = true
                    )

                    HorizontalDivider(color = PeachLight.copy(alpha = 0.5f))

                    Text(
                        text = stringResource(R.string.babys_info),
                        style = MaterialTheme.typography.titleMedium,
                        color = PeachPrimary,
                        fontWeight = FontWeight.SemiBold
                    )

                    // Baby name field with inline error
                    OutlinedTextField(
                        value = babyName,
                        onValueChange = {
                            babyName = it
                            if (it.isNotBlank()) babyNameError = ""
                        },
                        label = { Text(stringResource(R.string.babys_name)) },
                        placeholder = { Text("e.g., Aryan") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        isError = babyNameError.isNotEmpty(),
                        supportingText = {
                            if (babyNameError.isNotEmpty()) {
                                Text(babyNameError, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PeachPrimary,
                            focusedLabelColor = PeachPrimary,
                            unfocusedBorderColor = PeachLight
                        ),
                        singleLine = true
                    )

                    // Gender selection
                    Text(
                        text = stringResource(R.string.babys_gender),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextBrownLight
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        listOf("Boy" to "👦", "Girl" to "👧").forEach { (gender, emoji) ->
                            val isSelected = selectedGender == gender
                            val genderLabel = if (gender == "Boy")
                                stringResource(R.string.boy)
                            else
                                stringResource(R.string.girl)
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedGender = gender },
                                shape = RoundedCornerShape(16.dp),
                                color = if (isSelected) PeachContainer else CreamDark,
                                border = if (isSelected) BorderStroke(2.dp, PeachPrimary) else null
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = emoji, fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = genderLabel,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (isSelected) TextBrown else TextGrey,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }

                    // DOB picker with inline error
                    Text(
                        text = stringResource(R.string.date_of_birth),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextBrownLight
                    )
                    OutlinedButton(
                        onClick = { datePickerDialog.show() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (babyDob != null) TextBrown else TextGrey
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (dobError.isNotEmpty()) MaterialTheme.colorScheme.error
                            else if (babyDob != null) PeachPrimary
                            else PeachLight
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = if (dobError.isNotEmpty())
                                    MaterialTheme.colorScheme.error
                                else PeachPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = dobDisplayText,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    if (dobError.isNotEmpty()) {
                        Text(
                            text = dobError,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Save button
            Button(
                onClick = {
                    // ── FIX #10: per-field validation ──
                    var hasError = false
                    if (motherName.isBlank()) {
                        motherNameError = context.getString(R.string.error_name_required)
                        hasError = true
                    }
                    if (babyName.isBlank()) {
                        babyNameError = context.getString(R.string.error_baby_name_required)
                        hasError = true
                    }
                    if (babyDob == null) {
                        dobError = context.getString(R.string.error_dob_required)
                        hasError = true
                    }
                    if (hasError) return@Button

                    isSubmitting = true
                    viewModel.saveProfile(
                        motherName = motherName.trim(),
                        babyName = babyName.trim(),
                        babyGender = selectedGender,
                        babyDob = babyDob!!
                    )
                    onProfileSaved()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PeachPrimary,
                    contentColor = Cream
                ),
                enabled = !isSubmitting
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Cream,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = stringResource(R.string.start_journey),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
