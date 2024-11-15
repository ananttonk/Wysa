package com.anant.wysa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.anant.wysa.application.WysaApplication
import com.anant.wysa.module.ModuleImpl
import com.anant.wysa.navigation.MovieNavigation
import com.anant.wysa.screen.details.DetailViewModel
import com.anant.wysa.screen.favorite.FavoriteViewModel
import com.anant.wysa.screen.home.HomeViewModel
import com.anant.wysa.factory.viewModelFactory
import com.anant.wysa.ui.theme.WysaTheme

class MainActivity : ComponentActivity() {


    private val module: ModuleImpl by lazy {
        WysaApplication.instance.getAppModule()
    }

//    private val movieListViewModel by lazy {
//        ViewModelProvider(this, viewModelFactory { HomeViewModel(module.movieRepoImpl) }) [HomeViewModel::class.java]
//    }
//
//    private val detailViewModel by lazy {
//        ViewModelProvider(this, viewModelFactory { DetailViewModel(module.movieRepoImpl) }) [DetailViewModel::class.java]
//    }
//
//    private val favoriteViewModel by lazy {
//        ViewModelProvider(this, viewModelFactory { FavoriteViewModel(module.movieRepoImpl) }) [FavoriteViewModel::class.java]
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WysaTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    Surface {
        val navController = rememberNavController()
        MovieNavigation(navController)
    }
}

