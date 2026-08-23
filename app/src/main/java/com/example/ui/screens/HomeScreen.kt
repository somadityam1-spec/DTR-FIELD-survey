package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ReportEntity
import com.example.ui.components.LoadStatusBadge
import com.example.ui.theme.BackgroundWarmLinen
import com.example.ui.theme.NaturalGreenPrimary
import com.example.ui.theme.NaturalGreenPrimaryDark
import com.example.ui.theme.OutlineLight
import com.example.ui.theme.StatusModerateAmber
import com.example.ui.theme.StatusNormalGreen
import com.example.ui.theme.StatusOverloadRed
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.theme.ToneCoralBorder
import com.example.ui.theme.ToneCoralCard
import com.example.ui.theme.ToneCoralText
import com.example.ui.theme.ToneLavenderBadge
import com.example.ui.theme.ToneLavenderBorder
import com.example.ui.theme.ToneLavenderCard
import com.example.ui.theme.ToneLavenderText
import com.example.ui.theme.ToneSageBorder
import com.example.ui.theme.ToneSageCard
import com.example.ui.theme.ToneSageText
import com.example.ui.viewmodel.ReportViewModel
import com.example.util.ExcelExporter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ReportViewModel,
    onAddNewReport: () -> Unit,
    onReportClick: (ReportEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val reports by viewModel.filteredReports.collectAsState()
    val allReports by viewModel.allReports.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()

    val totalCount = allReports.size
    val overloadCount = allReports.count { it.loadStatus == "Over Load" }
    val workNeededCount = allReports.count {
        it.ltLineWork.isNotBlank() || it.earthingCondition != "Good" || it.laFixing != "Fixed" || it.ltPoleCondition != "Normal"
    }

    val handleExportAllExcel = {
        viewModel.exportAllReportsToExcel { file ->
            ExcelExporter.shareReportFile(
                context = context,
                file = file,
                subject = "All Field Supervisor DTR Reports - Excel"
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            modifier = Modifier.size(44.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White,
                            border = BorderStroke(1.dp, OutlineLight)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_dtr_survey_logo),
                                contentDescription = "DTR Survey Logo",
                                modifier = Modifier
                                    .padding(3.dp)
                                    .fillMaxSize()
                            )
                        }
                        Column {
                            Text(
                                text = "DTR SURVEY",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 17.sp,
                                    color = TextPrimaryLight,
                                    letterSpacing = 0.5.sp
                                )
                            )
                            Text(
                                text = "DATA • TRACK • REPORT",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = NaturalGreenPrimary,
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.8.sp
                                )
                            )
                        }
                    }
                },
                actions = {
                    // Export All Excel Button in Header with Natural Green tone
                    Button(
                        onClick = handleExportAllExcel,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NaturalGreenPrimary,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .testTag("export_all_excel_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FileDownload,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Excel .csv",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = TextPrimaryLight
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    viewModel.initNewReport()
                    onAddNewReport()
                },
                containerColor = NaturalGreenPrimary,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("NEW INSPECTION", fontWeight = FontWeight.Bold, fontSize = 14.sp, letterSpacing = 0.5.sp) },
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.testTag("add_new_report_fab")
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BackgroundWarmLinen)
        ) {
            // Stats Overview Cards in Natural Tones
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Total DTR",
                    count = totalCount.toString(),
                    cardBg = ToneSageCard,
                    borderColor = ToneSageBorder,
                    textColor = ToneSageText,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Overload",
                    count = overloadCount.toString(),
                    cardBg = ToneCoralCard,
                    borderColor = ToneCoralBorder,
                    textColor = ToneCoralText,
                    icon = Icons.Default.Warning,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Work Req.",
                    count = workNeededCount.toString(),
                    cardBg = Color(0xFFFEF3C7),
                    borderColor = Color(0xFFFDE68A),
                    textColor = Color(0xFF92400E),
                    modifier = Modifier.weight(1f)
                )
            }

            // Search Bar with Natural warm surface
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                placeholder = { Text("Search DTR Code, Village, Substation...", fontSize = 13.sp, color = TextSecondaryLight) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = NaturalGreenPrimary
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear search")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = NaturalGreenPrimary,
                    unfocusedBorderColor = OutlineLight
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .testTag("search_bar")
            )

            // Filter Chips in Natural Theme
            val filters = listOf("All", "Over Load", "Moderate load", "Under Load", "Work Needed")
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    val isSelected = filter == selectedFilter
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.onFilterSelected(filter) },
                        label = {
                            Text(
                                text = filter,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NaturalGreenPrimary,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = TextSecondaryLight
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) NaturalGreenPrimary else OutlineLight
                        ),
                        modifier = Modifier.testTag("filter_${filter.replace(" ", "_").lowercase()}")
                    )
                }
            }

            // Reports List
            if (reports.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(ToneSageCard),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = NaturalGreenPrimary,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (searchQuery.isNotEmpty()) "No matching reports found" else "No inspection reports yet",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimaryLight
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tap the + button below to create your first DTR field report",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondaryLight,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(reports, key = { it.id }) { report ->
                        ReportCardItem(
                            report = report,
                            onClick = { onReportClick(report) },
                            onWhatsAppShare = {
                                val file = ExcelExporter.exportToExcelCsv(context, listOf(report), "Report_${report.dtrCode.ifEmpty { "DTR" }}")
                                val text = ExcelExporter.generateTextSummary(report)
                                if (file != null) {
                                    ExcelExporter.shareReportFile(
                                        context = context,
                                        file = file,
                                        subject = "DTR Report ${report.dtrCode}",
                                        textBody = text,
                                        targetPackage = "com.whatsapp"
                                    )
                                } else {
                                    ExcelExporter.sharePlainText(context, text, targetPackage = "com.whatsapp")
                                }
                            },
                            onEmailShare = {
                                val file = ExcelExporter.exportToExcelCsv(context, listOf(report), "Report_${report.dtrCode.ifEmpty { "DTR" }}")
                                val text = ExcelExporter.generateTextSummary(report)
                                if (file != null) {
                                    ExcelExporter.shareViaEmail(
                                        context = context,
                                        file = file,
                                        subject = "Field Supervisor DTR Report - ${report.dtrCode}",
                                        body = text
                                    )
                                }
                            }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(80.dp)) // Padding for FAB
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    count: String,
    cardBg: Color,
    borderColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = cardBg,
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(icon, contentDescription = null, tint = textColor, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                    color = textColor.copy(alpha = 0.85f),
                    fontSize = 10.sp,
                    letterSpacing = 0.5.sp
                )
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = count,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                color = textColor,
                fontSize = 22.sp
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ReportCardItem(
    report: ReportEntity,
    onClick: () -> Unit,
    onWhatsAppShare: () -> Unit,
    onEmailShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("report_card_${report.dtrCode.replace(" ", "_").lowercase()}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, OutlineLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(15.dp)) {
            // Header Row: DTR Code + Capacity + Load Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(ToneSageCard),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = NaturalGreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = report.dtrCode.ifEmpty { "Unnamed DTR" },
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                            color = TextPrimaryLight
                        )
                        Text(
                            text = "${report.dtrCapacity} kVA • ${report.consumerType}",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = NaturalGreenPrimary
                        )
                    }
                }
                LoadStatusBadge(report.loadStatus)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Location Row
            if (report.dtrLocation.isNotBlank() || report.gpsLocation.isNotBlank()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = NaturalGreenPrimary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = report.dtrLocation.ifEmpty { report.gpsLocation },
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                        color = TextSecondaryLight,
                        maxLines = 1
                    )
                }
            }

            // Phase Current mini summary row
            if (report.loadRPhase.isNotBlank() || report.loadYPhase.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = BackgroundWarmLinen,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "R: ${report.loadRPhase.ifEmpty { "0" }}A   Y: ${report.loadYPhase.ifEmpty { "0" }}A   B: ${report.loadBPhase.ifEmpty { "0" }}A   N: ${report.loadNeutral.ifEmpty { "0" }}A",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            color = TextSecondaryLight
                        )
                    }
                }
            }

            // Pending Work badge if any
            if (report.ltLineWork.isNotBlank() || report.earthingCondition != "Good" || report.laFixing != "Fixed") {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ToneCoralCard,
                    border = BorderStroke(1.dp, ToneCoralBorder),
                    modifier = Modifier.padding(vertical = 3.dp)
                ) {
                    Text(
                        text = "⚠️ " + listOfNotNull(
                            if (report.earthingCondition != "Good") "Earthing (${report.earthingCondition})" else null,
                            if (report.laFixing != "Fixed") "LA (${report.laFixing})" else null,
                            if (report.ltLineWork.isNotBlank()) report.ltLineWork.split(",").firstOrNull() else null
                        ).joinToString(", "),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        ),
                        color = ToneCoralText,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        maxLines = 1
                    )
                }
            }

            // Surveyor info badge if assigned
            if (report.surveyorName.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = ToneLavenderCard,
                    border = BorderStroke(1.dp, ToneLavenderBorder),
                    modifier = Modifier.padding(vertical = 3.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "👷 Surveyed by: ${report.surveyorName} (${report.surveyorDesignation.ifEmpty { "Surveyor" }})",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            ),
                            color = ToneLavenderText,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Action Row: Quick WhatsApp & Email buttons + Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${report.loadMeasurementDate} ${report.loadMeasurementTime}",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = TextSecondaryLight.copy(alpha = 0.7f)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    // Quick WhatsApp button
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF25D366).copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, Color(0xFF25D366).copy(alpha = 0.3f)),
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onWhatsAppShare() }
                            .testTag("card_whatsapp_${report.dtrCode.replace(" ", "_").lowercase()}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "WhatsApp",
                                tint = Color(0xFF25D366),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "WhatsApp",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                ),
                                color = Color(0xFF15803D)
                            )
                        }
                    }

                    // Quick Email button
                    Surface(
                        shape = CircleShape,
                        color = ToneCoralCard,
                        border = BorderStroke(1.dp, ToneCoralBorder),
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onEmailShare() }
                            .testTag("card_email_${report.dtrCode.replace(" ", "_").lowercase()}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email",
                                tint = Color(0xFFEA4335),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Email",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                ),
                                color = ToneCoralText
                            )
                        }
                    }
                }
            }
        }
    }
}

