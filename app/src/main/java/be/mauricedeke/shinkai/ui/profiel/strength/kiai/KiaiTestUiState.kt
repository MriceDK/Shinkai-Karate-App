package be.mauricedeke.shinkai.ui.profiel.strength.kiai

import be.mauricedeke.shinkai.domain.model.BeltColor
import be.mauricedeke.shinkai.ui.profiel.strength.MeasurementPhase

data class KiaiTestUiState(
    val phase: MeasurementPhase = MeasurementPhase.IDLE,
    val currentDb: Int = 0,
    val peakDb: Int = 0,
    val bestDb: Int = 0,
    val progress: Float = 0f,
    val resultBelt: BeltColor? = null,
    val bestBelt: BeltColor? = null
)
