package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ActivityStreamEntity
import com.example.data.model.BadgeEntity
import com.example.data.model.FeedLogEntity
import com.example.data.model.MediaTelemetryEntity
import com.example.data.model.OperatorProfileEntity
import com.example.data.model.QuestEntity
import com.example.data.model.WorkshopProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HubDao {
    // Profile
    @Query("SELECT * FROM operator_profile WHERE id = 1")
    fun observeProfile(): Flow<OperatorProfileEntity?>

    @Query("SELECT * FROM operator_profile WHERE id = 1")
    suspend fun getProfileOnce(): OperatorProfileEntity?

    @Query("SELECT COUNT(*) FROM operator_profile")
    suspend fun getProfileCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: OperatorProfileEntity)

    // Feed Logs
    @Query("SELECT * FROM feed_logs ORDER BY createdAtMillis DESC, id DESC")
    fun observeFeedLogs(): Flow<List<FeedLogEntity>>

    @Query("SELECT * FROM feed_logs WHERE id = :id")
    suspend fun getFeedLogById(id: Int): FeedLogEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedLog(log: FeedLogEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedLogs(logs: List<FeedLogEntity>)

    @Update
    suspend fun updateFeedLog(log: FeedLogEntity)

    @Query("DELETE FROM feed_logs WHERE id = :id")
    suspend fun deleteFeedLogById(id: Int)

    @Query("DELETE FROM feed_logs")
    suspend fun clearFeedLogs()

    // Quests & Goals
    @Query("SELECT * FROM quests ORDER BY isLongTermGoal ASC, sortOrder ASC, id ASC")
    fun observeQuests(): Flow<List<QuestEntity>>

    @Query("SELECT * FROM quests WHERE id = :id")
    suspend fun getQuestById(id: Int): QuestEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuest(quest: QuestEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuests(quests: List<QuestEntity>)

    @Update
    suspend fun updateQuest(quest: QuestEntity)

    @Query("DELETE FROM quests WHERE id = :id")
    suspend fun deleteQuestById(id: Int)

    @Query("DELETE FROM quests")
    suspend fun clearQuests()

    // Workshop Projects
    @Query("SELECT * FROM workshop_projects ORDER BY isFeaturedHero DESC, id ASC")
    fun observeProjects(): Flow<List<WorkshopProjectEntity>>

    @Query("SELECT * FROM workshop_projects WHERE id = :id")
    suspend fun getProjectById(id: Int): WorkshopProjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: WorkshopProjectEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjects(projects: List<WorkshopProjectEntity>)

    @Update
    suspend fun updateProject(project: WorkshopProjectEntity)

    @Query("DELETE FROM workshop_projects")
    suspend fun clearProjects()

    // Media Telemetry
    @Query("SELECT * FROM media_telemetry ORDER BY id ASC")
    fun observeMediaTelemetry(): Flow<List<MediaTelemetryEntity>>

    @Query("SELECT * FROM media_telemetry WHERE id = :id")
    suspend fun getMediaById(id: Int): MediaTelemetryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMediaList(items: List<MediaTelemetryEntity>)

    @Update
    suspend fun updateMedia(item: MediaTelemetryEntity)

    @Query("DELETE FROM media_telemetry")
    suspend fun clearMedia()

    // Badges
    @Query("SELECT * FROM operator_badges ORDER BY isFeatured DESC, isUnlocked DESC, id ASC")
    fun observeBadges(): Flow<List<BadgeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBadges(badges: List<BadgeEntity>)

    @Query("DELETE FROM operator_badges")
    suspend fun clearBadges()

    // Activity Stream
    @Query("SELECT * FROM activity_stream ORDER BY createdAtMillis DESC, id DESC LIMIT 20")
    fun observeActivityStream(): Flow<List<ActivityStreamEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(item: ActivityStreamEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivities(items: List<ActivityStreamEntity>)

    @Query("DELETE FROM activity_stream")
    suspend fun clearActivities()
}
