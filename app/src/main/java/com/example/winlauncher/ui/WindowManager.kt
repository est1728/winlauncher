package com.example.winlauncher.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * ข้อมูลของหน้าต่างหนึ่งบาน (คล้าย HWND ของ Windows)
 * เก็บตำแหน่ง, ขนาด, สถานะ (ย่อ/ขยาย/ปกติ), และลำดับการซ้อน (z-index)
 */
class WindowState(
    val id: String,
    title: String,
    x: Dp = 40.dp,
    y: Dp = 40.dp,
    width: Dp = 360.dp,
    height: Dp = 480.dp,
    val minWidth: Dp = 220.dp,
    val minHeight: Dp = 160.dp,
) {
    var title by mutableStateOf(title)
    var offsetX by mutableStateOf(x)
    var offsetY by mutableStateOf(y)
    var width by mutableStateOf(width)
    var height by mutableStateOf(height)

    // จำค่าตำแหน่ง/ขนาดก่อน maximize ไว้ ใช้ตอนกด restore
    var preMaximizeX by mutableStateOf(x)
    var preMaximizeY by mutableStateOf(y)
    var preMaximizeWidth by mutableStateOf(width)
    var preMaximizeHeight by mutableStateOf(height)

    var isMinimized by mutableStateOf(false)
    var isMaximized by mutableStateOf(false)
    var zIndex by mutableStateOf(0f)
}

/**
 * ตัวจัดการหน้าต่างทั้งหมดบนเดสก์ท็อป — คล้าย Explorer.exe ของ Windows
 * มีหน้าที่: เปิด/ปิด/ย่อ/ขยายหน้าต่าง และจัดลำดับว่าอันไหนอยู่บนสุด (focus)
 */
class DesktopWindowManager {
    val windows = mutableStateListOf<WindowState>()
    private var topZ = 0f

    fun openWindow(
        id: String,
        title: String,
        width: Dp = 360.dp,
        height: Dp = 480.dp,
    ): WindowState {
        // ถ้าหน้าต่างนี้เปิดอยู่แล้ว (ถูกย่อไว้) ให้ดึงขึ้นมาแทนที่จะเปิดซ้ำ
        windows.find { it.id == id }?.let {
            it.isMinimized = false
            bringToFront(it)
            return it
        }
        val state = WindowState(id = id, title = title, width = width, height = height)
        bringToFront(state)
        windows.add(state)
        return state
    }

    fun closeWindow(state: WindowState) {
        windows.remove(state)
    }

    fun minimize(state: WindowState) {
        state.isMinimized = true
    }

    fun toggleMaximize(state: WindowState, desktopWidth: Dp, desktopHeight: Dp) {
        if (state.isMaximized) {
            // restore กลับไปตำแหน่ง/ขนาดเดิมก่อน maximize
            state.offsetX = state.preMaximizeX
            state.offsetY = state.preMaximizeY
            state.width = state.preMaximizeWidth
            state.height = state.preMaximizeHeight
            state.isMaximized = false
        } else {
            state.preMaximizeX = state.offsetX
            state.preMaximizeY = state.offsetY
            state.preMaximizeWidth = state.width
            state.preMaximizeHeight = state.height
            state.offsetX = 0.dp
            state.offsetY = 0.dp
            state.width = desktopWidth
            state.height = desktopHeight
            state.isMaximized = true
        }
    }

    fun bringToFront(state: WindowState) {
        topZ += 1f
        state.zIndex = topZ
    }
}

/** แปลง px จาก drag gesture (Offset) เป็นระยะ dp ให้บวก/ลบกับตำแหน่งหน้าต่างได้ */
data class DragDelta(val dx: Float, val dy: Float) {
    companion object {
        fun from(offset: Offset) = DragDelta(offset.x, offset.y)
    }
}
