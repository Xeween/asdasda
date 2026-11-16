package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ActivityStreamEntity
import com.example.data.model.BadgeEntity
import com.example.data.model.OperatorProfileEntity
import com.example.ui.components.AccentDot
import com.example.ui.components.GradientProgressBar
import com.example.ui.components.LevelRing
import com.example.ui.theme.BorderBright
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CoralAccent
import com.example.ui.theme.CoralDark
import com.example.ui.theme.CoralText
import com.example.ui.theme.CrimsonBg
import com.example.ui.theme.CrimsonRank
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanBg
import com.example.ui.theme.EmeraldBg
import com.example.ui.theme.EmeraldOnline
import com.example.ui.theme.MonoFontFamily
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.SurfaceInner
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VioletBg
import com.example.ui.theme.VioletBright
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ShowcaseScreen(
    profile: OperatorProfileEntity,
    badges: List<BadgeEntity>,
    activities: List<ActivityStreamEntity>,
    onCycleWatching: () -> Unit,
    onClaimDailyXp: () -> Unit,
    onResetSandboxXp: () -> Unit,
    onOpenBadgeVault: () -> Unit,
    onCopyFeedback: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboard = LocalClipboardManager.current
    val numberFmt = NumberFormat.getIntegerInstance(Locale.US)

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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Live Watching Banner + Hero Profile Card
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Watching status pill
                    Surface(
                        onClick = onCycleWatching,
                        shape = RoundedCornerShape(10.dp),
                        color = SurfaceCard,
                        border = BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("watching_status_pill")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(VioletBright)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = profile.watchingStatus,
                                fontFamily = MonoFontFamily,
                                fontSize = 11.sp,
                                color = TextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = Icons.Filled.SwapHoriz,
                                contentDescription = "Switch live status",
                                tint = TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Hero Profile Card
                    HeroOperatorCard(profile = profile, numberFmt = numberFmt)
                }
            }

            // 2. Comms & Dispatch Channels (Tap to Copy)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "/// COMMS & DISPATCH CHANNELS",
                            fontFamily = MonoFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.5.sp,
                            letterSpacing = 0.9.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = "TAP TO COPY",
                            fontFamily = MonoFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.sp,
                            letterSpacing = 0.8.sp,
                            color = CyanAccent
                        )
                    }

                    val copyChannel: (String, String) -> Unit = { label, value ->
                        clipboard.setText(AnnotatedString(value))
                        onCopyFeedback("Copied $label: $value")
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ChannelCopyCard(
                            label = "TELEGRAM",
                            value = profile.telegramHandle,
                            icon = Icons.Outlined.Send,
                            accentColor = CyanAccent,
                            onClick = { copyChannel("Telegram", profile.telegramHandle) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("copy_telegram_card")
                        )
                        ChannelCopyCard(
                            label = "STEAM ID",
                            value = profile.steamId,
                            icon = Icons.Filled.SportsEsports,
                            accentColor = CrimsonRank,
                            onClick = { copyChannel("Steam ID", profile.steamId) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("copy_steam_card")
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ChannelCopyCard(
                            label = "DISCORD",
                            value = profile.discordHandle,
                            icon = Icons.Outlined.Forum,
                            accentColor = VioletBright,
                            onClick = { copyChannel("Discord", profile.discordHandle) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("copy_discord_card")
                        )
                        ChannelCopyCard(
                            label = "GITHUB",
                            value = profile.githubHandle,
                            icon = Icons.Filled.Terminal,
                            accentColor = TextSecondary,
                            onClick = { copyChannel("GitHub", profile.githubHandle) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("copy_github_card")
                        )
                    }
                }
            }

            // 3. Operator Telemetry (Aggregate 2025)
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Operator Telemetry",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextPrimary
                        )
                        Text(
                            text = "AGGREGATE 2025",
                            fontFamily = MonoFontFamily,
                            fontSize = 10.sp,
                            letterSpacing = 0.8.sp,
                            color = TextMuted
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TelemetryStatCard(
                            icon = Icons.Filled.MilitaryTech,
                            iconColor = CoralAccent,
                            tag = "LIFETIME",
                            value = profile.questsCompletedTotal.toString(),
                            subtitle = "Quests Completed",
                            modifier = Modifier.weight(1f)
                        )
                        TelemetryStatCard(
                            icon = Icons.Filled.MenuBook,
                            iconColor = VioletBright,
                            tag = "PUBLIC",
                            value = profile.journalLogsTotal.toString(),
                            subtitle = "Journal Logs Published",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        TelemetryStatCard(
                            icon = Icons.Filled.RocketLaunch,
                            iconColor = CyanAccent,
                            tag = "PRODUCTION",
                            value = profile.projectsShippedTotal.toString(),
                            subtitle = "Projects Shipped",
                            modifier = Modifier.weight(1f)
                        )
                        TelemetryStatCard(
                            icon = Icons.Filled.LocalFireDepartment,
                            iconColor = CoralAccent,
                            tag = "HOT",
                            value = "${profile.activeStreakDays}d",
                            subtitle = "Active Coding Streak",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // 4. Featured Badges
            item {
                val featured = badges.filter { it.isFeatured }.take(4)
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Verified,
                                contentDescription = null,
                                tint = VioletBright,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Featured Badges",
                                style = MaterialTheme.typography.titleLarge,
                                color = TextPrimary
                            )
                        }

                        Surface(
                            onClick = onOpenBadgeVault,
                            color = Color.Transparent,
                            modifier = Modifier.testTag("view_all_badges_button")
                        ) {
                            Text(
                                text = "VIEW ALL (${badges.size}) >",
                                fontFamily = MonoFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.5.sp,
                                color = TextSecondary,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
                            )
                        }
                    }

                    featured.chunked(2).forEach { rowBadges ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowBadges.forEach { badge ->
                                BadgeCard(
                                    badge = badge,
                                    onClick = onOpenBadgeVault,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            // 5. XP Boost Sandbox
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
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Bolt,
                                    contentDescription = null,
                                    tint = CoralAccent,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "XP Boost Sandbox",
                                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                                    color = TextPrimary
                                )
                            }
                            Text(
                                text = "INSTANT SYNC",
                                fontFamily = MonoFontFamily,
                                fontSize = 10.sp,
                                letterSpacing = 0.8.sp,
                                color = TextSecondary
                            )
                        }

                        Text(
                            text = "Execute real-time profile state dispatching to test level thresholds and reward telemetry.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = onClaimDailyXp,
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CoralAccent,
                                    contentColor = CoralText
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .testTag("claim_daily_xp_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AddCircleOutline,
                                    contentDescription = stringResource(R.string.cd_claim_xp),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "CLAIM DAILY XP (+350 XP)",
                                    fontFamily = MonoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.5.sp
                                )
                            }

                            Surface(
                                onClick = onResetSandboxXp,
                                shape = RoundedCornerShape(10.dp),
                                color = SurfaceElevated,
                                border = BorderStroke(1.dp, BorderSubtle),
                                modifier = Modifier
                                    .size(46.dp)
                                    .testTag("reset_sandbox_xp_button")
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Filled.Refresh,
                                        contentDescription = stringResource(R.string.cd_reset_xp),
                                        tint = TextPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 6. Recent Activity Stream Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ChatBubbleOutline,
                            contentDescription = null,
                            tint = CyanAccent,
                            modifier = Modifier.size(17.dp)
                        )
                        Text(
                            text = "Recent Activity Stream",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextPrimary
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(CoralAccent)
                    )
                }
            }

            items(activities.take(6), key = { it.id }) { event ->
                ActivityStreamRow(event = event)
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun HeroOperatorCard(
    profile: OperatorProfileEntity,
    numberFmt: NumberFormat
) {
    val xpFraction = if (profile.nextLevelXp > 0) {
        profile.currentXp.toFloat() / profile.nextLevelXp.toFloat()
    } else 0f

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = SurfaceCard,
        border = BorderStroke(1.dp, BorderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Top Banner + Avatar Overlap
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(148.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_showcase_banner),
                    contentDescription = stringResource(R.string.cd_showcase_banner),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.25f),
                                    SurfaceCard.copy(alpha = 0.94f)
                                )
                            )
                        )
                )
                Text(
                    text = "PROFILE MATRIX v4.2",
                    fontFamily = MonoFontFamily,
                    fontSize = 9.5.sp,
                    color = TextSecondary.copy(alpha = 0.7f),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box {
                        Image(
                            painter = painterResource(id = R.drawable.img_operator_avatar),
                            contentDescription = stringResource(R.string.cd_operator_avatar),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(78.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(2.dp, VioletBright.copy(alpha = 0.75f), RoundedCornerShape(16.dp))
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = ObsidianBg,
                            border = BorderStroke(1.dp, EmeraldOnline.copy(alpha = 0.7f)),
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .offset(y = 6.dp)
                        ) {
                            Text(
                                text = "ONLINE",
                                fontFamily = MonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                color = EmeraldOnline,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Steam Level Pill Card
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceInner.copy(alpha = 0.92f),
                        border = BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            LevelRing(
                                level = profile.level,
                                progress = xpFraction,
                                size = 40.dp
                            )
                            Column {
                                Text(
                                    text = "STEAM LEVEL",
                                    fontFamily = MonoFontFamily,
                                    fontSize = 9.sp,
                                    color = TextMuted
                                )
                                Text(
                                    text = profile.tierLabel,
                                    fontFamily = MonoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.5.sp,
                                    color = TextPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            // Identity & Bio
            Column(
                modifier = Modifier.padding(start = 14.dp, end = 14.dp, top = 10.dp, bottom = 14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = profile.displayName,
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary
                    )
                    Surface(
                        shape = RoundedCornerShape(5.dp),
                        color = CrimsonBg,
                        border = BorderStroke(0.8.dp, CrimsonRank.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = profile.verifiedLabel,
                            fontFamily = MonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            color = CoralAccent,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = CyanBg
                ) {
                    Text(
                        text = profile.locationLabel,
                        fontFamily = MonoFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 10.sp,
                        color = CyanAccent,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Text(
                    text = profile.bioQuote,
                    style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                    color = TextSecondary
                )

                // Progression to next level box
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceInner,
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Bolt,
                                    contentDescription = null,
                                    tint = CoralAccent,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "PROGRESSION TO LEVEL ${profile.level + 1}",
                                    fontFamily = MonoFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                            }
                            Text(
                                text = "${numberFmt.format(profile.currentXp)} / ${numberFmt.format(profile.nextLevelXp)} XP",
                                fontFamily = MonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.5.sp,
                                color = CoralAccent
                            )
                        }
                        GradientProgressBar(progress = xpFraction, height = 7.dp)
                    }
                }
            }
        }
    }
}

@Composable
private fun ChannelCopyCard(
    label: String,
    value: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = SurfaceCard,
        border = BorderStroke(1.dp, BorderSubtle),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = SurfaceElevated,
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    fontFamily = MonoFontFamily,
                    fontSize = 8.5.sp,
                    color = TextMuted
                )
                Text(
                    text = value,
                    fontFamily = MonoFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.5.sp,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = Icons.Filled.ContentCopy,
                contentDescription = stringResource(R.string.cd_copy_handle),
                tint = TextMuted,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

@Composable
private fun TelemetryStatCard(
    icon: ImageVector,
    iconColor: Color,
    tag: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = SurfaceCard,
        border = BorderStroke(1.dp, BorderSubtle),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = tag,
                    fontFamily = MonoFontFamily,
                    fontSize = 9.5.sp,
                    color = TextMuted
                )
            }
            Text(
                text = value,
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 26.sp),
                color = TextPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun BadgeCard(
    badge: BadgeEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (tierBg, tierFg) = when (badge.tier.uppercase()) {
        "S-TIER" -> CrimsonBg to CoralAccent
        "MYTHIC" -> VioletBg to VioletBright
        "ELITE" -> CyanBg to CyanAccent
        else -> SurfaceElevated to TextSecondary
    }
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
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = SurfaceCard,
        border = BorderStroke(1.dp, BorderSubtle),
        modifier = modifier
    ) {
        Box(modifier = Modifier.padding(14.dp)) {
            Surface(
                shape = RoundedCornerShape(5.dp),
                color = tierBg,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Text(
                    text = badge.tier,
                    fontFamily = MonoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 8.5.sp,
                    color = tierFg,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceInner,
                    border = BorderStroke(1.dp, BorderBright),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = badge.name,
                            tint = tierFg,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Text(
                    text = badge.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 13.5.sp),
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = badge.requirement,
                    fontFamily = MonoFontFamily,
                    fontSize = 10.sp,
                    color = tierFg.copy(alpha = 0.9f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun ActivityStreamRow(event: ActivityStreamEntity) {
    val (bg, tint, icon) = when (event.accentType.uppercase()) {
        "VIOLET" -> Triple(VioletBg, VioletBright, Icons.Outlined.Movie)
        "CYAN" -> Triple(CyanBg, CyanAccent, Icons.Filled.SportsEsports)
        else -> Triple(CoralDark, CoralAccent, Icons.Filled.Code)
    }

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = SurfaceCard,
        border = BorderStroke(1.dp, BorderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = bg,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = tint,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = event.category,
                        fontFamily = MonoFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 9.5.sp,
                        color = tint
                    )
                    Text(
                        text = event.timeAgo,
                        fontFamily = MonoFontFamily,
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp),
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = event.detail,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
