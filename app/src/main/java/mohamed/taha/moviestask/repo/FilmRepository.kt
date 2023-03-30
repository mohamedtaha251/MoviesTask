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

    fun getTopRatedFilm(filmType: FilmType): Flow<PagingData<Film>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 20),
            pagingSourceFactory = {
                TopRatedFilmSource(api = api, filmType)
            }
        ).flow
    }

    fun getNowPlayingFilms(filmType: FilmType): Flow<PagingData<Film>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 20),
            pagingSourceFactory = {
                NowPlayingFilmSource(api = api, filmType)
            }
        ).flow
    }

    fun getMostPopularFilms(filmType: FilmType): Flow<PagingData<Film>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 20),
            pagingSourceFactory = {
                MostPopularFilmSource(api = api, filmType)
            }
        ).flow
    }

    suspend fun getWatchProviders(
        filmType: FilmType, filmId: Int
    ): Resource<WatchProviderResponse> {
        val response = try {
            if (filmType == FilmType.MOVIE) api.getWatchProviders(
                filmPath = "movie", filmId = filmId
            )
            else api.getWatchProviders(filmPath = "tv", filmId = filmId)
        } catch (e: Exception) {
            return Resource.Error("Error when loading providers")
        }
        Timber.d("WATCH", response)
        return Resource.Success(response)
    }
}
