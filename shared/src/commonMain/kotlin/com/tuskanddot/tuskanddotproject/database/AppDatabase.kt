package com.tuskanddot.tuskanddotproject.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import com.tuskanddot.tuskanddotproject.data.entities.AffirmationEntity
import com.tuskanddot.tuskanddotproject.database.dao.AffirmationDao

@Database(entities = [AffirmationEntity::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAffirmationDao(): AffirmationDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}