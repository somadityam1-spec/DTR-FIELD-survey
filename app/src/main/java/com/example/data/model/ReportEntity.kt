package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "field_reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // DTR Details
    val dtrCode: String = "",
    val dtrCapacity: String = "25", // 10, 16, 25, 63, 100 kVA
    val dtrLocation: String = "",
    val gpsLocation: String = "",
    val loadStatus: String = "Moderate load", // Over Load, Under Load, Moderate load
    val loadRPhase: String = "",
    val loadYPhase: String = "",
    val loadBPhase: String = "",
    val loadNeutral: String = "",
    val loadMeasurementDate: String = "",
    val loadMeasurementTime: String = "",
    val laFixing: String = "Fixed", // Fixed, Not Fixed, LA Required
    val earthingCondition: String = "Good", // Good, Damaged, Earthing Required, Not OK
    val kioskFixing: String = "Fixed", // Fixed, Not Fixed, Kiosk Required
    val isolatorCondition: String = "Male Female OK", // Male Female OK, Male Female Required, Damaged, Direct post insulator
    val fusingSystem: String = "Fuse wire 14", // Fuse wire 12, Fuse wire 14, Fuse wire 16, Fuse Required, Not Required

    // LT Line Details
    val ltLineType: String = "Bare conductor", // AB cable, Bare conductor, PVC cable
    val ltPoleCondition: String = "Normal", // Normal, Broken, Straightening, Earthing Pole required, Height required
    val ltLineWork: String = "", // Sagging Required, Mid Gap pole required, Bracket required, D-iron & shackle Required, Conversion required 1Ph to 3 Ph

    // Consumer Type
    val consumerType: String = "Village", // Village, Agriculture, Industrial, Single user

    // Who will survey & Field personnel details
    val surveyorName: String = "", // Person who will survey / conducting survey
    val surveyorDesignation: String = "Junior Engineer", // Junior Engineer, Line Supervisor, Survey Officer, Lineman, Agency Inspector
    val surveyAgency: String = "", // Sub-division / Feeder / Survey Agency
    val surveyDate: String = "", // Date of survey
    val supervisorName: String = "", // Supervisor in-charge / Approver
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
