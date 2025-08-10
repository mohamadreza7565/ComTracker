package com.app.comtracker.data.di

import android.content.Context
import com.app.comtracker.data.local.RoomAppDatabase
import com.app.comtracker.data.local.createDataBaseInstance
import com.app.comtracker.data.local.dao.TrackerHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class LocalModule {

    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context): RoomAppDatabase {
        return createDataBaseInstance(context)
    }

    @Provides
    fun provideTrackerHistoryDao(db: RoomAppDatabase): TrackerHistoryDao = db.trackerHistoryDao()
}