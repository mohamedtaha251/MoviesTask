package mohamed.taha.moviestask.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import mohamed.taha.moviestask.model.Film
import mohamed.taha.moviestask.paging.MostPopularFilmSource
import mohamed.taha.moviestask.paging.NowPlayingFilmSource
import mohamed.taha.moviestask.paging.TopRatedFilmSource
import mohamed.taha.moviestask.remote.ApiService
import mohamed.taha.moviestask.remote.response.WatchProviderResponse
import mohamed.taha.moviestask.util.FilmType
import mohamed.taha.moviestask.util.Resource
import timber.log.Timber
import javax.inject.Inject

class FilmRepository @Inject constructor(
    private val api: ApiService
) {

    fun getTopRatedFilm(): Flow<PagingData<Film>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 20),
            pagingSourceFactory = {
                TopRatedFilmSource(api = api)
            }
        ).flow
    }

    fun getNowPlayingFilms(): Flow<PagingData<Film>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 20),
            pagingSourceFactory = {
                NowPlayingFilmSource(api = api)
            }
        ).flow
    }

    fun getMostPopularFilms(): Flow<PagingData<Film>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 20),
            pagingSourceFactory = {
                MostPopularFilmSource(api = api)
            }
        ).flow
    }

    suspend fun getWatchProviders(
         filmId: Int
    ): Resource<WatchProviderResponse> {
        val response = try {
            api.getWatchProviders(
                filmPath = "movie", filmId = filmId
            )
        } catch (e: Exception) {
            return Resource.Error("Error when loading providers")
        }
        Timber.d("WATCH", response)
        return Resource.Success(response)
    }
}
