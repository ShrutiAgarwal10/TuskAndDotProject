package com.tuskanddot.tuskanddotproject.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuskanddot.tuskanddotproject.data.entities.AffirmationEntity
import com.tuskanddot.tuskanddotproject.database.dao.AffirmationDao
import com.tuskanddot.tuskanddotproject.prefs.PreferenceKeys
import com.tuskanddot.tuskanddotproject.prefs.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AffirmationUiState(
    val isLoading: Boolean = true,
    val affirmations: List<AffirmationEntity> = emptyList(),
    val topics: List<String> = emptyList(),
    val selectedTopic: String = "All",
    val currentAffirmation: AffirmationEntity? = null
)

class AffirmationViewModel(
    private val dao: AffirmationDao,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(AffirmationUiState())
    val uiState: StateFlow<AffirmationUiState> = _uiState.asStateFlow()

    private val _favoriteAffirmations = MutableStateFlow<List<AffirmationEntity>>(emptyList())
    val favoriteAffirmations: StateFlow<List<AffirmationEntity>> = _favoriteAffirmations.asStateFlow()

    init {
        fetchAffirmations()
        fetchFavorites()
    }

    private fun fetchAffirmations() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            dao.getAllAffirmations().collect { list ->
                val topics = list.map { it.topic }.distinct()
                val currentTopic = _uiState.value.selectedTopic
                val filtered = if (currentTopic == "All") list else list.filter { it.topic == currentTopic }
                val currentAff = _uiState.value.currentAffirmation ?: filtered.randomOrNull() ?: list.randomOrNull()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    affirmations = list,
                    topics = listOf("All") + topics,
                    currentAffirmation = currentAff
                )
            }
        }
    }

    private fun fetchFavorites() {
        viewModelScope.launch {
            dao.getFavorites().collect { favs ->
                _favoriteAffirmations.value = favs
            }
        }
    }

    fun selectTopic(topic: String) {
        val list = _uiState.value.affirmations
        val filtered = if (topic == "All") list else list.filter { it.topic == topic }
        val randomAff = filtered.randomOrNull()
        _uiState.value = _uiState.value.copy(
            selectedTopic = topic,
            currentAffirmation = randomAff
        )
    }

    fun changeRandomAffirmation() {
        val list = _uiState.value.affirmations
        val topic = _uiState.value.selectedTopic
        val filtered = if (topic == "All") list else list.filter { it.topic == topic }
        if (filtered.isNotEmpty()) {
            val current = _uiState.value.currentAffirmation
            val next = if (filtered.size > 1) {
                (filtered - current).randomOrNull() ?: filtered.random()
            } else {
                filtered.random()
            }
            _uiState.value = _uiState.value.copy(currentAffirmation = next)
        }
    }

    fun toggleFavorite(affirmation: AffirmationEntity) {
        viewModelScope.launch {
            val updatedFavorite = !affirmation.isFavorite
            dao.updateFavorite(affirmation.id, updatedFavorite)
            userPreferences.savePreference("${PreferenceKeys.FAV}_${affirmation.id}", updatedFavorite)

            val updatedList = _uiState.value.affirmations.map {
                if (it.id == affirmation.id) it.copy(isFavorite = updatedFavorite) else it
            }
            val current = _uiState.value.currentAffirmation
            val updatedCurrent = if (current?.id == affirmation.id) current.copy(isFavorite = updatedFavorite) else current
            _uiState.value = _uiState.value.copy(
                affirmations = updatedList,
                currentAffirmation = updatedCurrent
            )
        }
    }
}
