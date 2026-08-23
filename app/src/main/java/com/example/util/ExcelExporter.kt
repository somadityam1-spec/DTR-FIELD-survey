package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.data.model.ReportEntity
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ExcelExporter {

    private fun escapeCsv(value: String): String {
        var text = value
        if (text.contains("\"")) {
            text = text.replace("\"", "\"\"")
        }
        if (text.contains(",") || text.contains("\n") || text.contains("\r") || text.contains("\"")) {
            text = "\"$text\""
        }
        return text
    }

    /**
     * Generates a CSV file formatted for Microsoft Excel with UTF-8 BOM.
     */
    fun exportToExcelCsv(context: Context, reports: List<ReportEntity>, filenamePrefix: String = "DTR_Field_Report"): File? {
        try {
            val reportsDir = File(context.cacheDir, "reports")
            if (!reportsDir.exists()) {
                reportsDir.mkdirs()
            }

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val file = File(reportsDir, "${filenamePrefix}_$timestamp.csv")

            val outputStream = FileOutputStream(file)
            val writer = OutputStreamWriter(outputStream, StandardCharsets.UTF_8)

            // Write UTF-8 BOM so Microsoft Excel automatically recognizes Unicode/accented characters
            writer.write("\uFEFF")

            // Headers
            val headers = listOf(
                "Sl No",
                "DTR Code",
                "DTR Capacity (kVA)",
                "Location",
                "GPS Coordinates",
                "Load Status",
                "R Phase (Amps)",
                "Y Phase (Amps)",
                "B Phase (Amps)",
                "Neutral (Amps)",
                "Measurement Date",
                "Measurement Time",
                "LA Fixing Condition",
                "Earthing Condition",
                "Kiosk Fixing",
                "Isolator Condition",
                "Fusing System",
                "LT Line Type",
                "LT Pole Condition",
                "LT Line Work Required",
                "Consumer Type",
                "Who Will Survey / Surveyor Name",
                "Surveyor Designation",
                "Survey Agency / Feeder",
                "Survey Date",
                "Supervisor Name",
                "Remarks",
                "Report Created Date"
            )
            writer.write(headers.joinToString(",") { escapeCsv(it) } + "\n")

            // Rows
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            reports.forEachIndexed { index, report ->
                val row = listOf(
                    (index + 1).toString(),
                    report.dtrCode,
                    report.dtrCapacity,
                    report.dtrLocation,
                    report.gpsLocation,
                    report.loadStatus,
                    report.loadRPhase,
                    report.loadYPhase,
                    report.loadBPhase,
                    report.loadNeutral,
                    report.loadMeasurementDate,
                    report.loadMeasurementTime,
                    report.laFixing,
                    report.earthingCondition,
                    report.kioskFixing,
                    report.isolatorCondition,
                    report.fusingSystem,
                    report.ltLineType,
                    report.ltPoleCondition,
                    report.ltLineWork,
                    report.consumerType,
                    report.surveyorName,
                    report.surveyorDesignation,
                    report.surveyAgency,
                    report.surveyDate,
                    report.supervisorName,
                    report.remarks,
                    dateFormat.format(Date(report.createdAt))
                )
                writer.write(row.joinToString(",") { escapeCsv(it) } + "\n")
            }

            writer.flush()
            writer.close()
            outputStream.close()
            return file
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * Generates a single report text summary for WhatsApp quick preview.
     */
    fun generateTextSummary(report: ReportEntity): String {
        return buildString {
            appendLine("📋 *DTR FIELD SUPERVISOR REPORT*")
            appendLine("━━━━━━━━━━━━━━━━━━━")
            appendLine("🔹 *DTR Code:* ${report.dtrCode.ifEmpty { "N/A" }}")
            appendLine("⚡ *Capacity:* ${report.dtrCapacity} kVA")
            appendLine("📍 *Location:* ${report.dtrLocation.ifEmpty { "N/A" }}")
            if (report.gpsLocation.isNotBlank()) {
                appendLine("🌐 *GPS:* ${report.gpsLocation}")
            }
            appendLine("📊 *Load Status:* ${report.loadStatus}")
            appendLine("   • R-Phase: ${report.loadRPhase.ifEmpty { "0" }} A")
            appendLine("   • Y-Phase: ${report.loadYPhase.ifEmpty { "0" }} A")
            appendLine("   • B-Phase: ${report.loadBPhase.ifEmpty { "0" }} A")
            appendLine("   • Neutral: ${report.loadNeutral.ifEmpty { "0" }} A")
            appendLine("🕒 *Measured at:* ${report.loadMeasurementDate} ${report.loadMeasurementTime}")
            appendLine("━━━━━━━━━━━━━━━━━━━")
            appendLine("🛡️ *LA Condition:* ${report.laFixing}")
            appendLine("⏚ *Earthing:* ${report.earthingCondition}")
            appendLine("📦 *Kiosk:* ${report.kioskFixing}")
            appendLine("🔌 *Isolator:* ${report.isolatorCondition}")
            appendLine("⚡ *Fusing System:* ${report.fusingSystem}")
            appendLine("━━━━━━━━━━━━━━━━━━━")
            appendLine("📏 *LT Line Type:* ${report.ltLineType}")
            appendLine("🪵 *LT Pole Condition:* ${report.ltPoleCondition}")
            if (report.ltLineWork.isNotBlank()) {
                appendLine("🛠️ *Work Required:* ${report.ltLineWork}")
            }
            appendLine("👥 *Consumer Type:* ${report.consumerType}")
            appendLine("━━━━━━━━━━━━━━━━━━━")
            if (report.surveyorName.isNotBlank()) {
                appendLine("👷 *Surveyor (Who will survey):* ${report.surveyorName} (${report.surveyorDesignation})")
            }
            if (report.surveyAgency.isNotBlank()) {
                appendLine("🏢 *Survey Agency / Feeder:* ${report.surveyAgency}")
            }
            if (report.surveyDate.isNotBlank()) {
                appendLine("📅 *Survey Date:* ${report.surveyDate}")
            }
            if (report.supervisorName.isNotBlank()) {
                appendLine("🧑‍💼 *Supervisor In-Charge:* ${report.supervisorName}")
            }
            if (report.remarks.isNotBlank()) {
                appendLine("📝 *Remarks:* ${report.remarks}")
            }
            appendLine("━━━━━━━━━━━━━━━━━━━")
            appendLine("Generated via Field Supervisor Report App")
        }
    }

    /**
     * Share exported file via WhatsApp specifically or default app chooser.
     */
    fun shareReportFile(
        context: Context,
        file: File,
        subject: String = "Field Supervisor DTR Inspection Report",
        textBody: String = "Please find attached DTR inspection report in Excel format.",
        targetPackage: String? = null
    ) {
        try {
            val authority = "${context.packageName}.fileprovider"
            val uri: Uri = FileProvider.getUriForFile(context, authority, file)

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/comma-separated-values"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, textBody)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                if (targetPackage != null) {
                    setPackage(targetPackage)
                }
            }

            if (targetPackage != null) {
                try {
                    context.startActivity(intent)
                } catch (e: Exception) {
                    // If target app is not installed, fallback to generic chooser
                    val chooser = Intent.createChooser(intent.apply { `package` = null }, "Share Report Excel File")
                    context.startActivity(chooser)
                }
            } else {
                val chooser = Intent.createChooser(intent, "Share Report Excel File")
                context.startActivity(chooser)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to share file: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Send email with pre-filled subject and attached Excel file.
     */
    fun shareViaEmail(
        context: Context,
        file: File,
        subject: String = "DTR Field Inspection Report - ${SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())}",
        body: String = "Dear Admin / Executive Engineer,\n\nPlease find attached the DTR field inspection supervisor report.\n\nThank you."
    ) {
        try {
            val authority = "${context.packageName}.fileprovider"
            val uri: Uri = FileProvider.getUriForFile(context, authority, file)

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, body)
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(intent, "Send Report via Email")
            context.startActivity(chooser)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Could not open email app: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Share text only to WhatsApp or other apps.
     */
    fun sharePlainText(context: Context, text: String, targetPackage: String? = null) {
        try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
                if (targetPackage != null) {
                    setPackage(targetPackage)
                }
            }

            if (targetPackage != null) {
                try {
                    context.startActivity(intent)
                } catch (e: Exception) {
                    val chooser = Intent.createChooser(intent.apply { `package` = null }, "Share Report")
                    context.startActivity(chooser)
                }
            } else {
                val chooser = Intent.createChooser(intent, "Share Report")
                context.startActivity(chooser)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to share: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }
}
