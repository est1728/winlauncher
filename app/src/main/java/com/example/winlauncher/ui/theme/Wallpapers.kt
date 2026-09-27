package com.example.winlauncher.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * วอลเปเปอร์หนึ่งแบบ = จุดสี (blob) หลายจุดที่จะเอาไปวาดเป็นภาพไล่สีนุ่มๆ แบบ abstract
 * (สไตล์คล้าย Windows 11 bloom แต่เป็นชุดสี/รูปทรงของเราเอง ไม่ใช่ภาพต้นฉบับ)
 */
data class WallpaperOption(
    val id: String,
    val name: String,
    val isDark: Boolean,
    val baseColor: Color,
    val blobColors: List<Color>,
)

val WallpaperPresets = listOf(
    WallpaperOption(
        id = "azure_bloom",
        name = "Azure Bloom",
        isDark = false,
        baseColor = Color(0xFFE8F1FB),
        blobColors = listOf(Color(0xFF3A8DFF), Color(0xFF0F4FA8), Color(0xFF7FC4FF)),
    ),
    WallpaperOption(
        id = "midnight_bloom",
        name = "Midnight Bloom",
        isDark = true,
        baseColor = Color(0xFF0B1220),
        blobColors = listOf(Color(0xFF1E3A5F), Color(0xFF3A8DFF), Color(0xFF122A4A)),
    ),
    WallpaperOption(
        id = "sunset_glass",
        name = "Sunset Glass",
        isDark = false,
        baseColor = Color(0xFFFFF1E6),
        blobColors = listOf(Color(0xFFFF8A5B), Color(0xFFFFC15E), Color(0xFFEF5DA8)),
    ),
    WallpaperOption(
        id = "ember_dark",
        name = "Ember Dark",
        isDark = true,
        baseColor = Color(0xFF1A1010),
        blobColors = listOf(Color(0xFF7A2E2E), Color(0xFFEF5DA8), Color(0xFF4A1E1E)),
    ),
    WallpaperOption(
        id = "forest_mist",
        name = "Forest Mist",
        isDark = false,
        baseColor = Color(0xFFEAF5EC),
        blobColors = listOf(Color(0xFF2E9E6B), Color(0xFF0F6B47), Color(0xFF8FE0B5)),
    ),
    WallpaperOption(
        id = "graphite",
        name = "Graphite",
        isDark = true,
        baseColor = Color(0xFF17181A),
        blobColors = listOf(Color(0xFF3A3D42), Color(0xFF5B5F66), Color(0xFF232427)),
    ),
)
