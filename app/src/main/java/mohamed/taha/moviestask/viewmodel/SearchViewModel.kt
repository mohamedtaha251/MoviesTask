package mohamed.taha.moviestask.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import mohamed.taha.moviestask.repo.SearchRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import mohamed.taha.moviestask.model.Search
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {
    private var _multiSearch = mutableStateOf<Flow<PagingData<Search>>>(emptyFlow())
    val multiSearchState: State<Flow<PagingData<Search>>> = _multiSearch


    var searchParam = mutableStateOf("")
    var previousSearch = mutableStateOf("")
    var searchParamState: State<String> = searchParam

    init {
        searchParam.value = ""
    }

    fun searchRemoteMovie() {
        viewModelScope.launch {
            if (searchParam.value.isNotEmpty()) {
                _multiSearch.value = searchRepository.multiSearch(
                    searchParams = searchParam.value,
                ).map { result ->
                    result.filter {
                        ((it.title != null || it.originalName != null || it.originalTitle != null) &&
                                (it.mediaType == "tv" || it.mediaType == "movie"))
                    }
                }.cachedIn(viewModelScope)
            }
        }
    }
}