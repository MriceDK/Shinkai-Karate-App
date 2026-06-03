package be.mauricedeke.shinkai.ui.profiel.strength.punch

import be.mauricedeke.shinkai.domain.model.BeltColor
import be.mauricedeke.shinkai.ui.profiel.strength.MeasurementPhase

data class PunchTestUiState(
    val phase: MeasurementPhase = MeasurementPhase.IDLE,
    val score: Int = 0,
    val bestScore: Int = 0,
    val progress: Float = 0f,
    val resultBelt: BeltColor? = null,
    val bestBelt: BeltColor? = null
)
