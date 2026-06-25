package dev.auroralaboratories.trailweight

import dev.auroralaboratories.trailweight.preferences.UnitPreferences
import dev.auroralaboratories.trailweight.preferences.formatWeight
import junit.framework.TestCase.assertEquals
import org.junit.Test

class WeightFormatTest{

    @Test
    fun `grams to oz conversion is correct`(){
        UnitPreferences.setIsMetricForTesting(false)
        val weightInGrams = 1.0
        val result = formatWeight(weightInGrams)
        assertEquals("0.0 oz", result)
    }

    @Test
    fun `999 grams is correct`(){
        UnitPreferences.setIsMetricForTesting(true)
        val weightInGrams = 999.0
        val result = formatWeight(weightInGrams)
        assertEquals("999 g", result)
    }

    @Test
    fun `1000 grams is correct`(){
        UnitPreferences.setIsMetricForTesting(true)
        val weightInGrams = 1000.0
        val result = formatWeight(weightInGrams)
        assertEquals("1.00 kg", result)
    }

    @Test
    fun `1001 grams is correct`(){
        UnitPreferences.setIsMetricForTesting(true)
        val weightInGrams = 1001.0
        val result = formatWeight(weightInGrams)
        assertEquals("1.00 kg", result)
    }

    @Test
    fun `1oz gram is correct from grams`(){
        UnitPreferences.setIsMetricForTesting(false)
        val weightInGrams = 28.3495
        val result = formatWeight(weightInGrams)
        assertEquals("1.0 oz", result)
    }

    @Test
    fun `just under 1lb is correct from grams`() {
        UnitPreferences.setIsMetricForTesting(false)
        val weightInGrams = 453.591
        val result = formatWeight(weightInGrams)
        assertEquals("16.0 oz", result)
    }

    @Test
    fun `1lb is correct from grams`() {
        UnitPreferences.setIsMetricForTesting(false)
        val weightInGrams = 453.592
        val result = formatWeight(weightInGrams)
        assertEquals("1.00 lb", result)
    }

}