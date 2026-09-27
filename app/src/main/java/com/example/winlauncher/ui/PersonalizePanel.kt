package com.example.winlauncher.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.winlauncher.ui.theme.WallpaperOption
import com.example.winlauncher.ui.theme.WallpaperPresets

/**
 * หน้าต่าง "Personalize" — คล้ายเมนูคลิกขวาเลือก Personalize บนเดสก์ท็อป Windows
 * เลือกวอลเปเปอร์สำเร็จรูป + สลับโหมดมืด/สว่างของทั้ง taskbar และหน้าต่าง
 */
@Composable
fun PersonalizePanel(
    currentWallpaper: WallpaperOption,
    isDarkMode: Boolean,
    onWallpaperSelected: (WallpaperOption) -> Unit,
    onDarkModeChanged: (Boolean) -> Unit,
    onClose: () -> Unit,
) {
    Box(
        modifier = Modifier
            .width(340.dp)
            .heightIn(max = 480.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Personalize", style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = onClose) {
                    Icon(Icons.Filled.Close, contentDescription = "ปิด")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ---------- สลับธีมมืด/สว่าง ----------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("โหมดมืด (Dark mode)")
                Switch(checked = isDarkMode, onCheckedChange = onDarkModeChanged)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text("เลือกพื้นหลัง", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(8.dp))

            // ---------- กริดเลือกวอลเปเปอร์ ----------
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.heightIn(max = 260.dp),
            ) {
                items(WallpaperPresets) { option ->
                    WallpaperThumbnail(
                        option = option,
                        selected = option.id == currentWallpaper.id,
                        onClick = { onWallpaperSelected(option) },
                    )
                }
            }
        }
    }
}

@Composable
private fun WallpaperThumbnail(option: WallpaperOption, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(16f / 10f)
            .clip(RoundedCornerShape(6.dp))
            .border(
                width = if (selected) 3.dp else 1.dp,
                color = if (selected) MaterialTheme.colorScheme.primary else Color(0x33000000),
                shape = RoundedCornerShape(6.dp),
            )
            .clickable { onClick() }
    ) {
        WallpaperBackground(option = option, modifier = Modifier.fillMaxSize())
        if (selected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White)
            ) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = "เลือกอยู่",
                    tint = Color(0xFF0078D4),
                    modifier = Modifier.padding(2.dp).size(14.dp),
                )
            }
        }
    }
}
