package com.info.work.ambedkarmission

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.info.work.ambedkarmission.databinding.FeedbackLayoutBinding
import com.ois.todo.activity.BaseActivity

class FeedbackActivity:BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       var view= DataBindingUtil.setContentView<FeedbackLayoutBinding>(this,R.layout.feedback_layout)

    }

}