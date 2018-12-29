package com.info.work.ambedkarmission

import android.app.Dialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

import com.info.work.ambedkarmission.model.State
import com.info.work.ambedkarmission.model.StateDistrict
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.info.work.ambedkarmission.databinding.ActivityRegistrationBinding
import com.info.work.ambedkarmission.model.Registration
import com.ois.todo.activity.BaseActivity
import com.ois.todo.countrylist.DistrictAdapter
import com.ois.todo.countrylist.StateAdapter
import com.ois.todo.delegate.CountrySelectListener
import com.ois.todo.fragment.OTPFragment

import java.util.ArrayList
import java.util.concurrent.TimeUnit

class Registration : BaseActivity(), View.OnClickListener, CountrySelectListener {


    var mView: ActivityRegistrationBinding? = null
    var stateList: ArrayList<State> = ArrayList()
    var districtList: ArrayList<String> = ArrayList()

    private var countryDialog: Dialog? = null
    private var mAdapter: StateAdapter? = null
    private var mDistrictAdapter: DistrictAdapter? = null
    private var stateListClone = mutableListOf<State>()
    private var districtListClone = mutableListOf<String>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mView = DataBindingUtil.setContentView(this, R.layout.activity_registration)

        val obj = Utils.loadJSONFromAsset(this)
        var stateDistrict = Gson().fromJson<StateDistrict>(obj, StateDistrict::class.java)
        stateListClone = stateDistrict!!.states as ArrayList<State>

        mView?.submitBt?.setOnClickListener(this)
        mView?.txtCountryCode?.setOnClickListener(this)
        mView?.txtDistrict?.setOnClickListener(this)
        mView?.imgBack?.setOnClickListener(this)

       // firebaseSetUp()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.submitBt ->createUser()
            R.id.txtCountryCode ->showStateCodes(v)
            R.id.txtDistrict ->showDistrictCodes(v)
            R.id.imgBack ->finish()
        }
    }



    /**
     * Creating new user node under 'users'
     */
    private fun createUser() {

        if(mView!!.name.text.toString().trim()=="")
        {
            Toast.makeText(this@Registration,getString(R.string.name_require),Toast.LENGTH_SHORT).show()
            return
        }

        if(mView!!.mail.text.toString().trim()!="" && mView!!.mobilenumber.text.toString().trim()=="" )
        {
            Toast.makeText(this@Registration,getString(R.string.email_require),Toast.LENGTH_SHORT).show()
            return
        }

        if(mView!!.mail.text.toString().trim()=="" && mView!!.mobilenumber.text.toString().trim()!="" )
        {
            Toast.makeText(this@Registration,getString(R.string.email_require),Toast.LENGTH_SHORT).show()
            return
        }

        if(mView!!.txtCountryCode.text.toString().trim()==getString(R.string.selectState))
        {
            Toast.makeText(this@Registration,getString(R.string.state_require),Toast.LENGTH_SHORT).show()
            return
        }

        if(mView!!.txtDistrict.text.toString().trim()==getString(R.string.district))
        {
            Toast.makeText(this@Registration,getString(R.string.district_require),Toast.LENGTH_SHORT).show()
            return
        }


        var mobileNo=mView!!.mobilenumber.text.toString().trim()

        val registration = Registration(mView!!.name.text.toString().trim(), mView!!.father.text.toString().trim(), mView!!.castname.text.toString().trim(),
                mView!!.mail.text.toString().trim(),mobileNo , mView!!.txtCountryCode.text.toString().trim()
                , mView!!.txtDistrict.text.toString().trim(), mView!!.village.text.toString().trim(), mView!!.postalCode.text.toString().trim())


        phoneVerify("+91"+mobileNo,registration)




       }


    companion object {
        val TAG = "Ambedkarmission"
    }



    private fun showStateCodes(v: View) {
        countryDialog = Dialog(this)
        countryDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        countryDialog!!.setContentView(R.layout.dialog_select_country_code_layout)
        val counrtyRecycler = countryDialog!!.findViewById<View>(R.id.rvCountryCode) as RecyclerView
        val edtCountrySearch = countryDialog!!.findViewById<View>(R.id.edtCountrySearch) as EditText
        val title = countryDialog!!.findViewById<TextView>(R.id.title)

/*        edtCountrySearch.hint=getString(R.string.searchState)
        title.text=getString(R.string.searchState)*/

        stateList!!.clear()
        stateList!!.addAll(stateListClone)

        mAdapter = StateAdapter(this@Registration, stateList!!)
        val mLayoutManager = LinearLayoutManager(this)
        counrtyRecycler.layoutManager = mLayoutManager
        counrtyRecycler.itemAnimator = DefaultItemAnimator()
        counrtyRecycler.adapter = mAdapter

        mAdapter!!.onclickListener(this)

        countryDialog!!.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        countryDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val window = countryDialog!!.window
        window!!.setGravity(Gravity.CENTER)
        countryDialog!!.show()
        v.isEnabled = true

        edtCountrySearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                searchCountry(edtCountrySearch.text.toString().trim { it <= ' ' })
            }
        })

    }

    private fun showDistrictCodes(v: View) {
        countryDialog = Dialog(this)
        countryDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        countryDialog!!.setContentView(R.layout.dialog_select_country_code_layout)
        val counrtyRecycler = countryDialog!!.findViewById<View>(R.id.rvCountryCode) as RecyclerView
        val edtCountrySearch = countryDialog!!.findViewById<View>(R.id.edtCountrySearch) as EditText
        val title = countryDialog!!.findViewById<TextView>(R.id.title)

        edtCountrySearch.hint=getString(R.string.searchDistrict)
        title.text=getString(R.string.district)

        districtList!!.clear()
        districtList!!.addAll(districtListClone)

        mDistrictAdapter = DistrictAdapter(this@Registration, districtList!!)
        val mLayoutManager = LinearLayoutManager(this)
        counrtyRecycler.layoutManager = mLayoutManager
        counrtyRecycler.itemAnimator = DefaultItemAnimator()
        counrtyRecycler.adapter = mDistrictAdapter

        mDistrictAdapter!!.onclickListener(this)

        countryDialog!!.window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        countryDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val window = countryDialog!!.window
        window!!.setGravity(Gravity.CENTER)
        countryDialog!!.show()

        v.isEnabled = true

        edtCountrySearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                searchDistrict(edtCountrySearch.text.toString().trim { it <= ' ' })
            }
        })

    }

    private fun searchCountry(searchTxt: String) {
        try {
            if (!searchTxt.equals("", ignoreCase = true)) {
                if (stateList != null) {
                    if (stateList == null) {
                        stateList!!.clear()
                        stateList!!.addAll(stateListClone)
                    } else {
                        stateList!!.clear()
                        for (i in stateListClone!!.indices) {
                            if (stateListClone!![i].state.toLowerCase().contains(searchTxt)) {
                                stateList!!.add(stateListClone!![i])
                            }
                        }
                    }
                }

            } else {
                if (stateList != null) {
                    stateList!!.clear()
                    stateList!!.addAll(stateListClone)
                    mAdapter!!.notifyDataSetChanged()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            if (mAdapter != null) {
                mAdapter!!.notifyDataSetChanged()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }  private fun searchDistrict(searchTxt: String) {
        try {
            if (!searchTxt.equals("", ignoreCase = true)) {
                if (districtList != null) {
                    if (districtList == null) {
                        districtList!!.clear()
                        districtList!!.addAll(districtListClone)
                    } else {
                        districtList!!.clear()
                        for (i in districtListClone!!.indices) {
                            if (districtListClone!![i].toLowerCase().contains(searchTxt)) {
                                districtList!!.add(districtListClone!![i])
                            }
                        }
                    }
                }

            } else {
                if (districtList != null) {
                    districtList!!.clear()
                    districtList!!.addAll(districtListClone)
                    mDistrictAdapter!!.notifyDataSetChanged()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            if (mDistrictAdapter != null) {
                mDistrictAdapter!!.notifyDataSetChanged()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onCountryClick(position: Int) {
        mView!!.txtCountryCode!!.text = stateList!![position].state

        if(districtListClone.size>0)
            districtListClone.clear()

        districtListClone.addAll(stateList!![position].districts)

        if (countryDialog != null)
            countryDialog!!.cancel()
    }

    override fun onDistrictClick(position: Int) {
        mView!!.txtDistrict!!.text = districtList!![position]
        if (countryDialog != null)
            countryDialog!!.cancel()
    }

    private fun phoneVerify(phoneNum:String,registration: Registration) {
        val phoneNumber = "+918946851870"
        val smsCode = "123456"

        val firebaseAuth = FirebaseAuth.getInstance()
        val firebaseAuthSettings = firebaseAuth.firebaseAuthSettings
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNumber, smsCode)


        var intent=Intent(this@Registration,OTPFragment::class.java)
        intent.putExtra("registermodel",registration)
       // intent.putExtra("otp",phoneAuthCredential.smsCode)
        startActivity(intent)

        // Whenever verification is triggered with the whitelisted number,
        // provided it is not set for auto-retrieval, onCodeSent will be triggered.
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNum, 30L /*timeout*/, TimeUnit.SECONDS,
                this, object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onCodeSent(verificationId: String?,forceResendingToken: PhoneAuthProvider.ForceResendingToken?) {
                Log.d("varification id",verificationId)



            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                // Sign in with the credential
                Log.d("varification id","error")
                var intent=Intent(this@Registration,OTPFragment::class.java)
                intent.putExtra("registermodel",registration)
                intent.putExtra("otp",phoneAuthCredential.smsCode)
                startActivity(intent)


                // ...
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.d("varification id","error")
            }
        })
        // [END auth_test_phone_verify]
    }





}
