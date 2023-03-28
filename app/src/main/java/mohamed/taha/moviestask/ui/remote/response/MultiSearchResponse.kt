package mohamed.taha.moviestask.ui.remote.response

import mohamed.taha.moviestask.ui.model.Search
import com.google.gson.annotations.SerializedName

class MultiSearchResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<Search>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)
