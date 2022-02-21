package ru.kirea.carontrajectory.windows.cartrajectory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent
import ru.kirea.carontrajectory.databinding.CarTrajectionFragmentBinding
import ru.kirea.carontrajectory.di.Scopes

class CarTrajectoryFragment: Fragment(), CarTrajectoryView {

    private val scope = KoinJavaComponent.getKoin().createScope<CarTrajectoryFragment>()
    private val presenter: CarTrajectoryPresenter = scope.get(qualifier = named(Scopes.CAR_TRAJECTORY_PRESENTER))

    private var _binding: CarTrajectionFragmentBinding? = null
    private val binding
        get() = _binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = CarTrajectionFragmentBinding.inflate(inflater, container, false)
        setClickListener()
        presenter.carTrajectoryView = this

        //после создания всех view передаем размеры в прзентер
        binding?.root?.post {
            binding?.let {
                //заполняем размер поля и размер машинки
                presenter.setRegionSize(it.region.measuredWidth, it.region.measuredHeight)
                presenter.setCarSize(it.car.measuredWidth, it.car.measuredHeight)
            }
        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Поставить машинку на конретные координаты.
     * @param left отступ машинки слева.
     * @param top отступ машинки сверху.
     */
    override fun setCarPosition(left: Int, top: Int) {
        binding?.let {
            val layoutParams: LinearLayout.LayoutParams = it.car.layoutParams as LinearLayout.LayoutParams
            layoutParams.leftMargin = left
            layoutParams.topMargin = top
            it.car.layoutParams = layoutParams
            it.car.isVisible = true
            it.region.points = null
            it.region.invalidate()
        }
    }

    /**
     * Нарисовать маршрут машинки.
     * @param points список пар отсупов слева и сверху.
     * @param carWidth ширина машинки.
     * @param carHeight высота машинки.
     */
    override fun printRoute(points: List<Pair<Int, Int>>, carWidth: Int, carHeight: Int) {
        binding?.let {
            it.region.points = points
            it.region.carWidth = carWidth
            it.region.carHeight = carHeight
            it.region.invalidate()
        }
    }

    /** Задать обработчики кликов */
    private fun setClickListener() {
        binding?.let {
            //установка машинки в новое место
            it.start.setOnClickListener { v ->
                presenter.resetCarPosition(it.car)
                it.car.isEnabled = true
            }

            //клик по машинке
            it.car.setOnClickListener { v ->
                v.isEnabled = false //не разрешаем кликать по машинке пока она едет
                presenter.startMovement(v)
            }
        }
    }
}