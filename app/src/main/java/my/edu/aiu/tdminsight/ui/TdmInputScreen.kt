package my.edu.aiu.tdminsight.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import my.edu.aiu.tdminsight.engine.VancomycinWorkflow


private val SHARED_FIELDS = listOf(
    "Weight (kg)", "Age (years)", "Serum creatinine (umol/L)",
    "Dose (mg)", "Dosing interval tau (hr)", "Infusion duration (hr)"
)

private fun fieldsFor(workflow: VancomycinWorkflow?): List<String> = when (workflow) {
    VancomycinWorkflow.PRE -> listOf("Pre-dose level (mg/L)")
    VancomycinWorkflow.POST -> listOf(
        "Post-dose level (mg/L)",
        "Sampling time after infusion (hr)",
        "Desired Cmax (mg/L)"
    )
    VancomycinWorkflow.PRE_POST -> listOf(
        "Pre-dose level (mg/L)",
        "Post-dose level (mg/L)",
        "Sampling time after infusion (hr)",
        "Time between pre and post samples (hr)",
        "Desired Cmax (mg/L)"
    )
    null -> emptyList()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TdmInputScreen(
    onCalculate: (VancomycinWorkflow, Map<String, String>, isMale: Boolean) -> Unit
) {
    var selectedWorkflow by remember { mutableStateOf<VancomycinWorkflow?>(null) }
    var isMale by remember { mutableStateOf(true) }
    val fieldValues = remember { mutableStateMapOf<String, String>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("TDM Insight", fontWeight = FontWeight.Bold)
                        Text(
                            "Vancomycin TDM Calculator",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(16.dp))
            Text(
                "1. Select workflow",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf(
                    VancomycinWorkflow.PRE to "Pre",
                    VancomycinWorkflow.POST to "Post",
                    VancomycinWorkflow.PRE_POST to "Pre + Post"
                ).forEach { (workflow, label) ->
                    FilterChip(
                        selected = selectedWorkflow == workflow,
                        onClick = {
                            selectedWorkflow = workflow
                            fieldValues.clear()
                        },
                        label = { Text(label) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            Text(
                "2. Patient & dose parameters",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(10.dp))

            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Sex",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = isMale,
                            onClick = { isMale = true },
                            label = { Text("Male") },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = !isMale,
                            onClick = { isMale = false },
                            label = { Text("Female") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    SHARED_FIELDS.forEachIndexed { index, field ->
                        LabeledField(field, fieldValues)
                        if (index != SHARED_FIELDS.lastIndex) Spacer(Modifier.height(4.dp))
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            if (selectedWorkflow != null) {
                Text(
                    "3. Workflow-specific inputs",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(10.dp))
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        val fields = fieldsFor(selectedWorkflow)
                        fields.forEachIndexed { index, field ->
                            LabeledField(field, fieldValues)
                            if (index != fields.lastIndex) Spacer(Modifier.height(4.dp))
                        }
                    }
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        "Select a workflow above to see its required inputs.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            Button(
                onClick = { selectedWorkflow?.let { onCalculate(it, fieldValues.toMap(), isMale) } },
                enabled = selectedWorkflow != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Run TDM Calculation", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun LabeledField(label: String, values: MutableMap<String, String>) {
    OutlinedTextField(
        value = values[label] ?: "",
        onValueChange = { values[label] = it },
        label = { Text(label) },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    )
}