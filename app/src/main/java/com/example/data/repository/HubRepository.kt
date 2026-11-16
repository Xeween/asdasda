package com.example.data.repository

import com.example.data.local.HubDao
import com.example.data.local.OperatorSettings
import com.example.data.local.SeedData
import com.example.data.local.SettingsDataStore
import com.example.data.model.ActivityStreamEntity
import com.example.data.model.BadgeEntity
import com.example.data.model.FeedLogEntity
import com.example.data.model.MediaTelemetryEntity
import com.example.data.model.OperatorProfileEntity
import com.example.data.model.QuestEntity
import com.example.data.model.WorkshopProjectEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HubRepository(
    private val dao: HubDao,
    private val settingsStore: SettingsDataStore
) {
    val profileFlow: Flow<OperatorProfileEntity?> = dao.observeProfile()
    val feedLogsFlow: Flow<List<FeedLogEntity>> = dao.observeFeedLogs()
    val questsFlow: Flow<List<QuestEntity>> = dao.observeQuests()
    val projectsFlow: Flow<List<WorkshopProjectEntity>> = dao.observeProjects()
    val mediaFlow: Flow<List<MediaTelemetryEntity>> = dao.observeMediaTelemetry()
    val badgesFlow: Flow<List<BadgeEntity>> = dao.observeBadges()
    val activityStreamFlow: Flow<List<ActivityStreamEntity>> = dao.observeActivityStream()
    val settingsFlow: Flow<OperatorSettings> = settingsStore.settingsFlow

    suspend fun ensureSeeded() {
        if (dao.getProfileCount() == 0) {
            seedAll()
        }
    }

    suspend fun resetToDefaultSeed() {
        dao.clearFeedLogs()
        dao.clearQuests()
        dao.clearProjects()
        dao.clearMedia()
        dao.clearBadges()
        dao.clearActivities()
        seedAll()
    }

    private suspend fun seedAll() {
        val now = System.currentTimeMillis()
        dao.upsertProfile(SeedData.defaultProfile())
        dao.insertFeedLogs(SeedData.defaultFeedLogs(now))
        dao.insertQuests(SeedData.defaultQuests())
        dao.insertProjects(SeedData.defaultProjects())
        dao.insertMediaList(SeedData.defaultMediaTelemetry())
        dao.insertBadges(SeedData.defaultBadges())
        dao.insertActivities(SeedData.defaultActivities(now))
    }

    suspend fun postFeedLog(
        rawContent: String,
        selectedTag: String? = null,
        sourceLabel: String = "TELEGRAM // SAVED",
        sourceAccent: String = "CORAL"
    ) {
        val trimmed = rawContent.trim()
        if (trimmed.isEmpty()) return

        // Extract #TAGS from text + include selectedTag if not already present
        val extractedTags = Regex("#[A-Za-z0-9_]+")
            .findAll(trimmed)
            .map { it.value.uppercase(Locale.US) }
            .toMutableSet()
        if (!selectedTag.isNullOrBlank() && selectedTag != "#ALL") {
            extractedTags.add(selectedTag.uppercase(Locale.US))
        }
        if (extractedTags.isEmpty()) {
            extractedTags.add("#DIARY")
        }

        val cleanText = trimmed.replace(Regex("\\s+#[A-Za-z0-9_]+"), "").trim()
            .ifEmpty { trimmed }

        val timeFormat = SimpleDateFormat("HH:mm", Locale.US)
        val log = FeedLogEntity(
            dateGroup = "TODAY",
            sourceLabel = sourceLabel,
            sourceAccent = sourceAccent,
            timestampLabel = timeFormat.format(Date()),
            content = cleanText,
            tagsCsv = extractedTags.joinToString(","),
            reactionsEncoded = "🔥:1",
            userReactedCsv = "🔥",
            createdAtMillis = System.currentTimeMillis()
        )
        dao.insertFeedLog(log)

        dao.getProfileOnce()?.let { profile ->
            dao.upsertProfile(
                profile.copy(journalLogsTotal = profile.journalLogsTotal + 1)
            )
        }
        dao.insertActivity(
            ActivityStreamEntity(
                category = "TELEGRAM DISPATCH",
                timeAgo = "Just now",
                title = "Published log entry (${extractedTags.first()})",
                detail = cleanText.take(72),
                accentType = sourceAccent,
                createdAtMillis = System.currentTimeMillis()
            )
        )
    }

    suspend fun toggleFeedReaction(logId: Int, emoji: String) {
        val log = dao.getFeedLogById(logId) ?: return
        val counts = LinkedHashMap<String, Int>()
        if (log.reactionsEncoded.isNotBlank()) {
            log.reactionsEncoded.split(",").forEach { token ->
                val parts = token.split(":")
                if (parts.size == 2) {
                    val key = parts[0].trim()
                    val count = parts[1].trim().toIntOrNull() ?: 0
                    if (key.isNotEmpty() && count > 0) counts[key] = count
                }
            }
        }
        val userReacted = log.userReactedCsv
            .split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toMutableSet()

        if (emoji in userReacted) {
            userReacted.remove(emoji)
            val updated = (counts[emoji] ?: 1) - 1
            if (updated <= 0) counts.remove(emoji) else counts[emoji] = updated
        } else {
            userReacted.add(emoji)
            counts[emoji] = (counts[emoji] ?: 0) + 1
        }

        val encoded = counts.entries.joinToString(",") { "${it.key}:${it.value}" }
        dao.updateFeedLog(
            log.copy(
                reactionsEncoded = encoded,
                userReactedCsv = userReacted.joinToString(",")
            )
        )
    }

    suspend fun deleteFeedLog(logId: Int) {
        dao.deleteFeedLogById(logId)
    }

    suspend fun toggleQuestCompletion(questId: Int): Pair<Boolean, Int>? {
        val quest = dao.getQuestById(questId) ?: return null
        val newCompleted = !quest.isCompleted
        dao.updateQuest(quest.copy(isCompleted = newCompleted))

        val deltaXp = if (newCompleted) quest.xpReward else -quest.xpReward
        val deltaCount = if (newCompleted) 1 else -1
        awardXp(deltaXp, questDelta = deltaCount)

        if (newCompleted) {
            dao.insertActivity(
                ActivityStreamEntity(
                    category = "QUEST PROTOCOL",
                    timeAgo = "Just now",
                    title = "Cleared [${quest.rank}-Rank] ${quest.title}",
                    detail = "+${quest.xpReward} XP synced to Operator Level",
                    accentType = "CYAN",
                    createdAtMillis = System.currentTimeMillis()
                )
            )
        }
        return newCompleted to quest.xpReward
    }

    suspend fun incrementGoalProgress(questId: Int) {
        val quest = dao.getQuestById(questId) ?: return
        if (quest.progressCurrent >= quest.progressTarget) return
        val step = if (quest.progressTarget >= 100) 4 else 1
        val next = (quest.progressCurrent + step).coerceAtMost(quest.progressTarget)
        val completed = next >= quest.progressTarget
        dao.updateQuest(quest.copy(progressCurrent = next, isCompleted = completed))
        if (completed && !quest.isCompleted) {
            awardXp(quest.xpReward, questDelta = 1)
            dao.insertActivity(
                ActivityStreamEntity(
                    category = "MILESTONE CLEARED",
                    timeAgo = "Just now",
                    title = "Goal Completed: ${quest.title}",
                    detail = "+${quest.xpReward} XP long-term milestone unlocked",
                    accentType = "VIOLET",
                    createdAtMillis = System.currentTimeMillis()
                )
            )
        }
    }

    suspend fun addQuest(
        title: String,
        subtitle: String,
        rank: String,
        category: String,
        xpReward: Int,
        isLongTermGoal: Boolean,
        target: Int
    ) {
        val cleanTitle = title.trim()
        if (cleanTitle.isEmpty()) return
        dao.insertQuest(
            QuestEntity(
                title = cleanTitle,
                subtitle = subtitle.trim().ifEmpty { "Operator custom directive" },
                subtitleIcon = if (isLongTermGoal) "TARGET" else "TERMINAL",
                rank = rank,
                category = category.uppercase(Locale.US),
                xpReward = xpReward.coerceIn(10, 2000),
                isCompleted = false,
                isLongTermGoal = isLongTermGoal,
                progressCurrent = 0,
                progressTarget = target.coerceAtLeast(1),
                unitLabel = if (isLongTermGoal) "steps" else "",
                sortOrder = 50
            )
        )
    }

    suspend fun deleteQuest(questId: Int) {
        dao.deleteQuestById(questId)
    }

    suspend fun claimDailyXpBoost(): Int {
        val amount = 350
        awardXp(amount)
        dao.insertActivity(
            ActivityStreamEntity(
                category = "XP PROTOCOL",
                timeAgo = "Just now",
                title = "Claimed Daily XP Sandbox Boost",
                detail = "+350 XP dispatched to progression matrix",
                accentType = "CORAL",
                createdAtMillis = System.currentTimeMillis()
            )
        )
        return amount
    }

    suspend fun resetSandboxXp() {
        dao.getProfileOnce()?.let { profile ->
            dao.upsertProfile(
                profile.copy(
                    level = 74,
                    currentXp = 8450,
                    nextLevelXp = 10000
                )
            )
        }
    }

    private suspend fun awardXp(deltaXp: Int, questDelta: Int = 0) {
        val profile = dao.getProfileOnce() ?: return
        var level = profile.level
        var xp = profile.currentXp + deltaXp
        val nextXp = profile.nextLevelXp
        while (xp >= nextXp) {
            xp -= nextXp
            level += 1
        }
        while (xp < 0 && level > 1) {
            level -= 1
            xp += nextXp
        }
        xp = xp.coerceAtLeast(0)
        dao.upsertProfile(
            profile.copy(
                level = level,
                currentXp = xp,
                steamXp = (profile.steamXp + deltaXp).coerceAtLeast(0),
                questsCompletedTotal = (profile.questsCompletedTotal + questDelta).coerceAtLeast(0)
            )
        )
    }

    suspend fun toggleProjectStar(projectId: Int) {
        val project = dao.getProjectById(projectId) ?: return
        val nextStarred = !project.isStarredByUser
        val nextCount = if (nextStarred) project.starCount + 1 else (project.starCount - 1).coerceAtLeast(0)
        dao.updateProject(project.copy(isStarredByUser = nextStarred, starCount = nextCount))
    }

    suspend fun addWorkshopProject(
        name: String,
        badgeLabel: String,
        description: String,
        tagsCsv: String,
        progressPercent: Int
    ) {
        val cleanName = name.trim()
        if (cleanName.isEmpty()) return
        dao.insertProject(
            WorkshopProjectEntity(
                name = cleanName,
                badgeLabel = badgeLabel.trim().ifEmpty { "v0.1.0" },
                description = description.trim().ifEmpty { "Experimental operator module." },
                tagsCsv = tagsCsv.trim().ifEmpty { "#KOTLIN,#COMPOSE" },
                starCount = 1,
                isStarredByUser = true,
                milestoneLabel = if (progressPercent >= 100) "Release Candidate" else "Active Sprint",
                progressPercent = progressPercent.coerceIn(0, 100),
                primaryActionLabel = if (progressPercent >= 100) "LAUNCH LIVE SITE" else "LIVE DEMO",
                secondaryActionLabel = "GITHUB",
                isFeaturedHero = false
            )
        )
        dao.getProfileOnce()?.let { profile ->
            dao.upsertProfile(profile.copy(projectsShippedTotal = profile.projectsShippedTotal + 1))
        }
    }

    suspend fun incrementMediaProgress(mediaId: Int) {
        val item = dao.getMediaById(mediaId) ?: return
        if (item.progressCurrent >= item.progressTarget) return
        val next = item.progressCurrent + 1
        val unit = if (item.mediaType == "ANIME") {
            if (item.metricPrimary.contains("Pg")) "Pg" else "Ep"
        } else "Ach"
        val updatedPrimary = if (item.mediaType == "ANIME") {
            "$next/${item.progressTarget} $unit"
        } else {
            item.metricPrimary
        }
        val updatedStatus = if (next >= item.progressTarget) "COMPLETED" else item.statusBadge
        dao.updateMedia(
            item.copy(
                progressCurrent = next,
                metricPrimary = updatedPrimary,
                statusBadge = updatedStatus
            )
        )
    }

    suspend fun cycleWatchingStatus() {
        val statuses = listOf(
            "Watching: Frieren: Beyond Journey's End • Ep 28",
            "Watching: Steins;Gate • Ep 22",
            "Reading: Solo Leveling Vol. 4 • Pg 142",
            "Listening: Cyberpunk Edgerunners OST"
        )
        val profile = dao.getProfileOnce() ?: return
        val currentIdx = statuses.indexOf(profile.watchingStatus)
        val nextStatus = statuses[(currentIdx + 1) % statuses.size]
        dao.upsertProfile(profile.copy(watchingStatus = nextStatus))
    }

    suspend fun updateProfile(
        displayName: String,
        bioQuote: String,
        locationLabel: String,
        watchingStatus: String,
        telegramHandle: String,
        githubHandle: String
    ) {
        val profile = dao.getProfileOnce() ?: return
        dao.upsertProfile(
            profile.copy(
                displayName = displayName.trim().ifEmpty { profile.displayName },
                bioQuote = bioQuote.trim().ifEmpty { profile.bioQuote },
                locationLabel = locationLabel.trim().ifEmpty { profile.locationLabel },
                watchingStatus = watchingStatus.trim().ifEmpty { profile.watchingStatus },
                telegramHandle = telegramHandle.trim().ifEmpty { profile.telegramHandle },
                githubHandle = githubHandle.trim().ifEmpty { profile.githubHandle }
            )
        )
    }

    suspend fun setHapticFeedback(enabled: Boolean) = settingsStore.setHapticFeedback(enabled)
    suspend fun setCompactDensity(enabled: Boolean) = settingsStore.setCompactDensity(enabled)
    suspend fun setShowSystemTerminal(enabled: Boolean) = settingsStore.setShowSystemTerminal(enabled)
    suspend fun setAutoHashtag(enabled: Boolean) = settingsStore.setAutoHashtagOnPost(enabled)
    suspend fun recordLocalCheckpoint() {
        val stamp = SimpleDateFormat("HH:mm:ss", Locale.US).format(Date())
        settingsStore.updateCheckpoint("SAVED $stamp")
    }
}
