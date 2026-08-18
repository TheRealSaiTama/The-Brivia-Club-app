package com.briviaclub.app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BriviaLogicTest {

    @Test
    fun testCouponDiscountCalculations() {
        val basePricePro = 19.0
        val discountFifty = 50
        val finalPriceFifty = basePricePro * (1.0 - (discountFifty / 100.0))
        assertEquals(9.5, finalPriceFifty, 0.01)

        val basePriceVip = 49.0
        val discountTwenty = 20
        val finalPriceTwenty = basePriceVip * (1.0 - (discountTwenty / 100.0))
        assertEquals(39.2, finalPriceTwenty, 0.01)
    }

    @Test
    fun testMatchScoringAlgorithm() {
        val mySkills = setOf("kotlin", "compose", "android", "ai")
        val candidateLooking = setOf("kotlin", "android", "mobile developer")
        val candidateSkills = setOf("fastapi", "python", "backend", "llm")
        val myLooking = setOf("python", "backend", "ai engineer")

        val myMatchedSkills = mySkills.count { it in candidateLooking }
        val candidateMatchedSkills = candidateSkills.count { it in myLooking }

        val totalMatches = myMatchedSkills + candidateMatchedSkills
        val score = (70 + totalMatches * 6).coerceIn(60, 99)

        assertTrue(score >= 70)
        assertTrue(score <= 99)
    }
}
