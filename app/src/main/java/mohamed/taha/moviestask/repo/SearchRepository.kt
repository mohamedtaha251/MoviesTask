package mohamed.taha.moviestask.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import mohamed.taha.moviestask.model.Search
import mohamed.taha.moviestask.paging.SearchFilmSource
import mohamed.taha.moviestask.remote.ApiService
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val api: ApiService
) {
    fun multiSearch(searchParams: String): Flow<PagingData<Search>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = 20),
            pagingSourceFactory = {
                SearchFilmSource(api = api, searchParams = searchParams)
            }
        ).flow
    }
}