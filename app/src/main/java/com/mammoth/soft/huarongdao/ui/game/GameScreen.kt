package com.mammoth.soft.huarongdao.ui.game

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mammoth.soft.huarongdao.R
import com.mammoth.soft.huarongdao.ui.MainActivity.Companion.selectedItem
import com.mammoth.soft.huarongdao.ui.TopBar



@Composable
fun GameScreen(gameViewModel: GameViewModel, openDrawer: () -> Unit) {

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(
            title = stringResource(id = R.string.btv_game),
            buttonIcon = Icons.Filled.Menu,
            onButtonClicked = { openDrawer() }
        )
        Column {
            Spacer(Modifier.height(20.dp))

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "华容道 -" + stringResource(id = selectedItem),
                style = MaterialTheme.typography.h5
            )

            var chessState: List<Chess> by remember {
                mutableStateOf(opening.toList())
            }

            with(LocalDensity.current) {
                ChessBoard(
                    Modifier.weight(1f),
                    chessList = chessState,
                ) { cur, x, y ->
                    chessState = chessState.map {
                        if (it.name == cur) {
                            if (x != 0) it.checkAndMoveX(x, chessState)
                            else it.checkAndMoveY(y, chessState)
                        } else {
                            it
                        }
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}