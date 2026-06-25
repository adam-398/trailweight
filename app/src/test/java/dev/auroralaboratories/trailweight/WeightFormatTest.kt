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

}

