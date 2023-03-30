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


    private var _topRatedFilm = mutableStateOf<Flow<PagingData<Film>>>(emptyFlow())
    val topRatedFilmState: State<Flow<PagingData<Film>>> = _topRatedFilm

    private var _nowPlayingFilm = mutableStateOf<Flow<PagingData<Film>>>(emptyFlow())
    val nowPlayingMoviesState: State<Flow<PagingData<Film>>> = _nowPlayingFilm

    private var _mostPopularFilm = mutableStateOf<Flow<PagingData<Film>>>(emptyFlow())
    val mostPopularMoviesState: State<Flow<PagingData<Film>>> = _mostPopularFilm


    init {
        refreshAll()
    }

     fun refreshAll() {

        getTopRatedFilm( )
        getNowPlayingFilms( )
        getMostPopularFilms( )

    }


    private fun getTopRatedFilm() {
        viewModelScope.launch {
            _topRatedFilm.value = filmRepository.getTopRatedFilm().cachedIn(viewModelScope)
        }
    }

    private fun getNowPlayingFilms() {
        viewModelScope.launch {
            _nowPlayingFilm.value =
                filmRepository.getNowPlayingFilms().cachedIn(viewModelScope)
        }
    }
    private fun getMostPopularFilms() {
        viewModelScope.launch {
            _mostPopularFilm.value =
                filmRepository.getMostPopularFilms().cachedIn(viewModelScope)
        }
    }
}