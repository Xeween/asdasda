package com.example.data.local

import com.example.data.model.ActivityStreamEntity
import com.example.data.model.BadgeEntity
import com.example.data.model.FeedLogEntity
import com.example.data.model.MediaTelemetryEntity
import com.example.data.model.OperatorProfileEntity
import com.example.data.model.QuestEntity
import com.example.data.model.WorkshopProjectEntity

object SeedData {
    fun defaultProfile() = OperatorProfileEntity(
        id = 1,
        displayName = "Toodzie// 蓮",
        shortHandle = "Toodzie",
        tierLabel = "TIER V // S-RANK",
        verifiedLabel = "VERIFIED OPERATOR",
        locationLabel = "JP / TOKYO",
        bioQuote = "\"Zero latency, infinite focus. Building quiet masterpieces in the dark.\"",
        guildName = "Archival Syndicate",
        level = 74,
        currentXp = 8450,
        nextLevelXp = 10000,
        steamXp = 38920,
        watchingStatus = "Watching: Frieren: Beyond Journey's End • Ep 28",
        inGameStatus = "IN-GAME: CS2",
        telegramHandle = "@toodzie",
        steamId = "/qlichevasadbek",
        discordHandle = "toodzie",
        githubHandle = "@kaelen-sol",
        questsCompletedTotal = 142,
        journalLogsTotal = 389,
        projectsShippedTotal = 12,
        activeStreakDays = 24,
        dailyQuestStreak = 6
    )

    fun defaultFeedLogs(now: Long = System.currentTimeMillis()): List<FeedLogEntity> = listOf(
        FeedLogEntity(
            dateGroup = "TODAY",
            sourceLabel = "TELEGRAM // SAVED",
            sourceAccent = "CORAL",
            timestampLabel = "14:42",
            content = "Rebuilt the shader pipeline for the new web showcase. 60fps locked on mobile.",
            tagsCsv = "#CODE,#MILESTONE",
            reactionsEncoded = "🔥:4,❤️:2,⚡:5",
            createdAtMillis = now - 10_000L
        ),
        FeedLogEntity(
            dateGroup = "YESTERDAY",
            sourceLabel = "TERMINAL // LOG",
            sourceAccent = "VIOLET",
            timestampLabel = "23:18",
            content = "Rewatching Steins;Gate episode 22. Still unmatched writing.",
            tagsCsv = "#ANIME",
            reactionsEncoded = "✨:8,😭:3",
            createdAtMillis = now - 86_400_000L
        ),
        FeedLogEntity(
            dateGroup = "NOV 14",
            sourceLabel = "DAILY QUESTS // XP SYNC",
            sourceAccent = "CYAN",
            timestampLabel = "07:05",
            content = "Woke up at 6am, knocked out 3 S-Rank daily tasks. +150 XP towards Level 75!",
            tagsCsv = "#DIARY",
            reactionsEncoded = "🚀:6",
            createdAtMillis = now - 172_800_000L
        ),
        FeedLogEntity(
            dateGroup = "NOV 14",
            sourceLabel = "TERMINAL // WORKOUT",
            sourceAccent = "CORAL",
            timestampLabel = "06:30",
            content = "Morning 5km interval tempo run along Sumida riverfront. Avg pace 4:48/km.",
            tagsCsv = "#WORKOUT,#IDEA",
            reactionsEncoded = "⚡:4,🔥:3",
            createdAtMillis = now - 180_000_000L
        )
    )

    fun defaultQuests(): List<QuestEntity> = listOf(
        // Today's Tasks (6) — matching screenshot 2 (3 completed out of 6 or 4 of 6)
        // Screenshot shows 3 checked + header says 4 of 6 or wait: 1, 2, 6 checked visually, let's check:
        // #1 Ship Steam Web App v1.0 (checked, +150 XP)
        // #2 Morning Workout & 5km Run (checked, +100 XP)
        // #3 Read 30 pages of Light Novel / Tech Book (unchecked, +50 XP)
        // #4 Refactor LocalStorage Sync Engine (unchecked, +80 XP)
        // #5 Review Pull Requests & GitHub issues (unchecked, +40 XP)
        // #6 Hydration Protocol (3L Water Intake) (checked, +60 XP)
        QuestEntity(
            title = "Ship Steam Web App v1.0",
            subtitle = "Achievement Unlocked: Architect of Dimensions",
            subtitleIcon = "TROPHY",
            rank = "S",
            category = "STEAM CORE",
            xpReward = 150,
            isCompleted = true,
            isLongTermGoal = false,
            sortOrder = 1
        ),
        QuestEntity(
            title = "Morning Workout & 5km Run",
            subtitle = "Pace: 4:48/km • Endurance Protocol Cleared",
            subtitleIcon = "TIMER",
            rank = "S",
            category = "PHYSIQUE",
            xpReward = 100,
            isCompleted = true,
            isLongTermGoal = false,
            sortOrder = 2
        ),
        QuestEntity(
            title = "Read 30 pages of Light Novel / Tech Book",
            subtitle = "Currently: 'Solo Leveling Vol. 4' (Pg 142/320)",
            subtitleIcon = "BOOK",
            rank = "A",
            category = "LORE & INTEL",
            xpReward = 50,
            isCompleted = false,
            isLongTermGoal = false,
            sortOrder = 3
        ),
        QuestEntity(
            title = "Refactor LocalStorage Sync Engine",
            subtitle = "Migrate cache store to IndexedDB fallback queue",
            subtitleIcon = "SYNC",
            rank = "A",
            category = "SYSTEM",
            xpReward = 80,
            isCompleted = false,
            isLongTermGoal = false,
            sortOrder = 4
        ),
        QuestEntity(
            title = "Review Pull Requests & GitHub issues",
            subtitle = "3 pending reviews on telemetry backend",
            subtitleIcon = "TERMINAL",
            rank = "B",
            category = "COMMUNITY",
            xpReward = 40,
            isCompleted = false,
            isLongTermGoal = false,
            sortOrder = 5
        ),
        QuestEntity(
            title = "Hydration Protocol (3L Water Intake)",
            subtitle = "Daily biomarker target maintained",
            subtitleIcon = "WATER",
            rank = "B",
            category = "ROUTINE",
            xpReward = 60,
            isCompleted = true,
            isLongTermGoal = false,
            sortOrder = 6
        ),
        // Long-Term Goals (4)
        QuestEntity(
            title = "Release Aether Engine v1.0 Stable",
            subtitle = "WASM compute pipeline + mobile shader parity",
            subtitleIcon = "TARGET",
            rank = "S",
            category = "WORKSHOP",
            xpReward = 500,
            isCompleted = false,
            isLongTermGoal = true,
            progressCurrent = 92,
            progressTarget = 100,
            unitLabel = "%",
            sortOrder = 10
        ),
        QuestEntity(
            title = "Read 24 Light Novels & Systems Books",
            subtitle = "2025 Intellectual Archive Milestone",
            subtitleIcon = "BOOK",
            rank = "A",
            category = "LORE & INTEL",
            xpReward = 350,
            isCompleted = false,
            isLongTermGoal = true,
            progressCurrent = 14,
            progressTarget = 24,
            unitLabel = "books",
            sortOrder = 11
        ),
        QuestEntity(
            title = "Reach CS2 Premier 20,000 ELO",
            subtitle = "Solo/Duo tactical ladder climb",
            subtitleIcon = "TROPHY",
            rank = "S",
            category = "COMBAT REC",
            xpReward = 400,
            isCompleted = false,
            isLongTermGoal = true,
            progressCurrent = 18,
            progressTarget = 20,
            unitLabel = "k",
            sortOrder = 12
        ),
        QuestEntity(
            title = "30-Day 06:00 Wake-Up Protocol",
            subtitle = "Unbroken dawn discipline streak",
            subtitleIcon = "TIMER",
            rank = "A",
            category = "PHYSIQUE",
            xpReward = 300,
            isCompleted = false,
            isLongTermGoal = true,
            progressCurrent = 24,
            progressTarget = 30,
            unitLabel = "days",
            sortOrder = 13
        )
    )

    fun defaultProjects(): List<WorkshopProjectEntity> = listOf(
        WorkshopProjectEntity(
            name = "Aether Engine",
            badgeLabel = "v0.9.2",
            description = "High-performance modular WebGL 2.0 compute shader & voxel raymarching runtime.",
            tagsCsv = "#RUST,#WASM,#THREEJS",
            starCount = 1482,
            isStarredByUser = false,
            milestoneLabel = "Milestone: Alpha Architecture",
            progressPercent = 92,
            primaryActionLabel = "LIVE DEMO",
            secondaryActionLabel = "GITHUB",
            isFeaturedHero = true,
            repoUrl = "https://github.com/kaelen-sol/aether-engine"
        ),
        WorkshopProjectEntity(
            name = "Ronin Note",
            badgeLabel = "SHIPPED",
            description = "Offline-first encrypted markdown obsidian clone.",
            tagsCsv = "#TYPESCRIPT,#PWA,#INDEXED_DB",
            starCount = 634,
            isStarredByUser = false,
            milestoneLabel = "Release Candidate 1.0",
            progressPercent = 100,
            primaryActionLabel = "LAUNCH LIVE SITE",
            secondaryActionLabel = "",
            isFeaturedHero = false,
            repoUrl = "https://github.com/kaelen-sol/ronin-note"
        )
    )

    fun defaultMediaTelemetry(): List<MediaTelemetryEntity> = listOf(
        MediaTelemetryEntity(
            mediaType = "GAMING",
            title = "Counter-Strike 2",
            subtitle = "Competitive Mirage / Inferno Specialist",
            statusBadge = "ACTIVE",
            metricPrimary = "1,420 hrs",
            metricSecondary = "Premier 18,450",
            progressCurrent = 92,
            progressTarget = 100
        ),
        MediaTelemetryEntity(
            mediaType = "GAMING",
            title = "Elden Ring: Shadow of the Erdtree",
            subtitle = "NG+3 Dex/Arcane Katana Run",
            statusBadge = "100% ACH",
            metricPrimary = "218 hrs",
            metricSecondary = "42/42 Trophies",
            progressCurrent = 42,
            progressTarget = 42
        ),
        MediaTelemetryEntity(
            mediaType = "GAMING",
            title = "Cyberpunk 2077: Phantom Liberty",
            subtitle = "Netrunner Sandevistan Path-Traced",
            statusBadge = "COMPLETED",
            metricPrimary = "164 hrs",
            metricSecondary = "98% Street Cred",
            progressCurrent = 98,
            progressTarget = 100
        ),
        MediaTelemetryEntity(
            mediaType = "ANIME",
            title = "Frieren: Beyond Journey's End",
            subtitle = "Rated 10/10 • \"Pure Cinema\"",
            statusBadge = "SCROBBLED",
            metricPrimary = "28/28 Ep",
            metricSecondary = "MAL Score 10",
            progressCurrent = 28,
            progressTarget = 28
        ),
        MediaTelemetryEntity(
            mediaType = "ANIME",
            title = "Steins;Gate",
            subtitle = "Rewatch Protocol • Ep 22 Milestone",
            statusBadge = "REWATCH",
            metricPrimary = "22/24 Ep",
            metricSecondary = "Classic S-Tier",
            progressCurrent = 22,
            progressTarget = 24
        ),
        MediaTelemetryEntity(
            mediaType = "ANIME",
            title = "Solo Leveling (Light Novel Vol. 4)",
            subtitle = "Shadow Monarch Arc • Night Reading",
            statusBadge = "READING",
            metricPrimary = "142/320 Pg",
            metricSecondary = "Lore Sync",
            progressCurrent = 142,
            progressTarget = 320
        )
    )

    fun defaultBadges(): List<BadgeEntity> = listOf(
        BadgeEntity(
            tier = "S-TIER",
            name = "S-Rank Otaku",
            requirement = "100+ Anime Series",
            iconKey = "STAR",
            isFeatured = true,
            isUnlocked = true
        ),
        BadgeEntity(
            tier = "MYTHIC",
            name = "Quest Conqueror",
            requirement = "100+ Milestones",
            iconKey = "SHIELD",
            isFeatured = true,
            isUnlocked = true
        ),
        BadgeEntity(
            tier = "ELITE",
            name = "CS2 Sharpshooter",
            requirement = "500h Combat Rec",
            iconKey = "CHECK",
            isFeatured = true,
            isUnlocked = true
        ),
        BadgeEntity(
            tier = "GEN 0",
            name = "Genesis Vault",
            requirement = "Early Adopter",
            iconKey = "FINGERPRINT",
            isFeatured = true,
            isUnlocked = true
        ),
        BadgeEntity(
            tier = "S-TIER",
            name = "Shader Architect",
            requirement = "1,000+ GitHub Stars",
            iconKey = "CODE",
            isFeatured = false,
            isUnlocked = true
        ),
        BadgeEntity(
            tier = "ELITE",
            name = "Dawn Runner",
            requirement = "21-Day 6AM Streak",
            iconKey = "FLAME",
            isFeatured = false,
            isUnlocked = true
        ),
        BadgeEntity(
            tier = "MYTHIC",
            name = "Level 75 Ascendant",
            requirement = "Reach Operator Lv. 75",
            iconKey = "TROPHY",
            isFeatured = false,
            isUnlocked = false
        ),
        BadgeEntity(
            tier = "RARE",
            name = "Archival Chronicler",
            requirement = "400 Journal Logs",
            iconKey = "GAMEPAD",
            isFeatured = false,
            isUnlocked = false
        )
    )

    fun defaultActivities(now: Long = System.currentTimeMillis()): List<ActivityStreamEntity> = listOf(
        ActivityStreamEntity(
            category = "CODE DISPATCH",
            timeAgo = "18m ago",
            title = "Pushed 4 commits to neo-tokyo/matrix-core",
            detail = "Refactored real-time peer sockets with 0.8ms latency...",
            accentType = "CORAL",
            createdAtMillis = now - 1_080_000L
        ),
        ActivityStreamEntity(
            category = "MEDIA SCROBBLE",
            timeAgo = "2h ago",
            title = "Completed Season Finale",
            detail = "Frieren: Beyond Journey's End • Rated 10/10 \"Pure Cinema\"",
            accentType = "VIOLET",
            createdAtMillis = now - 7_200_000L
        ),
        ActivityStreamEntity(
            category = "GAMING TELEMETRY",
            timeAgo = "Yesterday",
            title = "Unlocked CS2 Achievement: \"Cold Calculation\"",
            detail = "Clutched 1v4 defuse in competitive Mirage lobby.",
            accentType = "CYAN",
            createdAtMillis = now - 86_400_000L
        )
    )
}
