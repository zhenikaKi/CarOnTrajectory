package ru.kirea.carontrajectory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.kirea.carontrajectory.databinding.MainActivityBinding
import ru.kirea.carontrajectory.windows.cartrajectory.CarTrajectoryFragment

class MainActivity : AppCompatActivity() {

    private val binding: MainActivityBinding by lazy { MainActivityBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, CarTrajectoryFragment())
            fragmentTransaction.commit()
        }
    }
}