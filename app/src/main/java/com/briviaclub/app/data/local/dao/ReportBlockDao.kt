package com.briviaclub.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.briviaclub.app.data.local.entity.ReportBlockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportBlockDao {
    @Query("SELECT targetUserId FROM reports_blocks WHERE reporterId = :userId AND type = 'block'")
    suspend fun getBlockedUserIds(userId: String): List<String>

    @Query("SELECT * FROM reports_blocks ORDER BY timestamp DESC")
    fun getAllReportsAndBlocksFlow(): Flow<List<ReportBlockEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReportOrBlock(entry: ReportBlockEntity)

    @Query("DELETE FROM reports_blocks WHERE reporterId = :userId AND targetUserId = :targetUserId AND type = 'block'")
    suspend fun unblockUser(userId: String, targetUserId: String)
}
