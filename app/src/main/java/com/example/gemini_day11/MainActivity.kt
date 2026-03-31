package com.example.gemini_day11

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gemini_day11.ui.theme.GeminiDay11Theme
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val windowSize = calculateWindowSizeClass(this)
            GeminiDay11Theme {
                GeminiSimpleScreen(windowSize.widthSizeClass)
            }
        }
    }
}

@Composable
fun GeminiSimpleScreen(windowSize: WindowWidthSizeClass) {
    var responseText by remember { mutableStateOf("Ready to ask Gemini!") }
    var isLoading by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val generativeModel = remember {
        GenerativeModel(
            modelName = "gemini-3.1-flash-lite-preview",
            apiKey = "AIzaSyCBwl1y7m8TLwR12hbnq4kz62Iw49jDd08"
        )
    }
    if (windowSize == WindowWidthSizeClass.Compact) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            ButtonSection(
                isLoading = isLoading,
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        responseText = "Thinking..."

                        try {
                            val prompt = "Tell me a funny joke."
                            val response = generativeModel.generateContent(prompt)
                            responseText = response.text ?: "No response generated."
                        } catch (e: Exception) {
                            responseText = "Error: ${e.localizedMessage}"
                        } finally {
                            isLoading = false
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            JokeText(responseText)
        }
    } else {
        // Landscape
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            ButtonSection(
                isLoading = isLoading,
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        responseText = "Thinking..."

                        try {
                            val prompt = "Tell me a funny joke."
                            val response = generativeModel.generateContent(prompt)
                            responseText = response.text ?: "No response generated."
                        } catch (e: Exception) {
                            responseText = "Error: ${e.localizedMessage}"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(20.dp))

            JokeText(
                responseText,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ButtonSection(
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick,
            enabled = !isLoading
        ) {
            Text(if (isLoading) "Asking Gemini..." else "Ask Gemini a Joke")
        }
    }
}

@Composable
fun JokeText(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}