package my.edu.aiu.tdminsight.engine

import kotlin.math.exp
import kotlin.math.ln

/**
 * Calculation engine, kept fully separate from any Composable/UI function
 * (case study requirement: "Do not embed calculation logic directly in
 * Composable or UI functions").
 *
 * Flow: UI -> Input State -> Validation -> this engine -> Result Model -> Results UI
 *
 * Each CalculationStep is tagged with a StepCategory so the results screen can group
 * them into the case study's required explanation flow:
 * Input Values -> Intermediate Values -> Pharmacokinetic Parameters -> Final Result
 *
 * TODO (you): expand validate() with the full validation checklist from the case study
 * (required-field, numeric/unit, range, cross-field timing checks, missing-value
 * detection for the selected workflow) before submission. What's here is a starting
 * foundation, not the complete validation layer the rubric expects.
 */
object VancomycinCalculationEngine {

    // Population constants for the hybrid 1-compartment model.
    // CONFIRM these with your lecturer-approved reference before final submission.
    private const val VD_PER_KG_DEFAULT = 0.7      // L/kg (typical adult range 0.4-0.9 L/kg)
    private const val KE_POP_INTERCEPT = 0.00083   // hr^-1
    private const val KE_POP_CRCL_SLOPE = 0.0044    // hr^-1 per (mL/min)

    /** Cockcroft-Gault creatinine clearance — standard renal-function input for Ke estimate. */
    fun creatinineClearance(patient: PatientInfo): Double {
        val scrMgDl = patient.serumCreatinineUmolL / 88.4
        val base = ((140 - patient.ageYears) * patient.weightKg) / (72 * scrMgDl)
        return if (patient.isMale) base else base * 0.85
    }

    /** Basic guard used by all three workflows before dividing by a rate constant. */
    private fun requirePositive(value: Double, warnings: MutableList<String>, label: String): Boolean {
        if (value <= 0.0 || value.isNaN()) {
            warnings += "$label calculated as zero, negative, or invalid — check your inputs " +
                    "(e.g. sample timing, levels drawn in the wrong order)."
            return false
        }
        return true
    }

    fun calculatePre(patient: PatientInfo, dose: DoseInfo, input: WorkflowInput.Pre): TdmResult {
        val steps = mutableListOf<CalculationStep>()
        val warnings = mutableListOf<String>()

        val crCl = creatinineClearance(patient)
        val vd = (input.vdOverrideLPerKg ?: VD_PER_KG_DEFAULT) * patient.weightKg
        steps += CalculationStep(
            "Volume of distribution (population)", "Vd = weight x Vd/kg", vd, "L",
            StepCategory.PARAMETER
        )

        val ke = KE_POP_INTERCEPT + (crCl * KE_POP_CRCL_SLOPE)
        steps += CalculationStep(
            "Elimination rate constant (population)", "Ke = a + (CrCl x b)", ke, "hr^-1",
            StepCategory.PARAMETER
        )
        val keOk = requirePositive(ke, warnings, "Ke")

        val halfLife = if (keOk) ln(2.0) / ke else Double.NaN
        steps += CalculationStep(
            "Half-life", "t1/2 = ln(2) / Ke", halfLife, "hr",
            StepCategory.PARAMETER
        )

        val expectedCmax = if (keOk) {
            input.preLevelMgL * exp(ke * (dose.intervalHours - dose.infusionDurationHours))
        } else Double.NaN
        steps += CalculationStep(
            "Expected Cmax (assuming Cmin = pre-level)",
            "Cmax = Cmin x e^(Ke x (tau - tinf))",
            expectedCmax, "mg/L",
            StepCategory.RESULT
        )

        return TdmResult(VancomycinWorkflow.PRE, ke, halfLife, vd, steps = steps, warnings = warnings)
    }

    fun calculatePost(patient: PatientInfo, dose: DoseInfo, input: WorkflowInput.Post): TdmResult {
        val steps = mutableListOf<CalculationStep>()
        val warnings = mutableListOf<String>()

        val crCl = creatinineClearance(patient)
        val vd = VD_PER_KG_DEFAULT * patient.weightKg
        steps += CalculationStep(
            "Volume of distribution (population)", "Vd = weight x Vd/kg", vd, "L",
            StepCategory.PARAMETER
        )

        val ke = KE_POP_INTERCEPT + (crCl * KE_POP_CRCL_SLOPE)
        steps += CalculationStep(
            "Elimination rate constant (population)", "Ke = a + (CrCl x b)", ke, "hr^-1",
            StepCategory.PARAMETER
        )
        val keOk = requirePositive(ke, warnings, "Ke")

        val halfLife = if (keOk) ln(2.0) / ke else Double.NaN
        steps += CalculationStep(
            "Half-life", "t1/2 = ln(2) / Ke", halfLife, "hr",
            StepCategory.PARAMETER
        )

        val newDose = if (keOk) {
            ke * vd * input.desiredCmaxMgL *
                    ((1 - exp(-ke * dose.intervalHours)) / (1 - exp(-ke * dose.infusionDurationHours)))
        } else Double.NaN
        steps += CalculationStep(
            "New maintenance dose",
            "MD = Ke x Vd x Cmax x [(1 - e^(-Ke*tau)) / (1 - e^(-Ke*tinf))]",
            newDose, "mg",
            StepCategory.RESULT
        )

        val expectedCmin = if (keOk) {
            input.desiredCmaxMgL * exp(-ke * (dose.intervalHours - dose.infusionDurationHours))
        } else Double.NaN
        steps += CalculationStep(
            "Expected Cmin with new dose",
            "Cmin = Cmax x e^(-Ke x (tau - tinf))",
            expectedCmin, "mg/L",
            StepCategory.RESULT
        )

        return TdmResult(VancomycinWorkflow.POST, ke, halfLife, vd, steps = steps, warnings = warnings)
    }

    fun calculatePrePost(patient: PatientInfo, dose: DoseInfo, input: WorkflowInput.PrePost): TdmResult {
        val steps = mutableListOf<CalculationStep>()
        val warnings = mutableListOf<String>()

        if (input.intervalBetweenSamplesHours <= 0.0) {
            warnings += "Time between pre and post samples must be greater than zero."
        }
        if (input.preLevelMgL <= 0.0 || input.postLevelMgL <= 0.0) {
            warnings += "Concentration levels must be greater than zero (cannot take ln of 0 or negative)."
        }

        // Patient-specific Ke from two measured levels (Sawchuk-Zaske)
        val ke = if (input.intervalBetweenSamplesHours > 0.0 && input.preLevelMgL > 0.0 && input.postLevelMgL > 0.0) {
            (ln(input.postLevelMgL) - ln(input.preLevelMgL)) / input.intervalBetweenSamplesHours
        } else Double.NaN
        steps += CalculationStep(
            "Elimination rate constant (patient-specific)",
            "Ke = [ln(Cpost) - ln(Cpre)] / (t2 - t1)",
            ke, "hr^-1",
            StepCategory.INTERMEDIATE
        )
        val keOk = requirePositive(ke, warnings, "Ke")
        if (keOk.not()) {
            warnings += "If Ke is negative, check that the post-dose level was drawn AFTER the " +
                    "pre-dose level, and during the elimination (not distribution) phase."
        }

        val halfLife = if (keOk) ln(2.0) / ke else Double.NaN
        steps += CalculationStep(
            "Half-life", "t1/2 = ln(2) / Ke", halfLife, "hr",
            StepCategory.PARAMETER
        )

        val trueCmax = if (keOk) {
            input.postLevelMgL / exp(-ke * input.samplingTimeAfterInfusionHours)
        } else Double.NaN
        steps += CalculationStep(
            "True Cmax (back-extrapolated to end of infusion)",
            "Cmax(true) = Cpost / e^(-Ke x t)",
            trueCmax, "mg/L",
            StepCategory.INTERMEDIATE
        )

        val trueCmin = if (keOk) {
            trueCmax * exp(-ke * (dose.intervalHours - dose.infusionDurationHours))
        } else Double.NaN
        steps += CalculationStep(
            "True Cmin (extrapolated)",
            "Cmin(true) = Cmax(true) x e^(-Ke x (tau - tinf))",
            trueCmin, "mg/L",
            StepCategory.INTERMEDIATE
        )

        val vdDenominator = if (keOk) ke * (trueCmax - trueCmin * exp(-ke * dose.infusionDurationHours)) else 0.0
        val vd = if (keOk && vdDenominator != 0.0) {
            (dose.doseMg / dose.infusionDurationHours) *
                    (1 - exp(-ke * dose.infusionDurationHours)) / vdDenominator
        } else {
            warnings += "Volume of distribution could not be calculated (division by zero) — check Cmax/Cmin inputs."
            Double.NaN
        }
        steps += CalculationStep(
            "Volume of distribution (patient-specific)",
            "Vd = [Dose/tinf x (1 - e^(-Ke*tinf))] / [Ke x (Cmax - Cmin x e^(-Ke*tinf))]",
            vd, "L",
            StepCategory.PARAMETER
        )

        val clearance = if (!vd.isNaN()) ke * vd else Double.NaN
        steps += CalculationStep(
            "Clearance", "CL = Ke x Vd", clearance, "L/hr",
            StepCategory.PARAMETER
        )

        val auc24 = if (!clearance.isNaN() && clearance != 0.0) {
            (dose.doseMg * (24.0 / dose.intervalHours)) / clearance
        } else Double.NaN
        steps += CalculationStep(
            "AUC (24 hr)", "AUC24 = (Dose x 24/tau) / CL", auc24, "mg.hr/L",
            StepCategory.RESULT
        )

        val newDose = if (keOk && !vd.isNaN()) {
            ke * vd * input.desiredCmaxMgL *
                    ((1 - exp(-ke * dose.intervalHours)) / (1 - exp(-ke * dose.infusionDurationHours)))
        } else Double.NaN
        steps += CalculationStep(
            "New maintenance dose",
            "MD = Ke x Vd x Cmax(desired) x [(1 - e^(-Ke*tau)) / (1 - e^(-Ke*tinf))]",
            newDose, "mg",
            StepCategory.RESULT
        )

        return TdmResult(VancomycinWorkflow.PRE_POST, ke, halfLife, vd, clearance, auc24, steps, warnings)
    }
}