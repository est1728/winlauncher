package com.example.winlauncher.ui

import android.os.Environment
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FileExplorerApp(textColor: Color) {
    val rootDir = remember { Environment.getExternalStorageDirectory() }
    var currentDir by remember { mutableStateOf(rootDir) }

    val files = remember(currentDir) {
        currentDir.listFiles()
            ?.sortedWith(compareBy({ !it.isDirectory }, { it.name.lowercase() }))
            ?: emptyList()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (currentDir.parentFile != null) {
                Icon(
                    Icons.Filled.ArrowUpward,
                    contentDescription = "ย้อนกลับ",
                    tint = textColor,
                    modifier = Modifier
                        .clickable { currentDir.parentFile?.let { currentDir = it } }
                        .padding(end = 8.dp)
                )
            }
            Text(text = currentDir.absolutePath, color = textColor, maxLines = 1)
        }

        if (files.isEmpty()) {
            Text(
                text = "โฟลเดอร์นี้ว่างเปล่า หรือแอปยังไม่ได้รับสิทธิ์เข้าถึงไฟล์",
                color = textColor,
                modifier = Modifier.padding(16.dp),
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(files) { file ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { if (file.isDirectory) currentDir = file }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            if (file.isDirectory) Icons.Filled.Folder else Icons.Filled.InsertDriveFile,
                            contentDescription = null,
                            tint = if (file.isDirectory) Color(0xFFFFC107) else textColor,
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = file.name, color = textColor)
                    }
                }
            }
        }
    }
}
