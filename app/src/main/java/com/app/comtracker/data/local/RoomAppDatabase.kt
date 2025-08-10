package com.app.comtracker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.comtracker.data.local.dao.TrackerHistoryDao
import com.app.comtracker.data.model.TrackerHistoryDTO

private const val DATABASE_NAME = "com_tracker_database"

@Database(
    autoMigrations = [],
    entities = [TrackerHistoryDTO::class],
    exportSchema = true,
    version = 1
)
internal abstract class RoomAppDatabase : RoomDatabase() {
    abstract fun trackerHistoryDao(): TrackerHistoryDao
}

internal fun createDataBaseInstance(context: Context): RoomAppDatabase {
    return Room.databaseBuilder(
        context,
        RoomAppDatabase::class.java,
        DATABASE_NAME
    ).build()
}