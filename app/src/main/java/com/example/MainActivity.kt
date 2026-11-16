package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.data.model.WorkshopProjectEntity
import com.example.ui.HubTab
import com.example.ui.HubViewModel
import com.example.ui.components.HubBottomBar
import com.example.ui.components.HubTopBar
import com.example.ui.screens.BadgeVaultDialog
import com.example.ui.screens.EditProfileDialog
import com.example.ui.screens.NewProjectDialog
import com.example.ui.screens.NewQuestDialog
import com.example.ui.screens.ProjectPreviewDialog
import com.example.ui.screens.ProjectsGamingScreen
import com.example.ui.screens.QuestsScreen
import com.example.ui.screens.QuickDispatchDialog
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.ShowcaseScreen
import com.example.ui.screens.SyncStatusDialog
import com.example.ui.screens.TelegramFeedScreen
import com.example.ui.theme.BorderBright
import com.example.ui.theme.BottomBarBg
import com.example.ui.theme.CoralAccent
import com.example.ui.theme.CoralDark
import com.example.ui.theme.HubOperatorTheme
import com.example.ui.theme.MonoFontFamily
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        setContent {
            HubOperatorTheme {
                HubOperatorApp()
            }
        }
    }
}

@Composable
fun HubOperatorApp() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val viewModel: HubViewModel = viewModel(
        factory = HubViewModel.Factory(context.applicationContext as android.app.Application)
    )
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: HubTab.SHOWCASE.route
    val currentTab = HubTab.entries.find { it.route == currentRoute } ?: HubTab.SHOWCASE

    val snackbarHostState = remember { SnackbarHostState() }
    val haptics = LocalHapticFeedback.current

    var showSyncDialog by remember { mutableStateOf(false) }
    var showQuickDialog by remember { mutableStateOf(false) }
    var showBadgeVault by remember { mutableStateOf(false) }
    var showNewQuestDialog by remember { mutableStateOf(false) }
    var showNewProjectDialog by remember { mutableStateOf(false) }
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var previewProject by remember { mutableStateOf<WorkshopProjectEntity?>(null) }

    LaunchedEffect(Unit) {
        viewModel.toastEvents.collect { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    val navigateToTab: (HubTab) -> Unit = { tab ->
        if (currentTab != tab) {
            navController.navigate(tab.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    if (currentTab != HubTab.SHOWCASE) {
        BackHandler {
            navigateToTab(HubTab.SHOWCASE)
        }
    }

    val screenTitle = when (currentTab) {
        HubTab.SHOWCASE -> stringResource(R.string.title_showcase)
        HubTab.TELEGRAM -> stringResource(R.string.title_telegram)
        HubTab.QUESTS -> stringResource(R.string.title_quests)
        HubTab.STATS -> stringResource(R.string.title_stats)
        HubTab.SETTINGS -> stringResource(R.string.title_settings)
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBg)
    ) {
        val useNavRail = maxWidth >= 700.dp

        Row(modifier = Modifier.fillMaxSize()) {
            if (useNavRail) {
                NavigationRail(
                    containerColor = BottomBarBg,
                    modifier = Modifier
                        .fillMaxHeight()
                        .statusBarsPadding()
                ) {
                    val items = listOf(
                        Triple(HubTab.SHOWCASE, R.string.nav_showcase, Icons.Filled.Badge),
                        Triple(HubTab.TELEGRAM, R.string.nav_telegram, Icons.Filled.ChatBubble),
                        Triple(HubTab.QUESTS, R.string.nav_quests, Icons.Filled.MilitaryTech),
                        Triple(HubTab.STATS, R.string.nav_stats, Icons.Filled.SportsEsports),
                        Triple(HubTab.SETTINGS, R.string.nav_settings, Icons.Filled.Settings)
                    )
                    items.forEach { (tab, labelRes, icon) ->
                        NavigationRailItem(
                            selected = currentTab == tab,
                            onClick = { navigateToTab(tab) },
                            icon = { Icon(icon, contentDescription = stringResource(labelRes)) },
                            label = {
                                Text(
                                    text = stringResource(labelRes),
                                    fontFamily = MonoFontFamily,
                                    fontSize = 9.sp
                                )
                            },
                            colors = NavigationRailItemDefaults.colors(
                                selectedIconColor = CoralAccent,
                                selectedTextColor = CoralAccent,
                                indicatorColor = CoralDark,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            )
                        )
                    }
                }
            }

            Scaffold(
                containerColor = ObsidianBg,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                topBar = {
                    HubTopBar(
                        title = screenTitle,
                        syncLabel = stringResource(R.string.sync_label),
                        onSyncClick = { showSyncDialog = true },
                        onQuickActionClick = { showQuickDialog = true },
                        onProfileClick = { navigateToTab(HubTab.SHOWCASE) }
                    )
                },
                bottomBar = {
                    if (!useNavRail) {
                        HubBottomBar(
                            currentTab = currentTab,
                            onSelectTab = navigateToTab
                        )
                    }
                },
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState) { data ->
                        Snackbar(
                            shape = RoundedCornerShape(12.dp),
                            containerColor = SurfaceElevated,
                            contentColor = TextPrimary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = data.visuals.message,
                                fontFamily = MonoFontFamily,
                                fontSize = 12.sp
                            )
                        }
                    }
                },
                modifier = Modifier.weight(1f)
            ) { innerPadding ->
                val profile = state.profile
                if (state.isLoading || profile == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = CoralAccent,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                } else {
                    NavHost(
                        navController = navController,
                        startDestination = HubTab.SHOWCASE.route,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        composable(HubTab.SHOWCASE.route) {
                            ShowcaseScreen(
                                profile = profile,
                                badges = state.badges,
                                activities = state.activities,
                                onCycleWatching = { viewModel.cycleWatchingStatus() },
                                onClaimDailyXp = {
                                    if (state.settings.hapticFeedback) {
                                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                    }
                                    viewModel.claimDailyXpBoost()
                                },
                                onResetSandboxXp = { viewModel.resetSandboxXp() },
                                onOpenBadgeVault = { showBadgeVault = true },
                                onCopyFeedback = { viewModel.emitNotice(it) }
                            )
                        }
                        composable(HubTab.TELEGRAM.route) {
                            TelegramFeedScreen(
                                logs = state.feedLogs,
                                searchQuery = state.feedSearchQuery,
                                selectedTag = state.selectedFeedTag,
                                onSearchChange = { viewModel.setFeedSearchQuery(it) },
                                onTagSelect = { viewModel.setSelectedFeedTag(it) },
                                onPostLog = { text, tag -> viewModel.postFeedLog(text, tag) },
                                onToggleReaction = { id, emoji -> viewModel.toggleFeedReaction(id, emoji) },
                                onDeleteLog = { viewModel.deleteFeedLog(it) },
                                onCopyFeedback = { viewModel.emitNotice(it) }
                            )
                        }
                        composable(HubTab.QUESTS.route) {
                            QuestsScreen(
                                profile = profile,
                                quests = state.quests,
                                selectedSubTab = state.questSubTab,
                                systemTerminalMessage = state.systemTerminalMessage,
                                showSystemTerminal = state.settings.showSystemTerminal,
                                onSelectSubTab = { viewModel.setQuestSubTab(it) },
                                onToggleQuest = { id ->
                                    if (state.settings.hapticFeedback) {
                                        haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    }
                                    viewModel.toggleQuestCompletion(id)
                                },
                                onIncrementGoal = { viewModel.incrementGoalProgress(it) },
                                onDeleteQuest = { viewModel.deleteQuest(it) },
                                onOpenNewQuestDialog = { showNewQuestDialog = true }
                            )
                        }
                        composable(HubTab.STATS.route) {
                            ProjectsGamingScreen(
                                profile = profile,
                                projects = state.projects,
                                mediaItems = state.mediaItems,
                                selectedSubTab = state.workshopSubTab,
                                onSelectSubTab = { viewModel.setWorkshopSubTab(it) },
                                onToggleProjectStar = { viewModel.toggleProjectStar(it) },
                                onOpenProjectPreview = { previewProject = it },
                                onOpenNewProjectDialog = { showNewProjectDialog = true },
                                onIncrementMediaProgress = { viewModel.incrementMediaProgress(it) },
                                onCopyFeedback = { viewModel.emitNotice(it) }
                            )
                        }
                        composable(HubTab.SETTINGS.route) {
                            SettingsScreen(
                                profile = profile,
                                settings = state.settings,
                                onEditProfile = { showEditProfileDialog = true },
                                onToggleHaptics = { viewModel.setHapticFeedback(it) },
                                onToggleCompact = { viewModel.setCompactDensity(it) },
                                onToggleTerminal = { viewModel.setShowSystemTerminal(it) },
                                onToggleAutoHashtag = { viewModel.setAutoHashtag(it) },
                                onRecordCheckpoint = { viewModel.recordLocalCheckpoint() },
                                onResetDemoData = { viewModel.resetAllDemoData() },
                                onCopyFeedback = { viewModel.emitNotice(it) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Active Modals
    if (showSyncDialog) {
        SyncStatusDialog(
            settings = state.settings,
            onRecordCheckpoint = { viewModel.recordLocalCheckpoint() },
            onOpenSettings = { navigateToTab(HubTab.SETTINGS) },
            onDismiss = { showSyncDialog = false }
        )
    }

    if (showQuickDialog) {
        QuickDispatchDialog(
            onClaimXp = { viewModel.claimDailyXpBoost() },
            onNewQuest = { showNewQuestDialog = true },
            onQuickLog = { viewModel.postFeedLog(it) },
            onCycleWatching = { viewModel.cycleWatchingStatus() },
            onDismiss = { showQuickDialog = false }
        )
    }

    if (showBadgeVault) {
        BadgeVaultDialog(
            badges = state.badges,
            onDismiss = { showBadgeVault = false }
        )
    }

    previewProject?.let { project ->
        ProjectPreviewDialog(
            project = project,
            onDismiss = { previewProject = null }
        )
    }

    if (showNewQuestDialog) {
        NewQuestDialog(
            defaultLongTerm = state.questSubTab == 1,
            onCreate = { title, sub, rank, cat, xp, longTerm, target ->
                viewModel.addQuest(title, sub, rank, cat, xp, longTerm, target)
            },
            onDismiss = { showNewQuestDialog = false }
        )
    }

    if (showNewProjectDialog) {
        NewProjectDialog(
            onCreate = { name, badge, desc, tags, progress ->
                viewModel.addWorkshopProject(name, badge, desc, tags, progress)
            },
            onDismiss = { showNewProjectDialog = false }
        )
    }

    if (showEditProfileDialog) {
        state.profile?.let { profile ->
            EditProfileDialog(
                profile = profile,
                onSave = { name, bio, loc, watch, tg, gh ->
                    viewModel.updateProfile(name, bio, loc, watch, tg, gh)
                },
                onDismiss = { showEditProfileDialog = false }
            )
        }
    }
}
