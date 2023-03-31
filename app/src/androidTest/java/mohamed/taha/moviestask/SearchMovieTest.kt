package mohamed.taha.moviestask

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import mohamed.taha.moviestask.ui.MainActivity
import mohamed.taha.moviestask.ui.activity.SearchActivity
import mohamed.taha.moviestask.ui.screens.Home
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchMovieTest : BaseTest() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()


    @Test
    fun testSearchNavigation() {
        Intents.init()
        composeTestRule.setContent {
            Home()
        }

        composeTestRule.onNodeWithTag("SearchBarHome").performClick()
        Intents.intended(IntentMatchers.hasComponent(SearchActivity::class.java.name))
        Intents.release()

    }


}

