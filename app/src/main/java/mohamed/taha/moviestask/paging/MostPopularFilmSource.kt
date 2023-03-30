package mohamed.taha.moviestask.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import mohamed.taha.moviestask.model.Film
import mohamed.taha.moviestask.remote.ApiService
import mohamed.taha.moviestask.util.FilmType

import retrofit2.HttpException
import java.io.IOException

class MostPopularFilmSource(private val api: ApiService, private val filmType: FilmType) :
    PagingSource<Int, Film>() {
    override fun getRefreshKey(state: PagingState<Int, Film>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Film> {
        return try {
            val nextPage = params.key ?: 1
            val nowPlayingMovies =
                if (filmType == FilmType.MOVIE) api.getPopularMovies(page = nextPage)
                else api.getOnTheAirTvShows(page = nextPage)

            LoadResult.Page(
                data = nowPlayingMovies.results,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (nowPlayingMovies.results.isEmpty()) null else nowPlayingMovies.page + 1
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }
}