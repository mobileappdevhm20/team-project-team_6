package com.easybill

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.easybill.data.Database
import com.google.android.material.bottomnavigation.BottomNavigationView
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    // keeps state when the activity gets re-loaded on device configuration change
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
         * Logging (debug-mode, shows class-name as part of the log-msg)
         */
        Timber.plant(Timber.DebugTree())

        /*
         * Database
         */
        val billDao = Database.getInstance(application).getBillDao()
        val billHeaderDao = Database.getInstance(application).getBillHeaderDao()
        val billItemDao = Database.getInstance(application).getBillItemDao()

        /*
         * ViewModel
         */
        val viewModelFactory = MainViewModelFactory(billDao, billHeaderDao, billItemDao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        /*
         * Navigation (bottom-bar menu)
         */
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val navController = navHostFragment?.findNavController()
        val bottomNavView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        val bottomMenuActionBar = AppBarConfiguration(setOf(
            R.id.bottom_menu_statistics, R.id.fragment_archive, R.id.bottom_menu_add))
        if (navController != null) {
            setupActionBarWithNavController(navController, bottomMenuActionBar)
            bottomNavView.setupWithNavController(navController)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()
    }

    /*fun fillDatabase() {
        val millisPerDay = 24 * 60 * 60 * 1000
        val converter = Converters()

        /*
         * Bill#1
         */
        val headOne = BillHeader()
        headOne.companyName = "Media Markt"
        headOne.address = "Fakestreet 1234, 80801 München"
        headOne.dateTime =
            converter.localDateTimeFromTimestamp(System.currentTimeMillis() - millisPerDay)!!
        // items #1.1
        val itemOneOne = BillItem()
        itemOneOne.amount = 1.0
        itemOneOne.name = "USB-Stick erster Güte"
        itemOneOne.price = 13.37
        // items #1.2
        val itemOneTwo = BillItem()
        itemOneTwo.amount = 3.0
        itemOneTwo.name = "Wirklich schnelle SSD"
        itemOneTwo.price = 199.00
        val billOne = Bill(
            headOne,
            listOf(itemOneOne, itemOneTwo)
        )
        viewModel.addBill(billOne)

        /*
         * Bill#2
         */
        val headTwo = BillHeader()
        headTwo.companyName = "Edeka"
        headTwo.address = "Irgendwostr. 13, 13370 Frankfurt"
        headTwo.dateTime = converter
            .localDateTimeFromTimestamp(System.currentTimeMillis() - 2 * millisPerDay)!!
        // items #2.1
        val itemTwoOne = BillItem()
        itemTwoOne.amount = 1.0
        itemTwoOne.name = "Käsebrot"
        itemTwoOne.price = 7.99
        // items #2.2
        val itemTwoTwo = BillItem()
        itemTwoTwo.amount = 3.0
        itemTwoTwo.name = "Milch von der Kuh"
        itemTwoTwo.price = 1.39
        // items #2.3
        val itemTwoThree = BillItem()
        itemTwoThree.amount = 3.0
        itemTwoThree.name = "Magerquark"
        itemTwoThree.price = 0.89
        val billTwo = Bill(
            headTwo,
            listOf(itemTwoOne, itemTwoTwo, itemTwoThree)
        )
        viewModel.addBill(billTwo)

        /*
         * Bill#3
         */
        val headThree = BillHeader()
        headThree.companyName = "McDonalds"
        headThree.address = "Hipstersquare 13, 08574 Bonn"
        headThree.dateTime = converter
            .localDateTimeFromTimestamp(System.currentTimeMillis() - 3 * millisPerDay)!!
        // items #3.1
        val itemThreeOne = BillItem()
        itemThreeOne.amount = 199.0
        itemThreeOne.name = "Cheeseburger"
        itemThreeOne.price = 1.0
        // items #3.2
        val itemThreeTwo = BillItem()
        itemThreeTwo.amount = 1.0
        itemThreeTwo.name = "Diät-Cola"
        itemThreeTwo.price = 1.0
        val billThree = Bill(
            headThree,
            listOf(itemThreeOne, itemThreeTwo)
        )
        viewModel.addBill(billThree)
    }*/
}
