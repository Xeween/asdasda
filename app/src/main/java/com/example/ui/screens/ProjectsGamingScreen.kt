package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.MediaTelemetryEntity
import com.example.data.model.OperatorProfileEntity
import com.example.data.model.WorkshopProjectEntity
import com.example.data.model.parseTags
import com.example.ui.components.GradientProgressBar
import com.example.ui.components.LevelRing
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BorderBright
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CoralAccent
import com.example.ui.theme.CoralText
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
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProjectsGamingScreen(
    profile: OperatorProfileEntity,
    projects: List<WorkshopProjectEntity>,
    mediaItems: List<MediaTelemetryEntity>,
    selectedSubTab: Int,
    onSelectSubTab: (Int) -> Unit,
    onToggleProjectStar: (Int) -> Unit,
    onOpenProjectPreview: (WorkshopProjectEntity) -> Unit,
    onOpenNewProjectDialog: () -> Unit,
    onIncrementMediaProgress: (Int) -> Unit,
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Workshop Profile Banner Card
            item {
                WorkshopHeroHeader(profile = profile, numberFmt = numberFmt)
            }

            // 2. Segmented Switch: Projects (Workshop) | Gaming & Anime
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceCard,
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        WorkshopTabChip(
                            label = "Projects (Workshop)",
                            icon = Icons.Filled.Build,
                            iconTint = CoralAccent,
                            selected = selectedSubTab == 0,
                            onClick = { onSelectSubTab(0) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("subtab_projects_workshop")
                        )
                        WorkshopTabChip(
                            label = "Gaming & Anime",
                            icon = Icons.Filled.SportsEsports,
                            iconTint = VioletBright,
                            selected = selectedSubTab == 1,
                            onClick = { onSelectSubTab(1) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("subtab_gaming_anime")
                        )
                    }
                }
            }

            if (selectedSubTab == 0) {
                // Section Header: /// WORKSHOP ENTRIES
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(16.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(CoralAccent)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "/// WORKSHOP ENTRIES",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = MonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.7.sp
                            ),
                            color = TextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${projects.size} REPOSITORIES",
                            fontFamily = MonoFontFamily,
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            onClick = onOpenNewProjectDialog,
                            shape = RoundedCornerShape(8.dp),
                            color = SurfaceElevated,
                            border = BorderStroke(1.dp, BorderBright),
                            modifier = Modifier.testTag("add_project_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = stringResource(R.string.cd_add_project),
                                    tint = CoralAccent,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = "ADD",
                                    fontFamily = MonoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.5.sp,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }

                items(projects, key = { it.id }) { project ->
                    if (project.isFeaturedHero) {
                        FeaturedProjectCard(
                            project = project,
                            numberFmt = numberFmt,
                            onStarClick = { onToggleProjectStar(project.id) },
                            onPrimaryClick = { onOpenProjectPreview(project) },
                            onGithubClick = {
                                val url = project.repoUrl.ifBlank { "https://github.com/kaelen-sol" }
                                clipboard.setText(AnnotatedString(url))
                                onCopyFeedback("Copied repository URL: $url")
                            }
                        )
                    } else {
                        SecondaryProjectCard(
                            project = project,
                            onPrimaryClick = { onOpenProjectPreview(project) }
                        )
                    }
                }
            } else {
                // Gaming & Anime Telemetry Sub-Tab
                val gamingItems = mediaItems.filter { it.mediaType == "GAMING" }
                val animeItems = mediaItems.filter { it.mediaType == "ANIME" }

                item {
                    SectionLabelRow(
                        title = "/// STEAM & COMBAT TELEMETRY",
                        trailing = "${gamingItems.size} TITLES",
                        accent = CyanAccent
                    )
                }

                items(gamingItems, key = { it.id }) { item ->
                    MediaTelemetryCard(
                        item = item,
                        accent = CyanAccent,
                        accentBg = CyanBg,
                        onIncrement = { onIncrementMediaProgress(item.id) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    SectionLabelRow(
                        title = "/// ANIME & LORE SCROBBLER",
                        trailing = "${animeItems.size} ENTRIES",
                        accent = VioletBright
                    )
                }

                items(animeItems, key = { it.id }) { item ->
                    MediaTelemetryCard(
                        item = item,
                        accent = VioletBright,
                        accentBg = VioletBg,
                        onIncrement = { onIncrementMediaProgress(item.id) }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun WorkshopHeroHeader(
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(176.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_workshop_profile_banner),
                contentDescription = stringResource(R.string.cd_workshop_banner),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.2f),
                                SurfaceCard.copy(alpha = 0.96f)
                            )
                        )
                    )
            )

            // In-Game Badge Top-Right
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = ObsidianBg.copy(alpha = 0.88f),
                border = BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(EmeraldOnline)
                    )
                    Text(
                        text = profile.inGameStatus,
                        fontFamily = MonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = TextPrimary
                    )
                }
            }

            // Bottom Row: Avatar, Handle/Guild, Steam XP + Ring
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_operator_avatar),
                    contentDescription = stringResource(R.string.cd_operator_avatar),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .border(2.dp, VioletBright.copy(alpha = 0.7f), RoundedCornerShape(14.dp))
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = profile.shortHandle,
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary
                    )
                    Text(
                        text = "Lv. ${profile.level} • ${profile.guildName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "STEAM XP",
                        fontFamily = MonoFontFamily,
                        fontSize = 9.sp,
                        color = TextSecondary
                    )
                    Text(
                        text = numberFmt.format(profile.steamXp),
                        fontFamily = MonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = CyanAccent
                    )
                }

                LevelRing(
                    level = profile.level,
                    progress = xpFraction,
                    size = 44.dp
                )
            }
        }
    }
}

@Composable
private fun WorkshopTabChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(9.dp),
        color = if (selected) SurfaceElevated else Color.Transparent,
        border = if (selected) BorderStroke(1.dp, BorderBright) else null,
        modifier = modifier.height(38.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) iconTint else TextMuted,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
                ),
                color = if (selected) TextPrimary else TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun FeaturedProjectCard(
    project: WorkshopProjectEntity,
    numberFmt: NumberFormat,
    onStarClick: () -> Unit,
    onPrimaryClick: () -> Unit,
    onGithubClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = SurfaceCard,
        border = BorderStroke(1.dp, BorderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // Shader Image Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(172.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_aether_shader),
                    contentDescription = stringResource(R.string.cd_aether_shader),
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
                                    SurfaceCard.copy(alpha = 0.85f)
                                )
                            )
                        )
                )

                // Top Tags + Star Pill
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        project.parseTags().take(3).forEachIndexed { idx, tag ->
                            Surface(
                                shape = RoundedCornerShape(5.dp),
                                color = ObsidianBg.copy(alpha = 0.85f),
                                border = BorderStroke(0.8.dp, BorderBright)
                            ) {
                                Text(
                                    text = tag,
                                    fontFamily = MonoFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.5.sp,
                                    color = if (idx == 0) CyanAccent else if (idx == 1) VioletBright else TextPrimary,
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }

                    Surface(
                        onClick = onStarClick,
                        shape = RoundedCornerShape(8.dp),
                        color = ObsidianBg.copy(alpha = 0.9f),
                        border = BorderStroke(
                            1.dp,
                            if (project.isStarredByUser) AmberGold else BorderBright
                        ),
                        modifier = Modifier.testTag("star_project_${project.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Star project",
                                tint = if (project.isStarredByUser) AmberGold else CoralAccent,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = numberFmt.format(project.starCount),
                                fontFamily = MonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }

            // Content Body
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = project.name,
                        style = MaterialTheme.typography.headlineSmall.copy(fontSize = 21.sp),
                        color = TextPrimary
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = SurfaceElevated,
                        border = BorderStroke(1.dp, BorderBright)
                    ) {
                        Text(
                            text = project.badgeLabel,
                            fontFamily = MonoFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.5.sp,
                            color = CoralAccent,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Text(
                    text = project.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

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
                            imageVector = Icons.Filled.Memory,
                            contentDescription = null,
                            tint = CyanAccent,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = project.milestoneLabel,
                            fontFamily = MonoFontFamily,
                            fontSize = 11.sp,
                            color = TextPrimary
                        )
                    }
                    Text(
                        text = "${project.progressPercent}%",
                        fontFamily = MonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.5.sp,
                        color = CoralAccent
                    )
                }

                GradientProgressBar(
                    progress = project.progressPercent / 100f,
                    height = 7.dp
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onPrimaryClick,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CoralAccent,
                            contentColor = CoralText
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("live_demo_button_${project.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = project.primaryActionLabel,
                            fontFamily = MonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp
                        )
                    }

                    OutlinedButton(
                        onClick = onGithubClick,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, BorderBright),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = SurfaceElevated,
                            contentColor = TextPrimary
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Code,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = project.secondaryActionLabel.ifBlank { "GITHUB" },
                            fontFamily = MonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SecondaryProjectCard(
    project: WorkshopProjectEntity,
    onPrimaryClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
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
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceElevated,
                    border = BorderStroke(1.dp, BorderBright),
                    modifier = Modifier.size(46.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.EditNote,
                            contentDescription = null,
                            tint = VioletBright,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = project.name,
                            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 19.sp),
                            color = TextPrimary
                        )
                        Surface(
                            shape = RoundedCornerShape(5.dp),
                            color = CyanBg
                        ) {
                            Text(
                                text = project.badgeLabel,
                                fontFamily = MonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.5.sp,
                                color = CyanAccent,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = project.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                project.parseTags().forEachIndexed { idx, tag ->
                    Surface(
                        shape = RoundedCornerShape(5.dp),
                        color = if (idx == 0) CyanBg else SurfaceElevated
                    ) {
                        Text(
                            text = tag,
                            fontFamily = MonoFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 9.5.sp,
                            color = if (idx == 0) CyanAccent else TextSecondary,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = SurfaceInner,
                border = BorderStroke(1.dp, BorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Verified,
                            contentDescription = null,
                            tint = VioletBright,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = project.milestoneLabel,
                            fontFamily = MonoFontFamily,
                            fontSize = 11.5.sp,
                            color = TextPrimary
                        )
                    }
                    Text(
                        text = "${project.progressPercent}% DONE",
                        fontFamily = MonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.5.sp,
                        color = VioletBright
                    )
                }
            }

            Button(
                onClick = onPrimaryClick,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VioletAccent,
                    contentColor = TextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("launch_site_button_${project.id}")
            ) {
                Icon(
                    imageVector = Icons.Filled.OpenInNew,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = project.primaryActionLabel,
                    fontFamily = MonoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.5.sp,
                    letterSpacing = 0.8.sp
                )
            }
        }
    }
}

@Composable
private fun SectionLabelRow(
    title: String,
    trailing: String,
    accent: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(15.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(accent)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            fontFamily = MonoFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = trailing,
            fontFamily = MonoFontFamily,
            fontSize = 10.sp,
            color = TextMuted
        )
    }
}

@Composable
private fun MediaTelemetryCard(
    item: MediaTelemetryEntity,
    accent: Color,
    accentBg: Color,
    onIncrement: () -> Unit
) {
    val fraction = if (item.progressTarget > 0) {
        item.progressCurrent.toFloat() / item.progressTarget.toFloat()
    } else 0f

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = SurfaceCard,
        border = BorderStroke(1.dp, BorderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(9.dp),
                    color = accentBg,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (item.mediaType == "GAMING") Icons.Filled.SportsEsports else Icons.Outlined.Movie,
                            contentDescription = null,
                            tint = accent,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = item.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Surface(
                    shape = RoundedCornerShape(5.dp),
                    color = accentBg
                ) {
                    Text(
                        text = item.statusBadge,
                        fontFamily = MonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        color = accent,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.metricPrimary,
                            fontFamily = MonoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = item.metricSecondary,
                            fontFamily = MonoFontFamily,
                            fontSize = 10.5.sp,
                            color = TextSecondary
                        )
                    }
                    GradientProgressBar(
                        progress = fraction,
                        height = 6.dp,
                        startColor = accent,
                        midColor = VioletBright,
                        endColor = CoralAccent
                    )
                }

                if (item.mediaType == "ANIME" && item.progressCurrent < item.progressTarget) {
                    Surface(
                        onClick = onIncrement,
                        shape = RoundedCornerShape(8.dp),
                        color = SurfaceElevated,
                        border = BorderStroke(1.dp, BorderBright),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Box(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+1",
                                fontFamily = MonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = CoralAccent
                            )
                        }
                    }
                }
            }
        }
    }
}
