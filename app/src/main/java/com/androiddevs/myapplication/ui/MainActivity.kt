package com.androiddevs.myapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.androiddevs.myapplication.R
import com.androiddevs.myapplication.database.ArticleDatabase
import com.androiddevs.myapplication.databinding.ActivityMainBinding
import com.androiddevs.myapplication.repositary.NewsRepositary
import com.androiddevs.myapplication.ui.viewModel.NewsViewModel
import com.androiddevs.myapplication.ui.viewModel.NewsViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var viewModel : NewsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityMainBinding.inflate(layoutInflater)


        val database = ArticleDatabase(this)
        val repositary = NewsRepositary(database)
        val factory = NewsViewModelFactory(repositary,application)
        viewModel = ViewModelProvider(this, factory).get(NewsViewModel::class.java)






        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_Fragment) as NavHostFragment
        navController = navHostFragment.navController
         binding.bottomNavigation.setupWithNavController(navController)

        setContentView(binding.root)


  }
}