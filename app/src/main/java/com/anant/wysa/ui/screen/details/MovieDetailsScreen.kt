package com.anant.wysa.ui.screen.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.anant.wysa.model.MovieDetailsData
import com.anant.wysa.ui.ext.WysaAppBar

@Composable
fun MovieDetailsScreen(movieId: Int,onBackPress:()-> Unit) {
    val viewModel: DetailViewModel = viewModel<DetailViewModel>(factory = viewModelFactory { DetailViewModel(WysaApplication.instance.getAppModule().movieRepoImpl) })
    val uiState by  viewModel.uistate.collectAsState()
    Box(Modifier.fillMaxSize()) {
        when(uiState){
            is DetailUIState.Error -> {}
            DetailUIState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is DetailUIState.Result -> Column(
                modifier = Modifier
                    .background(Color(0xFF070421))
                    .fillMaxSize()
            ) {
                WysaAppBar((uiState as DetailUIState.Result).data.title, backPressCallBack = onBackPress)
                MovieDetails((uiState as DetailUIState.Result).data)
            }
            is DetailUIState.Init -> {
                LaunchedEffect(movieId) {
                    viewModel.getMovieDetails(movieId)
                }
            }
        }

    }
}


@Composable
fun MovieDetails(movieDetails: MovieDetailsData?) {
    Card(
        modifier = Modifier
            .padding(14.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
    ) {
        Column(
            modifier = Modifier
                .background(Color(0xFF25233D))
                .fillMaxWidth()
        ) {
            Surface(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(14.dp),
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = "https://image.tmdb.org/t/p/w500${movieDetails?.backdropPath.orEmpty()}",
                        imageLoader = ImageLoader.Builder(LocalContext.current).build(),
                    ), contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.padding(top = 10.dp))

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = movieDetails?.title.orEmpty(), color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
                Text(
                    text = movieDetails?.spokenLanguages.toString(), color = Color.Gray,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
                Spacer(Modifier.padding(top = 10.dp))
                Text(
                    text = movieDetails?.overview.orEmpty(),
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                )
            }
        }
    }
}