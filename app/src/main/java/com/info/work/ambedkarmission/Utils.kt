package com.info.work.ambedkarmission

import android.app.Activity
import android.app.Dialog
import java.io.IOException
import kotlin.text.Charsets.UTF_8
import android.app.ProgressDialog
import android.content.Context


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


    }




}