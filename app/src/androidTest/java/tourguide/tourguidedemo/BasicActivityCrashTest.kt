package tourguide.tourguidedemo

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import org.hamcrest.Matchers.allOf
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class BasicActivityCrashTest {
    @Rule @JvmField
    var rule: ActivityTestRule<BasicActivity> = ActivityTestRule(BasicActivity::class.java)

    @Test
    fun testNoCrash() {
        onView(allOf(withId(R.id.button1))).perform(click())
        Assert.assertTrue("There is no crash :)", true)
    }
}
