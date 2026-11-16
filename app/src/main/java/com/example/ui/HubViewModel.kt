package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.HubDatabase
import com.example.data.local.OperatorSettings
import com.example.data.local.SettingsDataStore
import com.example.data.model.ActivityStreamEntity
import com.example.data.model.BadgeEntity
import com.example.data.model.FeedLogEntity
import com.example.data.model.MediaTelemetryEntity
import com.example.data.model.OperatorProfileEntity
import com.example.data.model.QuestEntity
import com.example.data.model.WorkshopProjectEntity
import com.example.data.repository.HubRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class HubTab(val route: String) {
    SHOWCASE("showcase"),
    TELEGRAM("telegram"),
    QUESTS("quests"),
    STATS("stats"),
    SETTINGS("settings")
}

data class HubUiState(
    val isLoading: Boolean = true,
    val profile: OperatorProfileEntity? = null,
    val feedLogs: List<FeedLogEntity> = emptyList(),
    val quests: List<QuestEntity> = emptyList(),
    val projects: List<WorkshopProjectEntity> = emptyList(),
    val mediaItems: List<MediaTelemetryEntity> = emptyList(),
    val badges: List<BadgeEntity> = emptyList(),
    val activities: List<ActivityStreamEntity> = emptyList(),
    val settings: OperatorSettings = OperatorSettings(),
    val feedSearchQuery: String = "",
    val selectedFeedTag: String = "#ALL",
    val questSubTab: Int = 0, // 0 = Today's Tasks, 1 = Long-Term Goals
    val workshopSubTab: Int = 0, // 0 = Projects (Workshop), 1 = Gaming & Anime
    val systemTerminalMessage: String = "\"Those who conquer the dawn own the day.\""
)

class HubViewModel(
    application: Application,
    private val repository: HubRepository
) : AndroidViewModel(application) {

    private data class FilterState(
        val feedSearchQuery: String = "",
        val selectedFeedTag: String = "#ALL",
        val questSubTab: Int = 0,
        val workshopSubTab: Int = 0,
        val systemTerminalMessage: String = "\"Those who conquer the dawn own the day.\""
    )

    private val filterState = MutableStateFlow(FilterState())

    private val _toastEvents = MutableSharedFlow<String>(extraBufferCapacity = 8)
    val toastEvents: SharedFlow<String> = _toastEvents.asSharedFlow()

    init {
        viewModelScope.launch {
            repository.ensureSeeded()
        }
    }

    val uiState: StateFlow<HubUiState> = combine(
        repository.profileFlow,
        repository.feedLogsFlow,
        repository.questsFlow,
        repository.projectsFlow,
        repository.mediaFlow,
        repository.badgesFlow,
        repository.activityStreamFlow,
        repository.settingsFlow,
        filterState
    ) { values ->
        val profile = values[0] as OperatorProfileEntity?
        @Suppress("UNCHECKED_CAST")
        val feedLogs = values[1] as List<FeedLogEntity>
        @Suppress("UNCHECKED_CAST")
        val quests = values[2] as List<QuestEntity>
        @Suppress("UNCHECKED_CAST")
        val projects = values[3] as List<WorkshopProjectEntity>
        @Suppress("UNCHECKED_CAST")
        val mediaItems = values[4] as List<MediaTelemetryEntity>
        @Suppress("UNCHECKED_CAST")
        val badges = values[5] as List<BadgeEntity>
        @Suppress("UNCHECKED_CAST")
        val activities = values[6] as List<ActivityStreamEntity>
        val settings = values[7] as OperatorSettings
        val filters = values[8] as FilterState

        HubUiState(
            isLoading = profile == null,
            profile = profile,
            feedLogs = feedLogs,
            quests = quests,
            projects = projects,
            mediaItems = mediaItems,
            badges = badges,
            activities = activities,
            settings = settings,
            feedSearchQuery = filters.feedSearchQuery,
            selectedFeedTag = filters.selectedFeedTag,
            questSubTab = filters.questSubTab,
            workshopSubTab = filters.workshopSubTab,
            systemTerminalMessage = filters.systemTerminalMessage
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HubUiState()
    )

    fun setFeedSearchQuery(query: String) {
        filterState.update { it.copy(feedSearchQuery = query) }
    }

    fun setSelectedFeedTag(tag: String) {
        filterState.update { it.copy(selectedFeedTag = tag) }
    }

    fun setQuestSubTab(tab: Int) {
        filterState.update { it.copy(questSubTab = tab) }
    }

    fun setWorkshopSubTab(tab: Int) {
        filterState.update { it.copy(workshopSubTab = tab) }
    }

    fun postFeedLog(content: String, tag: String? = null) {
        viewModelScope.launch {
            val activeTag = tag ?: uiState.value.selectedFeedTag
            repository.postFeedLog(content, activeTag)
            _toastEvents.tryEmit("Log dispatched to Telegram Feed")
        }
    }

    fun toggleFeedReaction(logId: Int, emoji: String) {
        viewModelScope.launch {
            repository.toggleFeedReaction(logId, emoji)
        }
    }

    fun deleteFeedLog(logId: Int) {
        viewModelScope.launch {
            repository.deleteFeedLog(logId)
            _toastEvents.tryEmit("Log entry archived")
        }
    }

    fun toggleQuestCompletion(questId: Int) {
        viewModelScope.launch {
            val result = repository.toggleQuestCompletion(questId) ?: return@launch
            val (completed, xp) = result
            val msg = if (completed) {
                "\"Quest protocol cleared. +$xp XP synced.\""
            } else {
                "\"Protocol reopened. -$xp XP reverted.\""
            }
            filterState.update { it.copy(systemTerminalMessage = msg) }
        }
    }

    fun incrementGoalProgress(questId: Int) {
        viewModelScope.launch {
            repository.incrementGoalProgress(questId)
            filterState.update {
                it.copy(systemTerminalMessage = "\"Long-term milestone telemetry updated.\"")
            }
        }
    }

    fun addQuest(
        title: String,
        subtitle: String,
        rank: String,
        category: String,
        xpReward: Int,
        isLongTermGoal: Boolean,
        target: Int
    ) {
        viewModelScope.launch {
            repository.addQuest(title, subtitle, rank, category, xpReward, isLongTermGoal, target)
            filterState.update {
                it.copy(systemTerminalMessage = "\"New $rank-Rank directive registered.\"")
            }
            _toastEvents.tryEmit("New directive added")
        }
    }

    fun deleteQuest(questId: Int) {
        viewModelScope.launch {
            repository.deleteQuest(questId)
        }
    }

    fun claimDailyXpBoost() {
        viewModelScope.launch {
            val xp = repository.claimDailyXpBoost()
            _toastEvents.tryEmit("+$xp XP synced to Operator Matrix")
        }
    }

    fun resetSandboxXp() {
        viewModelScope.launch {
            repository.resetSandboxXp()
            _toastEvents.tryEmit("Sandbox XP calibrated to baseline")
        }
    }

    fun toggleProjectStar(projectId: Int) {
        viewModelScope.launch {
            repository.toggleProjectStar(projectId)
        }
    }

    fun addWorkshopProject(
        name: String,
        badgeLabel: String,
        description: String,
        tagsCsv: String,
        progressPercent: Int
    ) {
        viewModelScope.launch {
            repository.addWorkshopProject(name, badgeLabel, description, tagsCsv, progressPercent)
            _toastEvents.tryEmit("Project added to Workshop")
        }
    }

    fun incrementMediaProgress(mediaId: Int) {
        viewModelScope.launch {
            repository.incrementMediaProgress(mediaId)
            _toastEvents.tryEmit("Scrobble progress incremented")
        }
    }

    fun cycleWatchingStatus() {
        viewModelScope.launch {
            repository.cycleWatchingStatus()
        }
    }

    fun updateProfile(
        displayName: String,
        bioQuote: String,
        locationLabel: String,
        watchingStatus: String,
        telegramHandle: String,
        githubHandle: String
    ) {
        viewModelScope.launch {
            repository.updateProfile(
                displayName = displayName,
                bioQuote = bioQuote,
                locationLabel = locationLabel,
                watchingStatus = watchingStatus,
                telegramHandle = telegramHandle,
                githubHandle = githubHandle
            )
            _toastEvents.tryEmit("Operator identity updated")
        }
    }

    fun setHapticFeedback(enabled: Boolean) {
        viewModelScope.launch { repository.setHapticFeedback(enabled) }
    }

    fun setCompactDensity(enabled: Boolean) {
        viewModelScope.launch { repository.setCompactDensity(enabled) }
    }

    fun setShowSystemTerminal(enabled: Boolean) {
        viewModelScope.launch { repository.setShowSystemTerminal(enabled) }
    }

    fun setAutoHashtag(enabled: Boolean) {
        viewModelScope.launch { repository.setAutoHashtag(enabled) }
    }

    fun recordLocalCheckpoint() {
        viewModelScope.launch {
            repository.recordLocalCheckpoint()
            _toastEvents.tryEmit("Local checkpoint verified")
        }
    }

    fun resetAllDemoData() {
        viewModelScope.launch {
            repository.resetToDefaultSeed()
            _toastEvents.tryEmit("Operator telemetry restored to baseline")
        }
    }

    fun emitNotice(message: String) {
        _toastEvents.tryEmit(message)
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val db = HubDatabase.getInstance(application)
            val prefs = SettingsDataStore(application)
            val repo = HubRepository(db.hubDao(), prefs)
            return HubViewModel(application, repo) as T
        }
    }
}
