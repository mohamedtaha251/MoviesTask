package mohamed.taha.moviestask.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import mohamed.taha.moviestask.model.Film
import mohamed.taha.moviestask.ui.screens.FilmDetails
import mohamed.taha.moviestask.ui.theme.MoviesTaskTheme

@AndroidEntryPoint
class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                }

        }
    }


    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, SearchActivity::class.java).apply {
           })
        }
    }
}

