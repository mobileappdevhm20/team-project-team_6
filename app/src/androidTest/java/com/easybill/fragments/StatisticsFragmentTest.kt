package com.easybill.fragments

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.easybill.R
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StatisticFragmentTest {

    // trivial test to ensure we have working fragment-tests
    @Test
    fun testStatisticTitle() {
        launchFragmentInContainer<StatisticsFragment>()
        onView(withId(R.id.statistic_title)).check(matches(withText(R.string.statistic_title)))
    }
}