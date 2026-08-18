package com.briviaclub.app.util

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.EnumMap

object QrCodeGenerator {

    /**
     * Generates an Android Bitmap for a given content string using ZXing.
     */
    fun generateQrBitmap(
        content: String,
        size: Int = 512,
        darkColor: Int = Color.BLACK,
        lightColor: Int = Color.WHITE,
        margin: Int = 1
    ): Bitmap? {
        if (content.isBlank()) return null
        return try {
            val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java).apply {
                put(EncodeHintType.CHARACTER_SET, "UTF-8")
                put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H)
                put(EncodeHintType.MARGIN, margin)
            }

            val bitMatrix = QRCodeWriter().encode(
                content,
                BarcodeFormat.QR_CODE,
                size,
                size,
                hints
            )

            val width = bitMatrix.width
            val height = bitMatrix.height
            val pixels = IntArray(width * height)

            for (y in 0 until height) {
                val offset = y * width
                for (x in 0 until width) {
                    pixels[offset + x] = if (bitMatrix.get(x, y)) darkColor else lightColor
                }
            }

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Generates a structured club check-in payload string for event door scanning.
     */
    fun buildCheckInPayload(
        userId: String,
        userName: String,
        tierName: String,
        location: String
    ): String {
        val memberCode = formatMemberId(userId, tierName)
        return "BRIVIA_PASS::$memberCode::$userId::$userName::$tierName::$location::${System.currentTimeMillis()}"
    }

    /**
     * Formats raw user ID into an elite membership ID like BC-GOLD-9482
     */
    fun formatMemberId(userId: String, tierName: String): String {
        val tierPrefix = when (tierName.lowercase()) {
            "founder_vip", "gold", "founder vip" -> "GOLD"
            "pro", "silver", "pro builder" -> "SLVR"
            else -> "COMM"
        }
        val hashSegment = Math.abs(userId.hashCode() % 9000 + 1000)
        return "BC-$tierPrefix-$hashSegment"
    }
}
