@file:Suppress("KDocUnresolvedReference")

package mohamed.taha.moviestask.remote

import mohamed.taha.moviestask.BuildConfig
import mohamed.taha.moviestask.remote.response.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    /** **Movies** */
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 0,
        @Query("api_key") apiKey: String = BuildConfig.YOUR_API_KEY,
        @Query("language") language: String = "en"
    ): FilmResponse

    @GET("discover/movie")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int = 0,
        @Query("api_key") apiKey: String = BuildConfig.YOUR_API_KEY,
        @Query("language") language: String = "en"
    ): FilmResponse

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int = 0,
        @Query("api_key") apiKey: String = BuildConfig.YOUR_API_KEY,
        @Query("language") language: String = "en"
    ): FilmResponse

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int = 0,
        @Query("api_key") apiKey: String = BuildConfig.YOUR_API_KEY,
        @Query("language") language: String = "en"
    ): FilmResponse

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") filmId: Int,
        @Query("page") page: Int = 0,
        @Query("api_key") apiKey: String = BuildConfig.YOUR_API_KEY,
        @Query("language") language: String = "en"
    ): FilmResponse

    @GET("genre/movie/list")
    suspend fun getMovieGenres(
        @Query("api_key") apiKey: String = BuildConfig.YOUR_API_KEY,
        @Query("language") language: String = "en"
    ): GenreResponse

    @GET("search/multi")
    suspend fun multiSearch(
        @Query("query") searchParams: String,
        @Query("page") page: Int = 0,
        @Query("api_key") apiKey: String = BuildConfig.YOUR_API_KEY,
        @Query("language") language: String = "en"
    ): MultiSearchResponse
    
    /** Watch providers (US only)*/

    @GET("{film_path}/{film_id}/watch/providers?")
    suspend fun getWatchProviders(
        @Path("film_path") filmPath: String,
        @Path("film_id") filmId: Int,
        @Query("api_key") apiKey: String = BuildConfig.YOUR_API_KEY,
    ): WatchProviderResponse
}
