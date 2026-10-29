package com.tuskanddot.tuskanddotproject.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tuskanddot.tuskanddotproject.data.entities.AffirmationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AffirmationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(affirmations: List<AffirmationEntity>)

    @Query("SELECT * FROM affirmations ORDER BY id ASC")
    fun getAllAffirmations(): Flow<List<AffirmationEntity>>

    @Query("""
        SELECT * FROM affirmations
        WHERE topic = :topic
        ORDER BY id ASC
    """)
    fun getAffirmationsByTopic(topic: String): Flow<List<AffirmationEntity>>

    @Query("""
        SELECT * FROM affirmations
        WHERE isFavorite = 1
        ORDER BY id ASC
    """)
    fun getFavorites(): Flow<List<AffirmationEntity>>

    @Query("""
        UPDATE affirmations
        SET isFavorite = :isFavorite
        WHERE id = :id
    """)
    suspend fun updateFavorite(id: String, isFavorite: Boolean)

    @Query("SELECT COUNT(*) FROM affirmations")
    suspend fun getCount(): Int
}