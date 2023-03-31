package mohamed.taha.moviestask

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher

fun selectBottomNavigationItem(index: Int): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return CoreMatchers.allOf(
                ViewMatchers.isAssignableFrom(BottomNavigationView::class.java),
                ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
            )
        }

        override fun getDescription(): String {
            return "Click on bottom navigation item with index $index"
        }

        override fun perform(uiController: UiController?, view: View?) {
            if (view != null && view is BottomNavigationView) {
                view.selectedItemId = view.menu.getItem(index).itemId
            }
        }
    }
}
