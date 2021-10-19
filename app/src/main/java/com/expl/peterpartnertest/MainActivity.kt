package com.expl.peterpartnertest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.expl.peterpartnertest.databinding.ActivityMainBinding
import com.expl.peterpartnertest.utilits.APP_ACTIVITY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var mToolbar: Toolbar
    lateinit var navController: NavController
    private var _binding: ActivityMainBinding?=null
    private val mBinding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        APP_ACTIVITY = this
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        mToolbar = mBinding.toolbar
        setSupportActionBar(mToolbar)
        title = ""
        setContentView(mBinding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
