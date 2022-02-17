package ru.kirea.carontrajectory.windows.cartrajectory

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import ru.kirea.carontrajectory.data.AnimationData
import kotlin.random.Random

/** Презентер окна с движением машинки */
class CarTrajectoryPresenter {

    companion object {
        //диапазон для рандома точек перемещения машины
        private const val RANGE_RANDOM_Y = 100
        private const val RANGE_RANDOM_X = 300

        const val ANIMATION_ANGLE_DURATION = 100L //время анимации на поворот машинки
        const val ANIMATION_TRANSLATION_DURATION = 300L //время анимации на движение машинки
        const val ANIMATION_ALL_DURATION = 400L //общее время анимации от одной точки к другой
    }

    private var width: Int = 0
    private var height: Int = 0

    private var carWidth: Int = 0
    private var carHeight: Int = 0

    //верхний левый край машинки относительно всей области
    private var topPosition: Int = 0
    private var leftPosition: Int = 0

    private var points: MutableList<Pair<Int, Int>> = mutableListOf()
    private var animatorSetList: AnimatorSet? = null
    var carTrajectoryView: CarTrajectoryView? = null

    /**
     * Задать размеры области, по которой машинка может ездить.
     * @param width ширина
     * @param height высота
     */
    fun setRegionSize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    /**
     * Задать размеры машинки.
     * @param carWidth ширина
     * @param carHeight высота
     */
    fun setCarSize(carWidth: Int, carHeight: Int) {
        this.carWidth = carWidth
        this.carHeight = carHeight
    }

    /**
     * Сбросить положение машинки.
     * @param view машинки.
     */
    fun resetCarPosition(view: View) {
        //изначально машинку ставим в низ экрана
        topPosition = height - carHeight
        leftPosition = Random.nextInt(width - carWidth)
        resetAnimationPosition(view)
        carTrajectoryView?.setCarPosition(leftPosition, topPosition)
    }

    /**
     * Начать процесс движения.
     * @param view машинки.
     */
    fun startMovement(view: View) {
        generatePoints()
        val animationDataList = getAnimationData()
        animatorSetList = getAnimatorSetWithAngle(view, animationDataList)
        animatorSetList?.start()
    }

    /**
     * Сформировать набор анимаций для движения машинки. У анимации смещения есть один нюанс:
     * смещение делается от текущего положение, которое имеет координаты 0 х 0.
     * А все последующие смещение делать нужно относительно этих нулей и без разницы где стоит машинка -
     * слева, по центру или справа. Всегда начало анимации будет с 0 х 0.
     * @param view машинка.
     * @param animationDataList данные для анимации от точки к точке. Для анимации нужно использовать разницу
     * между начальной и конечной точкой.
     * @return [AnimatorSet], содержащий анимацию с поворотами и движением машинки от точки к точке.
     */
    private fun getAnimatorSetWithAngle(view: View, animationDataList: List<AnimationData>): AnimatorSet {
        val animatorSetResult = AnimatorSet()

        var oldX = 0f
        var oldY = 0f
        var oldAngle = 0f

        animationDataList.forEachIndexed { index, animationData ->
            //вычисляем дельту для смещения и поворота
            val newX = oldX + animationData.toX - animationData.fromX
            val newY = oldY + animationData.toY - animationData.fromY
            val angle = animationData.angle * (if (animationData.toLeft) -1 else 1)
            val newAngle = oldAngle + (angle - oldAngle)

            //создаем анимации движения и поворота
            val animatorX = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, oldX, newX)
            val animatorY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, oldY, newY)
            val angleStart = ObjectAnimator.ofFloat(view, View.ROTATION, oldAngle, newAngle)
            angleStart.duration = ANIMATION_ANGLE_DURATION
            animatorX.duration = ANIMATION_TRANSLATION_DURATION
            animatorY.duration = ANIMATION_TRANSLATION_DURATION

            //добавляем анимации к общему списку анимаций
            val delay = ANIMATION_ALL_DURATION * index
            animatorSetResult.play(angleStart).after(delay)
            animatorSetResult.play(animatorX).with(animatorY).after(delay + ANIMATION_ANGLE_DURATION)

            oldX = newX
            oldY = newY
            oldAngle = newAngle
        }
        return animatorSetResult
    }

    /**
     * Подготовить данные для анимации по всем точкам.
     * @return список точек [AnimationData] для анимации
     */
    private fun getAnimationData(): List<AnimationData> {
        val animationDataList: MutableList<AnimationData> = mutableListOf()
        if (points.isNotEmpty()) {
            var oldX = points[0].first.toFloat()
            var oldY = points[0].second.toFloat()

            //строим анимацию перемещения от точки к точки
            for (ind in 1 until points.size) {
                val newX = points[ind].first.toFloat()
                val newY = points[ind].second.toFloat()
                animationDataList.add(AnimationData(oldX, newX, oldY, newY))
                oldX = newX
                oldY = newY
            }
        }

        return animationDataList
    }

    /** Сформировать точки движения машинки */
    private fun generatePoints() {
        //начало движения - текущее положение машинки
        points = mutableListOf()
        points.add(Pair(leftPosition, topPosition))
        var calcLeftPosition = leftPosition
        var calcTopPosition = topPosition

        while (calcTopPosition > 0) {
            calcLeftPosition = getRandomLeft(calcLeftPosition)
            calcTopPosition = getRandomTop(calcTopPosition)
            points.add(Pair(calcLeftPosition, calcTopPosition))
        }
        carTrajectoryView?.printRoute(points, carWidth, carHeight)
    }

    /**
     * Получить новое положение машинки по оси Y отноительно предыдущего положения.
     * @param oldTopPosition предыдущее положение.
     * @return новое положение по оси Y.
     */
    private fun getRandomTop(oldTopPosition: Int): Int {
        var newTop = oldTopPosition - Random.nextInt(RANGE_RANDOM_Y)
        if (newTop < 0) {
            newTop = 0
        }
        return newTop
    }

    /**
     * Получить новое положение машинки по оси X отноительно предыдущего положения.
     * @param oldLeftPosition предыдущее положение.
     * @return новое положение по оси X.
     */
    private fun getRandomLeft(oldLeftPosition: Int): Int {
        var left = 0
        do {
            left = Random.nextInt(RANGE_RANDOM_X * -1, RANGE_RANDOM_X)
        } while (oldLeftPosition + left < 0 || oldLeftPosition + left > width - carWidth)
        return oldLeftPosition + left
    }

    /**
     * Сбросить смещение анимации в исходное состояние.
     * @param view машинки.
     */
    private fun resetAnimationPosition(view: View) {
        animatorSetList?.cancel()
        val animatorX = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0f)
        val animatorY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0f)
        val angle = ObjectAnimator.ofFloat(view, View.ROTATION, 0f)
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animatorX, animatorY, angle)
        animatorSet.duration = 50
        animatorSet.start()
    }
}