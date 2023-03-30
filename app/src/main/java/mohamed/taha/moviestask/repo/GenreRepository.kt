package mohamed.taha.moviestask.repo


import mohamed.taha.moviestask.remote.ApiService
import mohamed.taha.moviestask.remote.response.GenreResponse
import mohamed.taha.moviestask.util.FilmType
import mohamed.taha.moviestask.util.Resource
import java.lang.Exception
import javax.inject.Inject

class GenreRepository @Inject constructor(private val api: ApiService) {
    suspend fun getMoviesGenre(): Resource<GenreResponse> {
        val response = try {
            api.getMovieGenres()
        } catch (e: Exception){
            return Resource.Error("Unknown error occurred!")
        }
        return Resource.Success(response)
    }
}