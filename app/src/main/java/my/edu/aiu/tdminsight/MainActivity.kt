package my.edu.aiu.tdminsight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import my.edu.aiu.tdminsight.engine.*
import my.edu.aiu.tdminsight.ui.TdmInputScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TdmInsightApp()
                }
            }
        }
    }
}

@Composable
fun TdmInsightApp() {
    var result by remember { mutableStateOf<TdmResult?>(null) }
    var lastInputs by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    if (result == null) {
        TdmInputScreen { workflow, fields, isMale ->
            val validation = TdmValidator.validate(workflow, fields)
            if (!validation.isValid) {
                errorMessage = validation.errors.joinToString("\n") { "• $it" }
                return@TdmInputScreen
            }

            try {
                val patient = PatientInfo(
                    weightKg = fields.req("Weight (kg)"),
                    ageYears = fields.req("Age (years)").toInt(),
                    serumCreatinineUmolL = fields.req("Serum creatinine (umol/L)"),
                    isMale = isMale
                )
                val dose = DoseInfo(
                    doseMg = fields.req("Dose (mg)"),
                    intervalHours = fields.req("Dosing interval tau (hr)"),
                    infusionDurationHours = fields.req("Infusion duration (hr)")
                )
                lastInputs = fields
                val calculated = when (workflow) {
                    VancomycinWorkflow.PRE -> VancomycinCalculationEngine.calculatePre(
                        patient, dose,
                        WorkflowInput.Pre(preLevelMgL = fields.req("Pre-dose level (mg/L)"))
                    )
                    VancomycinWorkflow.POST -> VancomycinCalculationEngine.calculatePost(
                        patient, dose,
                        WorkflowInput.Post(
                            postLevelMgL = fields.req("Post-dose level (mg/L)"),
                            samplingTimeAfterInfusionHours = fields.req("Sampling time after infusion (hr)"),
                            desiredCmaxMgL = fields.req("Desired Cmax (mg/L)")
                        )
                    )
                    VancomycinWorkflow.PRE_POST -> VancomycinCalculationEngine.calculatePrePost(
                        patient, dose,
                        WorkflowInput.PrePost(
                            preLevelMgL = fields.req("Pre-dose level (mg/L)"),
                            postLevelMgL = fields.req("Post-dose level (mg/L)"),
                            samplingTimeAfterInfusionHours = fields.req("Sampling time after infusion (hr)"),
                            intervalBetweenSamplesHours = fields.req("Time between pre and post samples (hr)"),
                            desiredCmaxMgL = fields.req("Desired Cmax (mg/L)")
                        )
                    )
                }
                // Merge the validator's soft warnings (e.g. odd timing) with the engine's own
                // calculation warnings (e.g. divide-by-zero guards) so both show on one screen.
                result = calculated.copy(warnings = calculated.warnings + validation.warnings)
            } catch (e: NumberFormatException) {
                errorMessage = e.message ?: "Please fill in all fields with valid numbers."
            }
        }
    } else {
        TdmResultScreen(
            result = result!!,
            inputs = lastInputs,
            onBack = { result = null }
        )
    }

    errorMessage?.let { msg ->
        AlertDialog(
            onDismissRequest = { errorMessage = null },
            confirmButton = { TextButton(onClick = { errorMessage = null }) { Text("OK") } },
            title = { Text("Invalid input") },
            text = { Text(msg) }
        )
    }
}

// Safe to assume these succeed now — TdmValidator.validate() already confirmed every
// required field is present and numeric before this code runs.
private fun Map<String, String>.req(key: String): Double =
    this[key]?.toDoubleOrNull() ?: throw NumberFormatException("Missing or invalid value for: $key")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TdmResultScreen(result: TdmResult, inputs: Map<String, String>, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Result — ${result.workflow.name.replace('_', '+')}") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("← Back") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(12.dp))

            if (result.warnings.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        result.warnings.forEach { w ->
                            Text(
                                "⚠ $w",
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // Stage 1: Input Values
            ResultSection(title = "1. Input values") {
                inputs.forEach { (label, value) ->
                    ResultRow(label, value)
                }
            }

            // Stage 2: Intermediate Values
            val intermediateSteps = result.steps.filter { it.category == StepCategory.INTERMEDIATE }
            if (intermediateSteps.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
                ResultSection(title = "2. Intermediate values") {
                    intermediateSteps.forEach { StepRow(it) }
                }
            }

            // Stage 3: Pharmacokinetic Parameters
            val parameterSteps = result.steps.filter { it.category == StepCategory.PARAMETER }
            if (parameterSteps.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
                ResultSection(title = "3. Pharmacokinetic parameters") {
                    parameterSteps.forEach { StepRow(it) }
                }
            }

            // Stage 4: Final Result
            val resultSteps = result.steps.filter { it.category == StepCategory.RESULT }
            if (resultSteps.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
                ResultSection(
                    title = "4. Final result",
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    resultSteps.forEach { StepRow(it, emphasize = true) }
                }
            }

            Spacer(Modifier.height(24.dp))
            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Back to inputs")
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ResultSection(
    title: String,
    containerColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surfaceVariant,
    content: @Composable ColumnScope.() -> Unit
) {
    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
    Spacer(Modifier.height(8.dp))
    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            content()
        }
    }
}

@Composable
private fun ResultRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun StepRow(step: CalculationStep, emphasize: Boolean = false) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(
            step.label,
            style = if (emphasize) MaterialTheme.typography.titleSmall else MaterialTheme.typography.bodyMedium,
            fontWeight = if (emphasize) FontWeight.Bold else FontWeight.Medium
        )
        Text(
            step.formula,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            "= ${"%.3f".format(step.value)} ${step.unit}",
            style = if (emphasize) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}