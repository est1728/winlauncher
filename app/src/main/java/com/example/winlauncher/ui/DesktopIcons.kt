package com.example.winlauncher.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.winlauncher.ui.theme.AppTileCalculator
import com.example.winlauncher.ui.theme.AppTileFileExplorer
import com.example.winlauncher.ui.theme.AppTileNotepad
import com.example.winlauncher.ui.theme.AppTileSettings

data class DesktopIconItem(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val tileColor: Color,
)

val DefaultDesktopIcons = listOf(
    DesktopIconItem("File Explorer", "File Explorer", Icons.Filled.Folder, AppTileFileExplorer),
    DesktopIconItem("Settings", "Settings", Icons.Filled.Settings, AppTileSettings),
    DesktopIconItem("Notepad", "Notepad", Icons.Filled.Description, AppTileNotepad),
    DesktopIconItem("Calculator", "Calculator", Icons.Filled.Calculate, AppTileCalculator),
)

@Composable
fun DesktopIconsGrid(
    icons: List<DesktopIconItem>,
    textColor: Color,
    onOpen: (DesktopIconItem) -> Unit,
) {
    Column(
        modifier = Modifier.padding(top = 16.dp, start = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        icons.forEach { item ->
            DesktopIconCell(item = item, textColor = textColor, onOpen = { onOpen(item) })
        }
    }
}

@Composable
private fun DesktopIconCell(item: DesktopIconItem, textColor: Color, onOpen: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = Modifier
            .width(76.dp)
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {},
                onDoubleClick = onOpen,
            )
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(item.tileColor),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                item.icon,
                contentDescription = item.label,
                tint = Color.White,
                modifier = Modifier.size(26.dp),
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = item.label,
            style = TextStyle(
                color = textColor,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                shadow = Shadow(color = Color.Black.copy(alpha = 0.6f), blurRadius = 4f),
            ),
            maxLines = 2,
        )
    }
}
