package mohamed.taha.moviestask.ui.remote.response

import mohamed.taha.moviestask.ui.model.Genre
import com.google.gson.annotations.SerializedName

data class GenreResponse(
    @SerializedName("genres")
    val genres: List<Genre>
)