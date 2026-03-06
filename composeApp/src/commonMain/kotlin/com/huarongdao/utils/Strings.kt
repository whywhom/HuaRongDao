package com.huarongdao.utils

data class Strings(
    // Common
    val appName: String,
    val ok: String,
    val cancel: String,
    val confirm: String,
    val back: String,
    val reset: String,
    val close: String,

    // Splash
    val splashTagline: String,
    val splashStart: String,

    // Nav
    val navHome: String,
    val navLevels: String,
    val navHelp: String,
    val navSettings: String,

    // Level Select
    val levelSelectTitle: String,
    val levelLocked: String,
    val levelCompleted: String,
    val bestMoves: String,
    val difficulty: String,
    val continueGame: String,
    val newGame: String,


    // Game
    val moves: String,
    val demoButton: String,
    val stopDemo: String,
    val solving: String,
    val noSolution: String,
    val congratulations: String,
    val youSolved: String,
    val movesUsed: String,
    val resetConfirm: String,
    val levelCompleteTitle: String,

    // Help
    val helpTitle: String,
    val helpIntro: String,
    val helpGoal: String,
    val helpGoalDesc: String,
    val helpMove: String,
    val helpMoveDesc: String,
    val helpTip: String,
    val helpTipDesc: String,
    val helpPieces: String,

    // Settings
    val settingsTitle: String,
    val darkMode: String,
    val language: String,
    val langSystem: String,
    val langChinese: String,
    val langEnglish: String,
    val sound: String,
    val vibration: String,
)

val EnglishStrings = Strings(
    appName = "Hua Rong Dao",
    ok = "OK",
    cancel = "Cancel",
    confirm = "Confirm",
    back = "Back",
    reset = "Reset",
    close = "Close",

    splashTagline = "The Classic Chinese Sliding Puzzle",
    splashStart = "Start Game",

    navHome = "Home",
    navLevels = "Levels",
    navHelp = "Help",
    navSettings = "Settings",

    levelSelectTitle = "Select Level",
    levelLocked = "Locked",
    levelCompleted = "Completed ✓",
    bestMoves = "Best: %d moves",
    difficulty = "Difficulty",
    continueGame = "Continue",
    newGame = "New Game",

    moves = "Moves: %d",
    demoButton = "Show Solution",
    stopDemo = "Stop Demo",
    solving = "Finding solution...",
    noSolution = "No solution found from this position",
    congratulations = "🎉 Congratulations!",
    youSolved = "You solved the puzzle!",
    movesUsed = "Moves used: %d",
    resetConfirm = "Reset this level? Your progress will be lost.",
    levelCompleteTitle = "Level Complete!",

    helpTitle = "How to Play",
    helpIntro = "Hua Rong Dao (华容道) is a classic Chinese sliding puzzle based on the Three Kingdoms legend.",
    helpGoal = "Goal",
    helpGoalDesc = "Move Cao Cao (曹操, the golden 2×2 piece) to the exit at the bottom center of the board.",
    helpMove = "Moving Pieces",
    helpMoveDesc = "Tap a piece to select it, then tap an adjacent empty cell, or drag pieces directly. Pieces can only move to empty spaces.",
    helpTip = "Tip",
    helpTipDesc = "Stuck? Use the 'Show Solution' button to watch an auto-solved demonstration. The solver uses BFS (Breadth-First Search) to find the shortest solution.",
    helpPieces = "Meet the Heroes",

    settingsTitle = "Settings",
    darkMode = "Dark Mode",
    language = "Language",
    langSystem = "System Default",
    langChinese = "中文",
    langEnglish = "English",
    sound = "Sound Effects",
    vibration = "Vibration"
)

val ChineseStrings = Strings(
    appName = "华容道",
    ok = "确定",
    cancel = "取消",
    confirm = "确认",
    back = "返回",
    reset = "重置",
    close = "关闭",

    splashTagline = "经典三国益智游戏",
    splashStart = "开始游戏",

    navHome = "主页",
    navLevels = "关卡",
    navHelp = "帮助",
    navSettings = "设置",

    levelSelectTitle = "选择关卡",
    levelLocked = "未解锁",
    levelCompleted = "已通关 ✓",
    bestMoves = "最佳: %d 步",
    difficulty = "难度",
    continueGame = "继续游戏",
    newGame = "重新开始",

    moves = "步数: %d",
    demoButton = "演示通关",
    stopDemo = "停止演示",
    solving = "正在求解...",
    noSolution = "当前局面无法求解",
    congratulations = "🎉 恭喜过关！",
    youSolved = "你成功通关了！",
    movesUsed = "用了 %d 步",
    resetConfirm = "确认重置本关？当前进度将丢失。",
    levelCompleteTitle = "通关成功！",

    helpTitle = "游戏说明",
    helpIntro = "华容道是一款以三国故事为背景的经典益智游戏。",
    helpGoal = "游戏目标",
    helpGoalDesc = "将曹操（金色2×2棋子）移动到棋盘底部中央的出口处即可过关。",
    helpMove = "移动方法",
    helpMoveDesc = "点击棋子选中后再点击相邻空格移动，也可以直接拖拽棋子。棋子只能移动到空格处。",
    helpTip = "小提示",
    helpTipDesc = "遇到困难？点击\"演示通关\"按钮观看自动解题过程。求解器使用BFS（广度优先搜索）算法找到最优解。",
    helpPieces = "认识英雄",

    settingsTitle = "设置",
    darkMode = "夜间模式",
    language = "语言",
    langSystem = "跟随系统",
    langChinese = "中文",
    langEnglish = "English",
    sound = "音效",
    vibration = "震动"
)
