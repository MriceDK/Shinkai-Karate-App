package be.mauricedeke.shinkai.data.remote.mapper

import be.mauricedeke.shinkai.domain.model.BeltColor

fun String?.toBeltColor(): BeltColor? = when (this?.uppercase()) {
    "WHITE" -> BeltColor.WHITE
    "YELLOW" -> BeltColor.YELLOW
    "ORANGE" -> BeltColor.ORANGE
    "RED" -> BeltColor.RED
    "GREEN" -> BeltColor.GREEN
    "BLUE" -> BeltColor.BLUE
    "PURPLE" -> BeltColor.PURPLE
    "BROWN_I", "BROWN_1", "BROWN1" -> BeltColor.BROWN_I
    "BROWN_II", "BROWN_2", "BROWN2" -> BeltColor.BROWN_II
    "BROWN_III", "BROWN_3", "BROWN3" -> BeltColor.BROWN_III
    "BLACK" -> BeltColor.BLACK
    else -> null
}
