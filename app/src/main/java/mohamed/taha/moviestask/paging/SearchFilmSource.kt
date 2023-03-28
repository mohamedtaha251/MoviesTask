package mohamed.taha.moviestask.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import mohamed.taha.moviestask.model.Search
import mohamed.taha.moviestask.remote.ApiService
import retrofit2.HttpException
import java.io.IOException

class SearchFilmSource(
    private val api: ApiService,
    private val searchParams: String,
) : PagingSource<Int, Search>() {
    override fun getRefreshKey(state: PagingState<Int, Search>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Search> {
        return try {
            val nextPage = params.key ?: 1
            val searchMovies = api.multiSearch(
                page = nextPage,
                searchParams = searchParams,
            )
            LoadResult.Page(
                data = searchMovies.results,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (searchMovies.results.isEmpty()) null else searchMovies.page + 1
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }
}