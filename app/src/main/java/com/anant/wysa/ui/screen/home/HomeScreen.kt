package com.anant.wysa.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.anant.wysa.R
import com.anant.wysa.application.WysaApplication
import com.anant.wysa.displayData.MovieDisplayData
import com.anant.wysa.factory.viewModelFactory
import com.anant.wysa.screen.favorite.FavoriteViewModel
import com.anant.wysa.util.WysaAppBar
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(openDetailScreen: (Int) -> Unit, openFavoriteScreen: () -> Unit) {
    val viewModel: HomeViewModel = viewModel <HomeViewModel>(factory =viewModelFactory { HomeViewModel(WysaApplication.instance.getAppModule().movieRepoImpl) } )
//    val favoriteViewModel: FavoriteViewModel = viewModel<FavoriteViewModel>()
    var expanded by remember { mutableStateOf(false) }
    val uiState by viewModel.uistate.collectAsStateWithLifecycle()
    val favMovieList by viewModel.favMovieList.collectAsState()
    Scaffold(topBar = {
        WysaAppBar(
            title = "Movie App",
            actions = {
                IconButton(onClick = {
                    openFavoriteScreen()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_favorite_24),
                        contentDescription = "Add to Wishlist",
                        tint = if (favMovieList.isEmpty()) Color.White else Color.Red
                    )
                }

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(8.dp)
                ) {

                }
            }
        )
    }) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                    modifier = Modifier
                        .padding(it)
                        .background(Color(0xFF070421))
                        .fillMaxSize()
                ) {
                    MainContent((uiState.data),
                        openDetailScreen = { id ->
                            openDetailScreen.invoke(id)
                        },
                        addFavClick = { addFavItem ->
                            viewModel.addItemToFav(addFavItem.result)
                        }, removeFavClick = { removeFavItem ->
                            viewModel.remove(removeFavItem)
                        }, onLastItemVisible = viewModel::onLastItemVisible
                    )
                }
            }
        }
}

@Composable
fun MainContent(
    movieList: List<MovieDisplayData>?,
    openDetailScreen: (Int) -> Unit,
    addFavClick: (movie: MovieDisplayData) -> Unit,
    removeFavClick: (movieId: Int) -> Unit,
    onLastItemVisible:() -> Unit
) {
    LazyColumn {
        items(movieList.orEmpty()) { list ->
            MovieListItem(movie = list,
                onItemClick = { movieId ->
                    openDetailScreen(movieId)
                },
                addFavClick = {
                    addFavClick.invoke(list)
                }, removeFavClick = {
                    removeFavClick.invoke(list.result.id)
                }
            )
        }
        item {
            onLastItemVisible()
        }
    }
}

@Composable
fun MovieListItem(
    movie: MovieDisplayData,
    onItemClick: (movieId: Int) -> Unit,
    addFavClick: () -> Unit,
    removeFavClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .padding(14.dp)
            .fillMaxWidth()
            .clickable {
                onItemClick(
                    movie.result.id
                )
            },
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
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
                        model = "https://image.tmdb.org/t/p/w200${movie.result.backdropPath}",
                        imageLoader = ImageLoader.Builder(LocalContext.current).build(),
                    ), contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

            }
            Column(
                modifier = Modifier.padding(4.dp)
            ) {
                Text(
                    text = movie.result.title, color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = movie.result.originalLanguage, color = Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.padding(top = 10.dp))
                Button(
                    onClick = {
                        scope.launch {
                            if (movie.isAddedToFav) {
                                removeFavClick()
                            } else {
                                addFavClick()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE31E24)
                    ),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .height(40.dp)
                        .width(190.dp)
                ) {
                    Text(
                        text = "${if (movie.isAddedToFav) "Remove" else "Add"} To Favourite",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}