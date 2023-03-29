package mohamed.taha.moviestask.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mohamed.taha.moviestask.model.Genre
import mohamed.taha.moviestask.remote.response.WatchProvider
import mohamed.taha.moviestask.repo.FilmRepository
import mohamed.taha.moviestask.repo.GenreRepository
import mohamed.taha.moviestask.util.FilmType
import mohamed.taha.moviestask.util.Resource
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    val repository: FilmRepository,
    private val genreRepository: GenreRepository
) : ViewModel() {

    private var _watchProviders = mutableStateOf<WatchProvider?>(null)
    val watchProviders: MutableState<WatchProvider?> = _watchProviders

    private var _filmGenres = mutableStateListOf(Genre(null, "All"))
    val filmGenres: SnapshotStateList<Genre> = _filmGenres

    fun getWatchProviders(filmId: Int, filmType: FilmType) {
        viewModelScope.launch {
            repository.getWatchProviders(filmType = filmType, filmId = filmId).also {
                if (it is Resource.Success) {
                    _watchProviders.value = it.data!!.results
                }
            }
        }
    }

    fun getFilmGenre(genreId:Int) {
        viewModelScope.launch {
            val defaultGenre = Genre(genreId, "All")
            when (val results = genreRepository.getMoviesGenre(FilmType.MOVIE)) {
                is Resource.Success -> {
                    _filmGenres.clear()
                    _filmGenres.add(defaultGenre)
                    results.data?.genres?.forEach {
                        _filmGenres.add(it)
                    }
                }
                is Resource.Error -> {
                    Timber.e("Error loading Genres")
                }
                else -> {}
            }
        }
    }
}
