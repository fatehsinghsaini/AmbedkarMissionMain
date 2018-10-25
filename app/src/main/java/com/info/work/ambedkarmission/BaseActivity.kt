package com.ois.todo.activity

import android.app.AlarmManager
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.gson.JsonObject


import java.util.*


open class BaseActivity : AppCompatActivity() {

    companion object {

        /*var roomDb: AppDatabase?=null*/
        val TAGAlarm = "NotificationHelper"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("compositeDisposable", ">>" + "new")





    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        Log.i("compositeDisposable", ">>" + "clear")
        // hideLoading()
        super.onStop()
    }

    override fun onDestroy() {
        Log.i("compositeDisposable", ">>" + "dispose")
        super.onDestroy()
    }



    override fun onBackPressed() {
        super.onBackPressed()
    }

}
