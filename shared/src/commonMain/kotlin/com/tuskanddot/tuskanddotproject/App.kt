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
import com.tuskanddot.tuskanddotproject.data.entities.AffirmationEntity
import com.tuskanddot.tuskanddotproject.database.dao.AffirmationDao
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

import tuskanddotproject.shared.generated.resources.Res
import tuskanddotproject.shared.generated.resources.compose_multiplatform

@OptIn(org.jetbrains.compose.resources.ExperimentalResourceApi::class)
@Composable
@Preview
fun App() {
    val dao = koinInject<AffirmationDao>()
    LaunchedEffect(Unit) {

        try {
            val bytes = Res.readBytes("files/affirmations.json")
            val jsonString = bytes.decodeToString()
            val jsonArray = Json.parseToJsonElement(jsonString).jsonArray

            println("AffirmationsApp: Loaded affirmations.json successfully. Content:")

            val affirmations = jsonArray.map { item ->
                val json = item.jsonObject

                AffirmationEntity(
                    id = json["id"]!!.jsonPrimitive.content,
                    topic = json["topic"]!!.jsonPrimitive.content,
                    text = json["text"]!!.jsonPrimitive.content,
                    isFavorite = false
                )
            }

            dao.insertAll(affirmations)

            println("Inserted ${affirmations.size} affirmations into Room")

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