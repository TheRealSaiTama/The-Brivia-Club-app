package com.briviaclub.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.briviaclub.app.data.local.entity.PaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {
    @Query("SELECT * FROM payments WHERE userId = :userId ORDER BY timestamp DESC")
    fun getPaymentsForUserFlow(userId: String): Flow<List<PaymentEntity>>

    @Query("SELECT * FROM payments ORDER BY timestamp DESC")
    fun getAllPaymentsFlow(): Flow<List<PaymentEntity>>

    @Query("SELECT SUM(amount) FROM payments WHERE status = 'success'")
    fun getTotalRevenueFlow(): Flow<Double?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentEntity)
}
