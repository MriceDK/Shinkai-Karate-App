package be.mauricedeke.shinkai.domain.model

fun BeltColor.kiaiDb(): Int = when (this) {
    BeltColor.YELLOW    -> 85
    BeltColor.ORANGE    -> 88
    BeltColor.RED       -> 90
    BeltColor.GREEN     -> 92
    BeltColor.BLUE      -> 94
    BeltColor.PURPLE    -> 96
    BeltColor.BROWN_I   -> 98
    BeltColor.BROWN_II  -> 100
    BeltColor.BROWN_III -> 103
    BeltColor.BLACK     -> 106
}

fun BeltColor.displayName(): String = when (this) {
    BeltColor.YELLOW    -> "Yellow belt"
    BeltColor.ORANGE    -> "Orange belt"
    BeltColor.RED       -> "Red belt"
    BeltColor.GREEN     -> "Green belt"
    BeltColor.BLUE      -> "Blue belt"
    BeltColor.PURPLE    -> "Purple belt"
    BeltColor.BROWN_I   -> "Brown belt I"
    BeltColor.BROWN_II  -> "Brown belt II"
    BeltColor.BROWN_III -> "Brown belt III"
    BeltColor.BLACK     -> "Black belt"
}

fun beltColorFromDb(db: Int): BeltColor = when {
    db >= BeltColor.BLACK.kiaiDb()     -> BeltColor.BLACK
    db >= BeltColor.BROWN_III.kiaiDb() -> BeltColor.BROWN_III
    db >= BeltColor.BROWN_II.kiaiDb()  -> BeltColor.BROWN_II
    db >= BeltColor.BROWN_I.kiaiDb()   -> BeltColor.BROWN_I
    db >= BeltColor.PURPLE.kiaiDb()    -> BeltColor.PURPLE
    db >= BeltColor.BLUE.kiaiDb()      -> BeltColor.BLUE
    db >= BeltColor.GREEN.kiaiDb()     -> BeltColor.GREEN
    db >= BeltColor.RED.kiaiDb()       -> BeltColor.RED
    db >= BeltColor.ORANGE.kiaiDb()    -> BeltColor.ORANGE
    else                               -> BeltColor.YELLOW
}
