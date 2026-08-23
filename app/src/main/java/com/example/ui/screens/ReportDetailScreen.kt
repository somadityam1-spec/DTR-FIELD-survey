package com.example.ui.screens

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ReportEntity
import com.example.ui.components.LoadStatusBadge
import com.example.ui.theme.BackgroundWarmLinen
import com.example.ui.theme.NaturalGreenPrimary
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
import com.example.ui.theme.ToneLavenderBadge
import com.example.ui.theme.ToneLavenderBorder
import com.example.ui.theme.ToneLavenderCard
import com.example.ui.theme.ToneLavenderText
import com.example.ui.theme.ToneSageBorder
import com.example.ui.theme.ToneSageCard
import com.example.ui.theme.ToneSageText
import com.example.ui.theme.ToneSkyBorder
import com.example.ui.theme.ToneSkyCard
import com.example.ui.theme.ToneSkyText
import com.example.util.ExcelExporter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ReportDetailScreen(
    report: ReportEntity,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onDuplicate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    val handleExportExcel = {
        val file = ExcelExporter.exportToExcelCsv(context, listOf(report), "Report_${report.dtrCode.ifEmpty { "DTR" }}")
        if (file != null) {
            ExcelExporter.shareReportFile(context, file, subject = "DTR Field Report - ${report.dtrCode}")
        }
    }

    val handleWhatsAppShare = {
        val file = ExcelExporter.exportToExcelCsv(context, listOf(report), "Report_${report.dtrCode.ifEmpty { "DTR" }}")
        val summary = ExcelExporter.generateTextSummary(report)
        if (file != null) {
            ExcelExporter.shareReportFile(
                context = context,
                file = file,
                subject = "DTR Report - ${report.dtrCode}",
                textBody = summary,
                targetPackage = "com.whatsapp"
            )
        } else {
            ExcelExporter.sharePlainText(context, summary, targetPackage = "com.whatsapp")
        }
    }

    val handleEmailShare = {
        val file = ExcelExporter.exportToExcelCsv(context, listOf(report), "Report_${report.dtrCode.ifEmpty { "DTR" }}")
        val summary = ExcelExporter.generateTextSummary(report)
        if (file != null) {
            ExcelExporter.shareViaEmail(
                context = context,
                file = file,
                subject = "Field Supervisor DTR Report - ${report.dtrCode} (${report.loadMeasurementDate})",
                body = "Dear Admin / Executive Engineer,\n\nPlease find attached the inspection report for DTR Code: ${report.dtrCode}.\n\n$summary"
            )
        }
    }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Delete Report?", fontWeight = FontWeight.Bold, color = TextPrimaryLight) },
            text = { Text("Are you sure you want to delete report for DTR ${report.dtrCode}? This action cannot be undone.", color = TextSecondaryLight) },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmDialog = false
                        onDelete()
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = StatusOverloadRed),
                    modifier = Modifier.testTag("confirm_delete_button")
                ) {
                    Text("Delete", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Cancel", color = TextSecondaryLight)
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "DTR: ${report.dtrCode.ifEmpty { "Report Details" }}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = TextPrimaryLight
                            )
                        )
                        Text(
                            text = "${report.dtrCapacity} kVA • ${report.consumerType}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextSecondaryLight,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("detail_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimaryLight
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.testTag("detail_edit_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = NaturalGreenPrimary
                        )
                    }
                    IconButton(
                        onClick = { showDeleteConfirmDialog = true },
                        modifier = Modifier.testTag("detail_delete_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = StatusOverloadRed
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
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // WhatsApp Button
                        Button(
                            onClick = handleWhatsAppShare,
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("detail_whatsapp_button")
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("WhatsApp", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        // Email Button
                        Button(
                            onClick = handleEmailShare,
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEA4335)),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("detail_email_button")
                        ) {
                            Icon(Icons.Default.Email, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Email", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        // Excel CSV Button
                        Button(
                            onClick = handleExportExcel,
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NaturalGreenPrimary),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("detail_excel_button")
                        ) {
                            Icon(Icons.Default.FileDownload, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Excel .csv", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Overview Card (Coral Tone)
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = ToneCoralCard),
                border = BorderStroke(1.5.dp, ToneCoralBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = report.dtrCode.ifEmpty { "Unnamed DTR" },
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                color = ToneCoralText
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = ToneCoralText,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${report.dtrCapacity} kVA Transformer",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = ToneCoralText
                                )
                            }
                        }
                        LoadStatusBadge(report.loadStatus)
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = ToneCoralBorder)

                    DetailItemRow(label = "Location", value = report.dtrLocation.ifEmpty { "Not specified" }, labelColor = ToneCoralText.copy(alpha = 0.7f), valueColor = ToneCoralText)
                    if (report.gpsLocation.isNotBlank()) {
                        DetailItemRow(label = "GPS Coordinates", value = report.gpsLocation, labelColor = ToneCoralText.copy(alpha = 0.7f), valueColor = ToneCoralText)
                    }
                    DetailItemRow(label = "Consumer Type", value = report.consumerType, labelColor = ToneCoralText.copy(alpha = 0.7f), valueColor = ToneCoralText)
                    DetailItemRow(label = "Measured On", value = "${report.loadMeasurementDate} ${report.loadMeasurementTime}", labelColor = ToneCoralText.copy(alpha = 0.7f), valueColor = ToneCoralText)
                    if (report.supervisorName.isNotBlank()) {
                        DetailItemRow(label = "Field Supervisor", value = report.supervisorName, labelColor = ToneCoralText.copy(alpha = 0.7f), valueColor = ToneCoralText)
                    }
                }
            }

            // Phase Current Measurements Card (Sage Tone)
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = ToneSageCard),
                border = BorderStroke(1.5.dp, ToneSageBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "⚡ Load Measurements",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = ToneSageText
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PhaseDisplayBadge("R Phase", report.loadRPhase, PhaseRed, Modifier.weight(1f))
                        PhaseDisplayBadge("Y Phase", report.loadYPhase, PhaseYellow, Modifier.weight(1f))
                        PhaseDisplayBadge("B Phase", report.loadBPhase, PhaseBlue, Modifier.weight(1f))
                        PhaseDisplayBadge("Neutral", report.loadNeutral, PhaseNeutral, Modifier.weight(1f))
                    }
                }
            }

            // Protection & Equipment Conditions (Sky Tone)
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = ToneSkyCard),
                border = BorderStroke(1.5.dp, ToneSkyBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "🛡️ Protection & Substation Hardware",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = ToneSkyText
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    DetailItemRow(label = "LA (Lightning Arrester)", value = report.laFixing, labelColor = ToneSkyText.copy(alpha = 0.7f), valueColor = ToneSkyText)
                    DetailItemRow(label = "Earthing Condition", value = report.earthingCondition, labelColor = ToneSkyText.copy(alpha = 0.7f), valueColor = ToneSkyText)
                    DetailItemRow(label = "Kiosk Fixing", value = report.kioskFixing, labelColor = ToneSkyText.copy(alpha = 0.7f), valueColor = ToneSkyText)
                    DetailItemRow(label = "Isolator Condition", value = report.isolatorCondition, labelColor = ToneSkyText.copy(alpha = 0.7f), valueColor = ToneSkyText)
                    DetailItemRow(label = "Fusing System", value = report.fusingSystem, labelColor = ToneSkyText.copy(alpha = 0.7f), valueColor = ToneSkyText)
                }
            }

            // LT Line Details (Sage Tone)
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = ToneSageCard),
                border = BorderStroke(1.5.dp, ToneSageBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "🔌 LT Line & Pole Network",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = ToneSageText
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    DetailItemRow(label = "LT Line Type", value = report.ltLineType, labelColor = ToneSageText.copy(alpha = 0.7f), valueColor = ToneSageText)
                    DetailItemRow(label = "LT Pole Condition", value = report.ltPoleCondition, labelColor = ToneSageText.copy(alpha = 0.7f), valueColor = ToneSageText)

                    if (report.ltLineWork.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Work Required:",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = ToneSageText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            report.ltLineWork.split(",").map { it.trim() }.filter { it.isNotBlank() }.forEach { workItem ->
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color.White.copy(alpha = 0.8f),
                                    border = BorderStroke(1.dp, ToneSageBorder)
                                ) {
                                    Text(
                                        text = "⚠️ $workItem",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                        color = ToneSageText,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Survey Team & Inspector Details Card (Lavender / Soft Purple Tone)
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = ToneLavenderCard),
                border = BorderStroke(1.5.dp, ToneLavenderBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "👷 Survey Team & Inspector",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = ToneLavenderText
                        )
                        if (report.surveyorName.isNotBlank()) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = ToneLavenderBadge
                            ) {
                                Text(
                                    text = report.surveyorDesignation.ifEmpty { "Surveyor" },
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = ToneLavenderText,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    DetailItemRow(
                        label = "Who Will Survey (Surveyor)",
                        value = report.surveyorName.ifEmpty { "Not assigned" },
                        labelColor = ToneLavenderText.copy(alpha = 0.7f),
                        valueColor = ToneLavenderText
                    )
                    DetailItemRow(
                        label = "Surveyor Designation",
                        value = report.surveyorDesignation.ifEmpty { "Junior Engineer" },
                        labelColor = ToneLavenderText.copy(alpha = 0.7f),
                        valueColor = ToneLavenderText
                    )
                    if (report.surveyAgency.isNotBlank()) {
                        DetailItemRow(
                            label = "Survey Agency / Feeder",
                            value = report.surveyAgency,
                            labelColor = ToneLavenderText.copy(alpha = 0.7f),
                            valueColor = ToneLavenderText
                        )
                    }
                    if (report.surveyDate.isNotBlank()) {
                        DetailItemRow(
                            label = "Survey Date",
                            value = report.surveyDate,
                            labelColor = ToneLavenderText.copy(alpha = 0.7f),
                            valueColor = ToneLavenderText
                        )
                    }
                    if (report.supervisorName.isNotBlank()) {
                        DetailItemRow(
                            label = "Supervisor In-Charge",
                            value = report.supervisorName,
                            labelColor = ToneLavenderText.copy(alpha = 0.7f),
                            valueColor = ToneLavenderText
                        )
                    }
                }
            }

            // Remarks Card (Coral Tone)
            if (report.remarks.isNotBlank()) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = ToneCoralCard),
                    border = BorderStroke(1.5.dp, ToneCoralBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "📝 Field Remarks & Notes",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = ToneCoralText
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = report.remarks,
                            style = MaterialTheme.typography.bodyMedium,
                            color = ToneCoralText
                        )
                    }
                }
            }

            // Secondary duplicate action
            OutlinedButton(
                onClick = onDuplicate,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, OutlineLight),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimaryLight),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("duplicate_report_button")
            ) {
                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp), tint = NaturalGreenPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Duplicate as New Inspection Template", fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DetailItemRow(
    label: String,
    value: String,
    labelColor: Color = TextSecondaryLight,
    valueColor: Color = TextPrimaryLight
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = labelColor
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = valueColor
        )
    }
}

@Composable
private fun PhaseDisplayBadge(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = Color.White.copy(alpha = 0.85f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = color
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (value.isNotBlank()) "$value A" else "0 A",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = color
            )
        }
    }
}

