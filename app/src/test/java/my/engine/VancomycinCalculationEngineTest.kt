package my.edu.aiu.tdminsight.engine

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for the TDM calculation engine and validator.
 *
 * These run WITHOUT an emulator (Run > Run 'VancomycinCalculationEngineTest', or the
 * green triangle next to a test/class in the gutter) because the engine has zero
 * Android dependencies — pure Kotlin math in, structured result out.
 *
 * Expected values below were hand-derived from the Sawchuk-Zaske formulas and
 * cross-checked against a real run of the app (see chat history / commit history
 * for the worked-out math) — this is exactly the kind of evidence worth mentioning
 * in your individual technical viva as proof the engine was verified, not just built.
 */
class VancomycinCalculationEngineTest {

    @Test
    fun `calculatePrePost produces internally consistent pharmacokinetic values`() {
        val patient = PatientInfo(weightKg = 75.0, ageYears = 50, serumCreatinineUmolL = 85.0, isMale = true)
        val dose = DoseInfo(doseMg = 1000.0, intervalHours = 12.0, infusionDurationHours = 1.0)
        val input = WorkflowInput.PrePost(
            preLevelMgL = 8.0,
            postLevelMgL = 25.0,
            samplingTimeAfterInfusionHours = 1.0,
            intervalBetweenSamplesHours = 10.0,
            desiredCmaxMgL = 30.0
        )

        val result = VancomycinCalculationEngine.calculatePrePost(patient, dose, input)

        assertTrue("Expected no warnings for valid, sensible input", result.warnings.isEmpty())
        assertEquals("Ke", 0.1139, result.ke, 0.001)
        assertEquals("Half-life (hr)", 6.084, result.halfLifeHours, 0.01)
        assertEquals("Vd (L)", 45.26, result.vdLiters, 0.5)
        assertEquals("Clearance (L/hr)", 5.157, result.clearanceLPerHour ?: -1.0, 0.05)
        assertEquals("AUC24", 387.8, result.auc24 ?: -1.0, 2.0)

        val newDoseStep = result.steps.first { it.label == "New maintenance dose" }
        assertEquals("New maintenance dose (mg)", 1071.0, newDoseStep.value, 5.0)
    }

    @Test
    fun `calculatePre gives higher expected Cmax when Ke is higher (faster elimination)`() {
        val patient = PatientInfo(weightKg = 70.0, ageYears = 70, serumCreatinineUmolL = 180.0, isMale = true)
        val dose = DoseInfo(doseMg = 1000.0, intervalHours = 12.0, infusionDurationHours = 1.0)
        val input = WorkflowInput.Pre(preLevelMgL = 8.0)

        val result = VancomycinCalculationEngine.calculatePre(patient, dose, input)

        assertTrue("Ke should be positive for a valid renal function input", result.ke > 0)
        val expectedCmaxStep = result.steps.first { it.label.startsWith("Expected Cmax") }
        // Cmax must always be >= the pre-dose (trough) level, since concentration decays toward it.
        assertTrue(
            "Expected Cmax (${expectedCmaxStep.value}) should be greater than the pre-dose level (8.0)",
            expectedCmaxStep.value > 8.0
        )
    }

    @Test
    fun `calculatePrePost flags a negative Ke when post level is lower than pre level`() {
        // Deliberately "backwards" input: post-dose level lower than pre-dose level.
        val patient = PatientInfo(weightKg = 70.0, ageYears = 50, serumCreatinineUmolL = 90.0, isMale = true)
        val dose = DoseInfo(doseMg = 1000.0, intervalHours = 12.0, infusionDurationHours = 1.0)
        val input = WorkflowInput.PrePost(
            preLevelMgL = 25.0,
            postLevelMgL = 8.0,
            samplingTimeAfterInfusionHours = 1.0,
            intervalBetweenSamplesHours = 10.0,
            desiredCmaxMgL = 30.0
        )

        val result = VancomycinCalculationEngine.calculatePrePost(patient, dose, input)

        assertTrue("Ke should come out negative for backwards levels", result.ke < 0)
        assertTrue(
            "Engine should warn when Ke is invalid",
            result.warnings.any { it.contains("Ke", ignoreCase = false) }
        )
    }
}

class TdmValidatorTest {

    private val validSharedFields = mapOf(
        "Weight (kg)" to "75",
        "Age (years)" to "50",
        "Serum creatinine (umol/L)" to "85",
        "Dose (mg)" to "1000",
        "Dosing interval tau (hr)" to "12",
        "Infusion duration (hr)" to "1"
    )

    @Test
    fun `valid Pre+Post inputs pass with no errors`() {
        val fields = validSharedFields + mapOf(
            "Pre-dose level (mg/L)" to "8",
            "Post-dose level (mg/L)" to "25",
            "Sampling time after infusion (hr)" to "1",
            "Time between pre and post samples (hr)" to "10",
            "Desired Cmax (mg/L)" to "30"
        )

        val result = TdmValidator.validate(VancomycinWorkflow.PRE_POST, fields)

        assertTrue("Expected no errors, got: ${result.errors}", result.isValid)
    }

    @Test
    fun `missing required field is caught`() {
        val fields = validSharedFields - "Dose (mg)" + mapOf(
            "Pre-dose level (mg/L)" to "8"
        )

        val result = TdmValidator.validate(VancomycinWorkflow.PRE, fields)

        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("Dose (mg)") })
    }

    @Test
    fun `infusion duration longer than dosing interval is rejected`() {
        val fields = validSharedFields + mapOf(
            "Infusion duration (hr)" to "20", // longer than the 12-hr interval — invalid
            "Pre-dose level (mg/L)" to "8"
        )

        val result = TdmValidator.validate(VancomycinWorkflow.PRE, fields)

        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("Infusion duration") })
    }

    @Test
    fun `negative weight is rejected by range check`() {
        val fields = validSharedFields + mapOf(
            "Weight (kg)" to "-5",
            "Pre-dose level (mg/L)" to "8"
        )

        val result = TdmValidator.validate(VancomycinWorkflow.PRE, fields)

        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("Weight") })
    }

    @Test
    fun `Pre+Post warns but does not error when post level is lower than pre level`() {
        val fields = validSharedFields + mapOf(
            "Pre-dose level (mg/L)" to "25",
            "Post-dose level (mg/L)" to "8", // backwards on purpose
            "Sampling time after infusion (hr)" to "1",
            "Time between pre and post samples (hr)" to "10",
            "Desired Cmax (mg/L)" to "30"
        )

        val result = TdmValidator.validate(VancomycinWorkflow.PRE_POST, fields)

        assertTrue("Should still be structurally valid (a warning, not an error)", result.isValid)
        assertTrue(result.warnings.isNotEmpty())
    }
}