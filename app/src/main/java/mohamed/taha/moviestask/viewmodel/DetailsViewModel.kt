package mohamed.taha.moviestask.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mohamed.taha.moviestask.remote.response.WatchProvider
import mohamed.taha.moviestask.repo.FilmRepository
import mohamed.taha.moviestask.util.FilmType
import mohamed.taha.moviestask.util.Resource
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(val repository: FilmRepository) : ViewModel() {

    private var _watchProviders = mutableStateOf<WatchProvider?>(null)
    val watchProviders: MutableState<WatchProvider?> = _watchProviders


    fun getWatchProviders(filmId: Int, filmType: FilmType) {
        viewModelScope.launch {
            repository.getWatchProviders(filmType = filmType, filmId = filmId).also {
                if (it is Resource.Success) {
                    _watchProviders.value = it.data!!.results
                }
            }
        }
    }
}
