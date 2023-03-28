package mohamed.taha.moviestask.ui.remote.response

import mohamed.taha.moviestask.ui.model.Cast
import com.google.gson.annotations.SerializedName

data class CastResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("cast")
    val castResult: List<Cast>
)
