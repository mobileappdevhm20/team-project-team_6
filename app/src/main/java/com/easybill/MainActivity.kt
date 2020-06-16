package com.easybill

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.easybill.database.Converters
import com.easybill.database.EasyBillDatabase
import com.easybill.database.model.Bill
import com.easybill.database.model.Head
import com.easybill.database.model.Item
import com.easybill.viewmodel.EasyBillViewModel
import com.easybill.viewmodel.EasyBillViewModelFactory
import java.util.Objects.requireNonNull

/**
 * Main-Activity of this application.
 */
class MainActivity : AppCompatActivity() {

    // keeps state when the activity gets re-loaded on device configuration change
    private lateinit var viewModel: EasyBillViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setup navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        NavigationUI.setupActionBarWithNavController(this, navHostFragment.navController)

        // get bill-dao and create view-model
        val headDao = EasyBillDatabase.getInstance(application).getHeadDao()
        val itemDao = EasyBillDatabase.getInstance(application).getItemDao()
        val billDao = EasyBillDatabase.getInstance(application).getBillDao()
        val viewModelFactory = EasyBillViewModelFactory(headDao, itemDao, billDao, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EasyBillViewModel::class.java)

        // fill database with mock-data
        // fillDatabase()

        // clear database-tables
        // clearDatabase()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp()
    }

    private fun fillDatabase() {
        val millisPerDay = 24 * 60 * 60 * 1000
        val converter = Converters()

        /*
         * Bill#1
         */
        val headOne = Head()
        headOne.storeName = "Media Markt"
        headOne.address = "Fakestreet 1234, 80801 München"
        headOne.time =
            converter.localDateTimeFromTimestamp(System.currentTimeMillis() - millisPerDay)!!
        // items #1.1
        val itemOneOne = Item()
        itemOneOne.amount = 1.0
        itemOneOne.tax = 0.19
        itemOneOne.name = "USB-Stick erster Güte"
        itemOneOne.nettoPrice = 13.37
        // items #1.2
        val itemOneTwo = Item()
        itemOneTwo.amount = 3.0
        itemOneTwo.tax = 0.19
        itemOneTwo.name = "Wirklich schnelle SSD"
        itemOneTwo.nettoPrice = 199.00
        val billOne = Bill(
            headOne,
            listOf(itemOneOne, itemOneTwo)
        )
        viewModel.addBill(billOne)

        /*
         * Bill#2
         */
        val headTwo = Head()
        headTwo.storeName = "Edeka"
        headTwo.address = "Irgendwostr. 13, 13370 Frankfurt"
        headTwo.time = converter
            .localDateTimeFromTimestamp(System.currentTimeMillis() - 2 * millisPerDay)!!
        // items #2.1
        val itemTwoOne = Item()
        itemTwoOne.amount = 1.0
        itemTwoOne.tax = 0.07
        itemTwoOne.name = "Käsebrot"
        itemTwoOne.nettoPrice = 7.99
        // items #2.2
        val itemTwoTwo = Item()
        itemTwoTwo.amount = 3.0
        itemTwoTwo.tax = 0.07
        itemTwoTwo.name = "Milch von der Kuh"
        itemTwoTwo.nettoPrice = 1.39
        // items #2.3
        val itemTwoThree = Item()
        itemTwoThree.amount = 3.0
        itemTwoThree.tax = 0.07
        itemTwoThree.name = "Magerquark"
        itemTwoThree.nettoPrice = 0.89
        val billTwo = Bill(
            headTwo,
            listOf(itemTwoOne, itemTwoTwo, itemTwoThree)
        )
        viewModel.addBill(billTwo)

        /*
         * Bill#3
         */
        val headThree = Head()
        headThree.storeName = "McDonalds"
        headThree.address = "Hipstersquare 13, 08574 Bonn"
        headThree.time = converter
            .localDateTimeFromTimestamp(System.currentTimeMillis() - 3 * millisPerDay)!!
        // items #3.1
        val itemThreeOne = Item()
        itemThreeOne.amount = 199.0
        itemThreeOne.tax = 0.07
        itemThreeOne.name = "Cheeseburger"
        itemThreeOne.nettoPrice = 1.0
        // items #3.2
        val itemThreeTwo = Item()
        itemThreeTwo.amount = 1.0
        itemThreeTwo.tax = 0.07
        itemThreeTwo.name = "Diät-Cola"
        itemThreeTwo.nettoPrice = 1.0
        val billThree = Bill(
            headThree,
            listOf(itemThreeOne, itemThreeTwo)
        )
        viewModel.addBill(billThree)
    }

    private fun clearDatabase() {
        val application = requireNonNull(this).application
        val headDao = EasyBillDatabase.getInstance(application).getHeadDao()
        val itemDao = EasyBillDatabase.getInstance(application).getItemDao()
        val billDao = EasyBillDatabase.getInstance(application).getBillDao()
        val viewModel = EasyBillViewModel(headDao, itemDao, billDao, application)
        viewModel.deleteAllBills()
    }
}
