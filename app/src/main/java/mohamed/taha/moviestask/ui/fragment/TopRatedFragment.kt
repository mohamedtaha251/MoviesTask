package mohamed.taha.moviestask.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import mohamed.taha.moviestask.R
import mohamed.taha.moviestask.ui.screens.TopRated
@AndroidEntryPoint
class TopRatedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_toprated, container, false).apply {
            findViewById<ComposeView>(R.id.composeView).setContent {
                TopRated()
            }
        }
    }
}