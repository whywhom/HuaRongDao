package com.huarongdao.domain.model

/**
 * Classic Hua Rong Dao / Klotski levels
 * Board: 4 cols x 5 rows
 * Piece IDs:
 *   0 = 曹操 CaoCao (2x2)
 *   1 = 关羽 GuanYu (2x1 horizontal)
 *   2 = 张飞 ZhangFei (1x2 vertical)
 *   3 = 赵云 ZhaoYun (1x2 vertical)
 *   4 = 黄忠 HuangZhong (1x2 vertical)
 *   5 = 马超 MaChao (1x2 vertical)
 *   6-9 = 卒 Soldiers (1x1)
 */
object LevelData {

    val levels: List<Level> = listOf(
        // Level 1 - 横刀立马 (Classic layout)
        Level(
            id = 1,
            nameZh = "横刀立马",
            nameEn = "Sword Standing",
            difficulty = 1,
            initialState = GameState(
                pieces = listOf(
                    Piece(0, PieceType.CAO_CAO,      col=1, row=0),  // 曹操
                    Piece(1, PieceType.GUAN_YU,      col=1, row=2),  // 关羽
                    Piece(2, PieceType.ZHANG_FEI,    col=0, row=0),  // 张飞
                    Piece(3, PieceType.ZHAO_YUN,     col=3, row=0),  // 赵云
                    Piece(4, PieceType.HUANG_ZHONG,  col=0, row=2),  // 黄忠
                    Piece(5, PieceType.MA_CHAO,      col=3, row=2),  // 马超
                    Piece(6, PieceType.SOLDIER,      col=0, row=4),  // 卒
                    Piece(7, PieceType.SOLDIER,      col=1, row=3),  // 卒
                    Piece(8, PieceType.SOLDIER,      col=2, row=3),  // 卒
                    Piece(9, PieceType.SOLDIER,      col=3, row=4),  // 卒
                )
            )
        ),
        // Level 2 - 指挥若定
        Level(
            id = 2,
            nameZh = "指挥若定",
            nameEn = "Strategic Command",
            difficulty = 2,
            initialState = GameState(
                pieces = listOf(
                    Piece(0, PieceType.CAO_CAO,      col=1, row=0),
                    Piece(1, PieceType.GUAN_YU,      col=1, row=2),
                    Piece(2, PieceType.ZHANG_FEI,    col=0, row=0),
                    Piece(3, PieceType.ZHAO_YUN,     col=3, row=0),
                    Piece(4, PieceType.HUANG_ZHONG,  col=0, row=3),
                    Piece(5, PieceType.MA_CHAO,      col=3, row=3),
                    Piece(6, PieceType.SOLDIER,      col=1, row=3),
                    Piece(7, PieceType.SOLDIER,      col=2, row=3),
                    Piece(8, PieceType.SOLDIER,      col=0, row=2),
                    Piece(9, PieceType.SOLDIER,      col=3, row=2),
                )
            )
        ),
        // Level 3 - 近在咫尺
        Level(
            id = 3,
            nameZh = "近在咫尺",
            nameEn = "So Close Yet So Far",
            difficulty = 2,
            initialState = GameState(
                pieces = listOf(
                    Piece(0, PieceType.CAO_CAO,      col=1, row=0),
                    Piece(1, PieceType.GUAN_YU,      col=1, row=2),
                    Piece(2, PieceType.ZHANG_FEI,    col=0, row=0),
                    Piece(3, PieceType.ZHAO_YUN,     col=3, row=0),
                    Piece(4, PieceType.HUANG_ZHONG,  col=0, row=2),
                    Piece(5, PieceType.MA_CHAO,      col=3, row=2),
                    Piece(6, PieceType.SOLDIER,      col=0, row=4),
                    Piece(7, PieceType.SOLDIER,      col=3, row=4),
                    Piece(8, PieceType.SOLDIER,      col=1, row=4),
                    Piece(9, PieceType.SOLDIER,      col=2, row=4),
                )
            )
        ),
        // Level 4 - 过五关
        Level(
            id = 4,
            nameZh = "过五关",
            nameEn = "Five Passes",
            difficulty = 3,
            initialState = GameState(
                pieces = listOf(
                    Piece(0, PieceType.CAO_CAO,      col=1, row=0),
                    Piece(1, PieceType.GUAN_YU,      col=0, row=4),
                    Piece(2, PieceType.ZHANG_FEI,    col=0, row=0),
                    Piece(3, PieceType.ZHAO_YUN,     col=3, row=0),
                    Piece(4, PieceType.HUANG_ZHONG,  col=0, row=2),  // moved
                    Piece(5, PieceType.MA_CHAO,      col=3, row=2),
                    Piece(6, PieceType.SOLDIER,      col=1, row=2),
                    Piece(7, PieceType.SOLDIER,      col=2, row=2),
                    Piece(8, PieceType.SOLDIER,      col=2, row=3),
                    Piece(9, PieceType.SOLDIER,      col=3, row=4),
                )
            )
        ),
        // Level 5 - 兵分三路
        Level(
            id = 5,
            nameZh = "兵分三路",
            nameEn = "Three Prong Attack",
            difficulty = 3,
            initialState = GameState(
                pieces = listOf(
                    Piece(0, PieceType.CAO_CAO,      col=1, row=0),
                    Piece(1, PieceType.GUAN_YU,      col=1, row=3),
                    Piece(2, PieceType.ZHANG_FEI,    col=0, row=0),
                    Piece(3, PieceType.ZHAO_YUN,     col=3, row=0),
                    Piece(4, PieceType.HUANG_ZHONG,  col=0, row=2),
                    Piece(5, PieceType.MA_CHAO,      col=3, row=2),
                    Piece(6, PieceType.SOLDIER,      col=1, row=2),
                    Piece(7, PieceType.SOLDIER,      col=2, row=2),
                    Piece(8, PieceType.SOLDIER,      col=0, row=4),
                    Piece(9, PieceType.SOLDIER,      col=3, row=4),
                )
            )
        ),
        // Level 6 - 围而不歼
        Level(
            id = 6,
            nameZh = "围而不歼",
            nameEn = "Surrounded",
            difficulty = 4,
            initialState = GameState(
                pieces = listOf(
                    Piece(0, PieceType.CAO_CAO,      col=1, row=0),
                    Piece(1, PieceType.GUAN_YU,      col=0, row=2),
                    Piece(2, PieceType.ZHANG_FEI,    col=0, row=0),
                    Piece(3, PieceType.ZHAO_YUN,     col=3, row=0),
                    Piece(4, PieceType.HUANG_ZHONG,  col=3, row=2),
                    Piece(5, PieceType.MA_CHAO,      col=1, row=3),
                    Piece(6, PieceType.SOLDIER,      col=2, row=3),
                    Piece(7, PieceType.SOLDIER,      col=2, row=2),
                    Piece(8, PieceType.SOLDIER,      col=0, row=4),
                    Piece(9, PieceType.SOLDIER,      col=3, row=4),
                )
            )
        ),
        // Level 7 - 捷足先登
        Level(
            id = 7,
            nameZh = "捷足先登",
            nameEn = "First to Arrive",
            difficulty = 4,
            initialState = GameState(
                pieces = listOf(
                    Piece(0, PieceType.CAO_CAO,      col=1, row=0),
                    Piece(1, PieceType.GUAN_YU,      col=1, row=2),
                    Piece(2, PieceType.ZHANG_FEI,    col=0, row=0),
                    Piece(3, PieceType.ZHAO_YUN,     col=3, row=1),
                    Piece(4, PieceType.HUANG_ZHONG,  col=0, row=3),
                    Piece(5, PieceType.MA_CHAO,      col=3, row=3),
                    Piece(6, PieceType.SOLDIER,      col=3, row=0),
                    Piece(7, PieceType.SOLDIER,      col=0, row=2),
                    Piece(8, PieceType.SOLDIER,      col=1, row=4),
                    Piece(9, PieceType.SOLDIER,      col=2, row=4),
                )
            )
        ),
        // Level 8 - 四路进兵 (Hard)
        Level(
            id = 8,
            nameZh = "四路进兵",
            nameEn = "Four Armies",
            difficulty = 5,
            initialState = GameState(
                pieces = listOf(
                    Piece(0, PieceType.CAO_CAO,      col=1, row=0),
                    Piece(1, PieceType.GUAN_YU,      col=1, row=4),
                    Piece(2, PieceType.ZHANG_FEI,    col=0, row=0),
                    Piece(3, PieceType.ZHAO_YUN,     col=3, row=0),
                    Piece(4, PieceType.HUANG_ZHONG,  col=0, row=2),
                    Piece(5, PieceType.MA_CHAO,      col=3, row=2),
                    Piece(6, PieceType.SOLDIER,      col=1, row=2),
                    Piece(7, PieceType.SOLDIER,      col=2, row=2),
                    Piece(8, PieceType.SOLDIER,      col=2, row=3),
                    Piece(9, PieceType.SOLDIER,      col=3, row=4),
                )
            )
        ),
    )
}
