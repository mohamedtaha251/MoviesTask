package mohamed.taha.moviestask.remote.response

import mohamed.taha.moviestask.model.Genre
import com.google.gson.annotations.SerializedName

data class GenreResponse(
    @SerializedName("genres")
    val genres: List<Genre>
)