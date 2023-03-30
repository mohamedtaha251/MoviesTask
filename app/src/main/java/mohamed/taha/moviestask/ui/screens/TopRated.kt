package mohamed.taha.moviestask.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Light
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items

import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.coil.CoilImage
import mohamed.taha.moviestask.R
import mohamed.taha.moviestask.ui.activity.MovieDetails
import mohamed.taha.moviestask.ui.sharedComposables.LoopReverseLottieLoader
import mohamed.taha.moviestask.ui.theme.AppOnPrimaryColor
import mohamed.taha.moviestask.ui.theme.AppPrimaryColor
import mohamed.taha.moviestask.ui.theme.ButtonColor
import mohamed.taha.moviestask.util.Constants.BASE_BACKDROP_IMAGE_URL
import mohamed.taha.moviestask.util.Constants.BASE_POSTER_IMAGE_URL
import mohamed.taha.moviestask.viewmodel.HomeViewModel
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import retrofit2.HttpException
import java.io.IOException

//@Destination
@Composable
fun TopRated(
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val context=LocalContext.current
    val topRatedFilms = homeViewModel.topRatedFilmState.value.collectAsLazyPagingItems()
    val landscape=false
    Box(
        contentAlignment = Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(AppPrimaryColor)
            .height(if (!landscape) 215.dp else 195.dp)
    ) {
        when (topRatedFilms.loadState.refresh) {
            is LoadState.Loading -> {
                LoopReverseLottieLoader(lottieFile = R.raw.loader)
            }
            is LoadState.NotLoading -> {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(topRatedFilms) { film ->
                        val imagePath =
                            if (landscape) "$BASE_BACKDROP_IMAGE_URL/${film?.backdropPath}"
                            else "$BASE_POSTER_IMAGE_URL/${film?.posterPath}"

                        TopRatedMovieItem(
                            landscape = landscape,
                            imageUrl = imagePath,
                            title = film?.title?:"",
                            voteAverage = film?.voteAverage?.toString()?:"",
                            modifier = Modifier
                                .height(if (landscape) 161.25.dp else 195.dp)
                        ) {
                            MovieDetails.start(context, film)

                        }
                    }
                }
            }
            is LoadState.Error -> {
                val error = topRatedFilms.loadState.refresh as LoadState.Error
                val errorMessage = when (error.error) {
                    is HttpException -> "Sorry, Something went wrong!\nTap to retry"
                    is IOException -> "Connection failed. Tap to retry!"
                    else -> "Failed! Tap to retry!"
                }
                Box(contentAlignment = Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(161.25.dp) // maintain the vertical space between two categories
                        .clickable {}
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
fun TopRatedMovieItem(
    imageUrl: String,
    title: String,
    voteAverage: String,
    modifier: Modifier,
    landscape: Boolean,
    onclick: () -> Unit,
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
                highlightColor = ButtonColor,
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
            contentScale = ContentScale.Crop,
            circularReveal = CircularReveal(duration = 1000),
            modifier = modifier.clip(RoundedCornerShape(8.dp)),
            contentDescription = "Movie item"
        )

        AnimatedVisibility(true) {
            Row(modifier=Modifier.fillMaxWidth()) {
                Text(
                    text = trimTitle(title),
                    modifier = Modifier
                        .padding(start = 4.dp, top = 4.dp, bottom = 10.dp)
                        .wrapContentSize(),
                    maxLines = 1,
                    color = AppOnPrimaryColor,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )

                Text(
                    text = voteAverage,

                    modifier = Modifier
                        .padding(start = 4.dp, top = 4.dp, bottom = 10.dp)
                        .wrapContentSize(),
                    maxLines = 1,
                    color = Color.Yellow,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.ExtraBold,

                    textAlign = TextAlign.End,

                )
            }

        }
    }
}

