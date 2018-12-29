package com.info.work.ambedkarmission

import android.app.Activity
import android.app.Dialog
import java.io.IOException
import kotlin.text.Charsets.UTF_8
import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.info.work.ambedkarmission.Registration.Companion.TAG
import com.info.work.ambedkarmission.model.UploadMedia
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Utils {



    companion object {

        var progressDialog:Dialog?=null

        fun loadJSONFromAsset(activity: Activity): String? {
            var json: String? = null
            try {
                val `is` = activity.assets.open("statesdistricts.json")
                val size = `is`.available()
                val buffer = ByteArray(size)
                `is`.read(buffer)
                `is`.close()


                json = String(buffer, UTF_8)
            } catch (ex: IOException) {
                ex.printStackTrace()
                return null
            }

            return json
        }

        fun showProDialog(context: Context): Dialog? {
            try {
                if (progressDialog != null && progressDialog!!.isShowing()) {
                    progressDialog!!.dismiss()
                }
              //  progressDialog = ProgressDialog(context, R.style.NewDialog)
                (progressDialog as ProgressDialog).setCancelable(false)
                (progressDialog as ProgressDialog).setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.progress))
                (progressDialog as ProgressDialog).setProgressStyle(android.R.style.Widget_ProgressBar_Large)
                (progressDialog as ProgressDialog).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return progressDialog
        }

        fun getCurrentDateTime():String{
            var cal= Calendar.getInstance()
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
            return sdf.format(cal.time)
        }




    }




}