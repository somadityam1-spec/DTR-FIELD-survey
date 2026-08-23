package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.ReportDao
import com.example.data.model.ReportEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ReportEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reportDao(): ReportDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "field_supervisor_report.db"
                )
                .fallbackToDestructiveMigration()
                .addCallback(DatabaseCallback(context))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val context: Context
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Seed initial field report sample so field supervisor can immediately see layout
                CoroutineScope(Dispatchers.IO).launch {
                    val dao = getDatabase(context).reportDao()
                    dao.insertReport(
                        ReportEntity(
                            dtrCode = "DTR-042",
                            dtrCapacity = "63",
                            dtrLocation = "Rampur North Substation",
                            gpsLocation = "23.2415° N, 87.8631° E",
                            loadStatus = "Moderate load",
                            loadRPhase = "72",
                            loadYPhase = "68",
                            loadBPhase = "74",
                            loadNeutral = "12",
                            loadMeasurementDate = "2026-08-22",
                            loadMeasurementTime = "11:30 AM",
                            laFixing = "Fixed",
                            earthingCondition = "Good",
                            kioskFixing = "Fixed",
                            isolatorCondition = "Male Female OK",
                            fusingSystem = "Fuse wire 14",
                            ltLineType = "AB cable",
                            ltPoleCondition = "Normal",
                            ltLineWork = "Bracket required",
                            consumerType = "Agriculture",
                            surveyorName = "Rajesh Sharma",
                            surveyorDesignation = "Junior Engineer",
                            surveyAgency = "North Zone DISCOM",
                            surveyDate = "2026-08-22",
                            supervisorName = "Field Supervisor 1",
                            remarks = "DTR is working smoothly, tree branches trimmed near pole."
                        )
                    )
                    dao.insertReport(
                        ReportEntity(
                            dtrCode = "DTR-108",
                            dtrCapacity = "25",
                            dtrLocation = "Kalyanpur Market Area",
                            gpsLocation = "23.2502° N, 87.8710° E",
                            loadStatus = "Over Load",
                            loadRPhase = "42",
                            loadYPhase = "44",
                            loadBPhase = "41",
                            loadNeutral = "18",
                            loadMeasurementDate = "2026-08-22",
                            loadMeasurementTime = "06:45 PM",
                            laFixing = "LA Required",
                            earthingCondition = "Damaged",
                            kioskFixing = "Not Fixed",
                            isolatorCondition = "Male Female Required",
                            fusingSystem = "Fuse Required",
                            ltLineType = "Bare conductor",
                            ltPoleCondition = "Straightening",
                            ltLineWork = "Sagging Required, Mid Gap pole required",
                            consumerType = "Industrial",
                            surveyorName = "Amit Verma",
                            surveyorDesignation = "Line Supervisor",
                            surveyAgency = "Kalyanpur Feeder Survey Team",
                            surveyDate = "2026-08-22",
                            supervisorName = "Field Supervisor 1",
                            remarks = "Urgent attention required: High evening peak load & damaged earthing."
                        )
                    )
                }
            }
        }
    }
}
