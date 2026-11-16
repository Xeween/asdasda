package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddReaction
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.FeedLogEntity
import com.example.data.model.parseReactions
import com.example.data.model.parseTags
import com.example.ui.components.AccentDot
import com.example.ui.theme.BorderBright
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CoralAccent
import com.example.ui.theme.CoralDark
import com.example.ui.theme.CoralText
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanBg
import com.example.ui.theme.MonoFontFamily
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceElevated
import com.example.ui.theme.SurfaceInner
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VioletBright

private val defaultFilterTags = listOf("#ALL", "#DIARY", "#CODE", "#ANIME", "#IDEA", "#WORKOUT")
private val quickReactionEmojis = listOf("🔥", "❤️", "⚡", "✨", "😭", "🚀", "🎮", "💯")

@Composable
fun TelegramFeedScreen(
    logs: List<FeedLogEntity>,
    searchQuery: String,
    selectedTag: String,
    onSearchChange: (String) -> Unit,
    onTagSelect: (String) -> Unit,
    onPostLog: (String, String?) -> Unit,
    onToggleReaction: (Int, String) -> Unit,
    onDeleteLog: (Int) -> Unit,
    onCopyFeedback: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboard = LocalClipboardManager.current
    var composerText by remember { mutableStateOf("") }
    var composerTag by remember { mutableStateOf<String?>(null) }
    var showTagMenu by remember { mutableStateOf(false) }

    val filteredLogs = remember(logs, searchQuery, selectedTag) {
        logs.filter { log ->
            val matchesTag = selectedTag == "#ALL" ||
                log.parseTags().any { it.equals(selectedTag, ignoreCase = true) }
            val q = searchQuery.trim()
            val matchesSearch = q.isEmpty() ||
                log.content.contains(q, ignoreCase = true) ||
                log.tagsCsv.contains(q, ignoreCase = true) ||
                log.sourceLabel.contains(q, ignoreCase = true)
            matchesTag && matchesSearch
        }
    }

    val groupedLogs = remember(filteredLogs) {
        filteredLogs.groupBy { it.dateGroup }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBg),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 620.dp)
                .fillMaxSize()
        ) {
            // Search & Filter Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SurfaceCard,
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 11.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(modifier = Modifier.weight(1f)) {
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.search_logs_hint),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextMuted
                                )
                            }
                            BasicTextField(
                                value = searchQuery,
                                onValueChange = onSearchChange,
                                singleLine = true,
                                textStyle = TextStyle(
                                    color = TextPrimary,
                                    fontSize = 14.sp
                                ),
                                cursorBrush = SolidColor(CoralAccent),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("feed_search_input")
                            )
                        }
                        if (searchQuery.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(R.string.cd_clear_search),
                                tint = TextSecondary,
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable { onSearchChange("") }
                            )
                        }
                    }
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(defaultFilterTags) { tag ->
                        val active = selectedTag.equals(tag, ignoreCase = true)
                        Surface(
                            onClick = { onTagSelect(tag) },
                            shape = RoundedCornerShape(7.dp),
                            color = if (active) CoralAccent else SurfaceCard,
                            border = BorderStroke(
                                1.dp,
                                if (active) CoralAccent else BorderSubtle
                            ),
                            modifier = Modifier.testTag("filter_chip_${tag.removePrefix("#")}")
                        ) {
                            Text(
                                text = tag,
                                fontFamily = MonoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = if (active) CoralText else TextSecondary,
                                modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Feed List
            if (filteredLogs.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.empty_feed_title),
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        Text(
                            text = stringResource(R.string.empty_feed_subtitle),
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    groupedLogs.forEach { (dateGroup, itemsInGroup) ->
                        item(key = "header_$dateGroup") {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(99.dp),
                                    color = SurfaceElevated,
                                    border = BorderStroke(1.dp, BorderSubtle)
                                ) {
                                    Text(
                                        text = dateGroup,
                                        fontFamily = MonoFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 10.sp,
                                        letterSpacing = 1.sp,
                                        color = TextSecondary,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        items(itemsInGroup, key = { it.id }) { log ->
                            FeedLogCard(
                                log = log,
                                onCopy = {
                                    clipboard.setText(AnnotatedString(log.content))
                                    onCopyFeedback("Log copied to clipboard")
                                },
                                onToggleReaction = { emoji -> onToggleReaction(log.id, emoji) },
                                onDelete = { onDeleteLog(log.id) }
                            )
                        }
                    }
                    item { Spacer(modifier = Modifier.height(6.dp)) }
                }
            }

            // Sticky Bottom Composer Bar
            Surface(
                color = ObsidianBg,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = SurfaceCard,
                    border = BorderStroke(1.dp, BorderSubtle),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box {
                            Surface(
                                onClick = { showTagMenu = true },
                                shape = RoundedCornerShape(10.dp),
                                color = if (composerTag != null) CoralDark else SurfaceElevated,
                                border = BorderStroke(
                                    1.dp,
                                    if (composerTag != null) CoralAccent.copy(alpha = 0.6f) else BorderSubtle
                                ),
                                modifier = Modifier
                                    .height(38.dp)
                                    .testTag("composer_tag_button")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Tag,
                                        contentDescription = stringResource(R.string.cd_tag_picker),
                                        tint = if (composerTag != null) CoralAccent else CyanAccent,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    if (composerTag != null) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = composerTag!!.removePrefix("#"),
                                            fontFamily = MonoFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            color = CoralAccent
                                        )
                                    }
                                }
                            }

                            DropdownMenu(
                                expanded = showTagMenu,
                                onDismissRequest = { showTagMenu = false },
                                containerColor = SurfaceElevated
                            ) {
                                defaultFilterTags.filter { it != "#ALL" }.forEach { tag ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = tag,
                                                fontFamily = MonoFontFamily,
                                                color = TextPrimary
                                            )
                                        },
                                        onClick = {
                                            composerTag = if (composerTag == tag) null else tag
                                            showTagMenu = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(vertical = 6.dp)
                        ) {
                            if (composerText.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.composer_hint),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextMuted
                                )
                            }
                            BasicTextField(
                                value = composerText,
                                onValueChange = { composerText = it },
                                maxLines = 3,
                                textStyle = TextStyle(
                                    color = TextPrimary,
                                    fontSize = 14.sp
                                ),
                                cursorBrush = SolidColor(CoralAccent),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                                keyboardActions = KeyboardActions(
                                    onSend = {
                                        if (composerText.isNotBlank()) {
                                            onPostLog(composerText, composerTag)
                                            composerText = ""
                                            composerTag = null
                                        }
                                    }
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("feed_composer_input")
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Surface(
                            onClick = {
                                if (composerText.isNotBlank()) {
                                    onPostLog(composerText, composerTag)
                                    composerText = ""
                                    composerTag = null
                                }
                            },
                            shape = CircleShape,
                            color = CoralAccent,
                            modifier = Modifier
                                .size(38.dp)
                                .testTag("feed_send_button")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowUpward,
                                    contentDescription = stringResource(R.string.cd_send_log),
                                    tint = CoralText,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeedLogCard(
    log: FeedLogEntity,
    onCopy: () -> Unit,
    onToggleReaction: (String) -> Unit,
    onDelete: () -> Unit
) {
    var showReactionMenu by remember { mutableStateOf(false) }
    val sourceTextColor = when (log.sourceAccent.uppercase()) {
        "VIOLET" -> VioletBright
        "CYAN" -> CyanAccent
        else -> TextSecondary
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCard,
        border = BorderStroke(1.dp, BorderSubtle),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("feed_log_card_${log.id}")
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Card Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AccentDot(accentType = log.sourceAccent)
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    text = log.sourceLabel,
                    fontFamily = MonoFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 10.5.sp,
                    letterSpacing = 0.8.sp,
                    color = sourceTextColor,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = log.timestampLabel,
                    fontFamily = MonoFontFamily,
                    fontSize = 11.5.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.width(6.dp))
                IconButton(
                    onClick = onCopy,
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ContentCopy,
                        contentDescription = stringResource(R.string.cd_copy_log),
                        tint = TextSecondary,
                        modifier = Modifier.size(15.dp)
                    )
                }
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.DeleteOutline,
                        contentDescription = "Delete log",
                        tint = TextMuted,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            // Content
            Text(
                text = log.content,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.5.sp, lineHeight = 21.sp),
                color = TextPrimary
            )

            // Hashtag Pills
            val tags = log.parseTags()
            if (tags.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    tags.forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(5.dp),
                            color = CyanBg
                        ) {
                            Text(
                                text = tag,
                                fontFamily = MonoFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.sp,
                                color = CyanAccent,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            // Emoji Reactions Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                log.parseReactions().forEach { reaction ->
                    Surface(
                        onClick = { onToggleReaction(reaction.emoji) },
                        shape = RoundedCornerShape(8.dp),
                        color = if (reaction.reactedByUser) CoralDark else SurfaceInner,
                        border = BorderStroke(
                            1.dp,
                            if (reaction.reactedByUser) CoralAccent.copy(alpha = 0.6f) else BorderSubtle
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = reaction.emoji, fontSize = 13.sp)
                            Text(
                                text = reaction.count.toString(),
                                fontFamily = MonoFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.5.sp,
                                color = if (reaction.reactedByUser) CoralAccent else TextPrimary
                            )
                        }
                    }
                }

                Box {
                    Surface(
                        onClick = { showReactionMenu = true },
                        shape = RoundedCornerShape(8.dp),
                        color = SurfaceInner,
                        border = BorderStroke(1.dp, BorderSubtle)
                    ) {
                        Box(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AddReaction,
                                contentDescription = stringResource(R.string.cd_add_reaction),
                                tint = TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showReactionMenu,
                        onDismissRequest = { showReactionMenu = false },
                        containerColor = SurfaceElevated
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            quickReactionEmojis.forEach { emoji ->
                                Surface(
                                    onClick = {
                                        onToggleReaction(emoji)
                                        showReactionMenu = false
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    color = SurfaceCard,
                                    border = BorderStroke(1.dp, BorderBright)
                                ) {
                                    Text(
                                        text = emoji,
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(6.dp)
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
