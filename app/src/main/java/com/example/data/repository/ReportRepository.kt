package com.example.data.repository

import com.example.data.dao.ReportDao
import com.example.data.model.ReportEntity
import kotlinx.coroutines.flow.Flow

class ReportRepository(private val reportDao: ReportDao) {
    val allReports: Flow<List<ReportEntity>> = reportDao.getAllReports()
    val reportCount: Flow<Int> = reportDao.getReportCount()
    val overloadCount: Flow<Int> = reportDao.getOverloadCount()

    fun getReportById(id: Long): Flow<ReportEntity?> = reportDao.getReportById(id)

    suspend fun getReportByIdDirect(id: Long): ReportEntity? = reportDao.getReportByIdDirect(id)

    suspend fun getAllReportsDirect(): List<ReportEntity> = reportDao.getAllReportsDirect()

    fun searchReports(query: String): Flow<List<ReportEntity>> = reportDao.searchReports(query)

    suspend fun insertReport(report: ReportEntity): Long = reportDao.insertReport(report)

    suspend fun updateReport(report: ReportEntity) = reportDao.updateReport(report)

    suspend fun deleteReport(report: ReportEntity) = reportDao.deleteReport(report)

    suspend fun deleteReportById(id: Long) = reportDao.deleteReportById(id)
}
