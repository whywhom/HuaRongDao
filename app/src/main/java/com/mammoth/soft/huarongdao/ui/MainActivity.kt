package com.mammoth.soft.huarongdao.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.mammoth.soft.huarongdao.ui.theme.ComposeHrdTheme
import com.mammoth.soft.huarongdao.data.GameLevels
import com.mammoth.soft.huarongdao.ui.game.Chess
import com.mammoth.soft.huarongdao.ui.game.opening
import com.mammoth.soft.huarongdao.ui.game.toList

class MainActivity : ComponentActivity() {

    companion object{
        var selectedItem = GameLevels.chessNameArray[0]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Hrd()
        }
    }
}