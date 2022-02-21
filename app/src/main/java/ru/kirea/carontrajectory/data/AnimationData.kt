package ru.kirea.carontrajectory.data

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan

/** Данные для создания анимации движения машинки от одной точки к другой */
data class AnimationData(

    /** Начальная координата по оси X */
    var fromX: Float = 0f,

    /** Начальная координата по оси Y */
    var fromY: Float = 0f,

    /** Конечная координата по оси X */
    var toX: Float = 0f,

    /** Конечная координата по оси Y */
    var toY: Float = 0f,

    /** Угол поворота машинки */
    var angle: Float = 0f,

    /** Машинка поворачивает налево или нет */
    var toLeft: Boolean = true
) {
    constructor(fromX_: Float, toX_: Float, fromY_: Float, toY_: Float):
            this(
                fromX = fromX_,
                fromY = fromY_,
                toX = toX_,
                toY = toY_,
                angle = atan(abs(fromX_ - toX_) / abs(fromY_ - toY_)) * 180 / PI.toFloat(),
                toLeft = toX_ < fromX_
            )
}
