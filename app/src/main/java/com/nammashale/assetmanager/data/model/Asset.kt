package com.nammashale.assetmanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a physical asset in the school/institution.
 *
 * This is a Room @Entity — each instance becomes a row in the "assets" table.
 * Room auto-generates the SQLite CREATE TABLE statement from this class.
 */
@Entity(tableName = "assets")
data class Asset(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    /** Display name of the asset (e.g. "Wooden Chair", "Projector") */
    val name: String,

    /** Unique serial/inventory number */
    val serialNo: String,

    /** Category: Furniture, Electronics, Sports Equipment, Books, Lab Equipment, Others */
    val category: String,

    /** Current condition: "Working", "Needs Repair", "Broken" */
    val condition: String = "Working",

    /** File path to the captured photo (null if no photo taken) */
    val photoPath: String? = null,

    /** Timestamp when the asset was first registered (epoch millis) */
    val createdAt: Long = System.currentTimeMillis(),

    /** Timestamp of the last update (epoch millis) */
    val updatedAt: Long = System.currentTimeMillis()
)
