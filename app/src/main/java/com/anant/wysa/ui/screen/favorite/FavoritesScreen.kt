package com.anant.wysa.ui.screen.favorite

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.anant.wysa.application.WysaApplication
import com.anant.wysa.factory.viewModelFactory
import com.anant.wysa.model.MovieListData
import com.anant.wysa.ui.ext.WysaAppBar
import kotlinx.coroutines.launch

@Composable
fun FavoritesScreen(
    onBackPress: () -> Unit,
    openDetailsScreen: (Int) -> Unit
) {
    val favoriteViewModel: FavoriteViewModel = viewModel<FavoriteViewModel>(factory = viewModelFactory { FavoriteViewModel(
        WysaApplication.instance.getAppModule().movieRepoImpl) })
    val favoriteList by favoriteViewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        favoriteViewModel.getFavList()
    }
    Scaffold(topBar = {
        WysaAppBar("Favorites List", backPressCallBack = {
            onBackPress.invoke()
        })
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .background(Color(0xFF070421))
                .fillMaxSize()
        ) {
            FavContain(favoriteList, removeItem = { removeFavItem ->
                favoriteViewModel.remove(removeFavItem.id)
            }) { id ->
                openDetailsScreen.invoke(id)
            }
        }
    }
}

@Composable
fun FavContain(
    favMovieList: List<MovieListData.Result>,
    removeItem: (MovieListData.Result) -> Unit,
    openDetailsScreen: (Int) -> Unit
) {
    LazyColumn {
        items(favMovieList) { list ->
            FavListItem(list, removeItem = { removeFavItem ->
                removeItem.invoke(removeFavItem)
            }, onItemClick = { id ->
                openDetailsScreen.invoke(id)
            })
        }
    }
}

@Composable
fun FavListItem(
    movie: MovieListData.Result,
    removeItem: (MovieListData.Result) -> Unit,
    onItemClick: (movieId: Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    Card(
        modifier = Modifier
            .padding(14.dp)
            .fillMaxWidth()
            .clickable {
                onItemClick(
                    movie.id
                )
            },
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color(0xFF25233D))
                .fillMaxWidth()
        ) {
            Surface(
                modifier = Modifier
                    .padding(12.dp)
                    .size(120.dp),
                shape = RoundedCornerShape(14.dp),
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = "https://image.tmdb.org/t/p/w500${movie.backdropPath}",
                        imageLoader = ImageLoader.Builder(LocalContext.current).build(),
                    ), contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier.padding(start = 4.dp, top = 12.dp)
            ) {
                Text(
                    text = movie.title, color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = movie.originalLanguage, color = Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.padding(top = 10.dp))
                Button(
                    onClick = {
                        scope.launch {
                            removeItem.invoke(movie)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE31E24)
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .height(40.dp)
                        .width(100.dp)
                ) {
                    Text(
                        text = "Remove From Favourite",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}