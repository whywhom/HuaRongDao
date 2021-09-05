package com.mammoth.soft.huarongdao.ui

import androidx.compose.material.DrawerState
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mammoth.soft.huarongdao.ui.game.Chess
import com.mammoth.soft.huarongdao.ui.game.GameScreen
import com.mammoth.soft.huarongdao.ui.game.GameViewModel
import com.mammoth.soft.huarongdao.ui.setting.SettingScreen
import com.mammoth.soft.huarongdao.ui.setting.SettingViewModel
import kotlinx.coroutines.launch

@Composable
fun HrdNavGraph(
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    startDestination: String = Screen.Game.route,
    drawerState: DrawerState,
) {
    val coroutineScope = rememberCoroutineScope()
    val openDrawer: () -> Unit = { coroutineScope.launch { scaffoldState.drawerState.open() } }

    NavHost(navController, startDestination = startDestination) {
        composable(Screen.Game.route) {
            val gameViewModel: GameViewModel = viewModel(
                factory = GameViewModel.provideFactory()
            )
            GameScreen(
                gameViewModel = gameViewModel,
                openDrawer = openDrawer,
            )
        }
        composable(Screen.Setting.route) {
            val settingViewModel: SettingViewModel = viewModel(
                factory = SettingViewModel.provideFactory()
            )
            SettingScreen(
                settingViewModel = settingViewModel,
                openDrawer = openDrawer
            )
        }
    }
}

