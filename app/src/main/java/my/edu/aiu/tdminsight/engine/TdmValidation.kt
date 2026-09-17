package my.edu.aiu.tdminsight.engine

data class ValidationResult(
    val errors: List<String> = emptyList(),
    val warnings: List<String> = emptyList()
) {
    val isValid: Boolean get() = errors.isEmpty()
}

object TdmValidator {

    /** Required + numeric check for one field. Adds to [errors] if missing/invalid. */
    private fun parse(fields: Map<String, String>, key: String, errors: MutableList<String>): Double? {
        val raw = fields[key]?.trim()
        if (raw.isNullOrEmpty()) {
            errors += "\"$key\" is required."
            return null
        }
        val value = raw.toDoubleOrNull()
        if (value == null) {
            errors += "\"$key\" must be a valid number (got \"$raw\")."
        }
        return value
    }

    fun validate(workflow: VancomycinWorkflow, fields: Map<String, String>): ValidationResult {
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        // --- Shared fields: required + numeric ---
        val weight = parse(fields, "Weight (kg)", errors)
        val age = parse(fields, "Age (years)", errors)
        val scr = parse(fields, "Serum creatinine (umol/L)", errors)
        val dose = parse(fields, "Dose (mg)", errors)
        val tau = parse(fields, "Dosing interval tau (hr)", errors)
        val tinf = parse(fields, "Infusion duration (hr)", errors)

        // --- Shared fields: range checks ---
        weight?.let { if (it <= 0 || it > 300) errors += "Weight (kg) should be between 0 and 300 kg." }
        age?.let { if (it < 0 || it > 120) errors += "Age (years) should be between 0 and 120." }
        scr?.let { if (it <= 0 || it > 2000) errors += "Serum creatinine (umol/L) should be a positive, realistic value (under 2000)." }
        dose?.let { if (it <= 0) errors += "Dose (mg) must be greater than zero." }
        tau?.let { if (it <= 0) errors += "Dosing interval tau (hr) must be greater than zero." }
        tinf?.let { if (it <= 0) errors += "Infusion duration (hr) must be greater than zero." }

        // --- Cross-field: infusion must fit inside the dosing interval ---
        if (tau != null && tinf != null && tau > 0 && tinf > 0 && tinf >= tau) {
            errors += "Infusion duration (hr) must be less than the dosing interval tau (hr) " +
                    "— you can't infuse for longer than the gap between doses."
        }

        // --- Workflow-specific required fields + checks ---
        when (workflow) {
            VancomycinWorkflow.PRE -> {
                val pre = parse(fields, "Pre-dose level (mg/L)", errors)
                pre?.let { if (it <= 0) errors += "Pre-dose level (mg/L) must be greater than zero." }
            }

            VancomycinWorkflow.POST -> {
                val post = parse(fields, "Post-dose level (mg/L)", errors)
                val t = parse(fields, "Sampling time after infusion (hr)", errors)
                val desired = parse(fields, "Desired Cmax (mg/L)", errors)

                post?.let { if (it <= 0) errors += "Post-dose level (mg/L) must be greater than zero." }
                t?.let { if (it < 0) errors += "Sampling time after infusion (hr) cannot be negative." }
                desired?.let { if (it <= 0) errors += "Desired Cmax (mg/L) must be greater than zero." }

                if (t != null && tau != null && tinf != null && t > (tau - tinf)) {
                    warnings += "Sampling time after infusion is later than when the next dose is due " +
                            "— double-check this timing."
                }
            }

            VancomycinWorkflow.PRE_POST -> {
                val pre = parse(fields, "Pre-dose level (mg/L)", errors)
                val post = parse(fields, "Post-dose level (mg/L)", errors)
                val t = parse(fields, "Sampling time after infusion (hr)", errors)
                val dt = parse(fields, "Time between pre and post samples (hr)", errors)
                val desired = parse(fields, "Desired Cmax (mg/L)", errors)

                pre?.let { if (it <= 0) errors += "Pre-dose level (mg/L) must be greater than zero." }
                post?.let { if (it <= 0) errors += "Post-dose level (mg/L) must be greater than zero." }
                t?.let { if (it < 0) errors += "Sampling time after infusion (hr) cannot be negative." }
                dt?.let { if (it <= 0) errors += "Time between pre and post samples (hr) must be greater than zero." }
                desired?.let { if (it <= 0) errors += "Desired Cmax (mg/L) must be greater than zero." }

                if (pre != null && post != null && post <= pre) {
                    warnings += "Post-dose level is not higher than the pre-dose level " +
                            "— unusual for a peak/trough pair. Double-check which level was drawn when."
                }
                if (t != null && tau != null && tinf != null && t > (tau - tinf)) {
                    warnings += "Sampling time after infusion is later than when the next dose is due " +
                            "— double-check this timing."
                }
            }
        }

        return ValidationResult(errors = errors, warnings = warnings)
    }
}