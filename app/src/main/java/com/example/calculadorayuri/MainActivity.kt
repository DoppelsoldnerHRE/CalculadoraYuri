import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculadoraTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculadoraApp()
                }
            }
        }
    }
}

@Composable
fun CalculadoraTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}

@Composable
fun CalculadoraApp() {
    var displayText by remember { mutableStateOf("0") }
    var currentInput by remember { mutableStateOf("") }
    var currentOperator by remember { mutableStateOf("") }
    var result by remember { mutableDoubleStateOf(0.0) }
    val context = LocalContext.current

    fun handleInput(input: String) {
        try {
            if (input == "." && !currentInput.contains(".")) {
                // Allow only one decimal point in the current input
                currentInput += input
            } else if (input != "." && currentInput != "0") {
                // Prevent appending multiple operators or invalid input
                currentInput += input
            } else if (input != "." && currentInput == "0") {
                currentInput = input
            }

            displayText = currentInput
        } catch (e: Exception) {
            Toast.makeText(context, "Erro ao processar entrada", Toast.LENGTH_SHORT).show()
        }
    }
    fun handleOperator(operator: String) {
        try {
            if (currentInput.isNotEmpty()) {
                result = currentInput.toDouble()
                currentOperator = operator
                currentInput = ""
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Operação inválida", Toast.LENGTH_SHORT).show()
        }
    }
    fun calculateResult() {
        try {
            if (currentInput.isNotEmpty() && currentOperator.isNotEmpty()) {
                val secondOperand = currentInput.toDouble()
                when (currentOperator) {
                    "+" -> result += secondOperand
                    "-" -> result -= secondOperand
                    "*" -> result *= secondOperand
                    "/" -> {
                        if (secondOperand != 0.0) {
                            result /= secondOperand
                        } else {
                            displayText = "Erro"
                            Toast.makeText(context, "Divisão por zero!", Toast.LENGTH_SHORT).show()
                            return
                        }
                    }
                }
                displayText = if (result % 1 == 0.0) {
                    result.toInt().toString()
                } else {
                    result.toString()
                }

                // Reset input and operator after calculation
                currentInput = displayText
                currentOperator = ""
            }
        } catch (e: Exception) {
            displayText = "Erro"
            Toast.makeText(context, "Erro no cálculo", Toast.LENGTH_SHORT).show()
        }
    }
    fun clearDisplay() {
        displayText = "0"
        currentInput = ""
        currentOperator = ""
        result = 0.0
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = displayText,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { handleInput("7") },
                modifier = Modifier.weight(1f)
            ) { Text("7") }
            Button(
                onClick = { handleInput("8") },
                modifier = Modifier.weight(1f)
            ) { Text("8") }
            Button(
                onClick = { handleInput("9") },
                modifier = Modifier.weight(1f)
            ) { Text("9") }
            Button(
                onClick = { handleOperator("/") },
                modifier = Modifier.weight(1f)
            ) { Text("/") }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { handleInput("4") },
                modifier = Modifier.weight(1f)
            ) { Text("4") }
            Button(
                onClick = { handleInput("5") },
                modifier = Modifier.weight(1f)
            ) { Text("5") }
            Button(
                onClick = { handleInput("6") },
                modifier = Modifier.weight(1f)
            ) { Text("6") }
            Button(
                onClick = { handleOperator("*") },
                modifier = Modifier.weight(1f)
            ) { Text("*") }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { handleInput("1") },
                modifier = Modifier.weight(1f)
            ) { Text("1") }
            Button(
                onClick = { handleInput("2") },
                modifier = Modifier.weight(1f)
            ) { Text("2") }
            Button(
                onClick = { handleInput("3") },
                modifier = Modifier.weight(1f)
            ) { Text("3") }
            Button(
                onClick = { handleOperator("-") },
                modifier = Modifier.weight(1f)
            ) { Text("-") }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { handleInput("0") },
                modifier = Modifier.weight(1f)
            ) { Text("0") }
            Button(
                onClick = { handleInput(".") },
                modifier = Modifier.weight(1f)
            ) { Text(".") }
            Button(
                onClick = { clearDisplay() },
                modifier = Modifier.weight(1f)
            ) { Text("C") }
            Button(
                onClick = { handleOperator("+") },
                modifier = Modifier.weight(1f)
            ) { Text("+") }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { calculateResult() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Text("=", style = MaterialTheme.typography.headlineSmall)
        }
    }
}
