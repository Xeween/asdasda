package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.OperatorSettings
import com.example.data.model.OperatorProfileEntity
import com.example.ui.theme.BorderBright
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CoralAccent
import com.example.ui.theme.CoralDark
import com.example.ui.theme.CoralText
import com.example.ui.theme.CrimsonBg
import com.example.ui.theme.CrimsonRank
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanBg
import com.example.ui.theme.MonoFontFamily
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VioletBg
import com.example.ui.theme.VioletBright

@Composable
fun SettingsScreen(
    profile: OperatorProfileEntity,
    settings: OperatorSettings,
    onEditProfile: () -> Unit,
    onToggleHaptics: (Boolean) -> Unit,
    onToggleCompact: (Boolean) -> Unit,
    onToggleTerminal: (Boolean) -> Unit,
    onToggleAutoHashtag: (Boolean) -> Unit,
    onRecordCheckpoint: () -> Unit,
    onResetDemoData: () -> Unit,
    onCopyFeedback: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboard = LocalClipboardManager.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBg),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .widthIn(max = 620.dp)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Operator Identity Card
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceCard,
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "/// OPERATOR IDENTITY",
                                    fontFamily = MonoFontFamily,
                                    fontSize = 10.sp,
                                    color = CyanAccent
                                )
                                Text(
                                    text = profile.displayName,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${profile.locationLabel} • Lv.${profile.level} • ${profile.tierLabel}",
                                    fontFamily = MonoFontFamily,
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }

                            Button(
                                onClick = onEditProfile,
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CoralAccent,
                                    contentColor = CoralText
                                ),
                                modifier = Modifier.testTag("edit_profile_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "EDIT",
                                    fontFamily = MonoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            // Interface & Telemetry Toggles
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceCard,
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Tune,
                                contentDescription = null,
                                tint = VioletBright,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "System & Interface Protocols",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary
                            )
                        }

                        SettingSwitchRow(
                            title = "Show System Log Terminal",
                            subtitle = "Display live SYSTEM_LOG footer bar in Quests",
                            checked = settings.showSystemTerminal,
                            onCheckedChange = onToggleTerminal,
                            testTag = "switch_system_terminal"
                        )
                        SettingSwitchRow(
                            title = "Tactile Haptic Feedback",
                            subtitle = "Subtle pulse feedback on quest & XP triggers",
                            checked = settings.hapticFeedback,
                            onCheckedChange = onToggleHaptics,
                            testTag = "switch_haptics"
                        )
                        SettingSwitchRow(
                            title = "Auto-Tag Journal Dispatches",
                            subtitle = "Default to #DIARY when posting untagged logs",
                            checked = settings.autoHashtagOnPost,
                            onCheckedChange = onToggleAutoHashtag,
                            testTag = "switch_auto_hashtag"
                        )
                        SettingSwitchRow(
                            title = "Compact Telemetry Density",
                            subtitle = "Tighter vertical card spacing on large screens",
                            checked = settings.compactDensity,
                            onCheckedChange = onToggleCompact,
                            testTag = "switch_compact_density"
                        )
                    }
                }
            }

            // Local Storage & Export Card
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceCard,
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Storage,
                                    contentDescription = null,
                                    tint = CyanAccent,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Local Room Engine",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextPrimary
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(5.dp),
                                color = CyanBg
                            ) {
                                Text(
                                    text = settings.lastSyncCheckpoint,
                                    fontFamily = MonoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.5.sp,
                                    color = CyanAccent,
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Text(
                            text = "All logs, quests, XP progression, and workshop projects persist locally via SQLite Room + Jetpack DataStore.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = onRecordCheckpoint,
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, BorderBright),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = SurfaceElevated,
                                    contentColor = TextPrimary
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Save,
                                    contentDescription = null,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "CHECKPOINT",
                                    fontFamily = MonoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.5.sp
                                )
                            }

                            OutlinedButton(
                                onClick = {
                                    val snapshot = """
                                        {"operator":"${profile.displayName}","level":${profile.level},"xp":${profile.currentXp},"questsCompleted":${profile.questsCompletedTotal},"logs":${profile.journalLogsTotal}}
                                    """.trimIndent()
                                    clipboard.setText(AnnotatedString(snapshot))
                                    onCopyFeedback("Copied operator JSON snapshot")
                                },
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, BorderBright),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = SurfaceElevated,
                                    contentColor = TextPrimary
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ContentCopy,
                                    contentDescription = null,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "EXPORT JSON",
                                    fontFamily = MonoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.5.sp
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = onResetDemoData,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, CrimsonRank.copy(alpha = 0.5f)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = CrimsonBg,
                                contentColor = CoralAccent
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("reset_demo_data_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.RestartAlt,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "RESTORE BASELINE TELEMETRY",
                                fontFamily = MonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun SettingSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp, fontWeight = FontWeight.Medium),
                color = TextPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = CoralText,
                checkedTrackColor = CoralAccent,
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = SurfaceElevated,
                uncheckedBorderColor = BorderBright
            ),
            modifier = Modifier.testTag(testTag)
        )
    }
}
