package com.ois.todo.fragment

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import com.google.firebase.database.*
import com.info.work.ambedkarmission.R
import com.info.work.ambedkarmission.model.Registration
import com.ois.todo.activity.BaseActivity
import com.ois.todo.util.Validation

class OTPFragment : BaseActivity(), View.OnClickListener {

    private var progressBar: RelativeLayout? = null
    private var txtEnterOtp: TextView? = null
    private var txtResendOtp: TextView? = null
    private var txtInputLayoutNumber: TextInputLayout? = null
    private var txtInputEditNumber: TextInputEditText? = null


    private var txtSubmit: TextView? = null
    private var outerMain: ScrollView? = null
    // private var userNameLayout: LinearLayout? = null
    private var type: String? = ""
    private var password: String? = ""
    private var mobile: String? = ""
    private var otpNo: String? = ""
    private var language: String? = ""
    private var deviceId: String? = ""
    private var imgBack: ImageView? = null
    var mFirebaseInstance: FirebaseDatabase? = null
    var mFirebaseDatabase: DatabaseReference? = null
    var userId: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.otp_layout)
        initView()

        firebaseSetUp()

        var registration:Registration?=null

        if(intent.hasExtra("registermodel"))
            registration=intent.getParcelableExtra<Registration>("registermodel")
        if(intent.hasExtra("otp"))
            otpNo=intent.getStringExtra("otp")


        // Get a reference to our posts
        val ref = mFirebaseInstance!!.getReference("project/ambedkar-mission/database/ambedkar-mission/data/registration")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue<Registration>(Registration::class.java)

                if(post==null || post!=registration){
                    mFirebaseDatabase!!.push().setValue(registration)
                    addUserChangeListener()
                }
                else
                    Toast.makeText(this@OTPFragment,getString(R.string.user_already_exist),Toast.LENGTH_SHORT).show()


            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })


    }

    private fun firebaseSetUp() {

        mFirebaseInstance = FirebaseDatabase.getInstance()

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance!!.getReference("registration")

        // store app title to 'app_title' node
        mFirebaseInstance!!.getReference("app_title").setValue("Missan")
        userId = mFirebaseDatabase!!.push().key


    }


    /**
     * Registration data change listener
     */
    private fun addUserChangeListener() {
        // Registration data change listener
        mFirebaseDatabase!!.child(userId!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(Registration::class.java)
                //finish()

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.e(com.info.work.ambedkarmission.Registration.TAG, "Failed to read user", error.toException())
            }
        })
    }


    private fun initView() {

        imgBack = findViewById<View>(R.id.imgBack) as ImageView
        progressBar = findViewById<View>(R.id.progressBar) as RelativeLayout
        txtEnterOtp = findViewById<View>(R.id.txtEnterOtp) as TextView
        outerMain = findViewById<View>(R.id.outerMain) as ScrollView
        txtResendOtp = findViewById<View>(R.id.txtResendOtp) as TextView
        txtInputLayoutNumber = findViewById<View>(R.id.txtInputLayoutNumber) as TextInputLayout
        txtInputEditNumber = findViewById<View>(R.id.txtInputEditNumber) as TextInputEditText


        //txtInputEditName = findViewById<View>(R.id.txtInputEditName) as TextInputEditText
        txtSubmit = findViewById<View>(R.id.txtSubmit) as TextView
        // userNameLayout = findViewById<View>(R.id.userNameLayout) as LinearLayout

        txtInputEditNumber!!.setSelection(0)
        txtInputEditNumber!!.requestFocus()
        setClickListener()
    }



    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBack -> {
                Validation.hideKeyboard(v, this@OTPFragment)
                onBackPressed()
            }
            R.id.outerMain -> {
               Validation.hideKeyboard(v, this@OTPFragment)
            }
            R.id.txtResendOtp -> {

                if (password != "")
                    resendOtp(v)

            }
            R.id.txtSubmit -> {
                var typedOtp = txtInputEditNumber!!.text.toString()
                if (typedOtp.isEmpty()) {
                    Toast.makeText(this@OTPFragment, resources.getString(R.string.please_enter_otp), Toast.LENGTH_SHORT).show()
                    return
                }

                if (Validation.isValidOtp(typedOtp, txtInputEditNumber!!)) {
                   /* if (typedOtp != otpNo) {
                        Toast.makeText(this@OTPFragment, resources.getString(R.string.otp_not_match), Toast.LENGTH_SHORT).show()
                        return
                    }*/


                        Validation.hideKeyboard(v, this@OTPFragment!!)
                        if (type == null)
                            otpverify(v)


                } else {
                    Toast.makeText(this@OTPFragment, resources.getString(R.string.otp_should_be_greater_than3), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun otpverify(v: View) {
   // if(otpNo==typedOtp)
        finish()

    }

    private fun resendOtp(v: View) {

    }


    private fun setClickListener() {
        txtResendOtp!!.setOnClickListener(this)
        txtSubmit!!.setOnClickListener(this)
        imgBack!!.setOnClickListener(this);
        outerMain!!.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View, ev: MotionEvent): Boolean {
                Validation.hideKeyboard(view, this@OTPFragment)
                return false
            }
        })

    }


    private fun openDailog() {
        val dialogBuilder = AlertDialog.Builder(this@OTPFragment!!)

        dialogBuilder.setTitle(R.string.app_name)
        dialogBuilder.setMessage(R.string.resend_message)
//        dialogBuilder.setCancelable(false)
        dialogBuilder.setPositiveButton("OK") { dialog, which ->
            //            super.onBackPressed()
            dialog.dismiss()
//            setResult(Activity.RESULT_OK)
//            finish()
        }

//        // Display a negative button on alert dialog
//        dialogBuilder.setNegativeButton("NO"){dialog,which ->
//            dialog.dismiss()
//
//        }

        val dialog = dialogBuilder.create()
        dialog.show()
    }



}