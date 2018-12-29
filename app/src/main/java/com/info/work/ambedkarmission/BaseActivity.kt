package com.ois.todo.activity

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.JsonObject
import com.info.work.ambedkarmission.R


import java.util.*


open class BaseActivity : AppCompatActivity() {



    var TAG="missan"
    var sharedPref:SharedPreferences?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("compositeDisposable", ">>" + "new")
         sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE)


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
