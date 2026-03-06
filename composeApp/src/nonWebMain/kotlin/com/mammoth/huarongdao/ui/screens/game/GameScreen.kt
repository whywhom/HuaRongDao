package com.mammoth.huarongdao.ui.screens.game

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.mammoth.huarongdao.ui.components.GameBoard
import com.mammoth.huarongdao.utils.Strings
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    lan: String,
    levelId: Int,
    strings: Strings,
    onNavigateBack: () -> Unit,
    viewModel: GameViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showResetDialog by remember { mutableStateOf(false) }
    var showCompleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(levelId) {
        viewModel.handleIntent(GameIntent.LoadLevel(levelId))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is GameEffect.LevelCompleted -> showCompleteDialog = true
                else -> {}
            }
        }
    }

    // Level complete dialog
    if (showCompleteDialog) {
        AlertDialog(
            onDismissRequest = { showCompleteDialog = false },
            title = { Text(strings.levelCompleteTitle, fontWeight = FontWeight.Bold) },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(strings.congratulations, fontSize = 24.sp, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(8.dp))
                    Text(strings.youSolved)
                    uiState.currentState?.moveCount?.let { moves ->
                        Text(strings.movesUsed.replace("%d", "$moves"))
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    showCompleteDialog = false
                    onNavigateBack()
                }) {
                    Text(strings.ok)
                }
            }
        )
    }

    // Reset confirmation dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text(strings.reset) },
            text = { Text(strings.resetConfirm) },
            confirmButton = {
                Button(onClick = {
                    showResetDialog = false
                    viewModel.handleIntent(GameIntent.ResetLevel)
                }) { Text(strings.confirm) }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) { Text(strings.cancel) }
            }
        )
    }

    // No solution dialog
    if (uiState.noSolutionFound) {
        AlertDialog(
            onDismissRequest = { viewModel.handleIntent(GameIntent.DismissError) },
            text = { Text(strings.noSolution) },
            confirmButton = {
                Button(onClick = { viewModel.handleIntent(GameIntent.DismissError) }) {
                    Text(strings.ok)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if(lan == "zh") uiState.level?.nameZh+"" else uiState.level?.nameEn+"",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        uiState.currentState?.let { state ->
                            Text(
                                text = strings.moves.replace("%d", "${state.moveCount}"),
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = strings.back)
                    }
                },
                actions = {
                    // Reset button
                    TextButton(onClick = { showResetDialog = true }) {
                        Text(strings.reset)
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Demo mode banner
            AnimatedVisibility(visible = uiState.isDemoMode) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "▶ Demo: ${uiState.demoCurrentIndex + 1}/${uiState.demoStates.size}",
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = { viewModel.handleIntent(GameIntent.StopDemo) }) {
                            Text(strings.stopDemo, color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }

            // Game Board - size adapts to screen
            BoxWithConstraints(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                val cellSize = minOf(maxWidth / 4, maxHeight / 5)

                uiState.currentState?.let { state ->
                    GameBoard(
                        lan = lan,
                        gameState = state,
                        selectedPieceId = uiState.selectedPieceId,
                        cellSize = cellSize,
                        isDemoMode = uiState.isDemoMode,
                        onPieceClick = { pieceId ->
                            viewModel.handleIntent(GameIntent.SelectPiece(pieceId))
                        },
                        onPieceDrag = { pieceId, dc, dr ->
                            viewModel.handleIntent(GameIntent.DragPiece(pieceId, dc, dr))
                        }
                    )
                }
            }

            // Direction buttons for selected piece
            uiState.selectedPieceId?.let { pieceId ->
                DirectionPad(
                    onMove = { dc, dr ->
                        viewModel.handleIntent(GameIntent.MovePiece(dc, dr))
                    }
                )
            }

            // Demo / solve button
            Button(
                onClick = {
                    if (uiState.isDemoMode) {
                        viewModel.handleIntent(GameIntent.StopDemo)
                    } else {
                        viewModel.handleIntent(GameIntent.RequestDemo)
                    }
                },
                enabled = !uiState.isSolving && !uiState.isSolved,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (uiState.isDemoMode)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.secondary
                )
            ) {
                if (uiState.isSolving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(strings.solving)
                } else if (uiState.isDemoMode) {
                    Text(strings.stopDemo)
                } else {
                    Text(strings.demoButton)
                }
            }
        }
    }
}

@Composable
fun DirectionPad(onMove: (Int, Int) -> Unit) {
    val btnSize = 48.dp
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = { onMove(0, -1) }, modifier = Modifier.size(btnSize)) {
            Text("▲", fontSize = 20.sp)
        }
        Row {
            IconButton(onClick = { onMove(-1, 0) }, modifier = Modifier.size(btnSize)) {
                Text("◄", fontSize = 20.sp)
            }
            Spacer(Modifier.size(btnSize))
            IconButton(onClick = { onMove(1, 0) }, modifier = Modifier.size(btnSize)) {
                Text("►", fontSize = 20.sp)
            }
        }
        IconButton(onClick = { onMove(0, 1) }, modifier = Modifier.size(btnSize)) {
            Text("▼", fontSize = 20.sp)
        }
    }
}
