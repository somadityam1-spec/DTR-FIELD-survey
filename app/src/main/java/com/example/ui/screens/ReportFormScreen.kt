package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ElectricMeter
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.components.MultiOptionSelector
import com.example.ui.components.PhaseCurrentInput
import com.example.ui.components.SectionHeader
import com.example.ui.components.SingleOptionSelector
import com.example.ui.theme.BackgroundWarmLinen
import com.example.ui.theme.NaturalGreenPrimary
import com.example.ui.theme.NaturalGreenPrimaryDark
import com.example.ui.theme.OutlineLight
import com.example.ui.theme.PhaseBlue
import com.example.ui.theme.PhaseNeutral
import com.example.ui.theme.PhaseRed
import com.example.ui.theme.PhaseYellow
import com.example.ui.theme.StatusModerateAmber
import com.example.ui.theme.StatusNormalGreen
import com.example.ui.theme.StatusOverloadRed
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.theme.ToneCoralBorder
import com.example.ui.theme.ToneCoralCard
import com.example.ui.theme.ToneCoralText
import com.example.ui.theme.ToneSageBorder
import com.example.ui.theme.ToneSageCard
import com.example.ui.theme.ToneSageText
import com.example.ui.theme.ToneSkyBorder
import com.example.ui.theme.ToneSkyCard
import com.example.ui.theme.ToneSkyText
import com.example.ui.viewmodel.ReportViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportFormScreen(
    viewModel: ReportViewModel,
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier
) {
    val report by viewModel.editingReport.collectAsState()
    val isGpsLoading by viewModel.isGpsLoading.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (fineGranted || coarseGranted) {
            viewModel.captureGpsLocation()
        }
    }

    val requestLocationAction = {
        val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (hasFine || hasCoarse) {
            viewModel.captureGpsLocation()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (report.id == 0L) "New Field Inspection" else "Edit DTR Report",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = TextPrimaryLight
                            )
                        )
                        Text(
                            text = "FIELD SUPERVISOR VERIFICATION",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextSecondaryLight,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimaryLight
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = TextPrimaryLight
                )
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 8.dp,
                color = Color.White,
                border = BorderStroke(1.dp, OutlineLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onBack,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, OutlineLight),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimaryLight),
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("cancel_report_button")
                    ) {
                        Text("Cancel", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    }

                    Button(
                        onClick = {
                            viewModel.saveCurrentReport(onSuccess = onSaved)
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NaturalGreenPrimary,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(2f)
                            .height(52.dp)
                            .testTag("save_report_button")
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("SAVE REPORT", fontWeight = FontWeight.Bold, fontSize = 14.sp, letterSpacing = 0.5.sp)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BackgroundWarmLinen)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // CARD 1: DTR Identification & GPS (Coral / Terracotta Tone)
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = ToneCoralCard),
                border = BorderStroke(1.5.dp, ToneCoralBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    SectionHeader(
                        title = "DTR Transformer Details",
                        subtitle = "Code, Capacity & Location",
                        icon = Icons.Default.Bolt,
                        iconColor = ToneCoralText,
                        iconBgColor = Color.White.copy(alpha = 0.65f),
                        badgeText = "01. DTR DETAILS"
                    )

                    // DTR Code input
                    OutlinedTextField(
                        value = report.dtrCode,
                        onValueChange = { code -> viewModel.updateFormField { it.copy(dtrCode = code) } },
                        label = { Text("DTR Code / ID *", color = ToneCoralText.copy(alpha = 0.8f)) },
                        placeholder = { Text("e.g. DTR-102 or DTR/VLG/25") },
                        singleLine = true,
                        leadingIcon = {
                            Icon(Icons.Default.Bolt, contentDescription = null, tint = ToneCoralText)
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White.copy(alpha = 0.8f),
                            focusedBorderColor = ToneCoralText,
                            unfocusedBorderColor = ToneCoralBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .testTag("dtr_code_input")
                    )

                    // DTR Capacity Chips
                    SingleOptionSelector(
                        label = "DTR Capacity (kVA)",
                        options = listOf("10", "16", "25", "63", "100"),
                        selectedOption = report.dtrCapacity,
                        onOptionSelected = { cap -> viewModel.updateFormField { it.copy(dtrCapacity = cap) } },
                        optionColors = mapOf(
                            "10" to NaturalGreenPrimary,
                            "16" to NaturalGreenPrimary,
                            "25" to NaturalGreenPrimary,
                            "63" to StatusModerateAmber,
                            "100" to StatusOverloadRed
                        ),
                        testTagPrefix = "dtr_capacity"
                    )

                    // DTR Location
                    OutlinedTextField(
                        value = report.dtrLocation,
                        onValueChange = { loc -> viewModel.updateFormField { it.copy(dtrLocation = loc) } },
                        label = { Text("DTR Location / Village / Substation", color = ToneCoralText.copy(alpha = 0.8f)) },
                        placeholder = { Text("e.g. Rampur Main Market") },
                        singleLine = true,
                        leadingIcon = {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = ToneCoralText)
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White.copy(alpha = 0.8f),
                            focusedBorderColor = ToneCoralText,
                            unfocusedBorderColor = ToneCoralBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .testTag("dtr_location_input")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // GPS Location Card
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White.copy(alpha = 0.75f),
                        border = BorderStroke(1.dp, ToneCoralBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = null,
                                tint = if (report.gpsLocation.isNotBlank()) NaturalGreenPrimary else ToneCoralText.copy(alpha = 0.6f),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "GPS Coordinates",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = ToneCoralText
                                    )
                                )
                                Text(
                                    text = report.gpsLocation.ifEmpty { "Not captured yet" },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (report.gpsLocation.isNotBlank()) ToneCoralText else ToneCoralText.copy(alpha = 0.6f)
                                )
                            }
                            Button(
                                onClick = requestLocationAction,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = NaturalGreenPrimary,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier.testTag("get_gps_button")
                            ) {
                                if (isGpsLoading) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text("Get GPS", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // CARD 2: Load Measurement & Status (Soft Sage / Mint Tone)
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = ToneSageCard),
                border = BorderStroke(1.5.dp, ToneSageBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    SectionHeader(
                        title = "Load Status & Phase Amps",
                        subtitle = "Over / Moderate / Under load & Measurements",
                        icon = Icons.Default.ElectricMeter,
                        iconColor = ToneSageText,
                        iconBgColor = Color.White.copy(alpha = 0.65f),
                        badgeText = "02. LOAD & MEASURE"
                    )

                    // Load Status Selector
                    SingleOptionSelector(
                        label = "Load Status (Select One)",
                        options = listOf("Under Load", "Moderate load", "Over Load"),
                        selectedOption = report.loadStatus,
                        onOptionSelected = { status -> viewModel.updateFormField { it.copy(loadStatus = status) } },
                        optionColors = mapOf(
                            "Under Load" to StatusNormalGreen,
                            "Moderate load" to StatusModerateAmber,
                            "Over Load" to StatusOverloadRed
                        ),
                        testTagPrefix = "load_status"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Load Measurement in Amperes (A)",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            letterSpacing = 0.3.sp
                        ),
                        color = ToneSageText.copy(alpha = 0.85f),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        PhaseCurrentInput(
                            phaseLabel = "R Phase",
                            value = report.loadRPhase,
                            onValueChange = { v -> viewModel.updateFormField { it.copy(loadRPhase = v) } },
                            badgeColor = PhaseRed,
                            modifier = Modifier.weight(1f),
                            testTag = "r_phase_input"
                        )
                        PhaseCurrentInput(
                            phaseLabel = "Y Phase",
                            value = report.loadYPhase,
                            onValueChange = { v -> viewModel.updateFormField { it.copy(loadYPhase = v) } },
                            badgeColor = PhaseYellow,
                            modifier = Modifier.weight(1f),
                            testTag = "y_phase_input"
                        )
                        PhaseCurrentInput(
                            phaseLabel = "B Phase",
                            value = report.loadBPhase,
                            onValueChange = { v -> viewModel.updateFormField { it.copy(loadBPhase = v) } },
                            badgeColor = PhaseBlue,
                            modifier = Modifier.weight(1f),
                            testTag = "b_phase_input"
                        )
                        PhaseCurrentInput(
                            phaseLabel = "Neutral",
                            value = report.loadNeutral,
                            onValueChange = { v -> viewModel.updateFormField { it.copy(loadNeutral = v) } },
                            badgeColor = PhaseNeutral,
                            modifier = Modifier.weight(1f),
                            testTag = "neutral_input"
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = report.loadMeasurementDate,
                            onValueChange = { d -> viewModel.updateFormField { it.copy(loadMeasurementDate = d) } },
                            label = { Text("Date (YYYY-MM-DD)", fontSize = 12.sp) },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White.copy(alpha = 0.8f),
                                focusedBorderColor = NaturalGreenPrimary,
                                unfocusedBorderColor = ToneSageBorder
                            ),
                            modifier = Modifier.weight(1f).testTag("date_input")
                        )
                        OutlinedTextField(
                            value = report.loadMeasurementTime,
                            onValueChange = { t -> viewModel.updateFormField { it.copy(loadMeasurementTime = t) } },
                            label = { Text("Time (e.g. 11:30 AM)", fontSize = 12.sp) },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White.copy(alpha = 0.8f),
                                focusedBorderColor = NaturalGreenPrimary,
                                unfocusedBorderColor = ToneSageBorder
                            ),
                            modifier = Modifier.weight(1f).testTag("time_input")
                        )
                    }
                }
            }

            // CARD 3: Protection & Hardware Conditions (Sky Blue Tone)
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = ToneSkyCard),
                border = BorderStroke(1.5.dp, ToneSkyBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    SectionHeader(
                        title = "Protection, Earthing & Fusing",
                        subtitle = "LA, Earthing, Kiosk, Isolator & Fuse",
                        icon = Icons.Default.Shield,
                        iconColor = ToneSkyText,
                        iconBgColor = Color.White.copy(alpha = 0.65f),
                        badgeText = "03. PROTECTION"
                    )

                    // LA (Lightning Arrester) fixing
                    SingleOptionSelector(
                        label = "LA (Lightning Arrester) Fixing Condition",
                        options = listOf("Fixed", "Not Fixed", "LA Required"),
                        selectedOption = report.laFixing,
                        onOptionSelected = { v -> viewModel.updateFormField { it.copy(laFixing = v) } },
                        optionColors = mapOf(
                            "Fixed" to StatusNormalGreen,
                            "Not Fixed" to StatusModerateAmber,
                            "LA Required" to StatusOverloadRed
                        ),
                        testTagPrefix = "la_fixing"
                    )

                    // Earthing Condition
                    SingleOptionSelector(
                        label = "Earthing Condition",
                        options = listOf("Good", "Damaged", "Earthing Required", "Not OK"),
                        selectedOption = report.earthingCondition,
                        onOptionSelected = { v -> viewModel.updateFormField { it.copy(earthingCondition = v) } },
                        optionColors = mapOf(
                            "Good" to StatusNormalGreen,
                            "Damaged" to StatusOverloadRed,
                            "Earthing Required" to StatusModerateAmber,
                            "Not OK" to StatusOverloadRed
                        ),
                        testTagPrefix = "earthing"
                    )

                    // Kiosk fixing
                    SingleOptionSelector(
                        label = "Kiosk Fixing Status",
                        options = listOf("Fixed", "Not Fixed", "Kiosk Required"),
                        selectedOption = report.kioskFixing,
                        onOptionSelected = { v -> viewModel.updateFormField { it.copy(kioskFixing = v) } },
                        optionColors = mapOf(
                            "Fixed" to StatusNormalGreen,
                            "Not Fixed" to StatusModerateAmber,
                            "Kiosk Required" to StatusOverloadRed
                        ),
                        testTagPrefix = "kiosk"
                    )

                    // Isolator condition
                    SingleOptionSelector(
                        label = "Isolator Condition",
                        options = listOf(
                            "Male Female OK",
                            "Male Female Required",
                            "Damaged",
                            "Direct post insulator"
                        ),
                        selectedOption = report.isolatorCondition,
                        onOptionSelected = { v -> viewModel.updateFormField { it.copy(isolatorCondition = v) } },
                        optionColors = mapOf(
                            "Male Female OK" to StatusNormalGreen,
                            "Male Female Required" to StatusModerateAmber,
                            "Damaged" to StatusOverloadRed,
                            "Direct post insulator" to NaturalGreenPrimary
                        ),
                        testTagPrefix = "isolator"
                    )

                    // Fusing System
                    SingleOptionSelector(
                        label = "Fusing System (Wire size / requirement)",
                        options = listOf(
                            "Fuse wire 12",
                            "Fuse wire 14",
                            "Fuse wire 16",
                            "Fuse Required",
                            "Not Required"
                        ),
                        selectedOption = report.fusingSystem,
                        onOptionSelected = { v -> viewModel.updateFormField { it.copy(fusingSystem = v) } },
                        optionColors = mapOf(
                            "Fuse wire 12" to NaturalGreenPrimary,
                            "Fuse wire 14" to NaturalGreenPrimary,
                            "Fuse wire 16" to NaturalGreenPrimary,
                            "Fuse Required" to StatusOverloadRed,
                            "Not Required" to Color(0xFF64748B)
                        ),
                        testTagPrefix = "fusing"
                    )
                }
            }

            // CARD 4: LT Line Details (Soft Sage / Mint Tone)
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = ToneSageCard),
                border = BorderStroke(1.5.dp, ToneSageBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    SectionHeader(
                        title = "LT Line & Pole Details",
                        subtitle = "Conductor Type, Pole Condition & Work Required",
                        icon = Icons.Default.Power,
                        iconColor = ToneSageText,
                        iconBgColor = Color.White.copy(alpha = 0.65f),
                        badgeText = "04. LT LINE"
                    )

                    // LT Line Type
                    SingleOptionSelector(
                        label = "LT Line Type",
                        options = listOf("AB cable", "Bare conductor", "PVC cable"),
                        selectedOption = report.ltLineType,
                        onOptionSelected = { v -> viewModel.updateFormField { it.copy(ltLineType = v) } },
                        optionColors = mapOf(
                            "AB cable" to StatusNormalGreen,
                            "Bare conductor" to StatusModerateAmber,
                            "PVC cable" to NaturalGreenPrimary
                        ),
                        testTagPrefix = "lt_line_type"
                    )

                    // LT Pole Condition
                    SingleOptionSelector(
                        label = "LT Pole Condition",
                        options = listOf(
                            "Normal",
                            "Broken",
                            "Straightening",
                            "Earthing Pole required",
                            "Height required"
                        ),
                        selectedOption = report.ltPoleCondition,
                        onOptionSelected = { v -> viewModel.updateFormField { it.copy(ltPoleCondition = v) } },
                        optionColors = mapOf(
                            "Normal" to StatusNormalGreen,
                            "Broken" to StatusOverloadRed,
                            "Straightening" to StatusModerateAmber,
                            "Earthing Pole required" to StatusModerateAmber,
                            "Height required" to StatusModerateAmber
                        ),
                        testTagPrefix = "lt_pole_condition"
                    )

                    // LT Line Work Required (Multi-select)
                    MultiOptionSelector(
                        label = "LT Line Maintenance Work Required (Select all that apply)",
                        options = listOf(
                            "Sagging Required",
                            "Mid Gap pole required",
                            "Bracket required",
                            "D-iron & shackle Required",
                            "Conversion required 1Ph to 3 Ph"
                        ),
                        selectedOptionsString = report.ltLineWork,
                        onOptionsChanged = { v -> viewModel.updateFormField { it.copy(ltLineWork = v) } },
                        testTagPrefix = "lt_line_work"
                    )
                }
            }

            // CARD 5: Survey Team, Inspector & Consumer Details (Coral Tone)
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = ToneCoralCard),
                border = BorderStroke(1.5.dp, ToneCoralBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    SectionHeader(
                        title = "Survey Team & Inspector Details",
                        subtitle = "Who will survey, designation, agency & supervisor",
                        icon = Icons.Default.Person,
                        iconColor = ToneCoralText,
                        iconBgColor = Color.White.copy(alpha = 0.65f),
                        badgeText = "05. SURVEY TEAM"
                    )

                    // Who will survey / Surveyor Name
                    OutlinedTextField(
                        value = report.surveyorName,
                        onValueChange = { name -> viewModel.updateFormField { it.copy(surveyorName = name) } },
                        label = { Text("Who Will Survey / Surveyor Name *", color = ToneCoralText.copy(alpha = 0.8f)) },
                        placeholder = { Text("e.g. Rajesh Sharma") },
                        singleLine = true,
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = ToneCoralText)
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White.copy(alpha = 0.8f),
                            focusedBorderColor = ToneCoralText,
                            unfocusedBorderColor = ToneCoralBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .testTag("surveyor_name_input")
                    )

                    // Surveyor Designation Selector
                    SingleOptionSelector(
                        label = "Surveyor Designation / Role",
                        options = listOf(
                            "Junior Engineer",
                            "Line Supervisor",
                            "Survey Officer",
                            "Lineman",
                            "Agency Inspector"
                        ),
                        selectedOption = report.surveyorDesignation,
                        onOptionSelected = { desig -> viewModel.updateFormField { it.copy(surveyorDesignation = desig) } },
                        optionColors = mapOf(
                            "Junior Engineer" to NaturalGreenPrimary,
                            "Line Supervisor" to ToneCoralText,
                            "Survey Officer" to StatusModerateAmber,
                            "Lineman" to Color(0xFF0D9488),
                            "Agency Inspector" to Color(0xFF6366F1)
                        ),
                        testTagPrefix = "surveyor_designation"
                    )

                    // Survey Agency / Sub-division / Feeder
                    OutlinedTextField(
                        value = report.surveyAgency,
                        onValueChange = { agency -> viewModel.updateFormField { it.copy(surveyAgency = agency) } },
                        label = { Text("Survey Agency / Sub-Division / Feeder", color = ToneCoralText.copy(alpha = 0.8f)) },
                        placeholder = { Text("e.g. North Zone DISCOM / Feeder 4") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White.copy(alpha = 0.8f),
                            focusedBorderColor = ToneCoralText,
                            unfocusedBorderColor = ToneCoralBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .testTag("survey_agency_input")
                    )

                    // Survey Date
                    OutlinedTextField(
                        value = report.surveyDate,
                        onValueChange = { sDate -> viewModel.updateFormField { it.copy(surveyDate = sDate) } },
                        label = { Text("Survey Date (YYYY-MM-DD)", color = ToneCoralText.copy(alpha = 0.8f)) },
                        placeholder = { Text("e.g. 2026-08-23") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White.copy(alpha = 0.8f),
                            focusedBorderColor = ToneCoralText,
                            unfocusedBorderColor = ToneCoralBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .testTag("survey_date_input")
                    )

                    // Supervisor In-Charge / Approver
                    OutlinedTextField(
                        value = report.supervisorName,
                        onValueChange = { name -> viewModel.updateFormField { it.copy(supervisorName = name) } },
                        label = { Text("Supervisor In-Charge / Approver", color = ToneCoralText.copy(alpha = 0.8f)) },
                        placeholder = { Text("e.g. Ramesh Kumar (Line Supervisor)") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White.copy(alpha = 0.8f),
                            focusedBorderColor = ToneCoralText,
                            unfocusedBorderColor = ToneCoralBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .testTag("supervisor_name_input")
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Consumer Type
                    SingleOptionSelector(
                        label = "Type of Consumer",
                        options = listOf("Village", "Agriculture", "Industrial", "Single user"),
                        selectedOption = report.consumerType,
                        onOptionSelected = { v -> viewModel.updateFormField { it.copy(consumerType = v) } },
                        optionColors = mapOf(
                            "Village" to NaturalGreenPrimary,
                            "Agriculture" to StatusNormalGreen,
                            "Industrial" to StatusModerateAmber,
                            "Single user" to Color(0xFF0D9488)
                        ),
                        testTagPrefix = "consumer_type"
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedTextField(
                        value = report.remarks,
                        onValueChange = { rem -> viewModel.updateFormField { it.copy(remarks = rem) } },
                        label = { Text("Additional Remarks / Field Notes", color = ToneCoralText.copy(alpha = 0.8f)) },
                        placeholder = { Text("e.g. Tree branches need trimming, oil level checked OK") },
                        minLines = 2,
                        maxLines = 4,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White.copy(alpha = 0.8f),
                            focusedBorderColor = ToneCoralText,
                            unfocusedBorderColor = ToneCoralBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .testTag("remarks_input")
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

