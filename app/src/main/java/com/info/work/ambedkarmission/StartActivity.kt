package com.info.work.ambedkarmission

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.ois.todo.activity.BaseActivity

class StartActivity :BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_slide4)

        Handler().postDelayed({
            startActivity(Intent(this@StartActivity, MainActivity::class.java))
            finish()

        },20000)

    }

}