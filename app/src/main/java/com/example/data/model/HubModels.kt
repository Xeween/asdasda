package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "operator_profile")
data class OperatorProfileEntity(
    @PrimaryKey val id: Int = 1,
    val displayName: String,
    val shortHandle: String,
    val tierLabel: String,
    val verifiedLabel: String,
    val locationLabel: String,
    val bioQuote: String,
    val guildName: String,
    val level: Int,
    val currentXp: Int,
    val nextLevelXp: Int,
    val steamXp: Int,
    val watchingStatus: String,
    val inGameStatus: String,
    val telegramHandle: String,
    val steamId: String,
    val discordHandle: String,
    val githubHandle: String,
    val questsCompletedTotal: Int,
    val journalLogsTotal: Int,
    val projectsShippedTotal: Int,
    val activeStreakDays: Int,
    val dailyQuestStreak: Int
)

@Entity(tableName = "feed_logs")
data class FeedLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateGroup: String,
    val sourceLabel: String,
    val sourceAccent: String, // CORAL, VIOLET, CYAN
    val timestampLabel: String,
    val content: String,
    val tagsCsv: String,
    val reactionsEncoded: String, // e.g. "🔥:4,❤️:2,⚡:5"
    val userReactedCsv: String = "",
    val createdAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "quests")
data class QuestEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val subtitle: String,
    val subtitleIcon: String, // TROPHY, TIMER, BOOK, SYNC, TERMINAL, WATER, TARGET
    val rank: String, // S, A, B, C
    val category: String,
    val xpReward: Int,
    val isCompleted: Boolean,
    val isLongTermGoal: Boolean = false,
    val progressCurrent: Int = 0,
    val progressTarget: Int = 1,
    val unitLabel: String = "",
    val sortOrder: Int = 0
)

@Entity(tableName = "workshop_projects")
data class WorkshopProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val badgeLabel: String,
    val description: String,
    val tagsCsv: String,
    val starCount: Int,
    val isStarredByUser: Boolean = false,
    val milestoneLabel: String,
    val progressPercent: Int,
    val primaryActionLabel: String,
    val secondaryActionLabel: String,
    val isFeaturedHero: Boolean = false,
    val repoUrl: String = ""
)

@Entity(tableName = "media_telemetry")
data class MediaTelemetryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mediaType: String, // GAMING, ANIME
    val title: String,
    val subtitle: String,
    val statusBadge: String,
    val metricPrimary: String,
    val metricSecondary: String,
    val progressCurrent: Int,
    val progressTarget: Int
)

@Entity(tableName = "operator_badges")
data class BadgeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tier: String, // S-TIER, MYTHIC, ELITE, GEN 0, RARE
    val name: String,
    val requirement: String,
    val iconKey: String, // STAR, SHIELD, CHECK, FINGERPRINT, CODE, FLAME, GAMEPAD, TROPHY
    val isFeatured: Boolean = false,
    val isUnlocked: Boolean = true
)

@Entity(tableName = "activity_stream")
data class ActivityStreamEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,
    val timeAgo: String,
    val title: String,
    val detail: String,
    val accentType: String, // CORAL, VIOLET, CYAN
    val createdAtMillis: Long = System.currentTimeMillis()
)

data class EmojiReaction(
    val emoji: String,
    val count: Int,
    val reactedByUser: Boolean
)

fun FeedLogEntity.parseReactions(): List<EmojiReaction> {
    if (reactionsEncoded.isBlank()) return emptyList()
    val userSet = userReactedCsv.split(",").map { it.trim() }.filter { it.isNotEmpty() }.toSet()
    return reactionsEncoded.split(",")
        .mapNotNull { token ->
            val parts = token.split(":")
            if (parts.size == 2) {
                val emoji = parts[0].trim()
                val count = parts[1].trim().toIntOrNull() ?: 1
                if (emoji.isNotEmpty() && count > 0) {
                    EmojiReaction(emoji, count, emoji in userSet)
                } else null
            } else null
        }
}

fun FeedLogEntity.parseTags(): List<String> =
    tagsCsv.split(",").map { it.trim() }.filter { it.isNotEmpty() }

fun WorkshopProjectEntity.parseTags(): List<String> =
    tagsCsv.split(",").map { it.trim() }.filter { it.isNotEmpty() }
