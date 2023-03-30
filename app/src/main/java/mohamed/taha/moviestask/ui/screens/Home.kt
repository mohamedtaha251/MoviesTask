package mohamed.taha.moviestask.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Light
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.coil.CoilImage
import mohamed.taha.moviestask.BuildConfig
import mohamed.taha.moviestask.R
import mohamed.taha.moviestask.model.Film
import mohamed.taha.moviestask.ui.activity.MovieDetails
import mohamed.taha.moviestask.ui.activity.SearchActivity
import mohamed.taha.moviestask.ui.sharedComposables.LoopReverseLottieLoader
import mohamed.taha.moviestask.ui.theme.AppOnPrimaryColor
import mohamed.taha.moviestask.ui.theme.AppPrimaryColor
import mohamed.taha.moviestask.ui.theme.ButtonColor
import mohamed.taha.moviestask.util.Constants.BASE_BACKDROP_IMAGE_URL
import mohamed.taha.moviestask.util.Constants.BASE_POSTER_IMAGE_URL
import mohamed.taha.moviestask.viewmodel.HomeViewModel
import mohamed.taha.moviestask.viewmodel.WatchListViewModel
import retrofit2.HttpException
import java.io.IOException

//@Destination
@Composable
fun Home(

    homeViewModel: HomeViewModel = hiltViewModel(),
    watchListViewModel: WatchListViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF180E36))
    ) {
        SearchMoviesBar( )
        NestedScroll( homeViewModel, watchListViewModel)
    }
}

@Composable
fun SearchMoviesBar() {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .padding(top = 12.dp, bottom = 4.dp, start = 8.dp, end = 8.dp)
            .fillMaxWidth()
            .padding(start = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {

        IconButton(
            onClick = {
                SearchActivity.start(context)
            }
        ) {
            Icon(
                modifier = Modifier.size(28.dp),
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "search icon",
                tint = AppOnPrimaryColor
            )
        }
    }
}

@Composable
fun NestedScroll(
    homeViewModel: HomeViewModel,
    watchListViewModel: WatchListViewModel
) {

    val topRatedFilms = homeViewModel.topRatedFilmState.value.collectAsLazyPagingItems()
    val nowPlayingFilms = homeViewModel.nowPlayingMoviesState.value.collectAsLazyPagingItems()
    val mostPopularFilms = homeViewModel.mostPopularMoviesState.value.collectAsLazyPagingItems()
    val myWatchList = watchListViewModel.watchList.value.collectAsState(initial = emptyList())

    LaunchedEffect(key1 = myWatchList.value.size) {

    }

    val listState: LazyListState = rememberLazyListState()
    LazyColumn(
        state = listState,
        modifier = Modifier
            .padding(horizontal = 2.dp)
            .fillMaxSize()
    ) {

        item {
            Text(
                text = "Top Rated",
                fontSize = 24.sp,
                color = AppOnPrimaryColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp, top = 14.dp, bottom = 8.dp)
            )
        }
        item {
            ScrollableMovieItems(
                
                pagingItems = topRatedFilms,

                onErrorClick = {
                    homeViewModel.refreshAll()
                }
            )
        }

        if (BuildConfig.FLAVOR == "Premium") {

            item {
                Text(
                    text = "Now Playing",
                    fontSize = 24.sp,
                    color = AppOnPrimaryColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp, top = 14.dp, bottom = 4.dp)
                )
            }
            item {
                ScrollableMovieItems(
                    
                    pagingItems = nowPlayingFilms,

                    onErrorClick = {
                        homeViewModel.refreshAll()
                    }
                )
            }
        }


        item {
            Text(
                text = "Favorites",
                fontSize = 24.sp,
                color = AppOnPrimaryColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp, top = 14.dp, bottom = 4.dp)
            )
        }
        item {
            ScrollableMovieItems(
                
                pagingItems = mostPopularFilms,

                onErrorClick = {
                    homeViewModel.refreshAll()
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Composable
fun MovieItem(
    imageUrl: String,
    title: String,
    modifier: Modifier,
    landscape: Boolean,
    onclick: () -> Unit
) {
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(all = 4.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onclick()
            },
        horizontalAlignment = Alignment.Start
    ) {
        CoilImage(
            imageModel = imageUrl,
            shimmerParams = ShimmerParams(
                baseColor = AppPrimaryColor,
                highlightColor = AppPrimaryColor,
                durationMillis = 500,
                dropOff = 0.65F,
                tilt = 20F
            ),
            failure = {
                Box(
                    contentAlignment = Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .border(
                            width = 1.dp,
                            color = Color.LightGray,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.image_not_available),
                        contentDescription = "no image"
                    )
                }
            },
            previewPlaceholder = R.drawable.popcorn,
            contentScale = Crop,
            circularReveal = CircularReveal(duration = 1000),
            modifier = modifier.clip(RoundedCornerShape(8.dp)),
            contentDescription = "Movie item"
        )

        AnimatedVisibility(visible = landscape) {
            Text(
                text = trimTitle(title),
                modifier = Modifier
                    .padding(start = 4.dp, top = 4.dp)
                    .fillMaxWidth(),
                maxLines = 1,
                color = AppOnPrimaryColor,
                overflow = TextOverflow.Ellipsis,
                fontWeight = Normal,
                textAlign = TextAlign.Start
            )
        }
    }
}

internal fun trimTitle(text: String) = if (text.length <= 26) text else {
    val textWithEllipsis = text.removeRange(startIndex = 26, endIndex = text.length)
    "$textWithEllipsis..."
}

@Composable
private fun ScrollableMovieItems(
    landscape: Boolean = false,
    pagingItems: LazyPagingItems<Film>,
    onErrorClick: () -> Unit
) {
    val context = LocalContext.current

    Box(
        contentAlignment = Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(if (!landscape) 215.dp else 195.dp)
    ) {
        when (pagingItems.loadState.refresh) {
            is LoadState.Loading -> {
                LoopReverseLottieLoader(lottieFile = R.raw.loader)
            }
            is LoadState.NotLoading -> {
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    items(pagingItems) { film ->
                        val imagePath =
                            if (landscape) "$BASE_BACKDROP_IMAGE_URL/${film?.backdropPath}"
                            else "$BASE_POSTER_IMAGE_URL/${film?.posterPath}"

                        MovieItem(
                            landscape = landscape,
                            imageUrl = imagePath,
                            title = film?.title ?: "",
                            modifier = Modifier
                                .width(if (landscape) 215.dp else 130.dp)
                                .height(if (landscape) 161.25.dp else 195.dp)
                        ) {
                            MovieDetails.start(context, film)

                        }
                    }
                }
            }
            is LoadState.Error -> {
                val error = pagingItems.loadState.refresh as LoadState.Error
                val errorMessage = when (error.error) {
                    is HttpException -> "Sorry, Something went wrong!\nTap to retry"
                    is IOException -> "Connection failed. Tap to retry!"
                    else -> "Failed! Tap to retry!"
                }
                Box(contentAlignment = Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(161.25.dp) // maintain the vertical space between two categories
                        .clickable {
                            onErrorClick()
                        }
                ) {
                    Text(
                        text = errorMessage,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        fontWeight = Light,
                        color = Color(0xFFE28B8B),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            else -> {
            }
        }
    }
}

@Composable
fun SelectableGenreChip(
    genre: String,
    selected: Boolean,
    onclick: () -> Unit
) {

    val animateChipBackgroundColor by animateColorAsState(
        targetValue = if (selected) Color(0xFFA0A1C2) else ButtonColor.copy(alpha = 0.5F),
        animationSpec = tween(
            durationMillis = if (selected) 100 else 50,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        )
    )

    Box(
        modifier = Modifier
            .padding(end = 4.dp)
            .clip(CircleShape)
            .background(
                color = animateChipBackgroundColor
            )
            .height(32.dp)
            .widthIn(min = 80.dp)
            /*.border(
                width = 0.5.dp,
                color = Color(0xC69495B1),
                shape = CircleShape
            )*/
            .padding(horizontal = 8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onclick()
            }
    ) {
        Text(
            text = genre,
            fontWeight = if (selected) Normal else Light,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Center),
            color = if (selected) Color(0XFF180E36) else Color.White.copy(alpha = 0.80F)
        )
    }
}

