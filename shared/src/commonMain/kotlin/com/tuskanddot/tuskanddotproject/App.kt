package com.tuskanddot.tuskanddotproject

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tuskanddot.tuskanddotproject.data.entities.AffirmationEntity
import com.tuskanddot.tuskanddotproject.database.dao.AffirmationDao
import com.tuskanddot.tuskanddotproject.ui.AffirmationScreen
import com.tuskanddot.tuskanddotproject.ui.AffirmationViewModel
import com.tuskanddot.tuskanddotproject.ui.TopicScreen
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import tuskanddotproject.shared.generated.resources.Res

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    val dao = koinInject<AffirmationDao>()

    LaunchedEffect(Unit) {
        try {
            val count = dao.getCount()
            if (count == 0) {
                val bytes = Res.readBytes("files/affirmations.json")
                val jsonString = bytes.decodeToString()
                val jsonArray = Json.parseToJsonElement(jsonString).jsonArray
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
            }
        } catch (e: Exception) {
            println("AffirmationsApp: Error reading/inserting affirmations: ${e.message}")
            e.printStackTrace()
        }
    }

    MaterialTheme {
        val navController = rememberNavController()
        val viewModel = koinViewModel<AffirmationViewModel>()

        NavHost(
            navController = navController,
            startDestination = "topics",
            modifier = Modifier.fillMaxSize().safeContentPadding()
        ) {
            composable("topics") {
                TopicScreen(
                    viewModel = viewModel,
                    onTopicClick = { topic ->
                        navController.navigate("affirmations/$topic")
                    }
                )
            }
            composable(
                route = "affirmations/{topic}",
                arguments = listOf(navArgument("topic") { type = NavType.StringType })
            ) { backStackEntry ->
                val topic = backStackEntry.savedStateHandle.get<String>("topic") ?: "All"
                AffirmationScreen(
                    viewModel = viewModel,
                    initialTopic = topic,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
