package com.shunsukeshoji.litweet

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.shunsukeshoji.litweet.presentation.main.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.Matchers


@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun check_start_activity() {
        val mainActivity: MainActivity = activityRule.launchActivity(null)
        assertThat(
            "MainActivity is running",
            mainActivity.isFinishing,
            Matchers.`is`(false)
        )
    }
}