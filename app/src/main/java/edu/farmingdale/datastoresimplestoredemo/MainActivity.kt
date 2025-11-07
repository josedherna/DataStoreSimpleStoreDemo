package edu.farmingdale.datastoresimplestoredemo
import android.content.Context
import java.io.PrintWriter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.farmingdale.datastoresimplestoredemo.data.AppPreferences
import edu.farmingdale.datastoresimplestoredemo.ui.theme.DataStoreSimpleStoreDemoTheme
import kotlinx.coroutines.launch
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DataStoreSimpleStoreDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DataStoreDemo(modifier = Modifier.padding(innerPadding))
                }
            }
        }
        writeToInternalFile()
        val fileContents = readFromInternalFile()
        Log.d("MainActivity", fileContents)
    }
    private fun writeToInternalFile() {
        val outputStream: FileOutputStream = openFileOutput("edufilename", Context.MODE_PRIVATE)
        val writer = PrintWriter(outputStream)

        // Write three lines
        writer.println("This world of fun")
        writer.println("and yet, and yet.")

        writer.close()
    }

    private fun readFromInternalFile(): String {
        val inputStream = openFileInput("edufilename")
        val reader = inputStream.bufferedReader()
        val stringBuilder = StringBuilder()

        // Append each line and newline character to stringBuilder
        reader.forEachLine {
            stringBuilder.append(it).append("\n CSC 371 \n").append(System.lineSeparator())
        }

        return stringBuilder.toString()
    }
}

@Composable
fun DataStoreDemo(modifier: Modifier) {
    val store = AppStorage(LocalContext.current)
    val appPrefs = store.appPreferenceFlow.collectAsState(AppPreferences())
    val coroutineScope = rememberCoroutineScope()

    //Task 1
    var highScore by rememberSaveable { mutableStateOf("") }

    //Task 1
    var darkMode by rememberSaveable { mutableStateOf(false) }

    //Task 2
    var userName by rememberSaveable { mutableStateOf("") }

    Column (modifier = Modifier.padding(50.dp)) {
        Text("Values = ${appPrefs.value.userName}, " +
                "${appPrefs.value.highScore}, ${appPrefs.value.darkMode}")
        Spacer(Modifier.height(20.dp))
        //Task 1
        OutlinedTextField(
            value = highScore,
            onValueChange = {
                highScore = it
            },
            label = {
                Text("High Score")
            }
        )
        Spacer(Modifier.height(20.dp))
        //Task 1
        Text("Dark Mode")
        Switch(
            checked = darkMode,
            onCheckedChange = {
                darkMode = it
            })
        Spacer(Modifier.height(20.dp))
        //Task 2
        OutlinedTextField(
            value = userName,
            onValueChange = {
                userName = it
            },
            label = {
                Text("Username")
            }
        )

        //Task 3
        Button(onClick = {
            coroutineScope.launch {
                store.saveUsername(userName)
                store.saveHighscore(highScore)
                store.saveDarkmode(darkMode)
            }
        }) {
            Text("Save Values")
        }
    }
}


