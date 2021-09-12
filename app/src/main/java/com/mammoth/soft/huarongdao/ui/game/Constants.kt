package com.mammoth.soft.huarongdao.ui.game

import androidx.compose.ui.unit.IntOffset
import com.mammoth.soft.huarongdao.R

val zhang = Chess("张飞", R.drawable.zhangfei, 1, 2)
val cao = Chess("曹操", R.drawable.caocao, 2, 2)
val huang = Chess("黄忠", R.drawable.huangzhong, 1, 2)
val zhao = Chess("赵云", R.drawable.zhaoyun, 1, 2)
val ma = Chess("马超", R.drawable.machao, 1, 2)
val guan = Chess("关羽", R.drawable.guanyu, 2, 1)


@OptIn(ExperimentalStdlibApi::class)
val zu = buildList {
    repeat(4) { add(Chess("卒$it", R.drawable.zu,1, 1)) }
}

typealias ChessOpening = List<Triple<Chess, Int, Int>>

@OptIn(ExperimentalStdlibApi::class)
val opening: ChessOpening = buildList {
    add(Triple(zhang, 0, 0))
    add(Triple(cao, 1, 0))
    add(Triple(zhao, 3, 0))
    add(Triple(huang, 0, 2))
    add(Triple(ma, 3, 2))
    add(Triple(guan, 1, 2))
    add(Triple(zu[0], 0, 4))
    add(Triple(zu[1], 1, 3))
    add(Triple(zu[2], 2, 3))
    add(Triple(zu[3], 3, 4))
}


fun ChessOpening.toList() =
    map { (chess, x, y) ->
        chess.moveBy(IntOffset(x * boardGridPx, y * boardGridPx))
    }
