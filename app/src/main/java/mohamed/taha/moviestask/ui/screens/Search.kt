package mohamed.taha.moviestask.ui.screens

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import mohamed.taha.moviestask.R
import mohamed.taha.moviestask.model.Film
import mohamed.taha.moviestask.ui.activity.MovieDetails
import mohamed.taha.moviestask.ui.sharedComposables.BackButton
import mohamed.taha.moviestask.ui.sharedComposables.SearchBar
import mohamed.taha.moviestask.ui.sharedComposables.SearchResultItem
import mohamed.taha.moviestask.ui.theme.AppOnPrimaryColor
import mohamed.taha.moviestask.ui.theme.AppPrimaryColor
import mohamed.taha.moviestask.util.Constants.BASE_POSTER_IMAGE_URL
import mohamed.taha.moviestask.viewmodel.DetailsViewModel
import mohamed.taha.moviestask.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    activity: Activity,
    searchViewModel: SearchViewModel = hiltViewModel(),
    detailsViewModel: DetailsViewModel = hiltViewModel(),
) {
    val searchResult = searchViewModel.multiSearchState.value.collectAsLazyPagingItems()

    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppPrimaryColor)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 16.dp)
                .fillMaxWidth(fraction = 0.60F)
        ) {
            val focusManager = LocalFocusManager.current
            BackButton {
                focusManager.clearFocus()
                activity.finish()
            }

            Text(
                text = "Search",
                modifier = Modifier.padding(start = 50.dp),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                color = AppOnPrimaryColor
            )
        }

        SearchBar(
            autoFocus = true,
            onSearch = {
                searchViewModel.searchRemoteMovie()
            })

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            when (searchResult.loadState.refresh) {
                is LoadState.NotLoading -> {
                    items(searchResult) { film ->
                        val focus = LocalFocusManager.current
                        SearchResultItem(
                            title = film!!.title,
                            mediaType = film.mediaType,
                            posterImage = "$BASE_POSTER_IMAGE_URL/${film.posterPath}",
                            genres = detailsViewModel.filmGenres.filter { genre ->
                                return@filter if (film.genreIds.isNullOrEmpty()) false else
                                    film.genreIds.contains(genre.id)
                            },
                            rating = (film.voteAverage ?: 0) as Double,
                            releaseYear = film.releaseDate,
                            onClick = {
                                val navFilm = Film(
                                    adult = film.adult ?: false,
                                    backdropPath = film.backdropPath,
                                    posterPath = film.posterPath,
                                    genreIds = film.genreIds,
                                    genres = film.genres,
                                    mediaType = film.mediaType,
                                    id = film.id ?: 0,
                                    imdbId = film.imdbId,
                                    originalLanguage = film.originalLanguage ?: "",
                                    overview = film.overview ?: "",
                                    popularity = film.popularity ?: 0F.toDouble(),
                                    releaseDate = film.releaseDate ?: "",
                                    runtime = film.runtime,
                                    title = film.title ?: "",
                                    video = film.video ?: false,
                                    voteAverage = film.voteAverage ?: 0F.toDouble(),
                                    voteCount = film.voteCount ?: 0
                                )
                                focus.clearFocus()
                                MovieDetails.start(context, navFilm)

                            })
                    }
                    if (searchResult.itemCount == 0) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 60.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.no_match_found),
                                    contentDescription = null
                                )
                            }

                        }
                    }
                }

                is LoadState.Loading -> item {
                    if (searchViewModel.searchParam.value.isNotEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                else -> item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.no_match_found),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}
