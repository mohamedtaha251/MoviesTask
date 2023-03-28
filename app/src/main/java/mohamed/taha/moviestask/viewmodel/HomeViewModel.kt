package mohamed.taha.moviestask.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import mohamed.taha.moviestask.model.Film
import mohamed.taha.moviestask.repo.FilmRepository
import mohamed.taha.moviestask.util.FilmType
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val filmRepository: FilmRepository) : ViewModel() {


    var selectedFilmType: MutableState<FilmType> = mutableStateOf(FilmType.MOVIE)


    private var _topRatedFilm = mutableStateOf<Flow<PagingData<Film>>>(emptyFlow())
    val topRatedFilmState: State<Flow<PagingData<Film>>> = _topRatedFilm

    private var _nowPlayingFilm = mutableStateOf<Flow<PagingData<Film>>>(emptyFlow())
    val nowPlayingMoviesState: State<Flow<PagingData<Film>>> = _nowPlayingFilm


    init {
        refreshAll()
    }

    private fun refreshAll(filmType: FilmType = selectedFilmType.value) {

        getTopRatedFilm( filmType)
        getNowPlayingFilms( filmType)

    }


    private fun getTopRatedFilm(filmType: FilmType) {
        viewModelScope.launch {
            _topRatedFilm.value = filmRepository.getTopRatedFilm(filmType).cachedIn(viewModelScope)
        }
    }

    private fun getNowPlayingFilms(filmType: FilmType) {
        viewModelScope.launch {
            _nowPlayingFilm.value =
                filmRepository.getNowPlayingFilms(filmType).cachedIn(viewModelScope)
        }
    }
}