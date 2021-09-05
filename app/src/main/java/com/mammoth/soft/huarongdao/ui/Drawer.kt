package com.mammoth.soft.huarongdao.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mammoth.soft.huarongdao.R
import com.mammoth.soft.huarongdao.data.GameLevels.chessNameArray
import com.mammoth.soft.huarongdao.ui.MainActivity.Companion.selectedItem
import com.mammoth.soft.huarongdao.ui.theme.ComposeHrdTheme
import kotlinx.coroutines.launch

private val screens = chessNameArray

@Composable
fun Drawer(
    navController: NavHostController,
    currentRoute: String,
    closeDrawer: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(200.dp)
            .padding(start = 20.dp, top = 20.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = "App icon"
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            screens.forEach { screen ->
                Spacer(
                    Modifier
                        .height(20.dp)
                )
                Text(
                    text = stringResource(screen),
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .fillMaxSize()
                        .selectable(
                            selected = screen == selectedItem,
                            onClick = {
                                selectedItem = if (selectedItem != screen)
                                    screen else -1
                                navController.navigate(Screen.Game.route)
                                closeDrawer
                            })
                )
            }
        }
    }
}