package ru.kirea.carontrajectory.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.kirea.carontrajectory.windows.cartrajectory.CarTrajectoryFragment
import ru.kirea.carontrajectory.windows.cartrajectory.CarTrajectoryPresenter

object Modules {

    //модуль экрана с машинкой
    val carTrajectoryWindow = module {
        scope<CarTrajectoryFragment> {
            scoped<CarTrajectoryPresenter>(qualifier = named(Scopes.CAR_TRAJECTORY_PRESENTER)) {
                CarTrajectoryPresenter()
            }
        }
    }
}
