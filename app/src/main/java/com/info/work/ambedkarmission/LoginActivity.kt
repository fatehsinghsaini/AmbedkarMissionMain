package com.info.work.ambedkarmission

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.info.work.ambedkarmission.databinding.LoginBinding
import com.ois.todo.activity.BaseActivity

class LoginActivity : BaseActivity() {

    private lateinit var auth: FirebaseAuth

    var mView: LoginBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mView = DataBindingUtil.setContentView(this, R.layout.login)

        auth = FirebaseAuth.getInstance()

        mView!!.submitBt.setOnClickListener {
            loginUser()
        }

        mView?.imgBack?.setOnClickListener{
            finish()
        }

    }

    private fun loginUser() {

        var email=mView!!.name.text.toString().trim()

        var password=mView!!.password.text.toString().trim()

        if (email.trim() == "") {
            Toast.makeText(this@LoginActivity, getString(R.string.name_require), Toast.LENGTH_SHORT).show()
            return
        }

        if (password == "") {
            Toast.makeText(this@LoginActivity, getString(R.string.password_require), Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        Toast.makeText(baseContext, getString(R.string.login_successfully),
                                Toast.LENGTH_SHORT).show()
                        val user = auth.currentUser

                        with (sharedPref!!.edit()) {
                            putString(getString(R.string.loginAuthKey), user!!.email)
                            apply()
                        }
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()

                    }

                    // ...
                }



    }

}