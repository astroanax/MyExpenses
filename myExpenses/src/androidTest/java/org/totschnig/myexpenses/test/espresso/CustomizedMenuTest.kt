package org.totschnig.myexpenses.test.espresso

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import org.junit.Test
import org.totschnig.myexpenses.R
import org.totschnig.myexpenses.dialog.MenuItem
import org.totschnig.myexpenses.dialog.name
import org.totschnig.myexpenses.preference.PrefKey
import org.totschnig.myexpenses.testutils.BaseMyExpensesTest

class CustomizedMenuTest: BaseMyExpensesTest() {

    @Test
    fun startWithCustomizedMenu() {
        // App should not expect any item to be present
        prefHandler.putStringSet(PrefKey.CUSTOMIZE_MAIN_MENU, setOf(MenuItem.Settings.name))
        val account1 = buildAccount("Test account 1")
        launch(account1.id)
        composeTestRule.onNodeWithText(getString(R.string.no_expenses)).assertIsDisplayed()
    }
}