package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.MilitaryTech
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.HubTab
import com.example.ui.theme.BorderBright
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.BottomBarBg
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
fun HubTopBar(
    title: String,
    syncLabel: String,
    onSyncClick: () -> Unit,
    onQuickActionClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = ObsidianBg,
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sync Status Pill
                Surface(
                    onClick = onSyncClick,
                    shape = RoundedCornerShape(10.dp),
                    color = SurfaceCard,
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier
                        .height(38.dp)
                        .testTag("sync_pill_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(CoralAccent)
                        )
                        Text(
                            text = syncLabel,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontSize = 11.sp,
                                letterSpacing = 0.8.sp
                            ),
                            color = TextPrimary
                        )
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = stringResource(R.string.cd_sync_status),
                            tint = TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Title & Overline
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.hub_overline),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp),
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Quick Bolt Button
                Surface(
                    onClick = onQuickActionClick,
                    shape = RoundedCornerShape(10.dp),
                    color = SurfaceCard,
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("quick_action_button")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.Bolt,
                            contentDescription = stringResource(R.string.cd_quick_actions),
                            tint = TextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Operator Profile Circle Button
                Surface(
                    onClick = onProfileClick,
                    shape = CircleShape,
                    color = CoralAccent,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("profile_circle_button")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = stringResource(R.string.cd_operator_profile),
                            tint = CoralText,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(BorderSubtle.copy(alpha = 0.6f))
            )
        }
    }
}

private data class BottomNavSpec(
    val tab: HubTab,
    val labelRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

private val bottomNavSpecs = listOf(
    BottomNavSpec(HubTab.SHOWCASE, R.string.nav_showcase, Icons.Filled.Badge, Icons.Outlined.Badge),
    BottomNavSpec(HubTab.TELEGRAM, R.string.nav_telegram, Icons.Filled.ChatBubble, Icons.Outlined.ChatBubbleOutline),
    BottomNavSpec(HubTab.QUESTS, R.string.nav_quests, Icons.Filled.MilitaryTech, Icons.Outlined.MilitaryTech),
    BottomNavSpec(HubTab.STATS, R.string.nav_stats, Icons.Filled.SportsEsports, Icons.Outlined.SportsEsports),
    BottomNavSpec(HubTab.SETTINGS, R.string.nav_settings, Icons.Filled.Settings, Icons.Outlined.Settings)
)

@Composable
fun HubBottomBar(
    currentTab: HubTab,
    onSelectTab: (HubTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = BottomBarBg,
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(BorderSubtle)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 4.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                bottomNavSpecs.forEach { spec ->
                    val selected = spec.tab == currentTab
                    val tint = if (selected) CoralAccent else TextMuted
                    val label = stringResource(spec.labelRes)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .minimumInteractiveComponentSize()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onSelectTab(spec.tab) }
                            .padding(vertical = 4.dp)
                            .testTag("nav_${spec.tab.route}")
                    ) {
                        Icon(
                            imageVector = if (selected) spec.selectedIcon else spec.unselectedIcon,
                            contentDescription = label,
                            tint = tint,
                            modifier = Modifier.size(21.dp)
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = label,
                            fontFamily = MonoFontFamily,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 9.sp,
                            letterSpacing = 0.5.sp,
                            color = tint,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GradientProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 7.dp,
    startColor: Color = VioletBright,
    midColor: Color = CrimsonRank,
    endColor: Color = CoralAccent
) {
    val clamped by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 450),
        label = "progressAnim"
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(99.dp))
            .background(Color(0xFF0A0B0F))
            .border(0.5.dp, BorderSubtle, RoundedCornerShape(99.dp))
    ) {
        if (clamped > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(clamped)
                    .height(height)
                    .clip(RoundedCornerShape(99.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(startColor, midColor, endColor)
                        )
                    )
            )
        }
    }
}

@Composable
fun LevelRing(
    level: Int,
    progress: Float,
    size: Dp = 44.dp,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0.05f, 1f),
        animationSpec = tween(durationMillis = 500),
        label = "levelRingAnim"
    )
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val stroke = Stroke(width = 3.5.dp.toPx(), cap = StrokeCap.Round)
            drawArc(
                color = BorderBright,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = stroke
            )
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(CoralAccent, CrimsonRank, VioletBright, CoralAccent)
                ),
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = stroke
            )
        }
        Text(
            text = level.toString(),
            fontFamily = MonoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = TextPrimary
        )
    }
}

@Composable
fun RankChip(rank: String) {
    val (bg, fg) = when (rank.uppercase()) {
        "S" -> CrimsonBg to CoralAccent
        "A" -> VioletBg to VioletBright
        "B" -> SurfaceElevated to TextSecondary
        else -> CyanBg to CyanAccent
    }
    Surface(
        shape = RoundedCornerShape(5.dp),
        color = bg,
        border = BorderStroke(0.8.dp, fg.copy(alpha = 0.45f))
    ) {
        Text(
            text = "RANK $rank",
            fontFamily = MonoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 9.5.sp,
            color = fg,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun CategoryPill(
    text: String,
    accentColor: Color = CyanAccent,
    backgroundColor: Color = CyanBg
) {
    Surface(
        shape = RoundedCornerShape(5.dp),
        color = backgroundColor
    ) {
        Text(
            text = text,
            fontFamily = MonoFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 9.5.sp,
            color = accentColor,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun AccentDot(accentType: String, modifier: Modifier = Modifier) {
    val color = when (accentType.uppercase()) {
        "VIOLET" -> VioletBright
        "CYAN" -> CyanAccent
        else -> CoralAccent
    }
    Box(
        modifier = modifier
            .size(7.dp)
            .clip(CircleShape)
            .background(color)
    )
}
