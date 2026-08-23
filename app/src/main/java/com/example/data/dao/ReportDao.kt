package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ReportEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {
    @Query("SELECT * FROM field_reports ORDER BY createdAt DESC")
    fun getAllReports(): Flow<List<ReportEntity>>

    @Query("SELECT * FROM field_reports WHERE id = :id")
    fun getReportById(id: Long): Flow<ReportEntity?>

    @Query("SELECT * FROM field_reports WHERE id = :id")
    suspend fun getReportByIdDirect(id: Long): ReportEntity?

    @Query("SELECT * FROM field_reports ORDER BY createdAt DESC")
    suspend fun getAllReportsDirect(): List<ReportEntity>

    @Query("SELECT COUNT(*) FROM field_reports")
    fun getReportCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM field_reports WHERE loadStatus = 'Over Load'")
    fun getOverloadCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ReportEntity): Long

    @Update
    suspend fun updateReport(report: ReportEntity)

    @Delete
    suspend fun deleteReport(report: ReportEntity)

    @Query("DELETE FROM field_reports WHERE id = :id")
    suspend fun deleteReportById(id: Long)

    @Query("SELECT * FROM field_reports WHERE dtrCode LIKE '%' || :query || '%' OR dtrLocation LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchReports(query: String): Flow<List<ReportEntity>>
}
