package mohamed.taha.moviestask.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import mohamed.taha.moviestask.model.Film
import mohamed.taha.moviestask.ui.screens.FilmDetails

@AndroidEntryPoint
class MovieDetails : ComponentActivity() {
    lateinit var film: Film
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getPassedArgs()
        setContent {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                FilmDetails(this, currentFilm = film)
            }

        }
    }

    private fun getPassedArgs() {
        intent.getStringExtra(ARG_FILM)?.let {
            film = Film.toModel(it) ?: return
        }
    }

    companion object {
        const val ARG_FILM = "argFilm"
        fun start(context: Context, film: Film?) {
            context.startActivity(Intent(context, MovieDetails::class.java).apply {
                putExtra(ARG_FILM, film?.toString())
            })
        }
    }
}

