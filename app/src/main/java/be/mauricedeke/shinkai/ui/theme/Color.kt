@file:Suppress("unused")

package be.mauricedeke.shinkai.ui.theme

import androidx.compose.ui.graphics.Color

val BrightRed = Color(0xFFE5383B)
val ShinkaiRed = BrightRed

val ShinkaiBackground = Color(0xFFF5F3F4)
val ShinkaiPressed = Color(0xFFD3D3D3)
val ShinkaiSelected = Color(0xFFF5F3F4)
val ShinkaiText = Color(0xFF0B090A)
val ShinkaiTextGray = Color(0xFFB1A7A6)
val ShinkaiBlack = Color(0xFF161A1D)
val ShinkaiWhite = Color(0xFFFFFFFF)
val ShinkaiRedAccent = Color(0xFFA90B0B)

val ShinkaiDark = ShinkaiBlack
val ShinkaiGray = ShinkaiBackground
val ShinkaiCardBg = ShinkaiWhite

val BeltWit = Color(0xFFFFFFFF)
val BeltWitDark = Color(0xFFE0E0E0)

val BeltYellow = Color(0xFFFFD700)
val BeltOrange = Color(0xFFFF8C00)
val BeltRed = Color(0xFFCC0000)
val BeltGreen = Color(0xFF1B5E20)
val BeltBlue = Color(0xFF1565C0)

val BeltPaars = Color(0xFF800080)

val BeltBruin1 = Color(0xFF8B4513)

val BeltBruin2 = Color(0xFF8B4513)

val BeltBruin3 = Color(0xFF8B4513)

val BeltZwart = Color(0xFF000000)
val BeltZwartDark = Color(0xFF4B4B4B)

fun be.mauricedeke.shinkai.domain.model.BeltColor.toColor(isDark: Boolean = false): Color =
    when (this) {
        be.mauricedeke.shinkai.domain.model.BeltColor.WHITE -> if (isDark) BeltWitDark else BeltWit
        be.mauricedeke.shinkai.domain.model.BeltColor.YELLOW -> BeltYellow
        be.mauricedeke.shinkai.domain.model.BeltColor.ORANGE -> BeltOrange
        be.mauricedeke.shinkai.domain.model.BeltColor.RED -> BeltRed
        be.mauricedeke.shinkai.domain.model.BeltColor.GREEN -> BeltGreen
        be.mauricedeke.shinkai.domain.model.BeltColor.BLUE -> BeltBlue
        be.mauricedeke.shinkai.domain.model.BeltColor.PURPLE -> BeltPaars
        be.mauricedeke.shinkai.domain.model.BeltColor.BROWN_I -> BeltBruin1
        be.mauricedeke.shinkai.domain.model.BeltColor.BROWN_II -> BeltBruin2
        be.mauricedeke.shinkai.domain.model.BeltColor.BROWN_III -> BeltBruin3
        be.mauricedeke.shinkai.domain.model.BeltColor.BLACK -> if (isDark) BeltZwartDark else BeltZwart
    }

fun String.toBeltColor(): be.mauricedeke.shinkai.domain.model.BeltColor {
    // Try direct enum match first (e.g. "YELLOW", "BROWN_I")
    runCatching { be.mauricedeke.shinkai.domain.model.BeltColor.valueOf(this.uppercase()) }
        .getOrNull()?.let { return it }
    return when (this.lowercase().trim()) {
        "wit", "white", "white belt" -> be.mauricedeke.shinkai.domain.model.BeltColor.WHITE
        "geel", "yellow", "yellow belt" -> be.mauricedeke.shinkai.domain.model.BeltColor.YELLOW
        "oranje", "orange", "orange belt" -> be.mauricedeke.shinkai.domain.model.BeltColor.ORANGE
        "rood", "red", "red belt" -> be.mauricedeke.shinkai.domain.model.BeltColor.RED
        "groen", "green", "green belt" -> be.mauricedeke.shinkai.domain.model.BeltColor.GREEN
        "blauw", "blue", "blue belt" -> be.mauricedeke.shinkai.domain.model.BeltColor.BLUE
        "paars", "purple", "purple belt" -> be.mauricedeke.shinkai.domain.model.BeltColor.PURPLE
        "bruin - i", "bruin i", "brown i", "brown belt i", "brown - i" -> be.mauricedeke.shinkai.domain.model.BeltColor.BROWN_I
        "bruin - ii", "bruin ii", "brown ii", "brown belt ii", "brown - ii" -> be.mauricedeke.shinkai.domain.model.BeltColor.BROWN_II
        "bruin - iii", "bruin iii", "brown iii", "brown belt iii", "brown - iii" -> be.mauricedeke.shinkai.domain.model.BeltColor.BROWN_III
        "zwart", "black", "black belt" -> be.mauricedeke.shinkai.domain.model.BeltColor.BLACK
        else -> be.mauricedeke.shinkai.domain.model.BeltColor.YELLOW
    }
}

