package mohamed.taha.moviestask.ui.screens

import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.Light
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.ericg.neatflix.sharedComposables.MovieGenreChip
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig
import com.gowtham.ratingbar.RatingBarStyle
import com.gowtham.ratingbar.StepSize
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.ShimmerParams
import com.skydoves.landscapist.coil.CoilImage
import mohamed.taha.moviestask.R
import mohamed.taha.moviestask.local.MyListMovie
import mohamed.taha.moviestask.model.Film
import mohamed.taha.moviestask.model.Genre
import mohamed.taha.moviestask.ui.sharedComposables.BackButton
import mohamed.taha.moviestask.ui.sharedComposables.ExpandableText
import mohamed.taha.moviestask.ui.theme.AppOnPrimaryColor
import mohamed.taha.moviestask.ui.theme.AppPrimaryColor
import mohamed.taha.moviestask.ui.theme.ButtonColor
import mohamed.taha.moviestask.util.Constants.BASE_BACKDROP_IMAGE_URL
import mohamed.taha.moviestask.util.Constants.BASE_POSTER_IMAGE_URL
import mohamed.taha.moviestask.util.FilmType
import mohamed.taha.moviestask.viewmodel.DetailsViewModel
import mohamed.taha.moviestask.viewmodel.HomeViewModel
import mohamed.taha.moviestask.viewmodel.WatchListViewModel
import java.text.SimpleDateFormat
import java.util.*

//@Destination
@Composable
fun FilmDetails(
    navigator: DestinationsNavigator? = null,
    homeViewModel: HomeViewModel = hiltViewModel(),
    detailsViewModel: DetailsViewModel = hiltViewModel(),
    watchListViewModel: WatchListViewModel = hiltViewModel(),
    currentFilm: Film,
    selectedFilmType: FilmType = FilmType.MOVIE
) {
    var film by remember {
        mutableStateOf(currentFilm)
    }
    val filmType: FilmType = remember { selectedFilmType }

    val date = SimpleDateFormat.getDateTimeInstance().format(Date())
    val watchListMovie = MyListMovie(
        mediaId = film.id,
        imagePath = film.posterPath,
        title = film.title,
        releaseDate = film.releaseDate,
        rating = film.voteAverage,
        addedOn = date
    )

    val addedToList = watchListViewModel.addedToWatchList.value


    LaunchedEffect(key1 = film) {
        watchListViewModel.exists(mediaId = film.id)
        detailsViewModel.getWatchProviders(film.id, selectedFilmType)
        detailsViewModel.getFilmGenre(film.id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF180E36))
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.33F)
        ) {
            val (
                backdropImage,
                backButton,
                movieTitleBox,
                moviePosterImage,
                translucentBr
            ) = createRefs()

            CoilImage(
                imageModel = "$BASE_BACKDROP_IMAGE_URL${film.backdropPath}",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .fillMaxHeight()
                    .constrainAs(backdropImage) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                failure = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(2.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.backdrop_not_available),
                            contentDescription = "no image"
                        )
                    }
                },
                shimmerParams = ShimmerParams(
                    baseColor = AppPrimaryColor,
                    highlightColor = ButtonColor,
                    durationMillis = 500,
                    dropOff = 0.65F,
                    tilt = 20F
                ),
                contentScale = Crop,
                contentDescription = "Header backdrop image",
            )

            BackButton(modifier = Modifier
                .constrainAs(backButton) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start, margin = 10.dp)
                }) {
                navigator?.navigateUp()
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color(0XFF180E36).copy(alpha = 0.5F),
                                Color(0XFF180E36)
                            ),
                            startY = 0.1F
                        )
                    )
                    .constrainAs(translucentBr) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(backdropImage.bottom)
                    }
            )

            Column(
                modifier = Modifier.constrainAs(movieTitleBox) {
                    start.linkTo(moviePosterImage.end, margin = 12.dp)
                    end.linkTo(parent.end, margin = 12.dp)
                    bottom.linkTo(moviePosterImage.bottom, margin = 10.dp)
                },
                verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = when (filmType) {
                            FilmType.TVSHOW -> "Series"
                            FilmType.MOVIE -> "Movie"
                        },
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(size = 4.dp))
                            .background(Color.DarkGray.copy(alpha = 0.65F))
                            .padding(2.dp),
                        color = AppOnPrimaryColor.copy(alpha = 0.78F),
                        fontSize = 12.sp,
                    )
                    Text(
                        text = when (film.adult) {
                            true -> "18+"
                            else -> "PG"
                        },
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clip(shape = RoundedCornerShape(size = 4.dp))
                            .background(
                                if (film.adult) Color(0xFFFF7070) else Color.DarkGray.copy(
                                    alpha = 0.65F
                                )
                            )
                            .padding(2.dp),
                        color = AppOnPrimaryColor.copy(alpha = 0.78F),
                        fontSize = 12.sp,
                    )
                }

                Text(
                    text = film.title,
                    modifier = Modifier
                        .padding(top = 2.dp, start = 4.dp, bottom = 4.dp)
                        .fillMaxWidth(0.5F),
                    maxLines = 2,
                    fontSize = 18.sp,
                    fontWeight = Bold,
                    color = Color.White.copy(alpha = 0.78F)
                )

                Text(
                    text = film.releaseDate,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp),
                    fontSize = 15.sp,
                    fontWeight = Light,
                    color = Color.White.copy(alpha = 0.56F)
                )


                RatingBar(
                    value = (film.voteAverage / 2).toFloat(),
                    modifier = Modifier.padding(start = 6.dp, bottom = 4.dp, top = 4.dp),
                    config = RatingBarConfig()
                        .style(RatingBarStyle.Normal)
                        .isIndicator(true)
                        .activeColor(Color(0XFFC9F964))
                        .hideInactiveStars(false)
                        .inactiveColor(Color.LightGray.copy(alpha = 0.3F))
                        .stepSize(StepSize.HALF)
                        .numStars(5)
                        .size(16.dp)
                        .padding (4.dp),
                    onValueChange = {},
                    onRatingChanged = {}
                )

                Text(
                    text = film.voteCount.toString() + " vote",
                    color = Color.Yellow,
                    modifier = Modifier
                        .padding(top = 3.dp, bottom = 4.dp, start = 4.dp, end = 4.dp)
                )






                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 4.dp, bottom = 8.dp)
                        .fillMaxWidth(0.42F),
                ) {


                    val context = LocalContext.current
                    IconButton(onClick = {
                        if (addedToList != 0) {
                            watchListViewModel.removeFromWatchList(watchListMovie.mediaId)

                            Toast.makeText(
                                context, "Removed from favorite", LENGTH_SHORT
                            ).show()

                        } else {
                            watchListViewModel.addToWatchList(watchListMovie)
                            Toast.makeText(
                                context, "Added to favorite", LENGTH_SHORT
                            ).show()
                        }
                    }) {
                        Icon(
                            painter = painterResource(
                                id = if (addedToList != 0) R.drawable.ic_added_to_list
                                else R.drawable.ic_add_to_list
                            ),
                            tint = AppOnPrimaryColor,
                            contentDescription = "add to watch list icon"
                        )
                    }
                }
            }

            CoilImage(
                imageModel = "$BASE_POSTER_IMAGE_URL/${film.posterPath}",
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .width(115.dp)
                    .height(172.5.dp)
                    .constrainAs(moviePosterImage) {
                        top.linkTo(backdropImage.bottom)
                        bottom.linkTo(backdropImage.bottom)
                        start.linkTo(parent.start)
                    }, failure = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.image_not_available),
                            contentDescription = "no image"
                        )
                    }
                },
                shimmerParams = ShimmerParams(
                    baseColor = AppPrimaryColor,
                    highlightColor = ButtonColor,
                    durationMillis = 500,
                    dropOff = 0.65F,
                    tilt = 20F
                ),
                previewPlaceholder = R.drawable.popcorn,
                contentScale = Crop,
                circularReveal = CircularReveal(duration = 1000),
                contentDescription = "movie poster"
            )
        }

        LazyRow(
            modifier = Modifier
                .padding(top = (96).dp, bottom = 4.dp, start = 4.dp, end = 4.dp)
                .fillMaxWidth()
        ) {
            val filmGenres: List<Genre> = detailsViewModel.filmGenres.filter { genre ->
                return@filter if (film.genreIds.isNullOrEmpty()) false else
                    film.genreIds!!.contains(genre.id)
            }
            filmGenres.forEach { genre ->
                item {
                    MovieGenreChip(
                        background = ButtonColor,
                        textColor = AppOnPrimaryColor,
                        genre = genre.name
                    )
                }
            }
        }

        Text(
            color = Color.White,
            text = film.overview,
            modifier = Modifier
                .padding(top = 3.dp, bottom = 4.dp, start = 4.dp, end = 4.dp)
                .fillMaxWidth()
        )


    }
}

