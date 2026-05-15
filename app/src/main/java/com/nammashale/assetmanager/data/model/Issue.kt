package com.nammashale.assetmanager.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents an issue/problem logged against a specific asset.
 *
 * Uses a foreign key to link each issue to its parent Asset.
 * When an asset is deleted, all its issues are automatically deleted too (CASCADE).
 */
@Entity(
    tableName = "issues",
    foreignKeys = [
        ForeignKey(
            entity = Asset::class,
            parentColumns = ["id"],
            childColumns = ["assetId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["assetId"])]
)
data class Issue(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    /** The asset this issue belongs to */
    val assetId: Long,

    /** Description of the issue */
    val description: String,

    /** Date the issue was reported (epoch millis) */
    val date: Long = System.currentTimeMillis(),

    /** Whether this issue has been resolved */
    val resolved: Boolean = false
)
