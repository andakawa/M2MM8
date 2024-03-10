import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import org.json.JSONArray
import org.json.JSONObject
import java.awt.Desktop
import java.awt.Toolkit
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URI
import java.util.*

@Composable
@Preview
fun App() {
    val headline by remember { mutableStateOf("M2M M8") }
    val subheading by remember { mutableStateOf("Testing M2M Endpoints made easy") }
    val intro by remember { mutableStateOf("Enter the ID, Secret and Scope of your client under test:") }
    val endpointInputDescription = "Enter the endpoint of the API under test"
    val authServerInfo by remember { mutableStateOf("Enter the URL of the auth endpoint") }
    val methodDescription by remember { mutableStateOf("POST, PUT, DELETE are experimental - use with care") }
    val authorNote by remember { mutableStateOf("https://github.com/andakawa") }
    val authorUrl = "https://github.com/andakawa" // Replace with the actual URL


    var inputId by remember { mutableStateOf("") }
    var inputSecret by remember { mutableStateOf("") }
    var inputScope by remember { mutableStateOf("") }
    var authServer by remember { mutableStateOf("") }

    var errorDialogShown by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val httpMethods = listOf(
        "GET",
        "POST",
        "PUT",
        "DELETE"
    )
    var selectedMethod by remember { mutableStateOf(httpMethods[0]) }

    var inputEndpoint by remember { mutableStateOf("") }
    var jsonResult by remember { mutableStateOf("") }

    val inputHistory by remember { mutableStateOf(mutableListOf<List<String>>()) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    val darkThemeColors = darkColors(
        primary = Color(0xFFFFA500),
        primaryVariant = Color(0xFF3700B3),
        secondary = Color(0xFF03DAC6),
        onPrimary = Color.White
    )

    MaterialTheme(colors = darkThemeColors) {
        Surface(color = Color.DarkGray) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column {
                    Column {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column {
                                Text(
                                    headline,
                                    style = TextStyle(fontSize = 3.em, fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colors.primary,
                                    modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 0.dp)
                                )

                                Text(
                                    subheading,
                                    style = TextStyle(
                                        fontSize = 1.em,
                                        fontStyle = MaterialTheme.typography.subtitle1.fontStyle
                                    ),
                                    color = MaterialTheme.colors.primary,
                                    modifier = Modifier.padding(42.dp, 0.dp, 0.dp, 32.dp)
                                )
                            }

                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                Button(
                                    onClick = { dropdownExpanded = true }, modifier = Modifier
                                        .padding(16.dp, 12.dp, 16.dp, 0.dp)
                                ) {
                                    Text("History", color = Color.Black)
                                }

                                DropdownMenu(
                                    expanded = dropdownExpanded,
                                    onDismissRequest = { dropdownExpanded = false },
                                    modifier = Modifier.fillMaxWidth(0.9f)
                                ) {
                                    inputHistory.forEachIndexed { index, historyItem ->
                                        DropdownMenuItem(onClick = {
                                            inputId = historyItem[0]
                                            inputSecret = historyItem[1]
                                            inputScope = historyItem[2]
                                            authServer = historyItem[3]
                                            inputEndpoint = historyItem[4]
                                            selectedMethod = historyItem[5]
                                            dropdownExpanded = false
                                        }) {
                                            Text(
                                                "Input Set ${index + 1} >> ${historyItem[5]} - ${historyItem[4]}",
                                                fontSize = 0.7.em
                                            )
                                        }
                                    }
                                }
                            }

                        }

                    }

                    Text(intro, modifier = Modifier.padding(32.dp, 0.dp, 0.dp, 0.dp), color = Color.White)

                    TextField(
                        value = inputId,
                        onValueChange = { inputId = it },
                        label = { Text("Client ID") },
                        modifier = Modifier.padding(32.dp, 12.dp, 32.dp, 0.dp).fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(textColor = Color.White)
                    )
                    TextField(
                        value = inputSecret,
                        onValueChange = { inputSecret = it },
                        label = { Text("Client Secret") },
                        modifier = Modifier.padding(32.dp, 12.dp, 32.dp, 0.dp).fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(textColor = Color.White)
                    )
                    TextField(
                        value = inputScope,
                        onValueChange = { inputScope = it },
                        label = { Text("Client Scope") },
                        modifier = Modifier.padding(32.dp, 12.dp, 32.dp, 0.dp).fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(textColor = Color.White)
                    )

                    Text(authServerInfo, modifier = Modifier.padding(32.dp, 12.dp, 32.dp, 0.dp), color = Color.White)
                    TextField(
                        value = authServer,
                        onValueChange = { authServer = it },
                        label = { Text("Auth Server URL") },
                        modifier = Modifier.padding(32.dp, 12.dp, 32.dp, 0.dp).fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(textColor = Color.White)
                    )

                    Text(
                        endpointInputDescription,
                        modifier = Modifier.padding(32.dp, 12.dp, 32.dp, 0.dp),
                        color = Color.White
                    )
                    TextField(
                        value = inputEndpoint,
                        onValueChange = { inputEndpoint = it },
                        label = { Text("Endpoint") },
                        modifier = Modifier.padding(32.dp, 12.dp, 32.dp, 0.dp).fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(textColor = Color.White)
                    )

                    Row(modifier = Modifier.padding(32.dp, 12.dp, 32.dp, 0.dp)) {
                        httpMethods.forEach { method ->
                            RadioButton(
                                selected = selectedMethod == method,
                                onClick = { selectedMethod = method },
                                colors = RadioButtonDefaults.colors(selectedColor = Color.White)
                            )
                            Text(
                                text = method,
                                style = TextStyle(color = Color.White),
                                modifier = Modifier.padding(start = 8.dp, end = 16.dp).align(Alignment.CenterVertically)
                            )
                        }
                    }

                    Text(
                        methodDescription,
                        modifier = Modifier.padding(32.dp, 12.dp, 32.dp, 0.dp),
                        color = Color.White,
                        fontSize = 0.8.em
                    )

                    // Button to trigger the API call
                    Button(onClick = {

                        try {
                            jsonResult =
                                callStack(inputId, inputSecret, authServer, inputScope, inputEndpoint, selectedMethod)
                            inputHistory.add(
                                listOf(
                                    inputId,
                                    inputSecret,
                                    inputScope,
                                    authServer,
                                    inputEndpoint,
                                    selectedMethod
                                )
                            )
                        } catch (e: Exception) {
                            errorMessage = e.message ?: "Unknown error"
                            errorDialogShown = true
                        }
                    }, modifier = Modifier.padding(32.dp, 12.dp, 32.dp, 0.dp)) {
                        Text("$selectedMethod Request", color = Color.Black)
                    }

                    if (errorDialogShown) {
                        AlertDialog(
                            onDismissRequest = { errorDialogShown = false },
                            title = { Text("Error") },
                            text = { Text("There has been an error in either the inputs or the resources are not available. $errorMessage") },
                            confirmButton = {
                                Button(onClick = { errorDialogShown = false }) {
                                    Text("OK")
                                }
                            }
                        )
                    }

                    Text(
                        "API call result (you can adjust the size of the window)",
                        modifier = Modifier.padding(32.dp, 12.dp, 32.dp, 0.dp),
                        color = Color.White
                    )
                    TextField(
                        value = jsonResult,
                        onValueChange = { jsonResult = it },
                        modifier = Modifier.padding(32.dp, 12.dp, 32.dp, 32.dp).fillMaxWidth().fillMaxWidth()
                            .weight(1f),
                        maxLines = Int.MAX_VALUE,
                        textStyle = TextStyle(color = Color.White, fontSize = 1.em)
                    )


                    val annotatedString = buildAnnotatedString {
                        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                            append(authorNote)
                            addStringAnnotation(tag = "URL", annotation = authorUrl, start = 0, end = authorNote.length)
                        }
                    }

                    ClickableText(
                        text = annotatedString,
                        onClick = { offset ->
                            annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                                .firstOrNull()?.let {
                                Desktop.getDesktop().browse(URI(it.item))
                            }
                        },
                        style = TextStyle(color = Color.Gray, fontSize = 1.em),
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(0.dp, 0.dp, 0.dp, 12.dp)
                    )

                    /*Text(
                        authorNote,
                        color = Color.Gray,
                        fontSize = 1.em,
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(0.dp, 0.dp, 0.dp, 12.dp)
                    )*/
                }
            }

        }
    }
}

fun main() = application {

    val screenHeight = Toolkit.getDefaultToolkit().screenSize.height
    val windowHeight = screenHeight * 0.8

    Window(
        onCloseRequest = ::exitApplication,
        title = "M2MM8 - Machine 2 Machine API Request Testing Tool",
        state = WindowState(height = windowHeight.dp)
    ) {
        App()
    }
}

fun callStack(
    clientId: String,
    clientSecret: String,
    authServer: String,
    scope: String,
    endpoint: String,
    selectedMethod: String
): String {
    val base64 = getAccessToken(clientId, clientSecret)
    val token = authenticateClient(authServer, base64, scope)
    return callEndpoint(endpoint, token, selectedMethod)
}

fun getAccessToken(clientId: String, clientSecret: String): String {

    val credentials = "$clientId:$clientSecret"
    val encodedCredentials = Base64.getEncoder().encodeToString(credentials.toByteArray())

    return encodedCredentials.trim()

}

fun authenticateClient(authServer: String, base64: String, scope: String): String {

    val command = listOf(
        "curl",
        "-X",
        "POST",
        authServer,
        "-H", "Authorization: Basic $base64",
        "-H", "Content-Type: application/x-www-form-urlencoded",
        "-d", "grant_type=client_credentials&scope=$scope"
    )

    val process = ProcessBuilder(command).start()
    val reader = BufferedReader(InputStreamReader(process.inputStream))
    val output = reader.readText()

    process.waitFor()

    val jsonResponse = JSONObject(output)
    val token = jsonResponse.getString("access_token")

    return token.toString()

}

@Suppress("IMPLICIT_CAST_TO_ANY")
fun callEndpoint(endpoint: String, token: String, selectedMethod: String): String {
    val command = listOf(
        "curl",
        "-X",
        selectedMethod,
        endpoint,
        "-H", "accept: */*",
        "-H", "authorization: Bearer $token"
    )

    val process = ProcessBuilder(command).start()
    val reader = BufferedReader(InputStreamReader(process.inputStream))
    val output = reader.readText()

    process.waitFor()


    val jsonElement = when {
        output.startsWith("[") -> JSONArray(output.trim())
        output.startsWith("{") -> JSONObject(output.trim())
        else -> "Not a JSON Response"
    }

    val jsonResponse = when (jsonElement) {
        is JSONArray -> {
            jsonElement.toString(4)
        }

        is JSONObject -> {
            jsonElement.toString(4)
        }

        else -> {
            "Not a JSON Response"
        }
    }

    return jsonResponse
}
