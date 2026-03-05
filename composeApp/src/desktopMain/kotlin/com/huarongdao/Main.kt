package com.huarongdao

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "华容道 - Hua Rong Dao",
        state = rememberWindowState(width = 420.dp, height = 760.dp)
    ) {
        App()
    }
}
