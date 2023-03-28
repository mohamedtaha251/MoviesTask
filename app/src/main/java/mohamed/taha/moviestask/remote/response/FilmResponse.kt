package mohamed.taha.moviestask.remote.response

import mohamed.taha.moviestask.model.Film
import com.google.gson.annotations.SerializedName

data class FilmResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<Film>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)
