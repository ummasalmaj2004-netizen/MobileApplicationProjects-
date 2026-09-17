package my.edu.aiu.tdminsight.engine


enum class VancomycinWorkflow { PRE, POST, PRE_POST }

data class PatientInfo(
    val weightKg: Double,
    val ageYears: Int,
    val serumCreatinineUmolL: Double,
    val isMale: Boolean
)

data class DoseInfo(
    val doseMg: Double,
    val intervalHours: Double,        // tau (τ) — dosing interval
    val infusionDurationHours: Double // tinf — length of the IV infusion
)

/** Inputs specific to each workflow — drives the dynamic form. */
sealed class WorkflowInput {
    data class Pre(
        val preLevelMgL: Double,              // measured trough / pre-dose level
        val vdOverrideLPerKg: Double? = null   // optional manual override
    ) : WorkflowInput()

    data class Post(
        val postLevelMgL: Double,
        val samplingTimeAfterInfusionHours: Double, // t: end-of-infusion -> sample time
        val desiredCmaxMgL: Double
    ) : WorkflowInput()

    data class PrePost(
        val preLevelMgL: Double,
        val postLevelMgL: Double,
        val samplingTimeAfterInfusionHours: Double,  // t: end-of-infusion -> post sample
        val intervalBetweenSamplesHours: Double,     // t2 - t1: between pre & post draws
        val desiredCmaxMgL: Double
    ) : WorkflowInput()
}

/**
 * Matches the case study's required explanation flow:
 * Input Values -> Intermediate Values -> Pharmacokinetic Parameters -> Final Result
 */
enum class StepCategory { INTERMEDIATE, PARAMETER, RESULT }

/** One line of the "explainable results" trail: Input -> Intermediate -> Result. */
data class CalculationStep(
    val label: String,
    val formula: String,
    val value: Double,
    val unit: String,
    val category: StepCategory
)

data class TdmResult(
    val workflow: VancomycinWorkflow,
    val ke: Double,
    val halfLifeHours: Double,
    val vdLiters: Double,
    val clearanceLPerHour: Double? = null,
    val auc24: Double? = null,
    val steps: List<CalculationStep>,
    val warnings: List<String> = emptyList()
)