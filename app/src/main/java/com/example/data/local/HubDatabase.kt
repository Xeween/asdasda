package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.ActivityStreamEntity
import com.example.data.model.BadgeEntity
import com.example.data.model.FeedLogEntity
import com.example.data.model.MediaTelemetryEntity
import com.example.data.model.OperatorProfileEntity
import com.example.data.model.QuestEntity
import com.example.data.model.WorkshopProjectEntity

@Database(
    entities = [
        OperatorProfileEntity::class,
        FeedLogEntity::class,
        QuestEntity::class,
        WorkshopProjectEntity::class,
        MediaTelemetryEntity::class,
        BadgeEntity::class,
        ActivityStreamEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class HubDatabase : RoomDatabase() {
    abstract fun hubDao(): HubDao

    companion object {
        @Volatile
        private var INSTANCE: HubDatabase? = null

        fun getInstance(context: Context): HubDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    HubDatabase::class.java,
                    "hub_operator_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
