package mohamed.taha.moviestask

import android.view.View
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.android.material.bottomnavigation.BottomNavigationView
import mohamed.taha.moviestask.ui.MainActivity
import mohamed.taha.moviestask.ui.screens.Home
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test

class TopRatedTest {
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


