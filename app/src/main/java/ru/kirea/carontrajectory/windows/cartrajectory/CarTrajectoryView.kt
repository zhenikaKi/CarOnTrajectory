package ru.kirea.carontrajectory.windows.cartrajectory

interface CarTrajectoryView {
    /**
     * Поставить машинку на конретные координаты.
     * @param left отступ машинки слева.
     * @param top отступ машинки сверху.
     */
    fun setCarPosition(left: Int, top: Int)

    /**
     * Нарисовать маршрут машинки.
     * @param points список пар отсупов слева и сверху.
     * @param carWidth ширина машинки.
     * @param carHeight высота машинки.
     */
    fun printRoute(points: List<Pair<Int, Int>>, carWidth: Int, carHeight: Int)
}