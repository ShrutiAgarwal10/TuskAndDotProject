package com.tuskanddot.tuskanddotproject

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.painterResource

import tuskanddotproject.shared.generated.resources.Res
import tuskanddotproject.shared.generated.resources.compose_multiplatform

@OptIn(org.jetbrains.compose.resources.ExperimentalResourceApi::class)
@Composable
@Preview
fun App() {
    LaunchedEffect(Unit) {
        try {
            val bytes = Res.readBytes("files/affirmations.json")
            val jsonString = bytes.decodeToString()
            println("AffirmationsApp: Loaded affirmations.json successfully. Content:")
            /*println(jsonString)

            val regex = Regex("""\{\s*"id"\s*:\s*"([^"]+)"\s*,\s*"topic"\s*:\s*"([^"]+)"\s*,\s*"text"\s*:\s*"([^"]+)"\s*\}""")
            val matches = regex.findAll(jsonString)
            println("AffirmationsApp: Parsed affirmations:")
            for (match in matches) {
                val (id, topic, text) = match.destructured
                println("AffirmationsApp: [$id] ($topic): $text")
            }*/
            /*val jsonArray = Json.parseToJsonElement(jsonString).jsonArray

            jsonArray.forEach { item ->
                val affirmation = item.jsonObject
                println(
                    "[${affirmation["id"]!!.jsonPrimitive.content}] " +
                            "(${affirmation["topic"]!!.jsonPrimitive.content}): " +
                            affirmation["text"]!!.jsonPrimitive.content
                )
            }*/
        } catch (e: Exception) {
            println("AffirmationsApp: Error reading affirmations.json: ${e.message}")
            e.printStackTrace()
        }
    }

    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = { showContent = !showContent }) {
                Text("Click me!")
            }
            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting")
                }
            }
        }
    }
}