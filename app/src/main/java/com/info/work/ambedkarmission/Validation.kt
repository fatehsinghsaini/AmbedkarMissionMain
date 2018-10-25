package com.ois.todo.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Environment
import android.support.design.widget.TextInputEditText
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.info.work.ambedkarmission.R
import java.io.File
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class Validation {

    companion object {


        fun isValidMobile(phone: String, txtPhone: EditText): Boolean {
            var check: Boolean
            if (!Pattern.matches("[a-zA-Z]+", phone)) {
                check = phone.length in 4..15
            } else {
//                txtPhone.setError("Not Valid Number")
                check = false
            }
            return check
        }

        public fun isValidOtp(phone: String, txtPhone: EditText): Boolean {
            var check: Boolean
            check = phone.length > 3
            return check
        }

        public fun isPasswordValidate(password: String, pwdEditText: TextInputEditText): Boolean {
            var check: Boolean
            if (!password.equals("")) {
                var PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,16})";
                var pattern = Pattern.compile(PASSWORD_PATTERN);
                var matcher = pattern.matcher(password);
                if (matcher.matches()) {
                    check = false
                    pwdEditText.setError("password not valid,should be alphanumeric,greater than 6 character")
                } else
                    check = true
            } else {
                check = false
                pwdEditText.setError("Enter Password")
            }
            return check;
        }


        public fun hideKeyboard(view: View, context: Context) {

            if (view != null) {
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
            }
        }

        fun hideKeyboard(activity: Activity) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        /**
         * validate your email address format. Ex-akhi@mani.com
         */
        fun emailValidator(email: String): Boolean {
            val pattern: Pattern
            val matcher: Matcher
            val EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
            pattern = Pattern.compile(EMAIL_PATTERN)
            matcher = pattern.matcher(email)
            return matcher.matches()
        }



        fun isViewInBounds(view: View, x: Int, y: Int): Boolean {
            val outRect = Rect()
            val location = IntArray(2)
            view.getDrawingRect(outRect)
            view.getLocationOnScreen(location)
            outRect.offset(location[0], location[1])
            // return outRect.contains(x, y);
            return true
        }


        fun getDetectedCountry(context: Context, defaultCountryIsoCode: String): String {
            detectSIMCountry(context)?.let {
                return it
            }

            detectNetworkCountry(context)?.let {
                return it
            }

            detectLocaleCountry(context)?.let {
                return it
            }

            return defaultCountryIsoCode.toUpperCase()
        }

        private fun detectSIMCountry(context: Context): String? {
            try {
                val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                Log.d("TAG", "detectSIMCountry: ${telephonyManager.simCountryIso}")
                return telephonyManager.simCountryIso
            } catch (e: Exception) {

                e.printStackTrace()
            }
            return null
        }

        private fun detectNetworkCountry(context: Context): String? {
            try {
                val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                Log.d("TAg", "detectNetworkCountry: ${telephonyManager.simCountryIso}")
                return telephonyManager.networkCountryIso
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        private fun detectLocaleCountry(context: Context): String? {
            try {
                val localeCountryISO = context.getResources().getConfiguration().locale.getCountry()
                Log.d("TAG", "detectLocaleCountry: $localeCountryISO")
                return localeCountryISO
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        fun openMaleFemaleDailog(ctx: Activity, textView: TextView) {
            var nameDialog: AlertDialog? = null
            val dialogBuilder = AlertDialog.Builder(ctx)
            val inflater = ctx.layoutInflater
            val dialogView = inflater.inflate(R.layout.male_female_dialog, null)
            dialogBuilder.setView(dialogView)


            val maleFemaleGroup = dialogView.findViewById<RadioGroup>(R.id.maleFemaleGroup) as RadioGroup
            val maleBt = dialogView.findViewById<RadioGroup>(R.id.male) as RadioButton
            val femaleBt = dialogView.findViewById<RadioGroup>(R.id.female) as RadioButton

            if (textView.text.toString() == ctx.getString(R.string.male))
                maleBt.isChecked = true
            if (textView.text.toString() == ctx.getString(R.string.female))
                femaleBt.isChecked = true


            val txtSubmit = dialogView.findViewById<View>(R.id.ok) as TextView
            val txtCancel = dialogView.findViewById<View>(R.id.cancel) as TextView

            dialogBuilder.setTitle("")
            nameDialog = dialogBuilder.create()
            nameDialog!!.setCancelable(false)

            txtSubmit.setOnClickListener {
                val selectedId = maleFemaleGroup.getCheckedRadioButtonId()
                val radioButton = dialogView.findViewById(selectedId) as RadioButton
                textView.setText(radioButton.text)
                if (nameDialog!!.isShowing)
                    nameDialog!!.dismiss()
            }
            txtCancel.setOnClickListener {
                if (nameDialog!!.isShowing)
                    nameDialog!!.dismiss()
            }
            nameDialog!!.show()
        }


        fun createFolder(ctx: Context, fileName: String): String {
            val sep = File.separator // Use this instead of hardcoding the "/"
            val newFolder = ctx.getString(R.string.app_name)
            val extStorageDirectory = Environment.getExternalStorageDirectory().toString()
            val myNewFolder = File(extStorageDirectory + sep + newFolder)
            myNewFolder.mkdir()
            var str = (Environment.getExternalStorageDirectory().toString() + sep + newFolder + sep + fileName)
            return str
        }



    }


}