package com.tuskanddot.tuskanddotproject.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "affirmations")
data class AffirmationEntity(
    @PrimaryKey
    val id: String,
    val topic: String,
    val text: String,
    val isFavorite: Boolean = false
)