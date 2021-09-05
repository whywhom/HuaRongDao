package com.mammoth.soft.huarongdao.ui

import androidx.annotation.StringRes
import com.mammoth.soft.huarongdao.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Game : Screen("game", R.string.btv_game)
    object Setting : Screen("setting", R.string.btv_setting)
}

val btvItems = listOf(
    Screen.Game,
    Screen.Setting,
)