package com.example.ui.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.model.ReportEntity
import com.example.data.repository.ReportRepository
import com.example.util.ExcelExporter
import com.example.util.LocationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReportViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ReportRepository
    private val locationHelper: LocationHelper

    init {
        val db = AppDatabase.getDatabase(application)
        repository = ReportRepository(db.reportDao())
        locationHelper = LocationHelper(application)
    }

    val allReports: StateFlow<List<ReportEntity>> = repository.allReports
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow("All") // All, Over Load, Moderate load, Under Load, Work Needed
    val selectedFilter = _selectedFilter.asStateFlow()

    val filteredReports: StateFlow<List<ReportEntity>> = combine(
        allReports,
        _searchQuery,
        _selectedFilter
    ) { reports, query, filter ->
        reports.filter { report ->
            val matchesQuery = query.isBlank() ||
                report.dtrCode.contains(query, ignoreCase = true) ||
                report.dtrLocation.contains(query, ignoreCase = true) ||
                report.consumerType.contains(query, ignoreCase = true) ||
                report.surveyorName.contains(query, ignoreCase = true) ||
                report.surveyorDesignation.contains(query, ignoreCase = true) ||
                report.surveyAgency.contains(query, ignoreCase = true) ||
                report.supervisorName.contains(query, ignoreCase = true)

            val matchesFilter = when (filter) {
                "All" -> true
                "Over Load" -> report.loadStatus == "Over Load"
                "Moderate load" -> report.loadStatus == "Moderate load"
                "Under Load" -> report.loadStatus == "Under Load"
                "Work Needed" -> report.ltLineWork.isNotBlank() ||
                                 report.earthingCondition != "Good" ||
                                 report.laFixing != "Fixed" ||
                                 report.ltPoleCondition != "Normal" ||
                                 report.fusingSystem.contains("Required", ignoreCase = true)
                else -> true
            }
            matchesQuery && matchesFilter
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Form editing state
    private val _editingReport = MutableStateFlow(createNewBlankReport())
    val editingReport = _editingReport.asStateFlow()

    private val _isGpsLoading = MutableStateFlow(false)
    val isGpsLoading = _isGpsLoading.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage = _toastMessage.asStateFlow()

    fun clearToast() {
        _toastMessage.value = null
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onFilterSelected(filter: String) {
        _selectedFilter.value = filter
    }

    fun initNewReport() {
        _editingReport.value = createNewBlankReport()
    }

    fun loadReportForEdit(report: ReportEntity) {
        _editingReport.value = report
    }

    fun updateFormField(updater: (ReportEntity) -> ReportEntity) {
        _editingReport.value = updater(_editingReport.value)
    }

    private fun createNewBlankReport(): ReportEntity {
        val now = Date()
        val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(now)
        val timeStr = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(now)
        return ReportEntity(
            loadMeasurementDate = dateStr,
            loadMeasurementTime = timeStr,
            surveyDate = dateStr
        )
    }

    fun captureGpsLocation() {
        _isGpsLoading.value = true
        locationHelper.fetchCurrentLocation(
            onLocationReceived = { coords ->
                _isGpsLoading.value = false
                updateFormField { it.copy(gpsLocation = coords) }
                _toastMessage.value = "GPS coordinates captured successfully!"
            },
            onError = { err ->
                _isGpsLoading.value = false
                _toastMessage.value = err
            }
        )
    }

    fun saveCurrentReport(onSuccess: () -> Unit) {
        val current = _editingReport.value
        if (current.dtrCode.isBlank()) {
            _toastMessage.value = "Please enter DTR Code"
            return
        }

        viewModelScope.launch {
            if (current.id == 0L) {
                repository.insertReport(current.copy(createdAt = System.currentTimeMillis(), updatedAt = System.currentTimeMillis()))
                _toastMessage.value = "Report saved successfully!"
            } else {
                repository.updateReport(current.copy(updatedAt = System.currentTimeMillis()))
                _toastMessage.value = "Report updated successfully!"
            }
            onSuccess()
        }
    }

    fun deleteReport(report: ReportEntity) {
        viewModelScope.launch {
            repository.deleteReport(report)
            _toastMessage.value = "Report deleted"
        }
    }

    fun duplicateReport(report: ReportEntity) {
        viewModelScope.launch {
            val now = Date()
            val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(now)
            val timeStr = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(now)
            val copy = report.copy(
                id = 0,
                dtrCode = "${report.dtrCode}-Copy",
                loadMeasurementDate = dateStr,
                loadMeasurementTime = timeStr,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            repository.insertReport(copy)
            _toastMessage.value = "Report duplicated"
        }
    }

    fun exportAllReportsToExcel(onExportSuccess: (java.io.File) -> Unit) {
        val list = allReports.value
        if (list.isEmpty()) {
            _toastMessage.value = "No reports to export"
            return
        }
        val file = ExcelExporter.exportToExcelCsv(getApplication(), list, "All_DTR_Reports")
        if (file != null) {
            onExportSuccess(file)
        } else {
            _toastMessage.value = "Failed to export Excel file"
        }
    }

    fun exportSingleReportToExcel(report: ReportEntity, onExportSuccess: (java.io.File) -> Unit) {
        val codeName = if (report.dtrCode.isNotBlank()) report.dtrCode.replace("/", "_") else "DTR"
        val file = ExcelExporter.exportToExcelCsv(getApplication(), listOf(report), "Report_$codeName")
        if (file != null) {
            onExportSuccess(file)
        } else {
            _toastMessage.value = "Failed to export Excel file"
        }
    }
}
