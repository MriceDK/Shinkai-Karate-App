package be.mauricedeke.shinkai.domain.model

fun BeltColor.punchScore(): Int = when (this) {
    BeltColor.WHITE -> 300
    BeltColor.YELLOW -> 400
    BeltColor.ORANGE -> 500
    BeltColor.RED -> 600
    BeltColor.GREEN -> 700
    BeltColor.BLUE -> 800
    BeltColor.PURPLE -> 900
    BeltColor.BROWN_I -> 1000
    BeltColor.BROWN_II -> 1050
    BeltColor.BROWN_III -> 1100
    BeltColor.BLACK -> 1150
}

fun beltColorFromPunch(score: Int): BeltColor = when {
    score >= BeltColor.BLACK.punchScore() -> BeltColor.BLACK
    score >= BeltColor.BROWN_III.punchScore() -> BeltColor.BROWN_III
    score >= BeltColor.BROWN_II.punchScore() -> BeltColor.BROWN_II
    score >= BeltColor.BROWN_I.punchScore() -> BeltColor.BROWN_I
    score >= BeltColor.PURPLE.punchScore() -> BeltColor.PURPLE
    score >= BeltColor.BLUE.punchScore() -> BeltColor.BLUE
    score >= BeltColor.GREEN.punchScore() -> BeltColor.GREEN
    score >= BeltColor.RED.punchScore() -> BeltColor.RED
    score >= BeltColor.ORANGE.punchScore() -> BeltColor.ORANGE
    else -> BeltColor.YELLOW
}
