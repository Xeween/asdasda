package com.example.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.OperatorSettings
import com.example.data.model.BadgeEntity
import com.example.data.model.OperatorProfileEntity
import com.example.data.model.WorkshopProjectEntity
import com.example.ui.theme.BorderBright
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CoralAccent
import com.example.ui.theme.CoralText
import com.example.ui.theme.CrimsonBg
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanBg
import com.example.ui.theme.EmeraldOnline
import com.example.ui.theme.MonoFontFamily
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.SurfaceInner
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VioletAccent
import com.example.ui.theme.VioletBg
import com.example.ui.theme.VioletBright
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SyncStatusDialog(
    settings: OperatorSettings,
    onRecordCheckpoint: () -> Unit,
    onOpenSettings: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = SurfaceCard,
            border = BorderStroke(1.dp, BorderBright)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
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
                        Box(
                            modifier = Modifier
                                .size(9.dp)
                                .clip(CircleShape)
                                .background(EmeraldOnline)
                        )
                        Text(
                            text = "SYNC TELEMETRY",
                            fontFamily = MonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = TextPrimary
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Filled.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceInner,
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        StatusLine("ENGINE", "SQLite Room + DataStore")
                        StatusLine("STATUS", settings.lastSyncCheckpoint)
                        StatusLine("MODE", "Offline-First Local Vault")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            onRecordCheckpoint()
                            onDismiss()
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CoralAccent,
                            contentColor = CoralText
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "SAVE CHECKPOINT",
                            fontFamily = MonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                    OutlinedButton(
                        onClick = {
                            onDismiss()
                            onOpenSettings()
                        },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, BorderBright),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "SETTINGS",
                            fontFamily = MonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = TextPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusLine(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontFamily = MonoFontFamily, fontSize = 10.sp, color = TextMuted)
        Text(text = value, fontFamily = MonoFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 10.5.sp, color = CyanAccent)
    }
}

@Composable
fun QuickDispatchDialog(
    onClaimXp: () -> Unit,
    onNewQuest: () -> Unit,
    onQuickLog: (String) -> Unit,
    onCycleWatching: () -> Unit,
    onDismiss: () -> Unit
) {
    var quickNote by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = SurfaceCard,
            border = BorderStroke(1.dp, BorderBright)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Filled.Bolt, contentDescription = null, tint = CoralAccent, modifier = Modifier.size(18.dp))
                        Text(
                            text = "QUICK COMMAND",
                            fontFamily = MonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = TextPrimary
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Filled.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                OutlinedTextField(
                    value = quickNote,
                    onValueChange = { quickNote = it },
                    label = { Text("Quick Telegram Log (#TAG optional)", fontSize = 12.sp) },
                    singleLine = true,
                    colors = hubTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        if (quickNote.isNotBlank()) {
                            onQuickLog(quickNote)
                            onDismiss()
                        }
                    },
                    enabled = quickNote.isNotBlank(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CoralAccent, contentColor = CoralText),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.Send, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("DISPATCH LOG", fontFamily = MonoFontFamily, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            onClaimXp()
                            onDismiss()
                        },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, BorderBright),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("+350 XP", fontFamily = MonoFontFamily, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = CoralAccent)
                    }
                    OutlinedButton(
                        onClick = {
                            onDismiss()
                            onNewQuest()
                        },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, BorderBright),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Filled.AddTask, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("QUEST", fontFamily = MonoFontFamily, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = TextPrimary)
                    }
                }

                OutlinedButton(
                    onClick = {
                        onCycleWatching()
                        onDismiss()
                    },
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.SwapHoriz, contentDescription = null, tint = VioletBright, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("CYCLE NOW WATCHING STATUS", fontFamily = MonoFontFamily, fontSize = 10.5.sp, color = TextSecondary)
                }
            }
        }
    }
}

@Composable
fun BadgeVaultDialog(
    badges: List<BadgeEntity>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = SurfaceCard,
            border = BorderStroke(1.dp, BorderBright),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 520.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "BADGE VAULT (${badges.count { it.isUnlocked }}/${badges.size})",
                        fontFamily = MonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TextPrimary
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Filled.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(badges, key = { it.id }) { badge ->
                        val icon = when (badge.iconKey.uppercase()) {
                            "STAR" -> Icons.Filled.Star
                            "SHIELD" -> Icons.Filled.Shield
                            "CHECK" -> Icons.Filled.Verified
                            "FINGERPRINT" -> Icons.Filled.Fingerprint
                            "CODE" -> Icons.Filled.Code
                            "FLAME" -> Icons.Filled.LocalFireDepartment
                            "TROPHY" -> Icons.Filled.MilitaryTech
                            else -> Icons.Filled.SportsEsports
                        }
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (badge.isUnlocked) SurfaceInner else ObsidianBg,
                            border = BorderStroke(1.dp, if (badge.isUnlocked) BorderBright else BorderSubtle)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = if (badge.isUnlocked) icon else Icons.Filled.Lock,
                                    contentDescription = badge.name,
                                    tint = if (badge.isUnlocked) CoralAccent else TextMuted,
                                    modifier = Modifier.size(22.dp)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = badge.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = if (badge.isUnlocked) TextPrimary else TextSecondary
                                    )
                                    Text(
                                        text = badge.requirement,
                                        fontFamily = MonoFontFamily,
                                        fontSize = 10.sp,
                                        color = TextMuted
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(5.dp),
                                    color = if (badge.isUnlocked) VioletBg else SurfaceElevated
                                ) {
                                    Text(
                                        text = badge.tier,
                                        fontFamily = MonoFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp,
                                        color = if (badge.isUnlocked) VioletBright else TextMuted,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectPreviewDialog(
    project: WorkshopProjectEntity,
    onDismiss: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "shaderWave")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (Math.PI * 2).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shaderPhase"
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = SurfaceCard,
            border = BorderStroke(1.dp, BorderBright)
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
                    Column {
                        Text(
                            text = "SANDBOX RUNTIME // ${project.badgeLabel}",
                            fontFamily = MonoFontFamily,
                            fontSize = 9.5.sp,
                            color = CyanAccent
                        )
                        Text(
                            text = project.name,
                            style = MaterialTheme.typography.titleLarge,
                            color = TextPrimary
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Filled.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                if (project.isFeaturedHero || project.name.contains("Aether", ignoreCase = true)) {
                    // Live Animated Compute Shader Wireframe Canvas
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = ObsidianBg,
                        border = BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(170.dp)
                    ) {
                        Box {
                            Canvas(modifier = Modifier.matchParentSize()) {
                                val rows = 10
                                val cols = 22
                                val stepX = size.width / (cols - 1)
                                val baseStepY = size.height / (rows + 2)
                                for (r in 1..rows) {
                                    val path = Path()
                                    val rowRatio = r / rows.toFloat()
                                    for (c in 0 until cols) {
                                        val x = c * stepX
                                        val wave = sin(c * 0.45f + phase + r * 0.4f) * 14f +
                                            cos(c * 0.25f - phase * 0.8f) * 8f
                                        val y = r * baseStepY + wave
                                        if (c == 0) path.moveTo(x, y) else path.lineTo(x, y)
                                    }
                                    val strokeColor = if (rowRatio > 0.55f) CoralAccent else CyanAccent
                                    drawPath(
                                        path = path,
                                        color = strokeColor.copy(alpha = 0.35f + 0.55f * rowRatio),
                                        style = Stroke(width = 1.6.dp.toPx())
                                    )
                                }
                                drawLine(
                                    color = VioletBright.copy(alpha = 0.25f),
                                    start = Offset(0f, size.height * 0.5f),
                                    end = Offset(size.width, size.height * 0.5f),
                                    strokeWidth = 1.dp.toPx()
                                )
                            }
                            Text(
                                text = "60 FPS VOXEL RAYMARCH // ACTIVE",
                                fontFamily = MonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.5.sp,
                                color = EmeraldOnline,
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(10.dp)
                            )
                        }
                    }
                } else {
                    // Encrypted Note Vault Preview
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = ObsidianBg,
                        border = BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "# /vault/2026-11-16-architecture.md",
                                fontFamily = MonoFontFamily,
                                fontSize = 10.5.sp,
                                color = VioletBright
                            )
                            Text(
                                text = "AES-256-GCM local block cipher initialized.\nSync state: Offline-first IndexedDB mirror ready.\n${project.description}",
                                fontFamily = MonoFontFamily,
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }

                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VioletAccent, contentColor = TextPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("CLOSE RUNTIME PREVIEW", fontFamily = MonoFontFamily, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun NewQuestDialog(
    defaultLongTerm: Boolean,
    onCreate: (title: String, subtitle: String, rank: String, category: String, xp: Int, isLongTerm: Boolean, target: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var subtitle by remember { mutableStateOf("") }
    var selectedRank by remember { mutableStateOf("A") }
    var category by remember { mutableStateOf("SYSTEM") }
    var xpText by remember { mutableStateOf("100") }
    var isLongTerm by remember { mutableStateOf(defaultLongTerm) }
    var targetSteps by remember { mutableIntStateOf(10) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = SurfaceCard,
            border = BorderStroke(1.dp, BorderBright)
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "NEW QUEST DIRECTIVE",
                    fontFamily = MonoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = CoralAccent
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Quest Title", fontSize = 12.sp) },
                    singleLine = true,
                    colors = hubTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = subtitle,
                    onValueChange = { subtitle = it },
                    label = { Text("Protocol Note / Subtitle", fontSize = 12.sp) },
                    singleLine = true,
                    colors = hubTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("S", "A", "B", "C").forEach { rank ->
                        val active = selectedRank == rank
                        Surface(
                            onClick = { selectedRank = rank },
                            shape = RoundedCornerShape(8.dp),
                            color = if (active) CoralAccent else SurfaceElevated,
                            border = BorderStroke(1.dp, if (active) CoralAccent else BorderSubtle),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "RANK $rank",
                                    fontFamily = MonoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.5.sp,
                                    color = if (active) CoralText else TextSecondary
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Category", fontSize = 12.sp) },
                        singleLine = true,
                        colors = hubTextFieldColors(),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = xpText,
                        onValueChange = { xpText = it.filter { ch -> ch.isDigit() } },
                        label = { Text("XP", fontSize = 12.sp) },
                        singleLine = true,
                        colors = hubTextFieldColors(),
                        modifier = Modifier.width(96.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Long-Term Goal Milestone",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary
                    )
                    Switch(
                        checked = isLongTerm,
                        onCheckedChange = { isLongTerm = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = CoralText, checkedTrackColor = CoralAccent)
                    )
                }

                if (isLongTerm) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Target Steps: $targetSteps",
                            fontFamily = MonoFontFamily,
                            fontSize = 12.sp,
                            color = CyanAccent
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(onClick = { if (targetSteps > 2) targetSteps -= 2 }) {
                                Text("-2", color = TextPrimary)
                            }
                            OutlinedButton(onClick = { targetSteps += 2 }) {
                                Text("+2", color = TextPrimary)
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("CANCEL", fontFamily = MonoFontFamily, fontSize = 11.sp, color = TextSecondary)
                    }
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onCreate(
                                    title,
                                    subtitle,
                                    selectedRank,
                                    category,
                                    xpText.toIntOrNull() ?: 100,
                                    isLongTerm,
                                    targetSteps
                                )
                                onDismiss()
                            }
                        },
                        enabled = title.isNotBlank(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CoralAccent, contentColor = CoralText),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("REGISTER", fontFamily = MonoFontFamily, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun NewProjectDialog(
    onCreate: (name: String, badge: String, desc: String, tags: String, progress: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var badge by remember { mutableStateOf("v0.2.0") }
    var desc by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("#KOTLIN,#COMPOSE") }
    var progressText by remember { mutableStateOf("65") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = SurfaceCard,
            border = BorderStroke(1.dp, BorderBright)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "ADD WORKSHOP REPOSITORY",
                    fontFamily = MonoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = CyanAccent
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Project Name", fontSize = 12.sp) },
                    singleLine = true,
                    colors = hubTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Short Description", fontSize = 12.sp) },
                    colors = hubTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = badge,
                        onValueChange = { badge = it },
                        label = { Text("Badge / Version", fontSize = 12.sp) },
                        singleLine = true,
                        colors = hubTextFieldColors(),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = progressText,
                        onValueChange = { progressText = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Done %", fontSize = 12.sp) },
                        singleLine = true,
                        colors = hubTextFieldColors(),
                        modifier = Modifier.width(88.dp)
                    )
                }
                OutlinedTextField(
                    value = tags,
                    onValueChange = { tags = it },
                    label = { Text("Tags (comma separated)", fontSize = 12.sp) },
                    singleLine = true,
                    colors = hubTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("CANCEL", fontFamily = MonoFontFamily, fontSize = 11.sp, color = TextSecondary)
                    }
                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                onCreate(name, badge, desc, tags, progressText.toIntOrNull() ?: 50)
                                onDismiss()
                            }
                        },
                        enabled = name.isNotBlank(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CoralAccent, contentColor = CoralText),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("DEPLOY", fontFamily = MonoFontFamily, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun EditProfileDialog(
    profile: OperatorProfileEntity,
    onSave: (displayName: String, bio: String, location: String, watching: String, telegram: String, github: String) -> Unit,
    onDismiss: () -> Unit
) {
    var displayName by remember { mutableStateOf(profile.displayName) }
    var bio by remember { mutableStateOf(profile.bioQuote) }
    var location by remember { mutableStateOf(profile.locationLabel) }
    var watching by remember { mutableStateOf(profile.watchingStatus) }
    var telegram by remember { mutableStateOf(profile.telegramHandle) }
    var github by remember { mutableStateOf(profile.githubHandle) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = SurfaceCard,
            border = BorderStroke(1.dp, BorderBright)
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "EDIT OPERATOR IDENTITY",
                    fontFamily = MonoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = CoralAccent
                )

                OutlinedTextField(
                    value = displayName,
                    onValueChange = { displayName = it },
                    label = { Text("Display Name", fontSize = 12.sp) },
                    singleLine = true,
                    colors = hubTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location Tag", fontSize = 12.sp) },
                    singleLine = true,
                    colors = hubTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Operator Quote", fontSize = 12.sp) },
                    colors = hubTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = watching,
                    onValueChange = { watching = it },
                    label = { Text("Now Watching / Reading", fontSize = 12.sp) },
                    singleLine = true,
                    colors = hubTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = telegram,
                        onValueChange = { telegram = it },
                        label = { Text("Telegram", fontSize = 12.sp) },
                        singleLine = true,
                        colors = hubTextFieldColors(),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = github,
                        onValueChange = { github = it },
                        label = { Text("GitHub", fontSize = 12.sp) },
                        singleLine = true,
                        colors = hubTextFieldColors(),
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("CANCEL", fontFamily = MonoFontFamily, fontSize = 11.sp, color = TextSecondary)
                    }
                    Button(
                        onClick = {
                            onSave(displayName, bio, location, watching, telegram, github)
                            onDismiss()
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CoralAccent, contentColor = CoralText),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("SAVE", fontFamily = MonoFontFamily, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun hubTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    focusedBorderColor = CoralAccent,
    unfocusedBorderColor = BorderBright,
    focusedLabelColor = CoralAccent,
    unfocusedLabelColor = TextSecondary,
    cursorColor = CoralAccent
)
