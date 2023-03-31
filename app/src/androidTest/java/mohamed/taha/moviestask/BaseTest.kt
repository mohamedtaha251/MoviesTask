package mohamed.taha.moviestask

import org.junit.Rule
import org.junit.rules.Timeout
import java.util.concurrent.TimeUnit

open class BaseTest {


    @get:Rule
    var timeoutRule = Timeout(30, TimeUnit.SECONDS)
}