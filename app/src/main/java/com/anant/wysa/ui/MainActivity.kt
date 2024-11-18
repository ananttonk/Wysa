package com.anant.wysa.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.anant.wysa.navigation.MovieNavigation
import com.anant.wysa.ui.theme.WysaTheme

class MainActivity : ComponentActivity() {

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

