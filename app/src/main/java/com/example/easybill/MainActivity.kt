package com.example.easybill

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.archiv.*

class MainActivity : AppCompatActivity() {

    val testCompany = arrayOf<String>("Oberpollinger","Saturn","H&M")
    val testDate = arrayOf<String>("27.04.2020","04.10.2019","05.08.2019")
    val testPrice = arrayOf<String>("295.00","539.00","74.98")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
