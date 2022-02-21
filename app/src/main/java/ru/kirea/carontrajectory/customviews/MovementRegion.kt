package ru.kirea.carontrajectory.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import ru.kirea.carontrajectory.R

/** Область движения машинки */
class MovementRegion(context: Context, attributeSet: AttributeSet? = null):
    LinearLayout(context, attributeSet) {

    companion object {
        private const val LINE_WIDTH = 10f //толщина линии
    }

    private val paint = Paint()
    private val path = Path()

    var points: List<Pair<Int, Int>>? = null
    var carWidth: Int = 0
    var carHeight: Int = 0

    init {
        paint.color = ResourcesCompat.getColor(context.resources, R.color.purple_500, null)
        paint.strokeWidth = LINE_WIDTH
        paint.style = Paint.Style.STROKE
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        path.reset()
        val carCenterX = carWidth/2
        val carCenterY = carHeight/2
        //нарисуем маршрут движения машинки
        points?.forEachIndexed { index, pair ->
            if (index == 0) {
                path.moveTo(pair.first.toFloat() + carCenterX, pair.second.toFloat() + carCenterY)
            }
            else {
                path.lineTo(pair.first.toFloat() + carCenterX, pair.second.toFloat() + carCenterY)
            }
        }

        canvas?.drawPath(path, paint)
    }
}