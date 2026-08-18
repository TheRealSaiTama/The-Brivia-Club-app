package com.briviaclub.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey val transactionId: String,
    val userId: String,
    val amount: Double,
    val currency: String = "USD",
    val planName: String,
    val gateway: String, // "Razorpay UPI", "Stripe Cards", "Netbanking"
    val status: String, // "success", "failed", "refunded"
    val timestamp: Long = System.currentTimeMillis(),
    val invoiceNumber: String,
    val couponCode: String = ""
)
