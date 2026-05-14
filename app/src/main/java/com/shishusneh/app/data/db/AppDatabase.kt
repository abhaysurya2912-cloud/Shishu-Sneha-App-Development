package com.shishusneh.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shishusneh.app.data.db.dao.DiaperDao
import com.shishusneh.app.data.db.dao.FeedingDao
import com.shishusneh.app.data.db.dao.GrowthRecordDao
import com.shishusneh.app.data.db.dao.MilestoneDao
import com.shishusneh.app.data.db.dao.MotherProfileDao
import com.shishusneh.app.data.db.dao.SleepDao
import com.shishusneh.app.data.db.dao.SymptomDao
import com.shishusneh.app.data.db.dao.VaccineDoneDao
import com.shishusneh.app.data.db.entities.DiaperRecord
import com.shishusneh.app.data.db.entities.FeedingRecord
import com.shishusneh.app.data.db.entities.GrowthRecord
import com.shishusneh.app.data.db.entities.MilestoneRecord
import com.shishusneh.app.data.db.entities.MotherProfile
import com.shishusneh.app.data.db.entities.SleepRecord
import com.shishusneh.app.data.db.entities.SymptomRecord
import com.shishusneh.app.data.db.entities.VaccineDoneRecord

@Database(
    entities = [
        MotherProfile::class, 
        GrowthRecord::class, 
        MilestoneRecord::class, 
        VaccineDoneRecord::class,
        FeedingRecord::class,
        SleepRecord::class,
        SymptomRecord::class,
        DiaperRecord::class
    ],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun motherProfileDao(): MotherProfileDao
    abstract fun growthRecordDao(): GrowthRecordDao
    abstract fun milestoneDao(): MilestoneDao
    abstract fun vaccineDoneDao(): VaccineDoneDao
    abstract fun feedingDao(): FeedingDao
    abstract fun sleepDao(): SleepDao
    abstract fun symptomDao(): SymptomDao
    abstract fun diaperDao(): DiaperDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "shishu_sneh_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
