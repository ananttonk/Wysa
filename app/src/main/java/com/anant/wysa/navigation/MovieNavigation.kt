package com.anant.wysa.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.anant.wysa.ui.screen.favorite.FavoritesScreen
import com.anant.wysa.ui.screen.home.HomeScreen
import com.anant.wysa.ui.screen.details.MovieDetailsScreen

@Composable
fun MovieNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavigationScreen.MainScreen.name) {
        composable(route = NavigationScreen.MainScreen.name) {
            HomeScreen(openDetailScreen = { id ->
                navController.navigate(route = NavigationScreen.MovieDetails.name + "/$id")
            }, openFavoriteScreen = {
                navController.navigate(NavigationScreen.FavoritesScreen.name)
            })
        }

        composable(route = NavigationScreen.FavoritesScreen.name) {
            FavoritesScreen(onBackPress = {
                navController.popBackStack()
            }, openDetailsScreen = { id ->
                navController.navigate(route = NavigationScreen.MovieDetails.name + "/$id")
            })

        }

        composable(
            route = NavigationScreen.MovieDetails.name + "/{movieId}",
            arguments = listOf(
                navArgument(name = "movieId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            MovieDetailsScreen(
                movieId = backStackEntry.arguments?.getInt("movieId") ?: 0, onBackPress = {
                    navController.popBackStack()
                })
        }
    }
}