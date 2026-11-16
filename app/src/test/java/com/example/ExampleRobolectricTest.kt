package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.HubDatabase
import com.example.data.local.SettingsDataStore
import com.example.data.repository.HubRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    private lateinit var context: Context
    private lateinit var db: HubDatabase
    private lateinit var repository: HubRepository

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, HubDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = HubRepository(db.hubDao(), SettingsDataStore(context))
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun `read app name from context`() {
        val appName = context.getString(R.string.app_name)
        assertEquals("Hub Operator", appName)
    }

    @Test
    fun `repository seeds and persists xp and logs`() = runTest {
        repository.ensureSeeded()
        val initialProfile = repository.profileFlow.first()!!
        assertEquals(74, initialProfile.level)
        assertEquals(8450, initialProfile.currentXp)

        repository.claimDailyXpBoost()
        val boostedProfile = repository.profileFlow.first()!!
        assertEquals(8800, boostedProfile.currentXp)

        val initialLogs = repository.feedLogsFlow.first().size
        repository.postFeedLog("Testing Room pipeline #CODE")
        val updatedLogs = repository.feedLogsFlow.first()
        assertEquals(initialLogs + 1, updatedLogs.size)
        assertTrue(updatedLogs.first().tagsCsv.contains("#CODE"))
    }
}
