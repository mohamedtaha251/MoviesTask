package mohamed.taha.moviestask
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import mohamed.taha.moviestask.ui.MainActivity
import mohamed.taha.moviestask.ui.screens.Home
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.compose.ui.test.*
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import mohamed.taha.moviestask.ui.activity.SearchActivity

@RunWith(AndroidJUnit4::class)
class MyHybridActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testClickButton() {
        composeTestRule.setContent {
            Home()
        }
        Intents.init()

        composeTestRule.onNodeWithTag("SearchBarHome").performClick()
        Intents.intended(IntentMatchers.hasComponent(SearchActivity::class.java.name))

    }
}

