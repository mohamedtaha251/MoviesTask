package mohamed.taha.moviestask

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import mohamed.taha.moviestask.ui.MainActivity
import org.hamcrest.CoreMatchers.allOf
import org.junit.Rule
import org.junit.Test

class TopRatedTest : BaseTest() {


    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()


    @Test
    fun testClickTopTopRated() {
        // Find the bottom navigation view
        val bottomNavigationViewMatcher = allOf(
            isAssignableFrom(BottomNavigationView::class.java),
            withEffectiveVisibility(Visibility.VISIBLE)
        )
        val bottomNavigationView = onView(bottomNavigationViewMatcher)

        // Click on the second item in the bottom navigation bar
        bottomNavigationView.perform(selectBottomNavigationItem(1))
        composeTestRule.onNodeWithTag("TopRatedList").assertIsDisplayed()

    }

}


